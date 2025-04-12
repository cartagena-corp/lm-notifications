package com.cartagenacorp.lm_notifications.service;

import com.cartagenacorp.lm_notifications.dto.NotificationPreferenceDTO;
import com.cartagenacorp.lm_notifications.entity.NotificationPreference;
import com.cartagenacorp.lm_notifications.entity.NotificationType;
import com.cartagenacorp.lm_notifications.repository.NotificationPreferenceRepository;
import com.cartagenacorp.lm_notifications.repository.NotificationTypeRepository;
import com.cartagenacorp.lm_notifications.util.JwtContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationPreferenceService {
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final NotificationTypeRepository notificationTypeRepository;

    @Autowired
    public NotificationPreferenceService(NotificationPreferenceRepository repository, NotificationTypeRepository notificationTypeRepository) {
        this.notificationPreferenceRepository = repository;
        this.notificationTypeRepository = notificationTypeRepository;
    }

    public List<NotificationPreference> getUserPreferences() {
        UUID userId = JwtContextHolder.getUserId();
        return notificationPreferenceRepository.findByUserId(userId);
    }

    @Transactional
    public void updatePreferences(List<NotificationPreferenceDTO> preferences) {
        UUID userId = JwtContextHolder.getUserId();
        for (NotificationPreferenceDTO dto : preferences) {
            NotificationType type = notificationTypeRepository.findById(dto.getType())
                    .orElseThrow(() -> new IllegalArgumentException("Type of notification not valid: " + dto.getType()));

            NotificationPreference pref = notificationPreferenceRepository.findByUserIdAndType(userId, type)
                    .orElse(new NotificationPreference(null, userId, type, dto.isEnabled()));

            pref.setEnabled(dto.isEnabled());
            notificationPreferenceRepository.save(pref);
        }
    }

    public boolean isEnabled(UUID userId, NotificationType type) {
        return notificationPreferenceRepository.findByUserIdAndType(userId, type)
                .map(NotificationPreference::isEnabled)
                .orElse(true);
    }
}
