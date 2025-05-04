package com.example.service_notification.model;

import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "notification_templates")
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String contentTemplate;

    private Boolean isActive;
}
