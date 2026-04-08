package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.domain.model.Payment;
import com.drogueria.bellavista.domain.repository.PaymentRepository;
import com.drogueria.bellavista.infrastructure.mapper.PaymentMapper;
import com.drogueria.bellavista.infrastructure.persistence.entity.PaymentEntity;
import com.drogueria.bellavista.infrastructure.persistence.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {
    
    private final PaymentJpaRepository jpaRepository;
    private final PaymentMapper mapper;
    
    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = mapper.toDomain(payment);
        PaymentEntity saved = jpaRepository.save(entity);
        return mapper.fromDomain(saved);
    }
    
    @Override
    public Optional<Payment> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::fromDomain);
    }
    
    @Override
    public Optional<Payment> findByStripePaymentId(String stripePaymentId) {
        return jpaRepository.findByStripePaymentId(stripePaymentId)
            .map(mapper::fromDomain);
    }
    
    @Override
    public Optional<Payment> findByStripeIntentId(String stripeIntentId) {
        return jpaRepository.findByStripeIntentId(stripeIntentId)
            .map(mapper::fromDomain);
    }
    
    @Override
    public List<Payment> findByOrderId(Long orderId) {
        return jpaRepository.findByOrderId(orderId).stream()
            .map(mapper::fromDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Payment> findByCustomerId(Long customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
            .map(mapper::fromDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Payment> findByStatus(Payment.PaymentStatus status) {
        PaymentEntity.PaymentStatus entityStatus = PaymentEntity.PaymentStatus.valueOf(status.name());
        return jpaRepository.findByStatus(entityStatus).stream()
            .map(mapper::fromDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByCreatedAtBetween(startDate, endDate).stream()
            .map(mapper::fromDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

