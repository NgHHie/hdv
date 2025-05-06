package com.example.service_notification.service;

import com.example.service_notification.client.response.CustomerResponse;
import com.example.service_notification.client.service.CustomerClient;
import com.example.service_notification.event.WarrantyNotificationEvent;
import com.example.service_notification.model.NotificationType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.service_notification.dto.NotificationRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {


    private final NotificationService notificationService;
    private final CustomerClient customerClient;




    @KafkaListener(topics = "warranty-notifications", groupId = "notification-group")
    public void consumeWarrantyNotification(String messageJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Thêm module hỗ trợ ngày tháng nếu cần
            objectMapper.registerModule(new JavaTimeModule());

            WarrantyNotificationEvent event = objectMapper.readValue(messageJson, WarrantyNotificationEvent.class);


//            public class WarrantyNotificationCreateEvent {
//                private Long warrantyRequestId;
//                private Long customerId;
//                private String productName;
//
//                @Enumerated(EnumType.STRING)
//                private NotificationType type;
//            }


//            public class NotificationRequestDto {
//                private Integer customerId;
//                private NotificationType type;
//                private Integer relatedEntityId;
//                private String email;
//                private String message;
//            }

            log.info("Consumed warranty notification event: {}", event);

            System.out.println(event);

            NotificationRequestDto notificationRequest = new NotificationRequestDto();
            notificationRequest.setCustomerId(Math.toIntExact(event.getCustomerId()));
            notificationRequest.setType(event.getType());
            notificationRequest.setRelatedEntityId(Math.toIntExact(event.getWarrantyRequestId()));
            notificationRequest.setEmail(event.getEmail());
            notificationRequest.setMessage(event.getMessage());
            notificationService.sendNotification(notificationRequest);
        } catch (Exception e) {
            log.error("Error processing warranty notification: {}", e.getMessage(), e);
        }
    }
}
