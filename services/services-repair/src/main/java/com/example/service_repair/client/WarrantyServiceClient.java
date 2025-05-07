package com.example.service_repair.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarrantyServiceClient {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${service.warranty.url}")
    private String warrantyServiceUrl;
    
    /**
     * Check if a product is still under warranty for a specific customer
     */
    public boolean checkWarrantyStatus(Integer productId, Integer customerId) {
        String url = "/api/v1/warranty/check?productId=" + productId + "&customerId=" + customerId;
        log.info("Checking warranty status for product: {}, customer: {}", productId, customerId);
        
        try {
            WarrantyStatusResponse response = webClientBuilder.build()
                .get()
                .uri(warrantyServiceUrl + url)
                .retrieve()
                .bodyToMono(WarrantyStatusResponse.class)
                .block(); // Blocking call to get synchronous behavior
            
            if (response != null) {
                return response.isValid();
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
    public boolean registerWarrantyClaim(Integer productId, Integer customerId, Integer repairId) {
        String url = "/api/v1/warranty/claim";
        log.info("Registering warranty claim for product: {}, customer: {}", productId, customerId);
        
        WarrantyClaimRequest request = new WarrantyClaimRequest();
        request.setProductId(productId);
        request.setCustomerId(customerId);
        request.setRepairId(repairId);
        
        try {
            WarrantyClaimResponse response = webClientBuilder.build()
                .post()
                .uri(warrantyServiceUrl + url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(WarrantyClaimResponse.class)
                .block(); // Blocking call to get synchronous behavior
            
            if (response != null) {
                return response.isRegistered();
            }
            return false;
        } catch (Exception e) {
            log.error("Failed to register warranty claim: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Update the status of a warranty in the warranty service
     */
    public void updateWarrantyStatus(Integer warrantyId, String status, String notes) {
        String url = "/api/v1/warranty/requests/" + warrantyId + "/update-repair-status";
        log.info("Updating warranty status to {} for warranty: {}", status, warrantyId);
        
        try {
            WarrantyStatusUpdateRequest request = new WarrantyStatusUpdateRequest();
            request.setStatus(status);
            request.setNotes(notes);
            
            webClientBuilder.build()
                .put()
                .uri(warrantyServiceUrl + url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    log.error("Failed to update warranty status: {}", e.getMessage());
                    return Mono.empty();
                })
                .block();
        } catch (Exception e) {
            log.error("Error updating warranty status: {}", e.getMessage());
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
        private Integer productId;
        private Integer customerId;
        private Integer repairId;
        
        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }
        
        public Integer getCustomerId() { return customerId; }
        public void setCustomerId(Integer customerId) { this.customerId = customerId; }
        
        public Integer getRepairId() { return repairId; }
        public void setRepairId(Integer repairId) { this.repairId = repairId; }
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
    
    // Request model for warranty status update
    static class WarrantyStatusUpdateRequest {
        private String status;
        private String notes;
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}