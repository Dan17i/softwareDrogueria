package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain model for Notification.
 * Represents system notifications for users.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private Boolean isRead;
    private String requiredRole;
    private String relatedEntityId;
    private String relatedEntityType;

    /**
     * Notification types enum.
     */
    public enum NotificationType {
        INVENTORY_ALERT,    // Alertas de inventario bajo
        ORDER_ALERT,        // Alertas de órdenes
        SYSTEM_ALERT,       // Alertas del sistema
        USER_ALERT          // Alertas de usuario
    }

    /**
     * Check if notification is for a specific role.
     */
    public boolean isForRole(String role) {
        return requiredRole == null || requiredRole.equals(role);
    }

    /**
     * Mark notification as read.
     */
    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }
}