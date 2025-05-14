package com.cartagenacorp.lm_notifications.controller;

import com.cartagenacorp.lm_notifications.dto.NotificationPreferenceDTO;
import com.cartagenacorp.lm_notifications.entity.NotificationPreference;
import com.cartagenacorp.lm_notifications.service.NotificationPreferenceService;
import com.cartagenacorp.lm_notifications.util.RequiresPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification-preferences")
public class NotificationPreferenceController {

    private final NotificationPreferenceService service;

    @Autowired
    public NotificationPreferenceController(NotificationPreferenceService service) {
        this.service = service;
    }

    @GetMapping
    @RequiresPermission({"NOTIFICATION_CRUD"})
    public ResponseEntity<List<NotificationPreference>> getPreferences() {
        return ResponseEntity.ok(service.getUserPreferences());
    }

    @PutMapping
    @RequiresPermission({"NOTIFICATION_CRUD"})
    public ResponseEntity<?> updateAll(@RequestBody List<NotificationPreferenceDTO> preferences) {
        service.updatePreferences(preferences);
        return ResponseEntity.noContent().build();
    }

}
