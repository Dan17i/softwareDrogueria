package com.drogueria.bellavista.infrastructure.persistence.entity;

import java.util.Objects;
import com.drogueria.bellavista.domain.model.Notification;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * JPA entity for Notification.
 */
@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Notification.NotificationType type;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private String requiredRole;

    @Column(nullable = false)
    private Boolean isRead;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime readAt;

    @Column
    private String relatedEntityId;

    @Column
    private String relatedEntityType;

    public NotificationEntity() {
    }

    public NotificationEntity(Long id, Notification.NotificationType type, String title, String message, String requiredRole, Boolean isRead, LocalDateTime createdAt, LocalDateTime readAt, String relatedEntityId, String relatedEntityType) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.message = message;
        this.requiredRole = requiredRole;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.readAt = readAt;
        this.relatedEntityId = relatedEntityId;
        this.relatedEntityType = relatedEntityType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Notification.NotificationType getType() {
        return type;
    }

    public void setType(Notification.NotificationType type) {
        this.type = type;
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

    public String getRequiredRole() {
        return requiredRole;
    }

    public void setRequiredRole(String requiredRole) {
        this.requiredRole = requiredRole;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
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
        if (!(o instanceof NotificationEntity other)) return false;
        return Objects.equals(id, other.id) && Objects.equals(type, other.type) && Objects.equals(title, other.title) && Objects.equals(message, other.message) && Objects.equals(requiredRole, other.requiredRole) && Objects.equals(isRead, other.isRead) && Objects.equals(createdAt, other.createdAt) && Objects.equals(readAt, other.readAt) && Objects.equals(relatedEntityId, other.relatedEntityId) && Objects.equals(relatedEntityType, other.relatedEntityType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, title, message, requiredRole, isRead, createdAt, readAt, relatedEntityId, relatedEntityType);
    }

    @Override
    public String toString() {
        return "NotificationEntity(" + "id=" + id + ", type=" + type + ", title=" + title + ", message=" + message + ", requiredRole=" + requiredRole + ", isRead=" + isRead + ", createdAt=" + createdAt + ", readAt=" + readAt + ", relatedEntityId=" + relatedEntityId + ", relatedEntityType=" + relatedEntityType + ")";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Notification.NotificationType type;
        private String title;
        private String message;
        private String requiredRole;
        private Boolean isRead;
        private LocalDateTime createdAt;
        private LocalDateTime readAt;
        private String relatedEntityId;
        private String relatedEntityType;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder type(Notification.NotificationType type) {
            this.type = type;
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

        public Builder requiredRole(String requiredRole) {
            this.requiredRole = requiredRole;
            return this;
        }

        public Builder isRead(Boolean isRead) {
            this.isRead = isRead;
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

        public Builder relatedEntityId(String relatedEntityId) {
            this.relatedEntityId = relatedEntityId;
            return this;
        }

        public Builder relatedEntityType(String relatedEntityType) {
            this.relatedEntityType = relatedEntityType;
            return this;
        }

        public NotificationEntity build() {
            return new NotificationEntity(id, type, title, message, requiredRole, isRead, createdAt, readAt, relatedEntityId, relatedEntityType);
        }
    }


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (isRead == null) {
            isRead = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (isRead && readAt == null) {
            readAt = LocalDateTime.now();
        }
    }
}