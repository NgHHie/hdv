package com.example.service_repair.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// Client for the Notification Service
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceClient {
    
    private final RestTemplate restTemplate;
    
    @Value("${service.notification.url}")
    private String notificationServiceUrl;
    
    public void sendRepairRequestCreatedNotification(Long customerId, Long repairId) {
        String url = notificationServiceUrl + "/api/v1/notifications/repair-created";
        log.info("Sending repair created notification to customer: {}", customerId);
        
        NotificationRequest request = new NotificationRequest();
        request.setCustomerId(customerId);
        request.setRepairId(repairId);
        request.setType("REPAIR_CREATED");
        request.setMessage("Your repair request has been created successfully. We will notify you when we receive your product.");
        
        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            log.error("Failed to send repair created notification: {}", e.getMessage());
        }
    }
    
    public void sendProductReceivedNotification(Long customerId, Long repairId) {
        String url = notificationServiceUrl + "/api/v1/notifications/product-received";
        log.info("Sending product received notification to customer: {}", customerId);
        
        NotificationRequest request = new NotificationRequest();
        request.setCustomerId(customerId);
        request.setRepairId(repairId);
        request.setType("PRODUCT_RECEIVED");
        request.setMessage("We have received your product and will begin diagnosis soon.");
        
        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            log.error("Failed to send product received notification: {}", e.getMessage());
        }
    }
    
    public void sendRepairDiagnosisStartedNotification(Long customerId, Long repairId) {
        String url = notificationServiceUrl + "/api/v1/notifications/diagnosis-started";
        log.info("Sending diagnosis started notification to customer: {}", customerId);
        
        NotificationRequest request = new NotificationRequest();
        request.setCustomerId(customerId);
        request.setRepairId(repairId);
        request.setType("DIAGNOSIS_STARTED");
        request.setMessage("We have started diagnosing your product. We'll update you once we complete the diagnosis.");
        
        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            log.error("Failed to send diagnosis started notification: {}", e.getMessage());
        }
    }
    
    public void sendRepairApprovalRequiredNotification(Long customerId, Long repairId, Double cost) {
        String url = notificationServiceUrl + "/api/v1/notifications/approval-required";
        log.info("Sending approval required notification to customer: {}", customerId);
        
        NotificationRequest request = new NotificationRequest();
        request.setCustomerId(customerId);
        request.setRepairId(repairId);
        request.setType("APPROVAL_REQUIRED");
        request.setMessage(String.format(
            "Your product requires repairs that are not covered by warranty. The estimated cost is $%.2f. Please approve or decline the repair.", 
            cost));
        
        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            log.error("Failed to send approval required notification: {}", e.getMessage());
        }
    }
    
    public void sendRepairInProgressNotification(Long customerId, Long repairId) {
        String url = notificationServiceUrl + "/api/v1/notifications/repair-progress";
        log.info("Sending repair in progress notification to customer: {}", customerId);
        
        NotificationRequest request = new NotificationRequest();
        request.setCustomerId(customerId);
        request.setRepairId(repairId);
        request.setType("REPAIR_IN_PROGRESS");
        request.setMessage("Repairs on your product have begun. We'll notify you when repairs are complete.");
        
        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            log.error("Failed to send repair in progress notification: {}", e.getMessage());
        }
    }
    
    public void sendRepairCompletedNotification(Long customerId, Long repairId) {
        String url = notificationServiceUrl + "/api/v1/notifications/repair-completed";
        log.info("Sending repair completed notification to customer: {}", customerId);
        
        NotificationRequest request = new NotificationRequest();
        request.setCustomerId(customerId);
        request.setRepairId(repairId);
        request.setType("REPAIR_COMPLETED");
        request.setMessage("Repairs on your product have been completed. We're preparing it for delivery.");
        
        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            log.error("Failed to send repair completed notification: {}", e.getMessage());
        }
    }
    
    public void sendProductShippingNotification(Long customerId, Long repairId) {
        String url = notificationServiceUrl + "/api/v1/notifications/product-shipping";
        log.info("Sending product shipping notification to customer: {}", customerId);
        
        NotificationRequest request = new NotificationRequest();
        request.setCustomerId(customerId);
        request.setRepairId(repairId);
        request.setType("PRODUCT_SHIPPING");
        request.setMessage("Your repaired product is on its way to you. You'll receive it shortly.");
        
        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            log.error("Failed to send product shipping notification: {}", e.getMessage());
        }
    }
    
    public void sendProductDeliveredNotification(Long customerId, Long repairId) {
        String url = notificationServiceUrl + "/api/v1/notifications/product-delivered";
        log.info("Sending product delivered notification to customer: {}", customerId);
        
        NotificationRequest request = new NotificationRequest();
        request.setCustomerId(customerId);
        request.setRepairId(repairId);
        request.setType("PRODUCT_DELIVERED");
        request.setMessage("Your product has been delivered. Please let us know if you have any feedback on our repair service.");
        
        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            log.error("Failed to send product delivered notification: {}", e.getMessage());
        }
    }
    
    public void sendRepairRequestRejectedNotification(Long customerId, Long repairId, String reason) {
        String url = notificationServiceUrl + "/api/v1/notifications/repair-rejected";
        log.info("Sending repair rejected notification to customer: {}", customerId);
        
        NotificationRequest request = new NotificationRequest();
        request.setCustomerId(customerId);
        request.setRepairId(repairId);
        request.setType("REPAIR_REJECTED");
        request.setMessage("We're sorry, but your repair request has been rejected. Reason: " + reason);
        
        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            log.error("Failed to send repair rejected notification: {}", e.getMessage());
        }
    }
    
    public void sendRepairRequestCancelledNotification(Long customerId, Long repairId) {
        String url = notificationServiceUrl + "/api/v1/notifications/repair-cancelled";
        log.info("Sending repair cancelled notification to customer: {}", customerId);
        
        NotificationRequest request = new NotificationRequest();
        request.setCustomerId(customerId);
        request.setRepairId(repairId);
        request.setType("REPAIR_CANCELLED");
        request.setMessage("Your repair request has been cancelled as requested.");
        
        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            log.error("Failed to send repair cancelled notification: {}", e.getMessage());
        }
    }
}

// Request model for notifications
class NotificationRequest {
    private Long customerId;
    private Long repairId;
    private String type;
    private String message;
    
    // Getters and setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    
    public Long getRepairId() { return repairId; }
    public void setRepairId(Long repairId) { this.repairId = repairId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}