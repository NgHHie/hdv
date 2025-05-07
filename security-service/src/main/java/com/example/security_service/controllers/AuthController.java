package com.example.security_service.controllers;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.security_service.dto.JwtResponse;
import com.example.security_service.dto.LoginRequest;
import com.example.security_service.dto.MessageResponse;
import com.example.security_service.dto.SignupRequest;
import com.example.security_service.models.ERole;
import com.example.security_service.models.Role;
import com.example.security_service.models.User;
import com.example.security_service.repositories.RoleRepository;
import com.example.security_service.repositories.UserRepository;
import com.example.security_service.security.jwt.JwtUtils;
import com.example.security_service.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> permissions = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getRole(),
                permissions));
    }

    @GetMapping("/check-permission")
    public Boolean checkPermission(@RequestParam String role,
            @RequestParam String path,
            @RequestParam String method) {
        try {
            String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8.name()).trim();
            logger.debug("Checking permission - Role: {}, Path: {}, Method: {}",
                    role, decodedPath, method);

            // Admin role có tất cả quyền
            if ("ROLE_ADMIN".equals(role)) {
                return true;
            }

            @SuppressWarnings("unchecked")
            List<Object[]> results = entityManager.createNativeQuery(
                    "SELECT p.name, p.path, p.method " +
                            "FROM permissions p " +
                            "JOIN role_permissions rp ON p.id = rp.permission_id " +
                            "JOIN roles r ON rp.role_id = r.id " +
                            "WHERE r.name = :roleName")
                    .setParameter("roleName", role)
                    .getResultList();

            logger.debug("Found {} permissions for role {}", results.size(), role);

            // Kiểm tra quyền thủ công
            AntPathMatcher pathMatcher = new AntPathMatcher();
            for (Object[] result : results) {
                String permName = (String) result[0];
                String permPath = (String) result[1];
                String permMethod = (String) result[2];

                boolean methodMatch = method.equalsIgnoreCase(permMethod);
                boolean pathMatch = pathMatcher.match(permPath, decodedPath);

                logger.debug("Permission check - Name: {}, Path: {}, Method: {}, MethodMatch: {}, PathMatch: {}",
                        permName, permPath, permMethod, methodMatch, pathMatch);

                if (methodMatch && pathMatch) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            logger.error("Error checking permission: {}", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username đã tồn tại!"));
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email đã được sử dụng!"));
            }

            // Create new user account
            User user = new User();
            user.setUsername(signUpRequest.getUsername());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(encoder.encode(signUpRequest.getPassword()));
            user.setFirstName(signUpRequest.getFirstName());
            user.setLastName(signUpRequest.getLastName());
            user.setActive(true);

            // Xác định role name dựa trên input
            String strRole = signUpRequest.getRole();
            ERole roleName = ERole.ROLE_CUSTOMER; // Default role

            if (strRole != null) {
                switch (strRole.toLowerCase()) {
                    case "admin":
                        roleName = ERole.ROLE_ADMIN;
                        break;
                    case "technician":
                        roleName = ERole.ROLE_TECHNICIAN;
                        break;
                }
            }

            // Truy vấn role trực tiếp từ database
            Query query = entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name");
            query.setParameter("name", roleName);

            Role role;
            try {
                role = (Role) query.getSingleResult();
            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error: Không tìm thấy role " + roleName));
            }

            // Tách và xử lý role từ db
            Role detachedRole = new Role();
            detachedRole.setId(role.getId());
            detachedRole.setName(role.getName());
            detachedRole.setDescription(role.getDescription());

            // Gán role cho user
            user.setRole(detachedRole);

            // Lưu user
            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("Đăng ký người dùng thành công!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}