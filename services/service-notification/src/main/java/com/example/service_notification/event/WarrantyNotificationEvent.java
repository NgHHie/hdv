package com.example.service_notification.event;

import com.example.service_notification.model.NotificationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private String message;
    private String customerName;
}