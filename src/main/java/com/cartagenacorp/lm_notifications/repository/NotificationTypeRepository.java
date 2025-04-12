package com.cartagenacorp.lm_notifications.repository;

import com.cartagenacorp.lm_notifications.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationType, String> {
}
