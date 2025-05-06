package com.example.service_warranty.event;

import com.example.service_warranty.dto.NotificationType;
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
    private String email;
    private String productName;
    private Long customerId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private String message;
}