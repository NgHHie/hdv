package com.example.service_notification.controllers;

import com.example.service_notification.dto.NotificationRequestDto;
import com.example.service_notification.dto.NotificationResponseDto;
import com.example.service_notification.model.NotificationType;
import com.example.service_notification.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    
    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    // @PostMapping
    // public ResponseEntity<NotificationResponseDto> sendNotification(@RequestBody NotificationRequestDto requestDto) {
    //     try {
    //         NotificationResponseDto responseDto = notificationService.sendNotification(requestDto);
    //         return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }
    // }
    
    // @PostMapping("/repair-created")
    // public ResponseEntity<NotificationResponseDto> sendRepairCreatedNotification(@RequestBody NotificationRequestDto requestDto) {
    //     requestDto.setType(NotificationType.WARRANTY_CREATE);
    //     return sendNotification(requestDto);
    // }
    
    // @PostMapping("/repair-rejected")
    // public ResponseEntity<NotificationResponseDto> sendRepairRejectedNotification(@RequestBody NotificationRequestDto requestDto) {
    //     requestDto.setType(NotificationType.WARRANTY_REJECT);
    //     return sendNotification(requestDto);
    // }
    
    // @PostMapping("/repair-approved")
    // public ResponseEntity<NotificationResponseDto> sendRepairApprovedNotification(@RequestBody NotificationRequestDto requestDto) {
    //     requestDto.setType(NotificationType.WARRANTY_APPROVED);
    //     return sendNotification(requestDto);
    // }
    
    // @PostMapping("/product-received")
    // public ResponseEntity<NotificationResponseDto> sendProductReceivedNotification(@RequestBody NotificationRequestDto requestDto) {
    //     requestDto.setType(NotificationType.PRODUCT_RECEIVED);
    //     return sendNotification(requestDto);
    // }
    
  
    
    // @PostMapping("/repair-completed")
    // public ResponseEntity<NotificationResponseDto> sendRepairCompletedNotification(@RequestBody NotificationRequestDto requestDto) {
    //     requestDto.setType(NotificationType.REPAIR_COMPLETED);
    //     return sendNotification(requestDto);
    // }
    
    // @PostMapping("/product-shipping")
    // public ResponseEntity<NotificationResponseDto> sendProductShippingNotification(@RequestBody NotificationRequestDto requestDto) {
    //     requestDto.setType(NotificationType.PRODUCT_SHIPPING);
    //     return sendNotification(requestDto);
    // }
    
    // @PostMapping("/product-delivered")
    // public ResponseEntity<NotificationResponseDto> sendProductDeliveredNotification(@RequestBody NotificationRequestDto requestDto) {
    //     requestDto.setType(NotificationType.PRODUCT_DELIVERED);
    //     return sendNotification(requestDto);
    // }
    
    // @PostMapping("/feedback-request")
    // public ResponseEntity<NotificationResponseDto> sendFeedbackRequestNotification(@RequestBody NotificationRequestDto requestDto) {
    //     requestDto.setType(NotificationType.FEEDBACK_REQUEST);
    //     return sendNotification(requestDto);
    // }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByCustomerId(@PathVariable Integer customerId) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByCustomerId(customerId);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }
    
    @GetMapping("/related/{relatedEntityId}")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByRelatedEntityId(@PathVariable Integer relatedEntityId) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByRelatedEntityId(relatedEntityId);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }
}