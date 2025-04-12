package com.cartagenacorp.lm_notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPreferenceDTO {
    private String type;
    private boolean enabled;
}
