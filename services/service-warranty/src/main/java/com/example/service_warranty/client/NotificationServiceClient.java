package com.example.service_warranty.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.service_warranty.dto.NotificationRequestDto;

import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceClient {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${service.notification.url:http://localhost:8085}")
    private String notificationServiceUrl;
    
    /**
     * Send warranty created (registration) notification
     */
    public void sendRepairCreatedNotification(NotificationRequestDto request) {
        log.info("Sending warranty registration notification to customer: {}", request.getCustomerId());
        
        try {     
            webClientBuilder.build()
                .post()
                .uri(notificationServiceUrl + "/api/v1/notifications/repair-created")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    log.error("Failed to send repair created notification: {}", e.getMessage());
                    return Mono.empty();
                })
                .subscribe();
            
            log.info("Repair created notification sent successfully to customer: {}", request.getCustomerId());
        } catch (Exception e) {
            log.error("Error sending repair created notification: {}", e.getMessage());
        }
    }
    
    /**
     * Send warranty rejected notification
     */
    public void sendWarrantyRejectedNotification(NotificationRequestDto request) {
        log.info("Sending warranty rejected notification to customer: {}", request.getCustomerId());
        
        try {
            webClientBuilder.build()
                .post()
                .uri(notificationServiceUrl + "/api/v1/notifications/repair-rejected")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    log.error("Failed to send repair rejected notification: {}", e.getMessage());
                    return Mono.empty();
                })
                .subscribe();
            
            log.info("Repair rejected notification sent successfully to customer: {}", request.getCustomerId());
        } catch (Exception e) {
            log.error("Error sending repair rejected notification: {}", e.getMessage());
        }
    }
    
    /**
     * Send warranty approved notification
     */
    public void sendWarrantyApprovedNotification(NotificationRequestDto request) {
        log.info("Sending warranty approved notification to customer: {}", request.getCustomerId());
        
        try {
            webClientBuilder.build()
                .post()
                .uri(notificationServiceUrl + "/api/v1/notifications/repair-approved")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    log.error("Failed to send repair approved notification: {}", e.getMessage());
                    return Mono.empty();
                })
                .subscribe();
            
            log.info("Repair approved notification sent successfully to customer: {}", request.getCustomerId());
        } catch (Exception e) {
            log.error("Error sending repair approved notification: {}", e.getMessage());
        }
    }
    
    /**
     * Send product received notification
     */
    public void sendProductReceivedNotification(NotificationRequestDto request) {
        log.info("Sending product received notification to customer: {}", request.getCustomerId());
        
        try {   
            webClientBuilder.build()
                .post()
                .uri(notificationServiceUrl + "/api/v1/notifications/product-received")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    log.error("Failed to send product received notification: {}", e.getMessage());
                    return Mono.empty();
                })
                .subscribe();
            
            log.info("Product received notification sent successfully to customer: {}", request.getCustomerId());
        } catch (Exception e) {
            log.error("Error sending product received notification: {}", e.getMessage());
        }
    }
    
    /**
     * Send repair completed notification
     */
    public void sendRepairCompletedNotification(Long customerId, Long warrantyRequestId, String message) {
        log.info("Sending repair completed notification to customer: {}", customerId);
        
        try {
            NotificationRequest request = new NotificationRequest();
            request.setCustomerId(customerId);
            request.setRelatedEntityId(warrantyRequestId);
            request.setMessage(message);
            
            webClientBuilder.build()
                .post()
                .uri(notificationServiceUrl + "/api/v1/notifications/repair-completed")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    log.error("Failed to send repair completed notification: {}", e.getMessage());
                    return Mono.empty();
                })
                .block();
            
            log.info("Repair completed notification sent successfully to customer: {}", customerId);
        } catch (Exception e) {
            log.error("Error sending repair completed notification: {}", e.getMessage());
        }
    }

    public void sendRepairInProgressNotification(NotificationRequestDto request) {
        log.info("Sending repair in progress notification to customer: {}", request.getCustomerId());
        
        try {   
            webClientBuilder.build()
                .post()
                .uri(notificationServiceUrl + "/api/v1/notifications/repair-in-progress")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    log.error("Failed to send repair in progress notification: {}", e.getMessage());
                    return Mono.empty();
                })
                .block();
            
            log.info("Repair in progress notification sent successfully to customer: {}", request.getCustomerId());
        } catch (Exception e) {
            log.error("Error sending repair in progress notification: {}", e.getMessage());
        }
    }
    
    public static class NotificationRequest {
        private Long customerId;
        private Long relatedEntityId;
        private String email;
        private String message;
        
        // Getters and setters
        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        
        public Long getRelatedEntityId() { return relatedEntityId; }
        public void setRelatedEntityId(Long relatedEntityId) { this.relatedEntityId = relatedEntityId; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    
}