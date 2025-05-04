package com.example.service_warranty.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepairServiceClient {
    
    private final RestTemplate restTemplate;
    
    @Value("${service.repair.url}")
    private String repairServiceUrl;
    
    /**
     * Create a repair request
     */
    public Long createRepairRequest(Long customerId, Long productId, Long warrantyId, 
                                  String issueDescription, String imageUrls) {
        String url = repairServiceUrl + "/api/v1/repairs";
        log.info("Creating repair request for customer: {}, product: {}", customerId, productId);
        
        RepairRequestDto request = new RepairRequestDto();
        request.setCustomerId(customerId);
        request.setProductId(productId);
        request.setWarrantyId(warrantyId);
        request.setIssueDescription(issueDescription);
        request.setImageUrls(imageUrls);
        
        try {
            ResponseEntity<RepairResponseDto> response = 
                    restTemplate.postForEntity(url, request, RepairResponseDto.class);
            
            if (response.getBody() != null) {
                return response.getBody().getId();
            }
            return null;
        } catch (Exception e) {
            log.error("Failed to create repair request: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Check repair status
     */
    public String getRepairStatus(Long repairId) {
        String url = repairServiceUrl + "/api/v1/repairs/" + repairId;
        log.info("Checking repair status for repair: {}", repairId);
        
        try {
            ResponseEntity<RepairResponseDto> response = 
                    restTemplate.getForEntity(url, RepairResponseDto.class);
            
            if (response.getBody() != null) {
                return response.getBody().getStatus();
            }
            return null;
        } catch (Exception e) {
            log.error("Failed to check repair status: {}", e.getMessage());
            return null;
        }
    }
    
    // DTO for repair request
    public static class RepairRequestDto {
        private Long warrantyId;
        private Long customerId;
        private Long productId;
        private String issueDescription;
        private String imageUrls;
        
        // Getters and setters
        public Long getWarrantyId() { return warrantyId; }
        public void setWarrantyId(Long warrantyId) { this.warrantyId = warrantyId; }
        
        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        
        public String getIssueDescription() { return issueDescription; }
        public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }
        
        public String getImageUrls() { return imageUrls; }
        public void setImageUrls(String imageUrls) { this.imageUrls = imageUrls; }
    }
    
    // DTO for repair response
    public static class RepairResponseDto {
        private Long id;
        private String status;
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}