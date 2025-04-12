package com.cartagenacorp.lm_notifications.service;

import com.cartagenacorp.lm_notifications.entity.Notification;
import com.cartagenacorp.lm_notifications.entity.NotificationType;
import com.cartagenacorp.lm_notifications.repository.NotificationRepository;
import com.cartagenacorp.lm_notifications.repository.NotificationTypeRepository;
import com.cartagenacorp.lm_notifications.util.JwtContextHolder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationPreferenceService notificationPreferenceService;
    private final NotificationTypeRepository notificationTypeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate,
                               NotificationPreferenceService notificationPreferenceService,
                               NotificationTypeRepository notificationTypeRepository) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
        this.notificationPreferenceService = notificationPreferenceService;
        this.notificationTypeRepository = notificationTypeRepository;
    }

    public Notification createNotification(UUID userId, String message, String typeName, JsonNode metadata) {
        NotificationType type = notificationTypeRepository.findById(typeName)
                .orElseThrow(() -> new IllegalArgumentException("Type of notification not valid: " + typeName));

        if (!notificationPreferenceService.isEnabled(userId, type)) {
            return null;
        }

        String metadataString = null;
        try {
            if (metadata != null) {
                metadataString = objectMapper.writeValueAsString(metadata);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON format for metadata", e);
        }

        Notification notification = new Notification(null, userId, message, type, false, LocalDateTime.now(), metadataString);
        Notification saved = notificationRepository.save(notification);

        long unreadCount = notificationRepository.countByUserIdAndReadFalse(userId);
        Map<String, Object> payload = new HashMap<>();
        payload.put("message", message);
        payload.put("unreadCount", unreadCount);
        payload.put("metadata", metadata);

        messagingTemplate.convertAndSend("/topic/notifications/" + userId, payload);
        return saved;
    }

    public List<Notification> getNotifications() {
        UUID userId = JwtContextHolder.getUserId();
        return notificationRepository.findByUserId(userId);
    }

    public void markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        if (!notification.isRead()) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    public void markAllAsRead() {
        UUID userId = JwtContextHolder.getUserId();
        List<Notification> notifications = notificationRepository.findByUserIdAndReadFalse(userId);

        if (notifications.isEmpty()) {
            throw new EntityNotFoundException("There are no unread notifications");
        }

        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

    public void deleteNotification(UUID id) {
        if (!notificationRepository.existsById(id)) {
            throw new EntityNotFoundException("Notification not found");
        }
        notificationRepository.deleteById(id);
    }

    public void deleteAllNotifications() {
        UUID userId = JwtContextHolder.getUserId();
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        if (notifications.isEmpty()) {
            throw new EntityNotFoundException("No notifications found");
        }
        notificationRepository.deleteAll(notifications);

    }
}
