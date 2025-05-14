package com.cartagenacorp.lm_notifications.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID userId;
    private String message;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "name")
    private NotificationType type;

    private boolean read;
    private LocalDateTime timestamp;

    private String metadata;

    private UUID projectId;
    private UUID issueId;
}
