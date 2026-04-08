package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Payment;
import com.drogueria.bellavista.domain.repository.PaymentRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de dominio - Casos de uso de Pagos
 * Contiene toda la lógica de negocio relacionada con pagos via Stripe
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    
    /**
     * Crear un nuevo pago.
     */
    public Payment createPayment(Long orderId, Long customerId, BigDecimal amount, String description) {
        if (orderId == null) {
            throw new BusinessException("Order ID es requerido para crear un pago");
        }
        
        if (customerId == null) {
            throw new BusinessException("Customer ID es requerido para crear un pago");
        }
        
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto del pago debe ser mayor a 0");
        }
        
        Payment payment = Payment.builder()
            .orderId(orderId)
            .customerId(customerId)
            .amount(amount)
            .currency("USD")
            .status(Payment.PaymentStatus.PENDING)
            .description(description)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        
        Payment saved = paymentRepository.save(payment);
        log.info("✅ Pago creado: ID={}, Orden={}, Monto={}", saved.getId(), orderId, amount);
        
        return saved;
    }
    
    /**
     * Obtener pago por ID.
     */
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
    }
    
    /**
     * Obtener pago por Stripe Payment ID.
     */
    public Payment getByStripePaymentId(String stripePaymentId) {
        return paymentRepository.findByStripePaymentId(stripePaymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment with Stripe ID: " + stripePaymentId + " not found"));
    }
    
    /**
     * Actualizar pago con datos de Stripe.
     */
    public Payment updatePaymentWithStripeData(Long paymentId, String stripePaymentId, String stripeIntentId, Payment.PaymentStatus status) {
        Payment payment = getPaymentById(paymentId);
        
        payment.setStripePaymentId(stripePaymentId);
        payment.setStripeIntentId(stripeIntentId);
        payment.setStatus(status);
        payment.setUpdatedAt(LocalDateTime.now());
        
        Payment updated = paymentRepository.save(payment);
        log.info("📤 Pago actualizado con datos de Stripe: ID={}, Status={}", paymentId, status);
        
        return updated;
    }
    
    /**
     * Marcar pago como exitoso.
     */
    public Payment markAsSucceeded(Long paymentId) {
        Payment payment = getPaymentById(paymentId);
        
        if (payment.isFinal()) {
            throw new BusinessException("El pago ya tiene un estado final: " + payment.getStatus());
        }
        
        payment.setStatus(Payment.PaymentStatus.SUCCEEDED);
        payment.setPaidAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        
        Payment updated = paymentRepository.save(payment);
        log.info("✅ Pago marcado como exitoso: ID={}", paymentId);
        
        return updated;
    }
    
    /**
     * Marcar pago como fallido.
     */
    public Payment markAsFailed(Long paymentId, String errorMessage) {
        Payment payment = getPaymentById(paymentId);
        
        if (payment.isFinal()) {
            throw new BusinessException("El pago ya tiene un estado final: " + payment.getStatus());
        }
        
        payment.setStatus(Payment.PaymentStatus.FAILED);
        payment.setErrorMessage(errorMessage);
        payment.setUpdatedAt(LocalDateTime.now());
        
        Payment updated = paymentRepository.save(payment);
        log.warn("❌ Pago marcado como fallido: ID={}, Error={}", paymentId, errorMessage);
        
        return updated;
    }
    
    /**
     * Obtener todos los pagos de una orden.
     */
    public List<Payment> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
    
    /**
     * Obtener todos los pagos de un cliente.
     */
    public List<Payment> getPaymentsByCustomerId(Long customerId) {
        return paymentRepository.findByCustomerId(customerId);
    }
    
    /**
     * Obtener pagos por estado.
     */
    public List<Payment> getPaymentsByStatus(Payment.PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }
    
    /**
     * Obtener pagos en un rango de fechas.
     */
    public List<Payment> getPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    /**
     * Refundar un pago.
     */
    public Payment refundPayment(Long paymentId) {
        Payment payment = getPaymentById(paymentId);
        
        if (!payment.isSuccessful()) {
            throw new BusinessException("Solo se pueden refundar pagos exitosos");
        }
        
        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        payment.setUpdatedAt(LocalDateTime.now());
        
        Payment updated = paymentRepository.save(payment);
        log.info("💰 Reembolso procesado: ID={}", paymentId);
        
        return updated;
    }
}
