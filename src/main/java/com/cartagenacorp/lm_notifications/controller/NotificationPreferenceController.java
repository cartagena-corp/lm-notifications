package com.cartagenacorp.lm_notifications.controller;

import com.cartagenacorp.lm_notifications.dto.NotificationPreferenceDTO;
import com.cartagenacorp.lm_notifications.entity.NotificationPreference;
import com.cartagenacorp.lm_notifications.service.NotificationPreferenceService;
import com.cartagenacorp.lm_notifications.util.RequiresPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notification-preferences")
@CrossOrigin("*")
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
        try {
            service.updatePreferences(preferences);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assigned user not found");
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
