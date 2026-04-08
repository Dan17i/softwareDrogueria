package com.drogueria.bellavista.infrastructure.persistence.repository;

import com.drogueria.bellavista.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
    
    Optional<PaymentEntity> findByStripePaymentId(String stripePaymentId);
    
    Optional<PaymentEntity> findByStripeIntentId(String stripeIntentId);
    
    List<PaymentEntity> findByOrderId(Long orderId);
    
    List<PaymentEntity> findByCustomerId(Long customerId);
    
    List<PaymentEntity> findByStatus(PaymentEntity.PaymentStatus status);
    
    List<PaymentEntity> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}

