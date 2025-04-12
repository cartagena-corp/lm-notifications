package com.cartagenacorp.lm_notifications.controller;

import com.cartagenacorp.lm_notifications.dto.NotificationDTO;
import com.cartagenacorp.lm_notifications.dto.NotificationRequestDTO;
import com.cartagenacorp.lm_notifications.entity.Notification;
import com.cartagenacorp.lm_notifications.mapper.NotificationMapper;
import com.cartagenacorp.lm_notifications.service.NotificationService;
import com.cartagenacorp.lm_notifications.util.RequiresPermission;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin("*")
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
        try {
            Notification notification = service.createNotification(request.getUserId(), request.getMessage(), request.getType(), request.getMetadata());
            if (notification == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(notificationMapper.toDTO(notification));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assigned user not found");
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
        try {
            service.markAsRead(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/read-all")
    @RequiresPermission({"NOTIFICATION_CRUD"})
    public ResponseEntity<?> markAllRead() {
        try {
            service.markAllAsRead();
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @RequiresPermission({"NOTIFICATION_CRUD"})
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            service.deleteNotification(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-all")
    @RequiresPermission({"NOTIFICATION_CRUD"})
    public ResponseEntity<?> deleteAll() {
        try {
            service.deleteAllNotifications();
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
