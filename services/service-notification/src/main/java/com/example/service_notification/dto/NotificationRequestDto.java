package com.example.service_notification.dto;

import com.example.service_notification.model.NotificationType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequestDto {
    private Integer customerId;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private Integer warrantyRequestId;
    private String customerName;
    private String email;
    private String message;
    private String productName;
}
