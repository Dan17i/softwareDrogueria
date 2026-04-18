package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.Payment;
import com.drogueria.bellavista.infrastructure.persistence.entity.PaymentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMapperTest {

    private PaymentMapper mapper;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        mapper = new PaymentMapper();
        now = LocalDateTime.now();
    }

    @Test
    @DisplayName("✅ fromDomain - entidad completa → Payment correcto")
    void shouldMapEntityToDomain() {
        PaymentEntity entity = PaymentEntity.builder()
            .id(1L).orderId(10L).customerId(20L)
            .stripePaymentId("pi_123").stripeIntentId("intent_456")
            .amount(new BigDecimal("150.00")).currency("USD")
            .status(PaymentEntity.PaymentStatus.SUCCEEDED)
            .paymentMethod("card").description("Pago test")
            .errorMessage(null).createdAt(now).updatedAt(now).paidAt(now)
            .build();

        Payment result = mapper.fromDomain(entity);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(10L, result.getOrderId());
        assertEquals(20L, result.getCustomerId());
        assertEquals("pi_123", result.getStripePaymentId());
        assertEquals("intent_456", result.getStripeIntentId());
        assertEquals(new BigDecimal("150.00"), result.getAmount());
        assertEquals("USD", result.getCurrency());
        assertEquals(Payment.PaymentStatus.SUCCEEDED, result.getStatus());
        assertEquals("card", result.getPaymentMethod());
        assertEquals("Pago test", result.getDescription());
        assertEquals(now, result.getCreatedAt());
        assertEquals(now, result.getPaidAt());
    }

    @Test
    @DisplayName("✅ fromDomain - null → null")
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(mapper.fromDomain(null));
    }

    @Test
    @DisplayName("✅ toDomain - Payment completo → entidad correcta")
    void shouldMapDomainToEntity() {
        Payment payment = Payment.builder()
            .id(2L).orderId(11L).customerId(21L)
            .stripePaymentId("pi_789").stripeIntentId("intent_012")
            .amount(new BigDecimal("200.00")).currency("USD")
            .status(Payment.PaymentStatus.PENDING)
            .paymentMethod("card").description("Test")
            .errorMessage("none").createdAt(now).updatedAt(now).paidAt(null)
            .build();

        PaymentEntity result = mapper.toDomain(payment);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(11L, result.getOrderId());
        assertEquals(21L, result.getCustomerId());
        assertEquals("pi_789", result.getStripePaymentId());
        assertEquals(PaymentEntity.PaymentStatus.PENDING, result.getStatus());
        assertEquals(new BigDecimal("200.00"), result.getAmount());
        assertNull(result.getPaidAt());
    }

    @Test
    @DisplayName("✅ toDomain - null → null")
    void shouldReturnNullWhenPaymentIsNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("🧪 fromDomain - todos los PaymentStatus se mapean correctamente")
    void shouldMapAllPaymentStatuses() {
        for (PaymentEntity.PaymentStatus entityStatus : PaymentEntity.PaymentStatus.values()) {
            PaymentEntity entity = PaymentEntity.builder()
                .id(1L).orderId(1L).customerId(1L)
                .amount(BigDecimal.ONE).currency("USD")
                .status(entityStatus).createdAt(now)
                .build();

            Payment result = mapper.fromDomain(entity);
            assertEquals(Payment.PaymentStatus.valueOf(entityStatus.name()), result.getStatus());
        }
    }

    @Test
    @DisplayName("🧪 toDomain - todos los PaymentStatus se mapean correctamente")
    void shouldMapAllDomainStatusesToEntity() {
        for (Payment.PaymentStatus domainStatus : Payment.PaymentStatus.values()) {
            Payment payment = Payment.builder()
                .id(1L).orderId(1L).customerId(1L)
                .amount(BigDecimal.ONE).currency("USD")
                .status(domainStatus).createdAt(now)
                .build();

            PaymentEntity result = mapper.toDomain(payment);
            assertEquals(PaymentEntity.PaymentStatus.valueOf(domainStatus.name()), result.getStatus());
        }
    }
}
