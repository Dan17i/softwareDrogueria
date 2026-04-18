package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.domain.model.Payment;
import com.drogueria.bellavista.infrastructure.mapper.PaymentMapper;
import com.drogueria.bellavista.infrastructure.persistence.entity.PaymentEntity;
import com.drogueria.bellavista.infrastructure.persistence.repository.PaymentJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentRepositoryAdapterTest {

    @Mock private PaymentJpaRepository jpaRepository;
    @Mock private PaymentMapper mapper;

    @InjectMocks
    private PaymentRepositoryAdapter adapter;

    private Payment domainPayment;
    private PaymentEntity entity;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        now = LocalDateTime.now();

        domainPayment = Payment.builder()
            .id(1L).orderId(10L).customerId(20L)
            .amount(new BigDecimal("100.00")).currency("USD")
            .status(Payment.PaymentStatus.PENDING)
            .createdAt(now).build();

        entity = PaymentEntity.builder()
            .id(1L).orderId(10L).customerId(20L)
            .amount(new BigDecimal("100.00")).currency("USD")
            .status(PaymentEntity.PaymentStatus.PENDING)
            .createdAt(now).build();
    }

    @Test
    @DisplayName("✅ save - convierte a entidad, persiste y retorna dominio")
    void shouldSavePayment() {
        when(mapper.toDomain(domainPayment)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(mapper.fromDomain(entity)).thenReturn(domainPayment);

        Payment result = adapter.save(domainPayment);

        assertEquals(domainPayment, result);
        verify(jpaRepository).save(entity);
    }

    @Test
    @DisplayName("✅ findById - encontrado → Optional con Payment")
    void shouldFindById() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.fromDomain(entity)).thenReturn(domainPayment);

        Optional<Payment> result = adapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    @DisplayName("❌ findById - no encontrado → Optional vacío")
    void shouldReturnEmptyWhenNotFound() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Payment> result = adapter.findById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("✅ findByStripePaymentId - encontrado → Optional con Payment")
    void shouldFindByStripePaymentId() {
        when(jpaRepository.findByStripePaymentId("pi_123")).thenReturn(Optional.of(entity));
        when(mapper.fromDomain(entity)).thenReturn(domainPayment);

        Optional<Payment> result = adapter.findByStripePaymentId("pi_123");

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("❌ findByStripePaymentId - no encontrado → Optional vacío")
    void shouldReturnEmptyForUnknownStripeId() {
        when(jpaRepository.findByStripePaymentId("unknown")).thenReturn(Optional.empty());

        assertFalse(adapter.findByStripePaymentId("unknown").isPresent());
    }

    @Test
    @DisplayName("✅ findByStripeIntentId - encontrado → Optional con Payment")
    void shouldFindByStripeIntentId() {
        when(jpaRepository.findByStripeIntentId("intent_456")).thenReturn(Optional.of(entity));
        when(mapper.fromDomain(entity)).thenReturn(domainPayment);

        Optional<Payment> result = adapter.findByStripeIntentId("intent_456");

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("✅ findByOrderId - retorna lista mapeada")
    void shouldFindByOrderId() {
        when(jpaRepository.findByOrderId(10L)).thenReturn(List.of(entity));
        when(mapper.fromDomain(entity)).thenReturn(domainPayment);

        List<Payment> result = adapter.findByOrderId(10L);

        assertEquals(1, result.size());
        verify(jpaRepository).findByOrderId(10L);
    }

    @Test
    @DisplayName("✅ findByCustomerId - retorna lista mapeada")
    void shouldFindByCustomerId() {
        when(jpaRepository.findByCustomerId(20L)).thenReturn(List.of(entity));
        when(mapper.fromDomain(entity)).thenReturn(domainPayment);

        List<Payment> result = adapter.findByCustomerId(20L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("✅ findByStatus - convierte status y retorna lista")
    void shouldFindByStatus() {
        when(jpaRepository.findByStatus(PaymentEntity.PaymentStatus.PENDING)).thenReturn(List.of(entity));
        when(mapper.fromDomain(entity)).thenReturn(domainPayment);

        List<Payment> result = adapter.findByStatus(Payment.PaymentStatus.PENDING);

        assertEquals(1, result.size());
        verify(jpaRepository).findByStatus(PaymentEntity.PaymentStatus.PENDING);
    }

    @Test
    @DisplayName("✅ findByCreatedAtBetween - retorna lista en rango de fechas")
    void shouldFindByDateRange() {
        LocalDateTime start = now.minusDays(1);
        LocalDateTime end = now.plusDays(1);
        when(jpaRepository.findByCreatedAtBetween(start, end)).thenReturn(List.of(entity));
        when(mapper.fromDomain(entity)).thenReturn(domainPayment);

        List<Payment> result = adapter.findByCreatedAtBetween(start, end);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("✅ deleteById - delega a jpaRepository")
    void shouldDeleteById() {
        doNothing().when(jpaRepository).deleteById(1L);

        adapter.deleteById(1L);

        verify(jpaRepository).deleteById(1L);
    }
}
