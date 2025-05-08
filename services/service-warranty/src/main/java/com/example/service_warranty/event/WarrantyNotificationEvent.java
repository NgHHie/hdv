package com.example.service_warranty.event;

import com.example.service_warranty.models.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyNotificationEvent {
    private Integer warrantyRequestId;
    private String email;
    private String productName;
    private Integer customerId;

   
    private NotificationType type;
    private String message;
    private String customerName;
}