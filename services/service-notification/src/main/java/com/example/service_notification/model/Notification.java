package com.example.service_notification.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer customerId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Integer relatedEntityId;    // sau cos the suwr dunjg de link toi warranty request

    private String email;

    @Column(columnDefinition = "TEXT")
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime sentAt;




}
