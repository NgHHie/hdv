package com.example.service_warranty.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceClient {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${service.product.url}")
    private String productServiceUrl;
    
    /**
     * Get product details to check warranty information
     */
    public ProductResponse getProductDetails(Long productId) {
        String url = "/api/v1/products/" + productId;
        log.info("Getting product details: {}", productId);
        
        try {
            return webClientBuilder.build().get()
                    .uri(productServiceUrl + url)
                    .retrieve()
                    .bodyToMono(ProductResponse.class)
                    .block(); // Blocking call for synchronous behavior
        } catch (Exception e) {
            log.error("Failed to get product details: {}", e.getMessage());
            return null;
        }
    }
    
    // Response model for product details
    public static class ProductResponse {
        private Long id;
        private String name;
        private String description;
        private String category;
        private LocalDate warrantyExpiration;
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public LocalDate getWarrantyExpiration() { return warrantyExpiration; }
        public void setWarrantyExpiration(LocalDate warrantyExpiration) { this.warrantyExpiration = warrantyExpiration; }
    }
}