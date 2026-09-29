package com.drogueria.bellavista.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * DTOs for Notification operations.
 */
public class NotificationDTO {

    public record Response(
            Long id,
            String title,
            String message,
            String type,
            Boolean isRead,
            String requiredRole,

            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
            LocalDateTime createdAt,

            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
            LocalDateTime readAt,

            String relatedEntityId,
            String relatedEntityType) {

        public Long getId() { return id; }
        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getType() { return type; }
        public Boolean getIsRead() { return isRead; }
        public String getRequiredRole() { return requiredRole; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getReadAt() { return readAt; }
        public String getRelatedEntityId() { return relatedEntityId; }
        public String getRelatedEntityType() { return relatedEntityType; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Long id;
            private String title;
            private String message;
            private String type;
            private Boolean isRead;
            private String requiredRole;
            private LocalDateTime createdAt;
            private LocalDateTime readAt;
            private String relatedEntityId;
            private String relatedEntityType;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder title(String title) { this.title = title; return this; }
            public Builder message(String message) { this.message = message; return this; }
            public Builder type(String type) { this.type = type; return this; }
            public Builder isRead(Boolean isRead) { this.isRead = isRead; return this; }
            public Builder requiredRole(String requiredRole) { this.requiredRole = requiredRole; return this; }
            public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public Builder readAt(LocalDateTime readAt) { this.readAt = readAt; return this; }
            public Builder relatedEntityId(String relatedEntityId) { this.relatedEntityId = relatedEntityId; return this; }
            public Builder relatedEntityType(String relatedEntityType) { this.relatedEntityType = relatedEntityType; return this; }

            public Response build() {
                return new Response(id, title, message, type, isRead, requiredRole,
                        createdAt, readAt, relatedEntityId, relatedEntityType);
            }
        }
    }

    public record CreateRequest(
            String title,
            String message,
            String type,
            String requiredRole) {

        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getType() { return type; }
        public String getRequiredRole() { return requiredRole; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String title;
            private String message;
            private String type;
            private String requiredRole;

            public Builder title(String title) { this.title = title; return this; }
            public Builder message(String message) { this.message = message; return this; }
            public Builder type(String type) { this.type = type; return this; }
            public Builder requiredRole(String requiredRole) { this.requiredRole = requiredRole; return this; }

            public CreateRequest build() {
                return new CreateRequest(title, message, type, requiredRole);
            }
        }
    }
}
