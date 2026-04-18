package com.drogueria.bellavista.infrastructure.persistence.entity;

import com.drogueria.bellavista.domain.model.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationEntityTest {

    @Test
    @DisplayName("✅ onCreate - setea createdAt y isRead=false cuando es null")
    void shouldSetDefaultsOnCreate() {
        NotificationEntity entity = NotificationEntity.builder()
            .title("Test").message("msg").requiredRole("USER")
            .type(Notification.NotificationType.SYSTEM_ALERT)
            .isRead(null)
            .build();

        entity.onCreate();

        assertNotNull(entity.getCreatedAt());
        assertFalse(entity.getIsRead());
    }

    @Test
    @DisplayName("✅ onCreate - no sobreescribe isRead cuando ya tiene valor")
    void shouldNotOverrideIsReadWhenAlreadySet() {
        NotificationEntity entity = NotificationEntity.builder()
            .title("Test").message("msg").requiredRole("USER")
            .type(Notification.NotificationType.SYSTEM_ALERT)
            .isRead(true)
            .build();

        entity.onCreate();

        assertTrue(entity.getIsRead());
    }

    @Test
    @DisplayName("✅ onUpdate - setea readAt cuando isRead=true y readAt es null")
    void shouldSetReadAtOnUpdateWhenRead() {
        NotificationEntity entity = NotificationEntity.builder()
            .title("Test").message("msg").requiredRole("USER")
            .type(Notification.NotificationType.INVENTORY_ALERT)
            .isRead(true).readAt(null)
            .build();

        entity.onUpdate();

        assertNotNull(entity.getReadAt());
    }

    @Test
    @DisplayName("✅ onUpdate - no sobreescribe readAt cuando ya tiene valor")
    void shouldNotOverrideReadAtWhenAlreadySet() {
        LocalDateTime existing = LocalDateTime.now().minusHours(1);
        NotificationEntity entity = NotificationEntity.builder()
            .title("Test").message("msg").requiredRole("USER")
            .type(Notification.NotificationType.ORDER_ALERT)
            .isRead(true).readAt(existing)
            .build();

        entity.onUpdate();

        assertEquals(existing, entity.getReadAt());
    }

    @Test
    @DisplayName("✅ onUpdate - no setea readAt cuando isRead=false")
    void shouldNotSetReadAtWhenNotRead() {
        NotificationEntity entity = NotificationEntity.builder()
            .title("Test").message("msg").requiredRole("USER")
            .type(Notification.NotificationType.USER_ALERT)
            .isRead(false).readAt(null)
            .build();

        entity.onUpdate();

        assertNull(entity.getReadAt());
    }

    @Test
    @DisplayName("🧪 builder y getters cubren todos los campos")
    void shouldExerciseBuilderAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        NotificationEntity entity = NotificationEntity.builder()
            .id(1L).type(Notification.NotificationType.SYSTEM_ALERT)
            .title("titulo").message("mensaje").requiredRole("ADMIN")
            .isRead(false).createdAt(now).readAt(null)
            .relatedEntityId("42").relatedEntityType("ORDER")
            .build();

        assertEquals(1L, entity.getId());
        assertEquals(Notification.NotificationType.SYSTEM_ALERT, entity.getType());
        assertEquals("titulo", entity.getTitle());
        assertEquals("mensaje", entity.getMessage());
        assertEquals("ADMIN", entity.getRequiredRole());
        assertFalse(entity.getIsRead());
        assertEquals(now, entity.getCreatedAt());
        assertNull(entity.getReadAt());
        assertEquals("42", entity.getRelatedEntityId());
        assertEquals("ORDER", entity.getRelatedEntityType());
    }

    @Test
    @DisplayName("🧪 setters modifican los campos correctamente")
    void shouldExerciseSetters() {
        NotificationEntity entity = new NotificationEntity();
        entity.setTitle("nuevo");
        entity.setMessage("msg");
        entity.setIsRead(true);
        entity.setRequiredRole("USER");

        assertEquals("nuevo", entity.getTitle());
        assertEquals("msg", entity.getMessage());
        assertTrue(entity.getIsRead());
        assertEquals("USER", entity.getRequiredRole());
    }

    @Test
    @DisplayName("🧪 equals y hashCode - dos entidades con mismo id son iguales")
    void shouldBeEqualWhenSameId() {
        LocalDateTime now = LocalDateTime.now();
        NotificationEntity e1 = NotificationEntity.builder().id(1L)
            .type(Notification.NotificationType.SYSTEM_ALERT)
            .title("t").message("m").requiredRole("U").isRead(false).createdAt(now).build();
        NotificationEntity e2 = NotificationEntity.builder().id(1L)
            .type(Notification.NotificationType.SYSTEM_ALERT)
            .title("t").message("m").requiredRole("U").isRead(false).createdAt(now).build();

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    @DisplayName("🧪 toString - no lanza excepción")
    void shouldProduceToString() {
        NotificationEntity entity = NotificationEntity.builder()
            .id(1L).type(Notification.NotificationType.USER_ALERT)
            .title("t").message("m").requiredRole("U").isRead(false)
            .createdAt(LocalDateTime.now()).build();

        assertNotNull(entity.toString());
    }
}
