package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.Payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Port (interface) for Payment repository.
 * Defines payment persistence contracts.
 */
public interface PaymentRepository {
    
    /**
     * Save a payment.
     */
    Payment save(Payment payment);
    
    /**
     * Find payment by ID.
     */
    Optional<Payment> findById(Long id);
    
    /**
     * Find payment by Stripe Payment ID.
     */
    Optional<Payment> findByStripePaymentId(String stripePaymentId);
    
    /**
     * Find payment by Stripe Intent ID.
     */
    Optional<Payment> findByStripeIntentId(String stripeIntentId);
    
    /**
     * Find all payments for an order.
     */
    List<Payment> findByOrderId(Long orderId);
    
    /**
     * Find all payments for a customer.
     */
    List<Payment> findByCustomerId(Long customerId);
    
    /**
     * Find payments by status.
     */
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    /**
     * Find payments within date range.
     */
    List<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Delete payment by ID.
     */
    void deleteById(Long id);
}

