package com.example.service_notification.repository;

import com.example.service_notification.model.Notification;
import com.example.service_notification.model.NotificationStatus;
import com.example.service_notification.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByCustomerId(Integer customerId);

    List<Notification> findByType(NotificationType type);

    List<Notification> findByStatus(NotificationStatus status);

    List<Notification> findByWarrantyRequestId(Integer warrantyRequestId);

    List<Notification> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Notification> findByCustomerIdAndType(Integer customerId, NotificationType type);
}
