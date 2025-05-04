package com.example.service_notification.dto;

import com.example.service_notification.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateRequestDto {
    private NotificationType type;
    private String subject;
    private String contentTemplate;
    private Boolean isActive;
}
