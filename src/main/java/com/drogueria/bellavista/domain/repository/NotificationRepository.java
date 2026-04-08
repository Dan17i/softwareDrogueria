package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.Notification;

import java.util.List;

/**
 * Port (interface) for Notification repository.
 * Defines notification persistence contracts.
 */
public interface NotificationRepository {

    /**
     * Save a notification.
     */
    Notification save(Notification notification);

    /**
     * Find notification by ID.
     */
    Notification findById(Long id);

    /**
     * Find all notifications for a specific role.
     */
    List<Notification> findByRequiredRole(String role);

    /**
     * Find all unread notifications for a specific role.
     */
    List<Notification> findUnreadByRequiredRole(String role);

    /**
     * Find all notifications.
     */
    List<Notification> findAll();

    /**
     * Mark notification as read.
     */
    void markAsRead(Long id);

    /**
     * Delete notification by ID.
     */
    void deleteById(Long id);
}