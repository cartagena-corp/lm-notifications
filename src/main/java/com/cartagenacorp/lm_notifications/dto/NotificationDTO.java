package com.cartagenacorp.lm_notifications.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private UUID id;
    private String message;
    private String type;
    private boolean read;
    private LocalDateTime timestamp;
    private JsonNode metadata;
    private UUID projectId;
    private UUID issueId;
}
