package com.drogueria.bellavista.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentEntityConditionsTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime OTHER_TIME = NOW.plusHours(1);

    private PaymentEntity full() {
        return PaymentEntity.builder()
            .id(1L).orderId(10L).customerId(20L)
            .stripePaymentId("pi_123").stripeIntentId("intent_456")
            .amount(new BigDecimal("150.00")).currency("USD")
            .status(PaymentEntity.PaymentStatus.PENDING)
            .paymentMethod("card").description("Test")
            .errorMessage(null).createdAt(NOW).updatedAt(NOW).paidAt(NOW)
            .build();
    }

    // ── Identity / instanceof guards ──────────────────────────────────────────

    @Test
    @DisplayName("🧪 equals - misma referencia → true (branch o==this)")
    void shouldBeEqualSameReference() {
        PaymentEntity e = full();
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
        assertNotEquals(full(), "string");
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
        PaymentEntity a = full(); a.setId(2L);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - orderId diferente → no iguales")
    void shouldNotBeEqualDifferentOrderId() {
        PaymentEntity a = full(); a.setOrderId(99L);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - customerId diferente → no iguales")
    void shouldNotBeEqualDifferentCustomerId() {
        PaymentEntity a = full(); a.setCustomerId(99L);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - stripePaymentId diferente → no iguales")
    void shouldNotBeEqualDifferentStripePaymentId() {
        PaymentEntity a = full(); a.setStripePaymentId("pi_other");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - stripeIntentId diferente → no iguales")
    void shouldNotBeEqualDifferentStripeIntentId() {
        PaymentEntity a = full(); a.setStripeIntentId("intent_other");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - amount diferente → no iguales")
    void shouldNotBeEqualDifferentAmount() {
        PaymentEntity a = full(); a.setAmount(new BigDecimal("999.00"));
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - currency diferente → no iguales")
    void shouldNotBeEqualDifferentCurrency() {
        PaymentEntity a = full(); a.setCurrency("EUR");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - status diferente → no iguales")
    void shouldNotBeEqualDifferentStatus() {
        PaymentEntity a = full(); a.setStatus(PaymentEntity.PaymentStatus.SUCCEEDED);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - paymentMethod diferente → no iguales")
    void shouldNotBeEqualDifferentPaymentMethod() {
        PaymentEntity a = full(); a.setPaymentMethod("bank");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - description diferente → no iguales")
    void shouldNotBeEqualDifferentDescription() {
        PaymentEntity a = full(); a.setDescription("Other");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - errorMessage diferente (this=null, other!=null) → no iguales")
    void shouldNotBeEqualDifferentErrorMessage() {
        PaymentEntity a = full(); a.setErrorMessage("fail");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - createdAt diferente → no iguales")
    void shouldNotBeEqualDifferentCreatedAt() {
        PaymentEntity a = full(); a.setCreatedAt(OTHER_TIME);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - updatedAt diferente → no iguales")
    void shouldNotBeEqualDifferentUpdatedAt() {
        PaymentEntity a = full(); a.setUpdatedAt(OTHER_TIME);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - paidAt diferente → no iguales")
    void shouldNotBeEqualDifferentPaidAt() {
        PaymentEntity a = full(); a.setPaidAt(OTHER_TIME);
        assertNotEquals(full(), a);
    }

    // ── Per-field: Branch A (this==null, other!=null) ────────────────────────

    @Test
    @DisplayName("🧪 equals - id: this=null, other=1L → no iguales")
    void shouldNotBeEqualIdThisNull() {
        PaymentEntity a = full(); a.setId(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - orderId: this=null, other=10L → no iguales")
    void shouldNotBeEqualOrderIdThisNull() {
        PaymentEntity a = full(); a.setOrderId(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - customerId: this=null, other=20L → no iguales")
    void shouldNotBeEqualCustomerIdThisNull() {
        PaymentEntity a = full(); a.setCustomerId(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - stripePaymentId: this=null, other='pi_123' → no iguales")
    void shouldNotBeEqualStripePaymentIdThisNull() {
        PaymentEntity a = full(); a.setStripePaymentId(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - stripeIntentId: this=null, other='intent_456' → no iguales")
    void shouldNotBeEqualStripeIntentIdThisNull() {
        PaymentEntity a = full(); a.setStripeIntentId(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - amount: this=null, other=150.00 → no iguales")
    void shouldNotBeEqualAmountThisNull() {
        PaymentEntity a = full(); a.setAmount(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - currency: this=null, other='USD' → no iguales")
    void shouldNotBeEqualCurrencyThisNull() {
        PaymentEntity a = full(); a.setCurrency(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - status: this=null, other=PENDING → no iguales")
    void shouldNotBeEqualStatusThisNull() {
        PaymentEntity a = full(); a.setStatus(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - paymentMethod: this=null, other='card' → no iguales")
    void shouldNotBeEqualPaymentMethodThisNull() {
        PaymentEntity a = full(); a.setPaymentMethod(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - description: this=null, other='Test' → no iguales")
    void shouldNotBeEqualDescriptionThisNull() {
        PaymentEntity a = full(); a.setDescription(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - createdAt: this=null, other=NOW → no iguales")
    void shouldNotBeEqualCreatedAtThisNull() {
        PaymentEntity a = full(); a.setCreatedAt(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - updatedAt: this=null, other=NOW → no iguales")
    void shouldNotBeEqualUpdatedAtThisNull() {
        PaymentEntity a = full(); a.setUpdatedAt(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - paidAt: this=null, other=NOW → no iguales")
    void shouldNotBeEqualPaidAtThisNull() {
        PaymentEntity a = full(); a.setPaidAt(null);
        assertNotEquals(a, full());
    }

    // ── Per-field: Branch B (this==null, other==null) ────────────────────────

    @Test
    @DisplayName("🧪 equals - id: ambos null → iguales")
    void shouldBeEqualWithNullId() {
        PaymentEntity a = full(); a.setId(null);
        PaymentEntity b = full(); b.setId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - stripePaymentId: ambos null → iguales")
    void shouldBeEqualWithNullStripePaymentId() {
        PaymentEntity a = full(); a.setStripePaymentId(null);
        PaymentEntity b = full(); b.setStripePaymentId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - stripeIntentId: ambos null → iguales")
    void shouldBeEqualWithNullStripeIntentId() {
        PaymentEntity a = full(); a.setStripeIntentId(null);
        PaymentEntity b = full(); b.setStripeIntentId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - paymentMethod: ambos null → iguales")
    void shouldBeEqualWithNullPaymentMethod() {
        PaymentEntity a = full(); a.setPaymentMethod(null);
        PaymentEntity b = full(); b.setPaymentMethod(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - description: ambos null → iguales")
    void shouldBeEqualWithNullDescription() {
        PaymentEntity a = full(); a.setDescription(null);
        PaymentEntity b = full(); b.setDescription(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - updatedAt: ambos null → iguales")
    void shouldBeEqualWithNullUpdatedAt() {
        PaymentEntity a = full(); a.setUpdatedAt(null);
        PaymentEntity b = full(); b.setUpdatedAt(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - paidAt: ambos null → iguales")
    void shouldBeEqualWithNullPaidAt() {
        PaymentEntity a = full(); a.setPaidAt(null);
        PaymentEntity b = full(); b.setPaidAt(null);
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
        PaymentEntity e = new PaymentEntity();
        assertDoesNotThrow(e::hashCode);
    }

    // ── toString / constructors ───────────────────────────────────────────────

    @Test
    @DisplayName("🧪 toString - no lanza excepción")
    void shouldProduceToString() {
        assertNotNull(full().toString());
    }

    @Test
    @DisplayName("🧪 noArgsConstructor y setters")
    void shouldUseNoArgsConstructorAndSetters() {
        PaymentEntity e = new PaymentEntity();
        e.setId(5L); e.setAmount(new BigDecimal("75.00"));
        e.setStatus(PaymentEntity.PaymentStatus.FAILED);
        e.setCurrency("COP"); e.setPaymentMethod("PSE");
        e.setDescription("desc"); e.setErrorMessage("err");
        e.setCreatedAt(NOW); e.setUpdatedAt(NOW); e.setPaidAt(null);
        assertEquals(5L, e.getId());
        assertEquals(PaymentEntity.PaymentStatus.FAILED, e.getStatus());
        assertNull(e.getPaidAt());
    }

    @Test
    @DisplayName("🧪 allArgsConstructor crea entidad completa")
    void shouldUseAllArgsConstructor() {
        PaymentEntity e = new PaymentEntity(1L, 10L, 20L, "pi_1", "intent_1",
            BigDecimal.TEN, "USD", PaymentEntity.PaymentStatus.SUCCEEDED,
            "card", "desc", null, NOW, NOW, NOW);
        assertEquals(PaymentEntity.PaymentStatus.SUCCEEDED, e.getStatus());
    }

    @Test
    @DisplayName("🧪 PaymentStatus - todos los valores accesibles")
    void shouldAccessAllStatuses() {
        assertEquals(7, PaymentEntity.PaymentStatus.values().length);
        for (PaymentEntity.PaymentStatus s : PaymentEntity.PaymentStatus.values()) {
            assertNotNull(PaymentEntity.PaymentStatus.valueOf(s.name()));
        }
    }
}
