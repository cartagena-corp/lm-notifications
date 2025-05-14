package com.cartagenacorp.lm_notifications.controller;

import com.cartagenacorp.lm_notifications.dto.NotificationDTO;
import com.cartagenacorp.lm_notifications.dto.NotificationRequestDTO;
import com.cartagenacorp.lm_notifications.entity.Notification;
import com.cartagenacorp.lm_notifications.mapper.NotificationMapper;
import com.cartagenacorp.lm_notifications.service.NotificationService;
import com.cartagenacorp.lm_notifications.util.RequiresPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;
    private final NotificationMapper notificationMapper;

    @Autowired
    public NotificationController(NotificationService service, NotificationMapper notificationMapper) {
        this.service = service;
        this.notificationMapper = notificationMapper;
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody NotificationRequestDTO request) {
        Notification notification = service.createNotification(
                request.getUserId(),
                request.getMessage(),
                request.getType(),
                request.getMetadata(),
                request.getProjectId(),
                request.getIssueId()
        );
        if (notification == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notificationMapper.toDTO(notification));
    }

    @GetMapping
    @RequiresPermission({"NOTIFICATION_CRUD"})
    public List<NotificationDTO> getAll() {
        return service.getNotifications().stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/read")
    @RequiresPermission({"NOTIFICATION_CRUD"})
    public ResponseEntity<?> markRead(@PathVariable UUID id) {
        service.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/read-all")
    @RequiresPermission({"NOTIFICATION_CRUD"})
    public ResponseEntity<?> markAllRead() {
        service.markAllAsRead();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @RequiresPermission({"NOTIFICATION_CRUD"})
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        service.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-all")
    @RequiresPermission({"NOTIFICATION_CRUD"})
    public ResponseEntity<?> deleteAll() {
        service.deleteAllNotifications();
        return ResponseEntity.noContent().build();
    }
}
