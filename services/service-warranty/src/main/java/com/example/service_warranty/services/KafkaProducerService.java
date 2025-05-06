package com.example.service_warranty.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.service_warranty.event.WarrantyNotificationEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String NOTIFICATIONTOPIC = "warranty-notifications";

    public void sendWarrantyEvent(WarrantyNotificationEvent event) {
        log.info("Sending warranty notification event to Kafka: {}", event);
        kafkaTemplate.send(NOTIFICATIONTOPIC, event);
    }
}