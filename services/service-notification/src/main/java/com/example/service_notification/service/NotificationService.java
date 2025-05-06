package com.example.service_notification.service;

import com.example.service_notification.dto.NotificationRequestDto;
import com.example.service_notification.dto.NotificationResponseDto;
import com.example.service_notification.dto.mapper.NotificationMapper;
import com.example.service_notification.event.WarrantyNotificationEvent;
import com.example.service_notification.model.Notification;
import com.example.service_notification.model.NotificationStatus;
import com.example.service_notification.model.NotificationTemplate;
import com.example.service_notification.model.NotificationType;
import com.example.service_notification.repository.NotificationRepository;
import com.example.service_notification.repository.NotificationTemplateRepository;

import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
    

   


    public NotificationResponseDto sendNotification(WarrantyNotificationEvent notificationEvent) {
        // Get template for the notification type
        Optional<NotificationTemplate> templateOpt = templateRepository.findByTypeAndIsActiveTrue(notificationEvent.getType());

        if (templateOpt.isEmpty()) {
            throw new IllegalArgumentException("No active template found for notification type: " + notificationEvent.getType());
        }

        Random random = new Random();
        NotificationTemplate template = templateOpt.get();

        

        //notification template
        
        //     private Integer id;

        //     @Enumerated(EnumType.STRING)
        //     private NotificationType type;

        //     private String subject;

        //     @Column(columnDefinition = "TEXT")
        //     private String contentTemplate;

        //     private Boolean isActive;
        // }





                //notification entity


            // private Integer id;

            // private Integer customerId;

            // @Enumerated(EnumType.STRING)
            // private NotificationType type;

            // private Integer warrantyRequestId;    // sau cos the suwr dunjg de link toi warranty request

            // private String email;

            // @Column(columnDefinition = "TEXT")
            // private String subject;

            // @Column(columnDefinition = "TEXT")
            // private String content;

            // @Enumerated(EnumType.STRING)
            // private NotificationStatus status;

            // private LocalDateTime createdAt;
            // private LocalDateTime sentAt;




        // Create notification entity
        Notification notification = new Notification();
        notification.setCustomerId(notificationEvent.getCustomerId());
        notification.setType(notificationEvent.getType());
        notification.setWarrantyRequestId(notificationEvent.getWarrantyRequestId());
        notification.setEmail(notificationEvent.getEmail());
        notification.setSubject(template.getSubject());

        // Process content with message from request

        String contentTemplate = template.getContentTemplate(); 
        NotificationType type = notificationEvent.getType();
        switch (type) {
            case NotificationType.WARRANTY_CREATE:
                contentTemplate = contentTemplate.replace("{{warrantyRequestId}}", String.valueOf(notificationEvent.getWarrantyRequestId()));
                contentTemplate = contentTemplate.replace("{{customerName}}", notificationEvent.getCustomerName());
                contentTemplate = contentTemplate.replace("{{productName}}", notificationEvent.getProductName());
                break;
            case NotificationType.WARRANTY_REJECT:
                contentTemplate = contentTemplate.replace("{{customerName}}", notificationEvent.getCustomerName());
                contentTemplate = contentTemplate.replace("{{message}}", notificationEvent.getMessage());
                break;

            case NotificationType.WARRANTY_APPROVED:
                contentTemplate = contentTemplate.replace("{{customerName}}", notificationEvent.getCustomerName());
                contentTemplate = contentTemplate.replace("{{productName}}", notificationEvent.getProductName());
                break;
            case NotificationType.PRODUCT_RECEIVED:
                contentTemplate = contentTemplate.replace("{{customerName}}", notificationEvent.getCustomerName());
                contentTemplate = contentTemplate.replace("{{productName}}", notificationEvent.getProductName());
                contentTemplate = contentTemplate.replace("{{warrantyRequestId}}", String.valueOf(notificationEvent.getWarrantyRequestId()));
                contentTemplate = contentTemplate.replace("{{message}}", notificationEvent.getMessage());
            case NotificationType.DIAGNOSIS_STARTED:
                contentTemplate = contentTemplate.replace("{{customerName}}", notificationEvent.getCustomerName());
                contentTemplate = contentTemplate.replace("{{productName}}", notificationEvent.getProductName());
                contentTemplate = contentTemplate.replace("{{message}}", notificationEvent.getMessage());
            case NotificationType.REPAIR_IN_PROGRESS:
                contentTemplate = contentTemplate.replace("{{customerName}}", notificationEvent.getCustomerName());
                contentTemplate = contentTemplate.replace("{{productName}}", notificationEvent.getProductName());
                contentTemplate = contentTemplate.replace("{{message}}", notificationEvent.getMessage());
                break;
            case NotificationType.REPAIR_COMPLETED:
                contentTemplate = contentTemplate.replace("{{customerName}}", notificationEvent.getCustomerName());
                contentTemplate = contentTemplate.replace("{{productName}}", notificationEvent.getProductName());
                contentTemplate = contentTemplate.replace("{{message}}", notificationEvent.getMessage());
                break;
            case NotificationType.PRODUCT_SHIPPING:
                contentTemplate = contentTemplate.replace("{{customerName}}", notificationEvent.getCustomerName());
                contentTemplate = contentTemplate.replace("{{productName}}", notificationEvent.getProductName());
                contentTemplate = contentTemplate.replace("{{message}}", notificationEvent.getMessage());
                // contentTemplate = contentTemplate.replace("{{trackingNumber}}", String.valueOf(random.nextInt(100000))); // hiep nho them url de thuc hien gui survey
                break;
            case NotificationType.PRODUCT_DELIVERED:
                contentTemplate = contentTemplate.replace("{{customerName}}", notificationEvent.getCustomerName());
                contentTemplate = contentTemplate.replace("{{productName}}", notificationEvent.getProductName());
                contentTemplate = contentTemplate.replace("{{message}}", notificationEvent.getMessage());
                break;
            case NotificationType.FEEDBACK_REQUEST:
                contentTemplate = contentTemplate.replace("{{customerName}}", notificationEvent.getCustomerName());
                contentTemplate = contentTemplate.replace("{{productName}}", notificationEvent.getProductName());
                contentTemplate = contentTemplate.replace("{{message}}", notificationEvent.getMessage());
                // contentTemplate = contentTemplate.replace("{{feedbackLink}}", notificationEvent.getMessage());  // hiep nho them url de thuc hien gui survey

                break;
            default:
                break;
        }
        notification.setContent(contentTemplate);
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
     public List<NotificationResponseDto> getNotificationsByCustomerId(Integer customerId) {
        List<Notification> notifications = notificationRepository.findByCustomerId(customerId);
        return notifications.stream()
                .map(NotificationMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }
    public List<NotificationResponseDto> getNotificationsByRelatedEntityId(Integer relatedEntityId) {
        List<Notification> notifications = notificationRepository.findByWarrantyRequestId(relatedEntityId);
        return notifications.stream()
                .map(NotificationMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }
   
}
