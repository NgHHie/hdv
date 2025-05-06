package com.example.service_notification.service;

import com.example.service_notification.dto.NotificationRequestDto;
import com.example.service_notification.dto.NotificationResponseDto;
import com.example.service_notification.dto.mapper.NotificationMapper;
import com.example.service_notification.model.Notification;
import com.example.service_notification.model.NotificationStatus;
import com.example.service_notification.model.NotificationTemplate;
import com.example.service_notification.model.NotificationType;
import com.example.service_notification.repository.NotificationRepository;
import com.example.service_notification.repository.NotificationTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String senderEmail;
    
    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                              NotificationTemplateRepository templateRepository,
                              JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.templateRepository = templateRepository;
        this.mailSender = mailSender;
    }
    

   


    public NotificationResponseDto sendNotification(NotificationRequestDto requestDto) {
        // Get template for the notification type
        Optional<NotificationTemplate> templateOpt = templateRepository.findByTypeAndIsActiveTrue(requestDto.getType());

        if (templateOpt.isEmpty()) {
            throw new IllegalArgumentException("No active template found for notification type: " + requestDto.getType());
        }

        NotificationTemplate template = templateOpt.get();

        // Create notification entity
        Notification notification = new Notification();
        notification.setCustomerId(requestDto.getCustomerId());
        notification.setType(requestDto.getType());
        notification.setRelatedEntityId(requestDto.getRelatedEntityId());
        notification.setEmail(requestDto.getEmail());
        notification.setSubject(template.getSubject());

        // Process content with message from request

        notification.setContent(content);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setCreatedAt(LocalDateTime.now());

        // Save notification first
        notification = notificationRepository.save(notification);

        // Send email
        boolean sendResult = sendEmail(notification);

        if (sendResult) {
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
        } else {
            notification.setStatus(NotificationStatus.FAILED);
        }

        // Update notification status
        notification = notificationRepository.save(notification);

        // Convert to response DTO
        return NotificationMapper.convertToResponseDto(notification);
    }
    private boolean sendEmail(Notification notification) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(notification.getEmail());
            message.setSubject(notification.getSubject());
            message.setText(notification.getContent());
            
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
     public List<NotificationResponseDto> getNotificationsByCustomerId(Long customerId) {
        List<Notification> notifications = notificationRepository.findByCustomerId(customerId);
        return notifications.stream()
                .map(NotificationMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }
    public List<NotificationResponseDto> getNotificationsByRelatedEntityId(Long relatedEntityId) {
        List<Notification> notifications = notificationRepository.findByRelatedEntityId(relatedEntityId);
        return notifications.stream()
                .map(NotificationMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }
   
}
