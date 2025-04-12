package com.cartagenacorp.lm_notifications.service;

import com.cartagenacorp.lm_notifications.entity.NotificationType;
import com.cartagenacorp.lm_notifications.repository.NotificationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationTypeService {

    private final NotificationTypeRepository notificationTypeRepository;

    @Autowired
    public NotificationTypeService(NotificationTypeRepository notificationTypeRepository) {
        this.notificationTypeRepository = notificationTypeRepository;
    }

    public List<NotificationType> getAll() {
        return notificationTypeRepository.findAll();
    }

    public NotificationType create(NotificationType type) {
        return notificationTypeRepository.save(type);
    }

    public void delete(String name) {
        notificationTypeRepository.deleteById(name);
    }
}
