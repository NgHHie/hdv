package com.example.service_warranty.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceClient {
    
    private final RestTemplate restTemplate;
    
    @Value("${service.product.url}")
    private String productServiceUrl;
    
    /**
     * Get product details to check warranty information
     */
    public ProductResponse getProductDetails(Long productId) {
        String url = productServiceUrl + "/api/v1/products/" + productId;
        log.info("Getting product details: {}", productId);
        
        try {
            ResponseEntity<ProductResponse> response = restTemplate.getForEntity(url, ProductResponse.class);
            return response.getBody();
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