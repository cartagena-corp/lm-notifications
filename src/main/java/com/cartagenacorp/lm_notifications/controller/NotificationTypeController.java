package com.cartagenacorp.lm_notifications.controller;

import com.cartagenacorp.lm_notifications.entity.NotificationType;
import com.cartagenacorp.lm_notifications.service.NotificationTypeService;
import com.cartagenacorp.lm_notifications.util.RequiresPermission;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification-types")
public class NotificationTypeController {

    private final NotificationTypeService notificationTypeService;

    public NotificationTypeController(NotificationTypeService notificationTypeService) {
        this.notificationTypeService = notificationTypeService;
    }

    @GetMapping
    @RequiresPermission({"NOTIFICATION_CRUD"})
    public List<NotificationType> getAll() {
        return notificationTypeService.getAll();
    }

    @PostMapping
    @RequiresPermission({"NOTIFICATION_TYPE_CRUD"})
    public ResponseEntity<NotificationType> create(@RequestBody NotificationType type) {
        return ResponseEntity.ok(notificationTypeService.create(type));
    }

    @DeleteMapping("/{name}")
    @RequiresPermission({"NOTIFICATION_TYPE_CRUD"})
    public ResponseEntity<?> delete(@PathVariable String name) {
        try {
            notificationTypeService.delete(name);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You cannot delete the notification type because it is used in preference");
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
