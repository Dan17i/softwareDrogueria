package com.drogueria.bellavista.domain.model;

import java.util.Objects;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain model for Payment.
 * Represents a payment transaction via Stripe.
 */
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

    public Payment() {
    }

    public Payment(Long id, Long orderId, Long customerId, String stripePaymentId, String stripeIntentId, BigDecimal amount, String currency, PaymentStatus status, String paymentMethod, String description, String errorMessage, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime paidAt) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.stripePaymentId = stripePaymentId;
        this.stripeIntentId = stripeIntentId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.paidAt = paidAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getStripePaymentId() {
        return stripePaymentId;
    }

    public void setStripePaymentId(String stripePaymentId) {
        this.stripePaymentId = stripePaymentId;
    }

    public String getStripeIntentId() {
        return stripeIntentId;
    }

    public void setStripeIntentId(String stripeIntentId) {
        this.stripeIntentId = stripeIntentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment other)) return false;
        return Objects.equals(id, other.id) && Objects.equals(orderId, other.orderId) && Objects.equals(customerId, other.customerId) && Objects.equals(stripePaymentId, other.stripePaymentId) && Objects.equals(stripeIntentId, other.stripeIntentId) && Objects.equals(amount, other.amount) && Objects.equals(currency, other.currency) && Objects.equals(status, other.status) && Objects.equals(paymentMethod, other.paymentMethod) && Objects.equals(description, other.description) && Objects.equals(errorMessage, other.errorMessage) && Objects.equals(createdAt, other.createdAt) && Objects.equals(updatedAt, other.updatedAt) && Objects.equals(paidAt, other.paidAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, customerId, stripePaymentId, stripeIntentId, amount, currency, status, paymentMethod, description, errorMessage, createdAt, updatedAt, paidAt);
    }

    @Override
    public String toString() {
        return "Payment(" + "id=" + id + ", orderId=" + orderId + ", customerId=" + customerId + ", stripePaymentId=" + stripePaymentId + ", stripeIntentId=" + stripeIntentId + ", amount=" + amount + ", currency=" + currency + ", status=" + status + ", paymentMethod=" + paymentMethod + ", description=" + description + ", errorMessage=" + errorMessage + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", paidAt=" + paidAt + ")";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
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

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder customerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder stripePaymentId(String stripePaymentId) {
            this.stripePaymentId = stripePaymentId;
            return this;
        }

        public Builder stripeIntentId(String stripeIntentId) {
            this.stripeIntentId = stripeIntentId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder status(PaymentStatus status) {
            this.status = status;
            return this;
        }

        public Builder paymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder paidAt(LocalDateTime paidAt) {
            this.paidAt = paidAt;
            return this;
        }

        public Payment build() {
            return new Payment(id, orderId, customerId, stripePaymentId, stripeIntentId, amount, currency, status, paymentMethod, description, errorMessage, createdAt, updatedAt, paidAt);
        }
    }

    
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

