package com.example.gateway_service.filter;

import com.example.gateway_service.security.JwtUtils;

import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;
    private final WebClient.Builder webClientBuilder;


    public JwtAuthenticationFilter(JwtUtils jwtUtils, WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.jwtUtils = jwtUtils;
        this.webClientBuilder = webClientBuilder;

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String requestPath = request.getPath().value();
            String requestMethod = request.getMethod().name();

            // Skip authentication for auth endpoints
            if (request.getURI().getPath().startsWith("/api/auth/")) {
                return chain.filter(exchange);
            }

            // Check if the path should be authenticated
            if (isOpenPath(requestPath)) {
                return chain.filter(exchange);
            }

            // Check for JWT token
            if (!request.getHeaders().containsKey("Authorization")) {
                return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Invalid Authorization header format", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            if (!jwtUtils.validateJwtToken(token)) {
                return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
            }

            // Extract user info from token
            String userId = getUserIdFromToken(token);
            String role = getRoleFromToken(token);

            if ("ROLE_ADMIN".equals(role)) {
                // Admin có tất cả quyền
                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .header("X-User-Id", userId)
                        .header("X-User-Role", role)
                        .build();
                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            }

            logger.debug("User ID: {}, Role: {}, Accessing path: {}, Method: {}",
                    userId, role, requestPath, requestMethod);

            // Kiểm tra quyền từ security service reactive
            String encodedPath = UriUtils.encodePath(requestPath, StandardCharsets.UTF_8);

            return webClientBuilder.build()
                    .get()
                    .uri("lb://service-security/api/auth/check-permission?role={role}&path={path}&method={method}",
                            role, encodedPath, requestMethod)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .defaultIfEmpty(false)
                    .onErrorReturn(false)
                    .flatMap(hasPermission -> {
                        if (!hasPermission) {
                            logger.warn("Access denied for user {} with role {} to path {} method {}",
                                    userId, role, requestPath, requestMethod);
                            return onError(exchange, "Access denied. Insufficient permissions.", HttpStatus.FORBIDDEN);
                        }

                        // Add user info to headers for downstream services
                        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                .header("X-User-Id", userId)
                                .header("X-User-Role", role)
                                .build();

                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    });
        };
    }


    private boolean isOpenPath(String path) {
        return path.startsWith("/api/auth") ||
                path.startsWith("/api/public") ||
                path.equals("/");
    }

    private String getUserIdFromToken(String token) {
        Claims claims = jwtUtils.getClaimsFromJwtToken(token);
        Long userId = claims.get("userId", Long.class);
        return userId != null ? userId.toString() : "anonymous";
    }

    private String getRoleFromToken(String token) {
        Claims claims = jwtUtils.getClaimsFromJwtToken(token);
        return claims.get("role", String.class);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        logger.error("Authentication error: {}", message);
        return response.setComplete();
    }

    public static class Config {
        
    }
}