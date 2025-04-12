package com.cartagenacorp.lm_notifications.mapper;

import com.cartagenacorp.lm_notifications.dto.NotificationDTO;
import com.cartagenacorp.lm_notifications.entity.Notification;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationMapper {

    private final ObjectMapper objectMapper;

    @Autowired
    public NotificationMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public NotificationDTO toDTO(Notification notification) {
        JsonNode metadataNode = null;

        try {
            if (notification.getMetadata() != null) {
                metadataNode = objectMapper.readTree(notification.getMetadata());
            }
        } catch (Exception e) {
            metadataNode = null;
        }


        return new NotificationDTO(
                notification.getId(),
                notification.getMessage(),
                notification.getType().getName(),
                notification.isRead(),
                notification.getTimestamp(),
                metadataNode
        );
    }
}

