package com.cartagenacorp.lm_notifications.repository;

import com.cartagenacorp.lm_notifications.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserId(UUID userId);
    List<Notification> findByUserIdAndReadFalse(UUID userId);
    long countByUserIdAndReadFalse(UUID userId);
}
