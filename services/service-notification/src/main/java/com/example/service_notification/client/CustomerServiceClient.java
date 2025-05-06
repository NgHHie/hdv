package com.example.service_notification.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceClient {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${service.customer.url}")
    private String customerServiceUrl;
    
    /**
     * Get purchase date for a specific customer and product
     */
    public String getCustomerEmail(Integer customerId) {
        String url = "/api/v1/customers/" + customerId;
        log.info("Getting customer from: {}{}", customerServiceUrl, url);
        
        try {
            CustomerResponse customer = webClientBuilder.build()
                    .get()
                    .uri(customerServiceUrl + url)
                    .retrieve()
                    .bodyToMono(CustomerResponse.class)
                    .block();
            if(customer != null) {
                log.warn("User found for id {}", customerId);
                return customer.getEmail(); 
            }
            return null;
        } catch (Exception e) {
            log.error("Failed to get customer: {}", e.getMessage());
            return null;
        }
    }
    
    // Response models
    public static class CustomerResponse {
        private Integer id;
        private String email;

        public Integer getId() { return id; }
        public String getEmail() { return email; }
    }
}
