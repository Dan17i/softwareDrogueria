package com.drogueria.bellavista.infrastructure.persistence.repository;

import com.drogueria.bellavista.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA repository for Notification entities.
 */
@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {

    /**
     * Find notifications by required role.
     */
    List<NotificationEntity> findByRequiredRole(String requiredRole);

    /**
     * Find unread notifications by required role.
     */
    List<NotificationEntity> findByRequiredRoleAndIsReadFalse(String requiredRole);

    /**
     * Find notifications by type.
     */
    List<NotificationEntity> findByType(String type);

    /**
     * Find notifications by related entity.
     */
    List<NotificationEntity> findByRelatedEntityIdAndRelatedEntityType(String relatedEntityId, String relatedEntityType);

    /**
     * Count unread notifications by role.
     */
    @Query("SELECT COUNT(n) FROM NotificationEntity n WHERE n.requiredRole = :role AND n.isRead = false")
    long countUnreadByRequiredRole(@Param("role") String role);

    /**
     * Find recent notifications ordered by creation date.
     */
    @Query("SELECT n FROM NotificationEntity n ORDER BY n.createdAt DESC")
    List<NotificationEntity> findAllOrderByCreatedAtDesc();
}