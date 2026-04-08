package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.Payment;
import com.drogueria.bellavista.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    
    public Payment fromDomain(PaymentEntity entity) {
        if (entity == null) return null;
        
        return Payment.builder()
            .id(entity.getId())
            .orderId(entity.getOrderId())
            .customerId(entity.getCustomerId())
            .stripePaymentId(entity.getStripePaymentId())
            .stripeIntentId(entity.getStripeIntentId())
            .amount(entity.getAmount())
            .currency(entity.getCurrency())
            .status(Payment.PaymentStatus.valueOf(entity.getStatus().name()))
            .paymentMethod(entity.getPaymentMethod())
            .description(entity.getDescription())
            .errorMessage(entity.getErrorMessage())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .paidAt(entity.getPaidAt())
            .build();
    }
    
    public PaymentEntity toDomain(Payment domain) {
        if (domain == null) return null;
        
        return PaymentEntity.builder()
            .id(domain.getId())
            .orderId(domain.getOrderId())
            .customerId(domain.getCustomerId())
            .stripePaymentId(domain.getStripePaymentId())
            .stripeIntentId(domain.getStripeIntentId())
            .amount(domain.getAmount())
            .currency(domain.getCurrency())
            .status(PaymentEntity.PaymentStatus.valueOf(domain.getStatus().name()))
            .paymentMethod(domain.getPaymentMethod())
            .description(domain.getDescription())
            .errorMessage(domain.getErrorMessage())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .paidAt(domain.getPaidAt())
            .build();
    }
}
