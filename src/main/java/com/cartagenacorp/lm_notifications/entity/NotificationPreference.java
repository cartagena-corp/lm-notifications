package com.cartagenacorp.lm_notifications.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "notification_preference")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "name")
    private NotificationType type;

    private boolean enabled;
}
