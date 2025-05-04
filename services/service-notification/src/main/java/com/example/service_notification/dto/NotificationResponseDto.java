package com.example.service_notification.dto;

import com.example.service_notification.model.NotificationStatus;
import com.example.service_notification.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private Integer id;
    private Integer customerId;
    private NotificationType type;
    private Integer relatedEntityId;
    private String email;
    private String subject;
    private NotificationStatus status;
    private LocalDateTime sentAt;
}
