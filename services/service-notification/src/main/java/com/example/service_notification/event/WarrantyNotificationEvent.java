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
    private Long warrantyRequestId;
    private Long customerId;
    private String productName;

    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private String message;
}