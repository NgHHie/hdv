package com.example.service_notification.dto.mapper;

import com.example.service_notification.dto.NotificationResponseDto;
import com.example.service_notification.model.Notification;

public class NotificationMapper {



    public static NotificationResponseDto convertToResponseDto(Notification notification) {
        NotificationResponseDto responseDto = new NotificationResponseDto();
        responseDto.setId(notification.getId());
        responseDto.setCustomerId(notification.getCustomerId());
        responseDto.setType(notification.getType());
        responseDto.setWarrantyRequestId(notification.getWarrantyRequestId());
        responseDto.setEmail(notification.getEmail());
        responseDto.setSubject(notification.getSubject());
        responseDto.setStatus(notification.getStatus());
        responseDto.setSentAt(notification.getSentAt());
        return responseDto;
    }

}
