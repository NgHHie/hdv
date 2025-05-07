package com.example.service_warranty.dto;

import com.example.service_warranty.models.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto {
    private Integer warrantyResponseId;
    private Integer customerId;
    private NotificationType type;
    private String email;
    private String message;
}

