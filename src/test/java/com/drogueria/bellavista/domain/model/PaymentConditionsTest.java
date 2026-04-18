package com.drogueria.bellavista.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentConditionsTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime OTHER_TIME = NOW.plusHours(1);

    private Payment full() {
        return Payment.builder()
            .id(1L).orderId(10L).customerId(20L)
            .stripePaymentId("pi_123").stripeIntentId("intent_456")
            .amount(new BigDecimal("150.00")).currency("USD")
            .status(Payment.PaymentStatus.PENDING)
            .paymentMethod("card").description("Test")
            .errorMessage(null).createdAt(NOW).updatedAt(NOW).paidAt(NOW)
            .build();
    }

    // ── Identity / instanceof guards ──────────────────────────────────────────

    @Test
    @DisplayName("🧪 equals - misma referencia → true (branch o==this)")
    void shouldBeEqualSameReference() {
        Payment p = full();
        assertEquals(p, p);
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
        Payment a = full(); a.setId(2L);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - orderId diferente → no iguales")
    void shouldNotBeEqualDifferentOrderId() {
        Payment a = full(); a.setOrderId(99L);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - customerId diferente → no iguales")
    void shouldNotBeEqualDifferentCustomerId() {
        Payment a = full(); a.setCustomerId(99L);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - stripePaymentId diferente → no iguales")
    void shouldNotBeEqualDifferentStripePaymentId() {
        Payment a = full(); a.setStripePaymentId("pi_other");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - stripeIntentId diferente → no iguales")
    void shouldNotBeEqualDifferentStripeIntentId() {
        Payment a = full(); a.setStripeIntentId("intent_other");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - amount diferente → no iguales")
    void shouldNotBeEqualDifferentAmount() {
        Payment a = full(); a.setAmount(new BigDecimal("999.00"));
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - currency diferente → no iguales")
    void shouldNotBeEqualDifferentCurrency() {
        Payment a = full(); a.setCurrency("EUR");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - status diferente → no iguales")
    void shouldNotBeEqualDifferentStatus() {
        Payment a = full(); a.setStatus(Payment.PaymentStatus.SUCCEEDED);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - paymentMethod diferente → no iguales")
    void shouldNotBeEqualDifferentPaymentMethod() {
        Payment a = full(); a.setPaymentMethod("bank");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - description diferente → no iguales")
    void shouldNotBeEqualDifferentDescription() {
        Payment a = full(); a.setDescription("Other");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - errorMessage diferente (this=null, other!=null) → no iguales")
    void shouldNotBeEqualDifferentErrorMessage() {
        Payment a = full(); a.setErrorMessage("fail");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - createdAt diferente → no iguales")
    void shouldNotBeEqualDifferentCreatedAt() {
        Payment a = full(); a.setCreatedAt(OTHER_TIME);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - updatedAt diferente → no iguales")
    void shouldNotBeEqualDifferentUpdatedAt() {
        Payment a = full(); a.setUpdatedAt(OTHER_TIME);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - paidAt diferente → no iguales")
    void shouldNotBeEqualDifferentPaidAt() {
        Payment a = full(); a.setPaidAt(OTHER_TIME);
        assertNotEquals(full(), a);
    }

    // ── Per-field: Branch A (this==null, other!=null) ────────────────────────

    @Test
    @DisplayName("🧪 equals - id: this=null, other=1L → no iguales")
    void shouldNotBeEqualIdThisNull() {
        Payment a = full(); a.setId(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - orderId: this=null, other=10L → no iguales")
    void shouldNotBeEqualOrderIdThisNull() {
        Payment a = full(); a.setOrderId(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - customerId: this=null, other=20L → no iguales")
    void shouldNotBeEqualCustomerIdThisNull() {
        Payment a = full(); a.setCustomerId(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - stripePaymentId: this=null, other='pi_123' → no iguales")
    void shouldNotBeEqualStripePaymentIdThisNull() {
        Payment a = full(); a.setStripePaymentId(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - stripeIntentId: this=null, other='intent_456' → no iguales")
    void shouldNotBeEqualStripeIntentIdThisNull() {
        Payment a = full(); a.setStripeIntentId(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - amount: this=null, other=150.00 → no iguales")
    void shouldNotBeEqualAmountThisNull() {
        Payment a = full(); a.setAmount(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - currency: this=null, other='USD' → no iguales")
    void shouldNotBeEqualCurrencyThisNull() {
        Payment a = full(); a.setCurrency(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - status: this=null, other=PENDING → no iguales")
    void shouldNotBeEqualStatusThisNull() {
        Payment a = full(); a.setStatus(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - paymentMethod: this=null, other='card' → no iguales")
    void shouldNotBeEqualPaymentMethodThisNull() {
        Payment a = full(); a.setPaymentMethod(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - description: this=null, other='Test' → no iguales")
    void shouldNotBeEqualDescriptionThisNull() {
        Payment a = full(); a.setDescription(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - createdAt: this=null, other=NOW → no iguales")
    void shouldNotBeEqualCreatedAtThisNull() {
        Payment a = full(); a.setCreatedAt(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - updatedAt: this=null, other=NOW → no iguales")
    void shouldNotBeEqualUpdatedAtThisNull() {
        Payment a = full(); a.setUpdatedAt(null);
        assertNotEquals(a, full());
    }

    @Test
    @DisplayName("🧪 equals - paidAt: this=null, other=NOW → no iguales")
    void shouldNotBeEqualPaidAtThisNull() {
        Payment a = full(); a.setPaidAt(null);
        assertNotEquals(a, full());
    }

    // ── Per-field: Branch B (this==null, other==null) ────────────────────────

    @Test
    @DisplayName("🧪 equals - id: ambos null → iguales")
    void shouldBeEqualWithNullId() {
        Payment a = full(); a.setId(null);
        Payment b = full(); b.setId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - stripePaymentId: ambos null → iguales")
    void shouldBeEqualWithNullStripePaymentId() {
        Payment a = full(); a.setStripePaymentId(null);
        Payment b = full(); b.setStripePaymentId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - stripeIntentId: ambos null → iguales")
    void shouldBeEqualWithNullStripeIntentId() {
        Payment a = full(); a.setStripeIntentId(null);
        Payment b = full(); b.setStripeIntentId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - paymentMethod: ambos null → iguales")
    void shouldBeEqualWithNullPaymentMethod() {
        Payment a = full(); a.setPaymentMethod(null);
        Payment b = full(); b.setPaymentMethod(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - description: ambos null → iguales")
    void shouldBeEqualWithNullDescription() {
        Payment a = full(); a.setDescription(null);
        Payment b = full(); b.setDescription(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - updatedAt: ambos null → iguales")
    void shouldBeEqualWithNullUpdatedAt() {
        Payment a = full(); a.setUpdatedAt(null);
        Payment b = full(); b.setUpdatedAt(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - paidAt: ambos null → iguales")
    void shouldBeEqualWithNullPaidAt() {
        Payment a = full(); a.setPaidAt(null);
        Payment b = full(); b.setPaidAt(null);
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
        Payment p = new Payment();
        assertDoesNotThrow(p::hashCode);
    }

    // ── toString / constructors / business methods ────────────────────────────

    @Test
    @DisplayName("🧪 toString - no lanza excepción")
    void shouldProduceToString() {
        assertNotNull(full().toString());
    }

    @Test
    @DisplayName("🧪 noArgsConstructor y setters")
    void shouldUseNoArgsConstructorAndSetters() {
        Payment p = new Payment();
        p.setId(3L); p.setAmount(new BigDecimal("50.00"));
        p.setStatus(Payment.PaymentStatus.FAILED);
        assertEquals(3L, p.getId());
        assertEquals(Payment.PaymentStatus.FAILED, p.getStatus());
    }

    @Test
    @DisplayName("🧪 allArgsConstructor crea objeto completo")
    void shouldUseAllArgsConstructor() {
        Payment p = new Payment(1L, 10L, 20L, "pi_1", "intent_1",
            BigDecimal.TEN, "USD", Payment.PaymentStatus.SUCCEEDED,
            "card", "desc", null, NOW, NOW, NOW);
        assertEquals(Payment.PaymentStatus.SUCCEEDED, p.getStatus());
        assertTrue(p.isSuccessful());
    }

    @Test
    @DisplayName("🧪 PaymentStatus - todos los valores accesibles")
    void shouldAccessAllStatuses() {
        assertEquals(7, Payment.PaymentStatus.values().length);
        for (Payment.PaymentStatus s : Payment.PaymentStatus.values()) {
            assertNotNull(Payment.PaymentStatus.valueOf(s.name()));
        }
    }

    @Test
    @DisplayName("🧪 isFinal - SUCCEEDED, FAILED, REFUNDED son finales; el resto no")
    void shouldIdentifyFinalStatuses() {
        Payment p = full();
        p.setStatus(Payment.PaymentStatus.SUCCEEDED); assertTrue(p.isFinal());
        p.setStatus(Payment.PaymentStatus.FAILED);    assertTrue(p.isFinal());
        p.setStatus(Payment.PaymentStatus.REFUNDED);  assertTrue(p.isFinal());
        p.setStatus(Payment.PaymentStatus.PENDING);   assertFalse(p.isFinal());
        p.setStatus(Payment.PaymentStatus.PROCESSING);assertFalse(p.isFinal());
        p.setStatus(Payment.PaymentStatus.DECLINED);  assertFalse(p.isFinal());
        p.setStatus(Payment.PaymentStatus.CANCELLED); assertFalse(p.isFinal());
    }

    @Test
    @DisplayName("🧪 isSuccessful - solo SUCCEEDED retorna true")
    void shouldBeSuccessfulOnlyForSucceeded() {
        Payment p = full();
        p.setStatus(Payment.PaymentStatus.SUCCEEDED); assertTrue(p.isSuccessful());
        p.setStatus(Payment.PaymentStatus.PENDING);   assertFalse(p.isSuccessful());
        p.setStatus(Payment.PaymentStatus.FAILED);    assertFalse(p.isSuccessful());
    }
}
