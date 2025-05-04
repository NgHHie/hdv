package com.example.service_notification.repository;

import com.example.service_notification.model.NotificationTemplate;
import com.example.service_notification.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationTemplateRepository  extends JpaRepository<NotificationTemplate, Integer> {
    Optional<NotificationTemplate> findByTypeAndIsActiveTrue(NotificationType type);

    List<NotificationTemplate> findByType(NotificationType type);

    List<NotificationTemplate> findByIsActiveTrue();
}
