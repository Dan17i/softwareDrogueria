package com.drogueria.bellavista.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentEntityConditionsTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

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

    @Test
    @DisplayName("🧪 equals - mismos campos → iguales")
    void shouldBeEqual() {
        assertEquals(full(), full());
    }

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
    @DisplayName("🧪 equals - amount diferente → no iguales")
    void shouldNotBeEqualDifferentAmount() {
        PaymentEntity a = full(); a.setAmount(new BigDecimal("999.00"));
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - status diferente → no iguales")
    void shouldNotBeEqualDifferentStatus() {
        PaymentEntity a = full(); a.setStatus(PaymentEntity.PaymentStatus.SUCCEEDED);
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - stripePaymentId diferente → no iguales")
    void shouldNotBeEqualDifferentStripeId() {
        PaymentEntity a = full(); a.setStripePaymentId("pi_other");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - stripeIntentId diferente → no iguales")
    void shouldNotBeEqualDifferentIntentId() {
        PaymentEntity a = full(); a.setStripeIntentId("intent_other");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - currency diferente → no iguales")
    void shouldNotBeEqualDifferentCurrency() {
        PaymentEntity a = full(); a.setCurrency("EUR");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - paymentMethod diferente → no iguales")
    void shouldNotBeEqualDifferentPaymentMethod() {
        PaymentEntity a = full(); a.setPaymentMethod("bank");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - errorMessage diferente → no iguales")
    void shouldNotBeEqualDifferentErrorMessage() {
        PaymentEntity a = full(); a.setErrorMessage("fail");
        assertNotEquals(full(), a);
    }

    @Test
    @DisplayName("🧪 equals - id null en ambos → iguales")
    void shouldBeEqualWithNullId() {
        PaymentEntity a = full(); a.setId(null);
        PaymentEntity b = full(); b.setId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - stripePaymentId null en ambos → iguales")
    void shouldBeEqualWithNullStripeId() {
        PaymentEntity a = full(); a.setStripePaymentId(null);
        PaymentEntity b = full(); b.setStripePaymentId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 equals - id null vs non-null → no iguales")
    void shouldNotBeEqualWhenOneIdNull() {
        PaymentEntity a = full(); a.setId(null);
        assertNotEquals(a, full());
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
        PaymentEntity e = new PaymentEntity();
        e.setId(5L);
        e.setAmount(new BigDecimal("75.00"));
        e.setStatus(PaymentEntity.PaymentStatus.FAILED);
        e.setCurrency("COP");
        e.setPaymentMethod("PSE");
        e.setDescription("desc");
        e.setErrorMessage("err");
        e.setCreatedAt(NOW);
        e.setUpdatedAt(NOW);
        e.setPaidAt(null);

        assertEquals(5L, e.getId());
        assertEquals(PaymentEntity.PaymentStatus.FAILED, e.getStatus());
        assertEquals("COP", e.getCurrency());
        assertNull(e.getPaidAt());
    }

    @Test
    @DisplayName("🧪 allArgsConstructor crea entidad completa")
    void shouldUseAllArgsConstructor() {
        PaymentEntity e = new PaymentEntity(
            1L, 10L, 20L, "pi_1", "intent_1",
            BigDecimal.TEN, "USD", PaymentEntity.PaymentStatus.SUCCEEDED,
            "card", "desc", null, NOW, NOW, NOW
        );
        assertEquals(PaymentEntity.PaymentStatus.SUCCEEDED, e.getStatus());
        assertEquals("USD", e.getCurrency());
    }

    @Test
    @DisplayName("🧪 PaymentStatus - todos los valores accesibles")
    void shouldAccessAllStatuses() {
        assertEquals(7, PaymentEntity.PaymentStatus.values().length);
        assertNotNull(PaymentEntity.PaymentStatus.valueOf("PENDING"));
        assertNotNull(PaymentEntity.PaymentStatus.valueOf("PROCESSING"));
        assertNotNull(PaymentEntity.PaymentStatus.valueOf("SUCCEEDED"));
        assertNotNull(PaymentEntity.PaymentStatus.valueOf("FAILED"));
        assertNotNull(PaymentEntity.PaymentStatus.valueOf("DECLINED"));
        assertNotNull(PaymentEntity.PaymentStatus.valueOf("CANCELLED"));
        assertNotNull(PaymentEntity.PaymentStatus.valueOf("REFUNDED"));
    }
}
