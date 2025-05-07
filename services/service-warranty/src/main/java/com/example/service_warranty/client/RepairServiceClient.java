package com.example.service_warranty.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.service_warranty.dto.RepairDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepairServiceClient {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${service.repair.url}")
    private String repairServiceUrl;
    
    /**
     * Create a repair request
     */
    public Integer createRepairRequest(Integer customerId, Integer productId, Integer warrantyId, 
        String issueDescription, String imageUrls) {
        String url = "/api/v1/repairs";
        log.info("Creating repair request for customer: {}, product: {}", customerId, productId);

        RepairRequestDto request = new RepairRequestDto();
        request.setCustomerId(customerId);
        request.setProductId(productId);
        request.setWarrantyId(warrantyId);
        request.setIssueDescription(issueDescription);
        request.setImageUrls(imageUrls);

        try {
            RepairResponseDto response = webClientBuilder.baseUrl(repairServiceUrl)
            .build()
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(RepairResponseDto.class)
            .block(); // Blocking call to get synchronous behavior

            if (response != null) {
                return response.getId();
            }
            return null;
        } catch (Exception e) {
            log.error("Failed to create repair request: {}", e.getMessage());
            return null;
        }
    }

    public RepairDto getRepairById(Integer repairId) {
        String url = "/api/v1/repairs/" + repairId;
        log.info("Getting repair details by id: {}", repairId);
        
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(repairServiceUrl + url)
                    .retrieve()
                    .bodyToMono(RepairDto.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to get repair details: {}", e.getMessage());
            return null;
        }
    }
        
    
    // DTO for repair request
    public static class RepairRequestDto {
        private Integer warrantyId;
        private Integer customerId;
        private Integer productId;
        private String issueDescription;
        private String imageUrls;
        
        // Getters and setters
        public Integer getWarrantyId() { return warrantyId; }
        public void setWarrantyId(Integer warrantyId) { this.warrantyId = warrantyId; }
        
        public Integer getCustomerId() { return customerId; }
        public void setCustomerId(Integer customerId) { this.customerId = customerId; }
        
        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }
        
        public String getIssueDescription() { return issueDescription; }
        public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }
        
        public String getImageUrls() { return imageUrls; }
        public void setImageUrls(String imageUrls) { this.imageUrls = imageUrls; }
    }
    
    // DTO for repair response
    public static class RepairResponseDto {
        private Integer id;
        private String status;
        
        // Getters and setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}