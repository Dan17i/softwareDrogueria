package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.domain.model.Notification;
import com.drogueria.bellavista.domain.repository.NotificationRepository;
import com.drogueria.bellavista.infrastructure.persistence.entity.NotificationEntity;
import com.drogueria.bellavista.infrastructure.persistence.repository.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter that implements NotificationRepository port using JPA.
 */
@Component
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = mapToEntity(notification);
        NotificationEntity savedEntity = jpaRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    public Notification findById(Long id) {
        return jpaRepository.findById(id)
            .map(this::mapToDomain)
            .orElse(null);
    }

    @Override
    public List<Notification> findByRequiredRole(String requiredRole) {
        return jpaRepository.findByRequiredRole(requiredRole)
            .stream()
            .map(this::mapToDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findUnreadByRequiredRole(String requiredRole) {
        return jpaRepository.findByRequiredRoleAndIsReadFalse(requiredRole)
            .stream()
            .map(this::mapToDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findAll() {
        return jpaRepository.findAll()
            .stream()
            .map(this::mapToDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long id) {
        NotificationEntity entity = jpaRepository.findById(id).orElse(null);
        if (entity != null) {
            entity.setIsRead(true);
            entity.setReadAt(java.time.LocalDateTime.now());
            jpaRepository.save(entity);
        }
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private NotificationEntity mapToEntity(Notification notification) {
        return NotificationEntity.builder()
            .id(notification.getId())
            .type(notification.getType())
            .title(notification.getTitle())
            .message(notification.getMessage())
            .requiredRole(notification.getRequiredRole())
            .isRead(notification.getIsRead())
            .createdAt(notification.getCreatedAt())
            .readAt(notification.getReadAt())
            .relatedEntityId(notification.getRelatedEntityId())
            .relatedEntityType(notification.getRelatedEntityType())
            .build();
    }

    private Notification mapToDomain(NotificationEntity entity) {
        return Notification.builder()
            .id(entity.getId())
            .type(entity.getType())
            .title(entity.getTitle())
            .message(entity.getMessage())
            .requiredRole(entity.getRequiredRole())
            .isRead(entity.getIsRead())
            .createdAt(entity.getCreatedAt())
            .readAt(entity.getReadAt())
            .relatedEntityId(entity.getRelatedEntityId())
            .relatedEntityType(entity.getRelatedEntityType())
            .build();
    }
}