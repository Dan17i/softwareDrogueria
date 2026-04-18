package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.domain.model.Notification;
import com.drogueria.bellavista.infrastructure.persistence.entity.NotificationEntity;
import com.drogueria.bellavista.infrastructure.persistence.repository.NotificationJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationRepositoryAdapterTest {

    @Mock private NotificationJpaRepository jpaRepository;
    @InjectMocks private NotificationRepositoryAdapter adapter;

    private LocalDateTime now;
    private NotificationEntity entity;
    private Notification domain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        now = LocalDateTime.now();

        entity = NotificationEntity.builder()
            .id(1L).type(Notification.NotificationType.INVENTORY_ALERT)
            .title("Stock bajo").message("Quedan 5 unidades en inventario.")
            .requiredRole("WAREHOUSE").isRead(false)
            .createdAt(now).readAt(null)
            .relatedEntityId("prod-1").relatedEntityType("PRODUCT")
            .build();

        domain = Notification.builder()
            .id(1L).type(Notification.NotificationType.INVENTORY_ALERT)
            .title("Stock bajo").message("Quedan 5 unidades en inventario.")
            .requiredRole("WAREHOUSE").isRead(false)
            .createdAt(now).readAt(null)
            .relatedEntityId("prod-1").relatedEntityType("PRODUCT")
            .build();
    }

    @Test
    @DisplayName("✅ save - persiste entidad y retorna dominio mapeado")
    void shouldSaveAndReturnDomain() {
        when(jpaRepository.save(any(NotificationEntity.class))).thenReturn(entity);

        Notification result = adapter.save(domain);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Stock bajo", result.getTitle());
        assertEquals(Notification.NotificationType.INVENTORY_ALERT, result.getType());
        verify(jpaRepository).save(any(NotificationEntity.class));
    }

    @Test
    @DisplayName("✅ findById - encontrado → retorna Notification")
    void shouldFindByIdWhenExists() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Notification result = adapter.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("WAREHOUSE", result.getRequiredRole());
    }

    @Test
    @DisplayName("❌ findById - no encontrado → retorna null")
    void shouldReturnNullWhenNotFound() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        Notification result = adapter.findById(99L);

        assertNull(result);
    }

    @Test
    @DisplayName("✅ findByRequiredRole - retorna lista mapeada")
    void shouldFindByRequiredRole() {
        when(jpaRepository.findByRequiredRole("WAREHOUSE")).thenReturn(List.of(entity));

        List<Notification> result = adapter.findByRequiredRole("WAREHOUSE");

        assertEquals(1, result.size());
        assertEquals("WAREHOUSE", result.get(0).getRequiredRole());
        verify(jpaRepository).findByRequiredRole("WAREHOUSE");
    }

    @Test
    @DisplayName("✅ findUnreadByRequiredRole - retorna solo no leídas")
    void shouldFindUnreadByRequiredRole() {
        when(jpaRepository.findByRequiredRoleAndIsReadFalse("WAREHOUSE")).thenReturn(List.of(entity));

        List<Notification> result = adapter.findUnreadByRequiredRole("WAREHOUSE");

        assertEquals(1, result.size());
        assertFalse(result.get(0).getIsRead());
    }

    @Test
    @DisplayName("✅ findAll - retorna todas las notificaciones")
    void shouldFindAll() {
        when(jpaRepository.findAll()).thenReturn(List.of(entity));

        List<Notification> result = adapter.findAll();

        assertEquals(1, result.size());
        verify(jpaRepository).findAll();
    }

    @Test
    @DisplayName("✅ markAsRead - entidad encontrada → actualiza isRead y readAt")
    void shouldMarkAsReadWhenEntityExists() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(jpaRepository.save(any(NotificationEntity.class))).thenReturn(entity);

        adapter.markAsRead(1L);

        assertTrue(entity.getIsRead());
        assertNotNull(entity.getReadAt());
        verify(jpaRepository).save(entity);
    }

    @Test
    @DisplayName("❌ markAsRead - entidad no encontrada → no invoca save")
    void shouldNotSaveWhenEntityNotFoundOnMarkAsRead() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        adapter.markAsRead(99L);

        verify(jpaRepository, never()).save(any());
    }

    @Test
    @DisplayName("✅ deleteById - delega a jpaRepository")
    void shouldDeleteById() {
        doNothing().when(jpaRepository).deleteById(1L);

        adapter.deleteById(1L);

        verify(jpaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("🧪 save - mapea todos los campos correctamente")
    void shouldMapAllFieldsOnSave() {
        NotificationEntity withRelated = NotificationEntity.builder()
            .id(2L).type(Notification.NotificationType.ORDER_ALERT)
            .title("Orden").message("msg").requiredRole("ADMIN")
            .isRead(true).createdAt(now).readAt(now)
            .relatedEntityId("ord-10").relatedEntityType("ORDER")
            .build();
        Notification domainWithRelated = Notification.builder()
            .id(2L).type(Notification.NotificationType.ORDER_ALERT)
            .title("Orden").message("msg").requiredRole("ADMIN")
            .isRead(true).createdAt(now).readAt(now)
            .relatedEntityId("ord-10").relatedEntityType("ORDER")
            .build();
        when(jpaRepository.save(any())).thenReturn(withRelated);

        Notification result = adapter.save(domainWithRelated);

        assertEquals("ord-10", result.getRelatedEntityId());
        assertEquals("ORDER", result.getRelatedEntityType());
        assertTrue(result.getIsRead());
        assertEquals(now, result.getReadAt());
    }
}
