package com.cartagenacorp.lm_notifications.repository;

import com.cartagenacorp.lm_notifications.entity.NotificationPreference;
import com.cartagenacorp.lm_notifications.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {
    List<NotificationPreference> findByUserId(UUID userId);
    Optional<NotificationPreference> findByUserIdAndType(UUID userId, NotificationType type);
}
