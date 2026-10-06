package com.drogueria.bellavista.infrastructure.persistence.entity;

import java.util.Objects;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payment_order", columnList = "order_id"),
    @Index(name = "idx_payment_customer", columnList = "customer_id"),
    @Index(name = "idx_payment_status", columnList = "status"),
    @Index(name = "idx_payment_stripe_id", columnList = "stripe_payment_id"),
    @Index(name = "idx_payment_created", columnList = "created_at")
})
public class PaymentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(name = "stripe_payment_id", unique = true)
    private String stripePaymentId;
    
    @Column(name = "stripe_intent_id", unique = true)
    private String stripeIntentId;
    
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    
    @Column(name = "currency", nullable = false)
    private String currency;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    
    @Column(name = "payment_method")
    private String paymentMethod;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "error_message")
    private String errorMessage;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public PaymentEntity() {
    }

    public PaymentEntity(Long id, Long orderId, Long customerId, String stripePaymentId, String stripeIntentId, BigDecimal amount, String currency, PaymentStatus status, String paymentMethod, String description, String errorMessage, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime paidAt) {
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
        if (!(o instanceof PaymentEntity other)) return false;
        return Objects.equals(id, other.id) && Objects.equals(orderId, other.orderId) && Objects.equals(customerId, other.customerId) && Objects.equals(stripePaymentId, other.stripePaymentId) && Objects.equals(stripeIntentId, other.stripeIntentId) && Objects.equals(amount, other.amount) && Objects.equals(currency, other.currency) && Objects.equals(status, other.status) && Objects.equals(paymentMethod, other.paymentMethod) && Objects.equals(description, other.description) && Objects.equals(errorMessage, other.errorMessage) && Objects.equals(createdAt, other.createdAt) && Objects.equals(updatedAt, other.updatedAt) && Objects.equals(paidAt, other.paidAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, customerId, stripePaymentId, stripeIntentId, amount, currency, status, paymentMethod, description, errorMessage, createdAt, updatedAt, paidAt);
    }

    @Override
    public String toString() {
        return "PaymentEntity(" + "id=" + id + ", orderId=" + orderId + ", customerId=" + customerId + ", stripePaymentId=" + stripePaymentId + ", stripeIntentId=" + stripeIntentId + ", amount=" + amount + ", currency=" + currency + ", status=" + status + ", paymentMethod=" + paymentMethod + ", description=" + description + ", errorMessage=" + errorMessage + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", paidAt=" + paidAt + ")";
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

        public PaymentEntity build() {
            return new PaymentEntity(id, orderId, customerId, stripePaymentId, stripeIntentId, amount, currency, status, paymentMethod, description, errorMessage, createdAt, updatedAt, paidAt);
        }
    }

    
    public enum PaymentStatus {
        PENDING,
        PROCESSING,
        SUCCEEDED,
        FAILED,
        DECLINED,
        CANCELLED,
        REFUNDED
    }
}

