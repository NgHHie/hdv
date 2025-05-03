package com.example.service_repair.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// Client for the Warranty Service
@Service
@RequiredArgsConstructor
@Slf4j
public class WarrantyServiceClient {
    
    private final RestTemplate restTemplate;
    
    @Value("${service.warranty.url}")
    private String warrantyServiceUrl;
    
    /**
     * Check if a product is still under warranty for a specific customer
     */
    public boolean checkWarrantyStatus(Long productId, Long customerId) {
        String url = warrantyServiceUrl + "/api/v1/warranty/check?productId=" + productId + "&customerId=" + customerId;
        log.info("Checking warranty status for product: {}, customer: {}", productId, customerId);
        
        try {
            ResponseEntity<WarrantyStatusResponse> response = restTemplate.getForEntity(url, WarrantyStatusResponse.class);
            if (response.getBody() != null) {
                return response.getBody().isValid();
            }
            return false;
        } catch (Exception e) {
            log.error("Failed to check warranty status: {}", e.getMessage());
            // Default to false if service is unavailable
            return false;
        }
    }
    
    /**
     * Register a warranty claim for a product
     */
    public boolean registerWarrantyClaim(Long productId, Long customerId, Long repairId) {
        String url = warrantyServiceUrl + "/api/v1/warranty/claim";
        log.info("Registering warranty claim for product: {}, customer: {}", productId, customerId);
        
        WarrantyClaimRequest request = new WarrantyClaimRequest();
        request.setProductId(productId);
        request.setCustomerId(customerId);
        request.setRepairId(repairId);
        
        try {
            ResponseEntity<WarrantyClaimResponse> response = restTemplate.postForEntity(url, request, WarrantyClaimResponse.class);
            if (response.getBody() != null) {
                return response.getBody().isRegistered();
            }
            return false;
        } catch (Exception e) {
            log.error("Failed to register warranty claim: {}", e.getMessage());
            return false;
        }
    }
    
    // Response model for warranty status check
    static class WarrantyStatusResponse {
        private boolean valid;
        private String expirationDate;
        
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        
        public String getExpirationDate() { return expirationDate; }
        public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }
    }
    
    // Request model for warranty claim
    static class WarrantyClaimRequest {
        private Long productId;
        private Long customerId;
        private Long repairId;
        
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        
        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        
        public Long getRepairId() { return repairId; }
        public void setRepairId(Long repairId) { this.repairId = repairId; }
    }
    
    // Response model for warranty claim registration
    static class WarrantyClaimResponse {
        private boolean registered;
        private String claimId;
        
        public boolean isRegistered() { return registered; }
        public void setRegistered(boolean registered) { this.registered = registered; }
        
        public String getClaimId() { return claimId; }
        public void setClaimId(String claimId) { this.claimId = claimId; }
    }
}

