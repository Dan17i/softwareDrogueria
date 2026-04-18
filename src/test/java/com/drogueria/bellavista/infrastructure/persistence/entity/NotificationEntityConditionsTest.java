package com.drogueria.bellavista.infrastructure.persistence.entity;

import com.drogueria.bellavista.domain.model.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationEntityConditionsTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime OTHER_TIME = NOW.plusHours(1);

    private NotificationEntity full() {
        return NotificationEntity.builder()
            .id(1L).type(Notification.NotificationType.INVENTORY_ALERT)
            .title("T").message("M").requiredRole("ADMIN")
            .isRead(true).createdAt(NOW).readAt(NOW)
            .relatedEntityId("e1").relatedEntityType("PRODUCT")
            .build();
    }

    // ── Identity / instanceof guards ──────────────────────────────────────────

    @Test
    @DisplayName("🧪 equals - misma referencia → true (branch o==this)")
    void shouldBeEqualSameReference() {
        NotificationEntity e = full();
        assertEquals(e, e);
    }

    @Test
    @DisplayName("🧪 equals - comparar con null → false (branch instanceof)")
    void shouldNotEqualNull() {
        assertNotEquals(null, full());
    }

    @Test
    @DisplayName("🧪 equals - comparar con clase diferente → false (branch instanceof)")
    void shouldNotEqualDifferentClass() {
        assertFalse(full().equals("string"));
    }

    @Test
    @DisplayName("🧪 canEqual - mismo tipo → true")
    void shouldCanEqualSameType() {
        assertTrue(full().canEqual(full()));
    }

    // ── Branch D (both non-null, same value) ─────────────────────────────────

    @Test
    @DisplayName("🧪 equals - todos los campos iguales → iguales")
    void shouldBeEqual() {
        assertEquals(full(), full());
    }

    // ── Per-field: Branch C (this!=null, other!=null, different) ─────────────

    @Test
    @DisplayName("🧪 equals - id diferente → no iguales")
    void shouldNotBeEqualDifferentId() {
        NotificationEntity a = full(); a.setId(2L);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - type diferente → no iguales")
    void shouldNotBeEqualDifferentType() {
        NotificationEntity a = full(); a.setType(Notification.NotificationType.ORDER_ALERT);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - title diferente → no iguales")
    void shouldNotBeEqualDifferentTitle() {
        NotificationEntity a = full(); a.setTitle("Other");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - message diferente → no iguales")
    void shouldNotBeEqualDifferentMessage() {
        NotificationEntity a = full(); a.setMessage("Other");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - requiredRole diferente → no iguales")
    void shouldNotBeEqualDifferentRequiredRole() {
        NotificationEntity a = full(); a.setRequiredRole("USER");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - isRead diferente → no iguales")
    void shouldNotBeEqualDifferentIsRead() {
        NotificationEntity a = full(); a.setIsRead(false);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - createdAt diferente → no iguales")
    void shouldNotBeEqualDifferentCreatedAt() {
        NotificationEntity a = full(); a.setCreatedAt(OTHER_TIME);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - readAt diferente → no iguales")
    void shouldNotBeEqualDifferentReadAt() {
        NotificationEntity a = full(); a.setReadAt(OTHER_TIME);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - relatedEntityId diferente → no iguales")
    void shouldNotBeEqualDifferentRelatedEntityId() {
        NotificationEntity a = full(); a.setRelatedEntityId("e2");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - relatedEntityType diferente → no iguales")
    void shouldNotBeEqualDifferentRelatedEntityType() {
        NotificationEntity a = full(); a.setRelatedEntityType("ORDER");
        assertNotEquals(full(), a);
    }

    // ── Per-field: Branch A (this==null, other!=null) ────────────────────────

    @Test
    @DisplayName("🧪 equals - id: this=null, other=1L → no iguales")
    void shouldNotBeEqualIdThisNull() {
        NotificationEntity a = full(); a.setId(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - type: this=null, other=INVENTORY_ALERT → no iguales")
    void shouldNotBeEqualTypeThisNull() {
        NotificationEntity a = full(); a.setType(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - title: this=null, other='T' → no iguales")
    void shouldNotBeEqualTitleThisNull() {
        NotificationEntity a = full(); a.setTitle(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - message: this=null, other='M' → no iguales")
    void shouldNotBeEqualMessageThisNull() {
        NotificationEntity a = full(); a.setMessage(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - requiredRole: this=null, other='ADMIN' → no iguales")
    void shouldNotBeEqualRequiredRoleThisNull() {
        NotificationEntity a = full(); a.setRequiredRole(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - isRead: this=null, other=true → no iguales")
    void shouldNotBeEqualIsReadThisNull() {
        NotificationEntity a = full(); a.setIsRead(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - createdAt: this=null, other=NOW → no iguales")
    void shouldNotBeEqualCreatedAtThisNull() {
        NotificationEntity a = full(); a.setCreatedAt(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - readAt: this=null, other=NOW → no iguales")
    void shouldNotBeEqualReadAtThisNull() {
        NotificationEntity a = full(); a.setReadAt(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - relatedEntityId: this=null, other='e1' → no iguales")
    void shouldNotBeEqualRelatedEntityIdThisNull() {
        NotificationEntity a = full(); a.setRelatedEntityId(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - relatedEntityType: this=null, other='PRODUCT' → no iguales")
    void shouldNotBeEqualRelatedEntityTypeThisNull() {
        NotificationEntity a = full(); a.setRelatedEntityType(null);
        assertNotEquals(a, full());
    }

    // ── Per-field: Branch B (this==null, other==null) ────────────────────────

    @Test
    @DisplayName("🧪 equals - id: ambos null → iguales")
    void shouldBeEqualWithNullId() {
        NotificationEntity a = full(); a.setId(null);
        NotificationEntity b = full(); b.setId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - type: ambos null → iguales")
    void shouldBeEqualWithNullType() {
        NotificationEntity a = full(); a.setType(null);
        NotificationEntity b = full(); b.setType(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - title: ambos null → iguales")
    void shouldBeEqualWithNullTitle() {
        NotificationEntity a = full(); a.setTitle(null);
        NotificationEntity b = full(); b.setTitle(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - message: ambos null → iguales")
    void shouldBeEqualWithNullMessage() {
        NotificationEntity a = full(); a.setMessage(null);
        NotificationEntity b = full(); b.setMessage(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - requiredRole: ambos null → iguales")
    void shouldBeEqualWithNullRequiredRole() {
        NotificationEntity a = full(); a.setRequiredRole(null);
        NotificationEntity b = full(); b.setRequiredRole(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - isRead: ambos null → iguales")
    void shouldBeEqualWithNullIsRead() {
        NotificationEntity a = full(); a.setIsRead(null);
        NotificationEntity b = full(); b.setIsRead(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - createdAt: ambos null → iguales")
    void shouldBeEqualWithNullCreatedAt() {
        NotificationEntity a = full(); a.setCreatedAt(null);
        NotificationEntity b = full(); b.setCreatedAt(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - readAt: ambos null → iguales")
    void shouldBeEqualWithNullReadAt() {
        NotificationEntity a = full(); a.setReadAt(null);
        NotificationEntity b = full(); b.setReadAt(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - relatedEntityId: ambos null → iguales")
    void shouldBeEqualWithNullRelatedEntityId() {
        NotificationEntity a = full(); a.setRelatedEntityId(null);
        NotificationEntity b = full(); b.setRelatedEntityId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - relatedEntityType: ambos null → iguales")
    void shouldBeEqualWithNullRelatedEntityType() {
        NotificationEntity a = full(); a.setRelatedEntityType(null);
        NotificationEntity b = full(); b.setRelatedEntityType(null);
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
        NotificationEntity e = new NotificationEntity();
        assertDoesNotThrow(e::hashCode);
    }

    @Test
    @DisplayName("🧪 toString - no lanza excepción")
    void shouldProduceToString() {
        assertNotNull(full().toString());
    }
}
