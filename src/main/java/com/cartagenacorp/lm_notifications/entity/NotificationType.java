package com.cartagenacorp.lm_notifications.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationType {
    @Id
    private String name;
}
