package com.drogueria.bellavista.application.service;

import com.drogueria.bellavista.application.dto.NotificationDTO;
import com.drogueria.bellavista.application.mapper.NotificationMapper;
import com.drogueria.bellavista.domain.model.Notification;
import com.drogueria.bellavista.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application service for Notification operations.
 * Coordinates between controllers and domain services.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationApplicationService {

    private final NotificationService notificationService;
    private final NotificationMapper mapper;

    /**
     * Get all notifications for a specific role.
     */
    public List<NotificationDTO.Response> getNotificationsForRole(String role) {
        List<Notification> notifications = notificationService.getNotificationsForRole(role);
        return notifications.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get unread notifications for a specific role.
     */
    public List<NotificationDTO.Response> getUnreadNotificationsForRole(String role) {
        List<Notification> notifications = notificationService.getUnreadNotificationsForRole(role);
        return notifications.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get all notifications (admin only).
     */
    public List<NotificationDTO.Response> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return notifications.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Mark notification as read.
     */
    public void markAsRead(Long notificationId) {
        notificationService.markAsRead(notificationId);
    }

    /**
     * Generate inventory alerts.
     */
    public void generateInventoryAlerts() {
        notificationService.generateInventoryAlerts();
    }

    /**
     * Delete notification.
     */
    public void deleteNotification(Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }
}