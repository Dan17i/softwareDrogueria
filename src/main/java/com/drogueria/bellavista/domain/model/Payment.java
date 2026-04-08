package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain model for Payment.
 * Represents a payment transaction via Stripe.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    
    private Long id;
    private Long orderId;
    private Long customerId;
    private String stripePaymentId;
    private String stripeIntentId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String paymentMethod;
    private String description;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime paidAt;
    
    /**
     * Payment status enum.
     */
    public enum PaymentStatus {
        PENDING,           // Pago iniciado pero pendiente
        PROCESSING,        // Procesando pago
        SUCCEEDED,         // Pago exitoso
        FAILED,            // Pago fallido
        DECLINED,          // Tarjeta rechazada
        CANCELLED,         // Pago cancelado por usuario
        REFUNDED           // Reembolso realizado
    }
    
    /**
     * Check if payment is successful.
     */
    public boolean isSuccessful() {
        return PaymentStatus.SUCCEEDED.equals(status);
    }
    
    /**
     * Check if payment is final (no more changes possible).
     */
    public boolean isFinal() {
        return PaymentStatus.SUCCEEDED.equals(status) || 
               PaymentStatus.FAILED.equals(status) || 
               PaymentStatus.REFUNDED.equals(status);
    }
}

