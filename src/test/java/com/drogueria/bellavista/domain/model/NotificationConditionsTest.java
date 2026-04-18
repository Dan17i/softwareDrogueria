package com.drogueria.bellavista.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationConditionsTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime OTHER_TIME = NOW.plusHours(1);

    private Notification full() {
        return Notification.builder()
            .id(1L).title("T").message("M")
            .type(Notification.NotificationType.INVENTORY_ALERT)
            .createdAt(NOW).readAt(NOW).isRead(true)
            .requiredRole("ADMIN").relatedEntityId("e1").relatedEntityType("PRODUCT")
            .build();
    }

    // ── Identity / instanceof guards ──────────────────────────────────────────

    @Test
    @DisplayName("🧪 equals - misma referencia → true (branch o==this)")
    void shouldBeEqualSameReference() {
        Notification n = full();
        assertEquals(n, n);
    }

    @Test
    @DisplayName("🧪 equals - comparar con null → false (branch instanceof)")
    void shouldNotEqualNull() {
        assertNotEquals(null, full());
    }

    @Test
    @DisplayName("🧪 equals - comparar con clase diferente → false (branch instanceof)")
    void shouldNotEqualDifferentClass() {
        assertNotEquals(full(), "string");
    }

    @Test
    @DisplayName("🧪 canEqual - mismo tipo → true")
    void shouldCanEqualSameType() {
        assertTrue(full().canEqual(full()));
    }

    // ── Branch D (both non-null, same value) — covered by base equality ───────

    @Test
    @DisplayName("🧪 equals - todos los campos iguales → iguales")
    void shouldBeEqual() {
        assertEquals(full(), full());
    }

    // ── Per-field: Branch C (this!=null, other!=null, different) ─────────────

    @Test
    @DisplayName("🧪 equals - id diferente → no iguales")
    void shouldNotBeEqualDifferentId() {
        Notification a = full(); a.setId(2L);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - title diferente → no iguales")
    void shouldNotBeEqualDifferentTitle() {
        Notification a = full(); a.setTitle("Other");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - message diferente → no iguales")
    void shouldNotBeEqualDifferentMessage() {
        Notification a = full(); a.setMessage("Other");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - type diferente → no iguales")
    void shouldNotBeEqualDifferentType() {
        Notification a = full(); a.setType(Notification.NotificationType.ORDER_ALERT);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - createdAt diferente → no iguales")
    void shouldNotBeEqualDifferentCreatedAt() {
        Notification a = full(); a.setCreatedAt(OTHER_TIME);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - readAt diferente → no iguales")
    void shouldNotBeEqualDifferentReadAt() {
        Notification a = full(); a.setReadAt(OTHER_TIME);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - isRead diferente → no iguales")
    void shouldNotBeEqualDifferentIsRead() {
        Notification a = full(); a.setIsRead(false);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - requiredRole diferente → no iguales")
    void shouldNotBeEqualDifferentRole() {
        Notification a = full(); a.setRequiredRole("USER");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - relatedEntityId diferente → no iguales")
    void shouldNotBeEqualDifferentRelatedEntityId() {
        Notification a = full(); a.setRelatedEntityId("e2");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - relatedEntityType diferente → no iguales")
    void shouldNotBeEqualDifferentRelatedEntityType() {
        Notification a = full(); a.setRelatedEntityType("ORDER");
        assertNotEquals(full(), a);
    }

    // ── Per-field: Branch A (this==null, other!=null) ────────────────────────

    @Test
    @DisplayName("🧪 equals - id: this=null, other=1L → no iguales")
    void shouldNotBeEqualIdThisNull() {
        Notification a = full(); a.setId(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - title: this=null, other='T' → no iguales")
    void shouldNotBeEqualTitleThisNull() {
        Notification a = full(); a.setTitle(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - message: this=null, other='M' → no iguales")
    void shouldNotBeEqualMessageThisNull() {
        Notification a = full(); a.setMessage(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - type: this=null, other=INVENTORY_ALERT → no iguales")
    void shouldNotBeEqualTypeThisNull() {
        Notification a = full(); a.setType(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - createdAt: this=null, other=NOW → no iguales")
    void shouldNotBeEqualCreatedAtThisNull() {
        Notification a = full(); a.setCreatedAt(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - readAt: this=null, other=NOW → no iguales")
    void shouldNotBeEqualReadAtThisNull() {
        Notification a = full(); a.setReadAt(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - isRead: this=null, other=true → no iguales")
    void shouldNotBeEqualIsReadThisNull() {
        Notification a = full(); a.setIsRead(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - requiredRole: this=null, other='ADMIN' → no iguales")
    void shouldNotBeEqualRequiredRoleThisNull() {
        Notification a = full(); a.setRequiredRole(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - relatedEntityId: this=null, other='e1' → no iguales")
    void shouldNotBeEqualRelatedEntityIdThisNull() {
        Notification a = full(); a.setRelatedEntityId(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - relatedEntityType: this=null, other='PRODUCT' → no iguales")
    void shouldNotBeEqualRelatedEntityTypeThisNull() {
        Notification a = full(); a.setRelatedEntityType(null);
        assertNotEquals(a, full());
    }

    // ── Per-field: Branch B (this==null, other==null) ────────────────────────

    @Test
    @DisplayName("🧪 equals - id: ambos null → iguales")
    void shouldBeEqualWithNullId() {
        Notification a = full(); a.setId(null);
        Notification b = full(); b.setId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - title: ambos null → iguales")
    void shouldBeEqualWithNullTitle() {
        Notification a = full(); a.setTitle(null);
        Notification b = full(); b.setTitle(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - message: ambos null → iguales")
    void shouldBeEqualWithNullMessage() {
        Notification a = full(); a.setMessage(null);
        Notification b = full(); b.setMessage(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - type: ambos null → iguales")
    void shouldBeEqualWithNullType() {
        Notification a = full(); a.setType(null);
        Notification b = full(); b.setType(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - createdAt: ambos null → iguales")
    void shouldBeEqualWithNullCreatedAt() {
        Notification a = full(); a.setCreatedAt(null);
        Notification b = full(); b.setCreatedAt(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - readAt: ambos null → iguales")
    void shouldBeEqualWithNullReadAt() {
        Notification a = full(); a.setReadAt(null);
        Notification b = full(); b.setReadAt(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - isRead: ambos null → iguales")
    void shouldBeEqualWithNullIsRead() {
        Notification a = full(); a.setIsRead(null);
        Notification b = full(); b.setIsRead(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - requiredRole: ambos null → iguales")
    void shouldBeEqualWithNullRole() {
        Notification a = full(); a.setRequiredRole(null);
        Notification b = full(); b.setRequiredRole(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - relatedEntityId: ambos null → iguales")
    void shouldBeEqualWithNullRelatedEntityId() {
        Notification a = full(); a.setRelatedEntityId(null);
        Notification b = full(); b.setRelatedEntityId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - relatedEntityType: ambos null → iguales")
    void shouldBeEqualWithNullRelatedEntityType() {
        Notification a = full(); a.setRelatedEntityType(null);
        Notification b = full(); b.setRelatedEntityType(null);
        assertEquals(a, b);
    }

    // ── hashCode ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("🧪 hashCode - objetos iguales → mismo hashCode")
    void shouldHaveSameHashCode() {
        assertEquals(full().hashCode(), full().hashCode());
    }

    @Test
    @DisplayName("🧪 hashCode - con campos null → no lanza excepción")
    void shouldHashCodeWithNullFields() {
        Notification n = new Notification();
        assertDoesNotThrow(n::hashCode);
    }

    // ── toString / constructors ───────────────────────────────────────────────

    @Test
    @DisplayName("🧪 toString - no lanza excepción")
    void shouldProduceToString() {
        assertNotNull(full().toString());
    }

    @Test
    @DisplayName("🧪 noArgsConstructor y setters funcionan")
    void shouldUseNoArgsConstructorAndSetters() {
        Notification n = new Notification();
        n.setId(5L); n.setTitle("T2"); n.setMessage("M2");
        n.setType(Notification.NotificationType.SYSTEM_ALERT);
        n.setCreatedAt(NOW); n.setIsRead(false); n.setRequiredRole("USER");
        assertEquals(5L, n.getId());
        assertFalse(n.getIsRead());
    }

    @Test
    @DisplayName("🧪 allArgsConstructor crea objeto completo")
    void shouldUseAllArgsConstructor() {
        Notification n = new Notification(1L, "T", "M",
            Notification.NotificationType.USER_ALERT, NOW, NOW, true, "ADMIN", "e1", "ORDER");
        assertEquals(1L, n.getId());
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
