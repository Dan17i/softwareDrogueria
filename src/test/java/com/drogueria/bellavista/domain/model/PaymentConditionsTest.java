package com.drogueria.bellavista.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentConditionsTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

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

    @Test
    @DisplayName("🧪 equals - mismos campos → iguales")
    void shouldBeEqual() {
        assertEquals(full(), full());
    }

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
    @DisplayName("🧪 equals - amount diferente → no iguales")
    void shouldNotBeEqualDifferentAmount() {
        Payment a = full(); a.setAmount(new BigDecimal("999.00"));
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - status diferente → no iguales")
    void shouldNotBeEqualDifferentStatus() {
        Payment a = full(); a.setStatus(Payment.PaymentStatus.SUCCEEDED);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - stripePaymentId diferente → no iguales")
    void shouldNotBeEqualDifferentStripeId() {
        Payment a = full(); a.setStripePaymentId("pi_other");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - stripeIntentId diferente → no iguales")
    void shouldNotBeEqualDifferentIntentId() {
        Payment a = full(); a.setStripeIntentId("intent_other");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - currency diferente → no iguales")
    void shouldNotBeEqualDifferentCurrency() {
        Payment a = full(); a.setCurrency("EUR");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - paymentMethod diferente → no iguales")
    void shouldNotBeEqualDifferentPaymentMethod() {
        Payment a = full(); a.setPaymentMethod("bank");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - errorMessage diferente → no iguales")
    void shouldNotBeEqualDifferentErrorMessage() {
        Payment a = full(); a.setErrorMessage("fail");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - id null en ambos → iguales")
    void shouldBeEqualWithNullId() {
        Payment a = full(); a.setId(null);
        Payment b = full(); b.setId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - stripePaymentId null en ambos → iguales")
    void shouldBeEqualWithNullStripeId() {
        Payment a = full(); a.setStripePaymentId(null);
        Payment b = full(); b.setStripePaymentId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 hashCode - objetos iguales → mismo hashCode")
    void shouldHaveSameHashCode() {
        assertEquals(full().hashCode(), full().hashCode());
    }

    @Test
    @DisplayName("🧪 toString - no lanza excepción")
    void shouldProduceToString() {
        assertNotNull(full().toString());
    }

    @Test
    @DisplayName("🧪 noArgsConstructor y setters")
    void shouldUseNoArgsConstructorAndSetters() {
        Payment p = new Payment();
        p.setId(3L);
        p.setAmount(new BigDecimal("50.00"));
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
        assertNotNull(Payment.PaymentStatus.valueOf("PENDING"));
        assertNotNull(Payment.PaymentStatus.valueOf("PROCESSING"));
        assertNotNull(Payment.PaymentStatus.valueOf("SUCCEEDED"));
        assertNotNull(Payment.PaymentStatus.valueOf("FAILED"));
        assertNotNull(Payment.PaymentStatus.valueOf("DECLINED"));
        assertNotNull(Payment.PaymentStatus.valueOf("CANCELLED"));
        assertNotNull(Payment.PaymentStatus.valueOf("REFUNDED"));
    }

    @Test
    @DisplayName("🧪 isFinal - SUCCEEDED, FAILED, REFUNDED son finales")
    void shouldIdentifyFinalStatuses() {
        Payment p = full();
        p.setStatus(Payment.PaymentStatus.SUCCEEDED);
        assertTrue(p.isFinal());
        p.setStatus(Payment.PaymentStatus.FAILED);
        assertTrue(p.isFinal());
        p.setStatus(Payment.PaymentStatus.REFUNDED);
        assertTrue(p.isFinal());
        p.setStatus(Payment.PaymentStatus.PENDING);
        assertFalse(p.isFinal());
        p.setStatus(Payment.PaymentStatus.PROCESSING);
        assertFalse(p.isFinal());
        p.setStatus(Payment.PaymentStatus.DECLINED);
        assertFalse(p.isFinal());
        p.setStatus(Payment.PaymentStatus.CANCELLED);
        assertFalse(p.isFinal());
    }
}
