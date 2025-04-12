package com.cartagenacorp.lm_notifications.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {
    private UUID userId;
    private String message;
    private String type;
    private JsonNode metadata;
}
