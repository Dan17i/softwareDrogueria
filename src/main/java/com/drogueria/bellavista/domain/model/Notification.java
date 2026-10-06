package com.drogueria.bellavista.domain.model;

import java.util.Objects;

import java.time.LocalDateTime;

/**
 * Domain model for Notification.
 * Represents system notifications for users.
 */
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

    public Notification() {
    }

    public Notification(Long id, String title, String message, NotificationType type, LocalDateTime createdAt, LocalDateTime readAt, Boolean isRead, String requiredRole, String relatedEntityId, String relatedEntityType) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
        this.readAt = readAt;
        this.isRead = isRead;
        this.requiredRole = requiredRole;
        this.relatedEntityId = relatedEntityId;
        this.relatedEntityType = relatedEntityType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getRequiredRole() {
        return requiredRole;
    }

    public void setRequiredRole(String requiredRole) {
        this.requiredRole = requiredRole;
    }

    public String getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(String relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public String getRelatedEntityType() {
        return relatedEntityType;
    }

    public void setRelatedEntityType(String relatedEntityType) {
        this.relatedEntityType = relatedEntityType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification other)) return false;
        return Objects.equals(id, other.id) && Objects.equals(title, other.title) && Objects.equals(message, other.message) && Objects.equals(type, other.type) && Objects.equals(createdAt, other.createdAt) && Objects.equals(readAt, other.readAt) && Objects.equals(isRead, other.isRead) && Objects.equals(requiredRole, other.requiredRole) && Objects.equals(relatedEntityId, other.relatedEntityId) && Objects.equals(relatedEntityType, other.relatedEntityType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, message, type, createdAt, readAt, isRead, requiredRole, relatedEntityId, relatedEntityType);
    }

    @Override
    public String toString() {
        return "Notification(" + "id=" + id + ", title=" + title + ", message=" + message + ", type=" + type + ", createdAt=" + createdAt + ", readAt=" + readAt + ", isRead=" + isRead + ", requiredRole=" + requiredRole + ", relatedEntityId=" + relatedEntityId + ", relatedEntityType=" + relatedEntityType + ")";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
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

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder type(NotificationType type) {
            this.type = type;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder readAt(LocalDateTime readAt) {
            this.readAt = readAt;
            return this;
        }

        public Builder isRead(Boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        public Builder requiredRole(String requiredRole) {
            this.requiredRole = requiredRole;
            return this;
        }

        public Builder relatedEntityId(String relatedEntityId) {
            this.relatedEntityId = relatedEntityId;
            return this;
        }

        public Builder relatedEntityType(String relatedEntityType) {
            this.relatedEntityType = relatedEntityType;
            return this;
        }

        public Notification build() {
            return new Notification(id, title, message, type, createdAt, readAt, isRead, requiredRole, relatedEntityId, relatedEntityType);
        }
    }


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