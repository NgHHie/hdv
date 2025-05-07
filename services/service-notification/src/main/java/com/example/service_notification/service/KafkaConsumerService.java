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
  



    @KafkaListener(topics = "warranty-notifications", groupId = "notification-group")
    public void consumeWarrantyNotification(String messageJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
          
            objectMapper.registerModule(new JavaTimeModule());

            WarrantyNotificationEvent event = objectMapper.readValue(messageJson, WarrantyNotificationEvent.class);

            log.info("Consumed warranty notification event: {}", event);

          

            
            notificationService.sendNotification(event);
        } catch (Exception e) {
            log.error("Error processing warranty notification: {}", e.getMessage(), e);
        }
    }
}
