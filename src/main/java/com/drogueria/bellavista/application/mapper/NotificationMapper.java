package com.drogueria.bellavista.application.mapper;

import com.drogueria.bellavista.application.dto.NotificationDTO;
import com.drogueria.bellavista.domain.model.Notification;
import org.springframework.stereotype.Component;

/**
 * Mapper for Notification DTOs and Domain models.
 */
@Component
public class NotificationMapper {

    /**
     * Convert domain Notification to Response DTO.
     */
    public NotificationDTO.Response toResponse(Notification notification) {
        if (notification == null) return null;

        return NotificationDTO.Response.builder()
            .id(notification.getId())
            .title(notification.getTitle())
            .message(notification.getMessage())
            .type(notification.getType().name())
            .isRead(notification.getIsRead())
            .requiredRole(notification.getRequiredRole())
            .createdAt(notification.getCreatedAt())
            .readAt(notification.getReadAt())
            .relatedEntityId(notification.getRelatedEntityId())
            .relatedEntityType(notification.getRelatedEntityType())
            .build();
    }

    /**
     * Convert CreateRequest DTO to domain Notification.
     */
    public Notification fromCreateRequest(NotificationDTO.CreateRequest request) {
        if (request == null) return null;

        return Notification.builder()
            .title(request.getTitle())
            .message(request.getMessage())
            .type(Notification.NotificationType.valueOf(request.getType()))
            .isRead(false)
            .requiredRole(request.getRequiredRole())
            .createdAt(java.time.LocalDateTime.now())
            .build();
    }
}