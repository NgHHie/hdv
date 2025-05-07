package com.example.service_warranty.client;

import com.example.service_warranty.client.ProductServiceClient.ProductResponse;
import com.example.service_warranty.dto.WarrantyValidationDto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConditionServiceClient {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${service.condition.url}")
    private String conditionServiceUrl;
    
    /**
     * Validate warranty request against warranty conditions
     */
    public Boolean validateWarrantyConditions(WarrantyValidationDto validationDto, ProductResponse product) {
        String url = "/api/v1/conditions/validate";
        log.info("Sending validation request to condition service for warranty request: {}", 
                validationDto.getWarrantyRequestId());
        ValidationDTO request = new ValidationDTO();
        request.setWarrantyValidationDto(validationDto);
        request.setProduct(product);
        try {
            return webClientBuilder.build()
                    .post()
                    .uri(conditionServiceUrl + url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to validate warranty conditions: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Get all active warranty conditions
     */
    public List<ConditionDTO> getAllActiveConditions() {
        String url = "/api/v1/conditions/active";
        log.info("Getting all active warranty conditions");
        
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(conditionServiceUrl + url)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to get active warranty conditions: {}", e.getMessage());
            return List.of();
        }
    }
    
    // DTO for condition
    public static class ConditionDTO {
        private Integer id;
        private String name;
        private String description;
        private Boolean isActive;
        
        // Getters and setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public Boolean getIsActive() { return isActive; }
        public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    }

    @Data
    public static class ValidationDTO {
        WarrantyValidationDto warrantyValidationDto;
        ProductResponse product;
    }
}