package com.drogueria.bellavista.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationConditionsTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

    private Notification full() {
        return Notification.builder()
            .id(1L).title("T").message("M")
            .type(Notification.NotificationType.INVENTORY_ALERT)
            .createdAt(NOW).readAt(NOW).isRead(true)
            .requiredRole("ADMIN").relatedEntityId("e1").relatedEntityType("PRODUCT")
            .build();
    }

    @Test
    @DisplayName("🧪 equals - mismos campos → iguales")
    void shouldBeEqual() {
        assertEquals(full(), full());
    }

    @Test
    @DisplayName("🧪 equals - id diferente → no iguales")
    void shouldNotBeEqualDifferentId() {
        Notification a = full();
        Notification b = full();
        b.setId(2L);
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - título diferente → no iguales")
    void shouldNotBeEqualDifferentTitle() {
        Notification a = full();
        Notification b = full();
        b.setTitle("Other");
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - message diferente → no iguales")
    void shouldNotBeEqualDifferentMessage() {
        Notification a = full();
        Notification b = full();
        b.setMessage("Other");
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - type diferente → no iguales")
    void shouldNotBeEqualDifferentType() {
        Notification a = full();
        Notification b = full();
        b.setType(Notification.NotificationType.ORDER_ALERT);
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - isRead diferente → no iguales")
    void shouldNotBeEqualDifferentIsRead() {
        Notification a = full();
        Notification b = full();
        b.setIsRead(false);
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - requiredRole diferente → no iguales")
    void shouldNotBeEqualDifferentRole() {
        Notification a = full();
        Notification b = full();
        b.setRequiredRole("USER");
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - relatedEntityId diferente → no iguales")
    void shouldNotBeEqualDifferentRelatedEntityId() {
        Notification a = full();
        Notification b = full();
        b.setRelatedEntityId("e2");
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - relatedEntityType diferente → no iguales")
    void shouldNotBeEqualDifferentRelatedEntityType() {
        Notification a = full();
        Notification b = full();
        b.setRelatedEntityType("ORDER");
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - id null en ambos → iguales")
    void shouldBeEqualWithNullId() {
        Notification a = full(); a.setId(null);
        Notification b = full(); b.setId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - id null vs non-null → no iguales")
    void shouldNotBeEqualWhenOneIdNull() {
        Notification a = full(); a.setId(null);
        Notification b = full();
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - requiredRole null en ambos → iguales")
    void shouldBeEqualWithNullRole() {
        Notification a = full(); a.setRequiredRole(null);
        Notification b = full(); b.setRequiredRole(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 hashCode - objetos iguales tienen mismo hashCode")
    void shouldHaveSameHashCode() {
        assertEquals(full().hashCode(), full().hashCode());
    }

    @Test
    @DisplayName("🧪 toString - no lanza excepción")
    void shouldProduceToString() {
        assertNotNull(full().toString());
    }

    @Test
    @DisplayName("🧪 noArgsConstructor y setters funcionan")
    void shouldUseNoArgsConstructorAndSetters() {
        Notification n = new Notification();
        n.setId(5L);
        n.setTitle("T2");
        n.setMessage("M2");
        n.setType(Notification.NotificationType.SYSTEM_ALERT);
        n.setCreatedAt(NOW);
        n.setIsRead(false);
        n.setRequiredRole("USER");

        assertEquals(5L, n.getId());
        assertEquals("T2", n.getTitle());
        assertFalse(n.getIsRead());
    }

    @Test
    @DisplayName("🧪 allArgsConstructor crea objeto completo")
    void shouldUseAllArgsConstructor() {
        Notification n = new Notification(
            1L, "T", "M", Notification.NotificationType.USER_ALERT,
            NOW, NOW, true, "ADMIN", "e1", "ORDER"
        );
        assertEquals(1L, n.getId());
        assertEquals(Notification.NotificationType.USER_ALERT, n.getType());
        assertTrue(n.getIsRead());
    }

    @Test
    @DisplayName("🧪 NotificationType - todos los valores accesibles")
    void shouldAccessAllNotificationTypes() {
        assertEquals(4, Notification.NotificationType.values().length);
        assertNotNull(Notification.NotificationType.valueOf("INVENTORY_ALERT"));
        assertNotNull(Notification.NotificationType.valueOf("ORDER_ALERT"));
        assertNotNull(Notification.NotificationType.valueOf("SYSTEM_ALERT"));
        assertNotNull(Notification.NotificationType.valueOf("USER_ALERT"));
    }
}
