package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Payment;
import com.drogueria.bellavista.domain.repository.PaymentRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
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

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Payment pendingPayment;
    private Payment succeededPayment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pendingPayment = Payment.builder()
            .id(1L)
            .orderId(10L)
            .customerId(20L)
            .amount(new BigDecimal("150.00"))
            .currency("USD")
            .status(Payment.PaymentStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        succeededPayment = Payment.builder()
            .id(2L)
            .orderId(10L)
            .customerId(20L)
            .amount(new BigDecimal("200.00"))
            .currency("USD")
            .status(Payment.PaymentStatus.SUCCEEDED)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("✅ createPayment - happy path crea pago PENDING")
    void shouldCreatePaymentSuccessfully() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(pendingPayment);

        Payment result = paymentService.createPayment(10L, 20L, new BigDecimal("150.00"), "Test pago");

        assertNotNull(result);
        assertEquals(Payment.PaymentStatus.PENDING, result.getStatus());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("❌ createPayment - orderId null lanza BusinessException")
    void shouldThrowWhenOrderIdIsNull() {
        assertThrows(BusinessException.class,
            () -> paymentService.createPayment(null, 20L, new BigDecimal("100.00"), "desc"));
        verifyNoInteractions(paymentRepository);
    }

    @Test
    @DisplayName("❌ createPayment - customerId null lanza BusinessException")
    void shouldThrowWhenCustomerIdIsNull() {
        assertThrows(BusinessException.class,
            () -> paymentService.createPayment(10L, null, new BigDecimal("100.00"), "desc"));
        verifyNoInteractions(paymentRepository);
    }

    @Test
    @DisplayName("❌ createPayment - amount cero lanza BusinessException")
    void shouldThrowWhenAmountIsZero() {
        assertThrows(BusinessException.class,
            () -> paymentService.createPayment(10L, 20L, BigDecimal.ZERO, "desc"));
        verifyNoInteractions(paymentRepository);
    }

    @Test
    @DisplayName("❌ createPayment - amount negativo lanza BusinessException")
    void shouldThrowWhenAmountIsNegative() {
        assertThrows(BusinessException.class,
            () -> paymentService.createPayment(10L, 20L, new BigDecimal("-10.00"), "desc"));
        verifyNoInteractions(paymentRepository);
    }

    @Test
    @DisplayName("❌ createPayment - amount null lanza BusinessException")
    void shouldThrowWhenAmountIsNull() {
        assertThrows(BusinessException.class,
            () -> paymentService.createPayment(10L, 20L, null, "desc"));
        verifyNoInteractions(paymentRepository);
    }

    @Test
    @DisplayName("✅ getPaymentById - retorna pago existente")
    void shouldGetPaymentById() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(pendingPayment));

        Payment result = paymentService.getPaymentById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("❌ getPaymentById - no existe lanza ResourceNotFoundException")
    void shouldThrowWhenPaymentNotFound() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.getPaymentById(99L));
    }

    @Test
    @DisplayName("✅ updatePaymentWithStripeData - actualiza stripeId, intentId y status")
    void shouldUpdatePaymentWithStripeData() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(pendingPayment));
        when(paymentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.updatePaymentWithStripeData(
            1L, "pi_stripe_123", "intent_456", Payment.PaymentStatus.PROCESSING);

        assertEquals("pi_stripe_123", result.getStripePaymentId());
        assertEquals("intent_456", result.getStripeIntentId());
        assertEquals(Payment.PaymentStatus.PROCESSING, result.getStatus());
    }

    @Test
    @DisplayName("✅ markAsSucceeded - pago PENDING → SUCCEEDED con paidAt")
    void shouldMarkPaymentAsSucceeded() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(pendingPayment));
        when(paymentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.markAsSucceeded(1L);

        assertEquals(Payment.PaymentStatus.SUCCEEDED, result.getStatus());
        assertNotNull(result.getPaidAt());
    }

    @Test
    @DisplayName("❌ markAsSucceeded - pago en estado final lanza BusinessException")
    void shouldThrowWhenMarkingFinalPaymentAsSucceeded() {
        when(paymentRepository.findById(2L)).thenReturn(Optional.of(succeededPayment));

        assertThrows(BusinessException.class, () -> paymentService.markAsSucceeded(2L));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    @DisplayName("✅ markAsFailed - pago PENDING → FAILED con errorMessage")
    void shouldMarkPaymentAsFailed() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(pendingPayment));
        when(paymentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.markAsFailed(1L, "Fondos insuficientes");

        assertEquals(Payment.PaymentStatus.FAILED, result.getStatus());
        assertEquals("Fondos insuficientes", result.getErrorMessage());
    }

    @Test
    @DisplayName("❌ markAsFailed - pago en estado final lanza BusinessException")
    void shouldThrowWhenMarkingFinalPaymentAsFailed() {
        when(paymentRepository.findById(2L)).thenReturn(Optional.of(succeededPayment));

        assertThrows(BusinessException.class, () -> paymentService.markAsFailed(2L, "error"));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    @DisplayName("✅ refundPayment - pago SUCCEEDED → REFUNDED")
    void shouldRefundSucceededPayment() {
        when(paymentRepository.findById(2L)).thenReturn(Optional.of(succeededPayment));
        when(paymentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.refundPayment(2L);

        assertEquals(Payment.PaymentStatus.REFUNDED, result.getStatus());
    }

    @Test
    @DisplayName("❌ refundPayment - pago no SUCCEEDED lanza BusinessException")
    void shouldThrowWhenRefundingNonSucceededPayment() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(pendingPayment));

        assertThrows(BusinessException.class, () -> paymentService.refundPayment(1L));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    @DisplayName("✅ getPaymentsByOrderId - delega al repository")
    void shouldGetPaymentsByOrderId() {
        when(paymentRepository.findByOrderId(10L)).thenReturn(List.of(pendingPayment));

        List<Payment> result = paymentService.getPaymentsByOrderId(10L);

        assertEquals(1, result.size());
        verify(paymentRepository).findByOrderId(10L);
    }

    @Test
    @DisplayName("✅ getPaymentsByCustomerId - delega al repository")
    void shouldGetPaymentsByCustomerId() {
        when(paymentRepository.findByCustomerId(20L)).thenReturn(List.of(pendingPayment));

        List<Payment> result = paymentService.getPaymentsByCustomerId(20L);

        assertEquals(1, result.size());
        verify(paymentRepository).findByCustomerId(20L);
    }

    @Test
    @DisplayName("✅ getPaymentsByStatus - delega al repository")
    void shouldGetPaymentsByStatus() {
        when(paymentRepository.findByStatus(Payment.PaymentStatus.PENDING)).thenReturn(List.of(pendingPayment));

        List<Payment> result = paymentService.getPaymentsByStatus(Payment.PaymentStatus.PENDING);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("🧪 Payment.isSuccessful - solo SUCCEEDED retorna true")
    void paymentIsSuccessfulOnlyWhenSucceeded() {
        assertTrue(succeededPayment.isSuccessful());
        assertFalse(pendingPayment.isSuccessful());
    }

    @Test
    @DisplayName("🧪 Payment.isFinal - SUCCEEDED, FAILED y REFUNDED son finales")
    void paymentIsFinalForTerminalStatuses() {
        assertTrue(succeededPayment.isFinal());
        assertTrue(Payment.builder().status(Payment.PaymentStatus.FAILED).build().isFinal());
        assertTrue(Payment.builder().status(Payment.PaymentStatus.REFUNDED).build().isFinal());
        assertFalse(pendingPayment.isFinal());
        assertFalse(Payment.builder().status(Payment.PaymentStatus.PROCESSING).build().isFinal());
        assertFalse(Payment.builder().status(Payment.PaymentStatus.DECLINED).build().isFinal());
        assertFalse(Payment.builder().status(Payment.PaymentStatus.CANCELLED).build().isFinal());
    }
}
