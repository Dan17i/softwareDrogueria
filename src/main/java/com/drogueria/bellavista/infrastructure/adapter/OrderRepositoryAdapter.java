package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.repository.OrderRepository;
import com.drogueria.bellavista.infrastructure.mapper.OrderMapper;
import com.drogueria.bellavista.infrastructure.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador - Implementa OrderRepository usando Spring Data JPA
 */
@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {
    
    private final JpaOrderRepository jpaRepository;
    private final JpaOrderItemRepository jpaItemRepository;
    private final OrderMapper mapper;
    
    @Override
    public Order save(Order order) {
        // Guardar orden
        OrderEntity orderEntity = mapper.toEntity(order);
        OrderEntity savedOrderEntity = jpaRepository.save(orderEntity);
        
        // Guardar ítems
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            order.getItems().forEach(item -> {
                OrderItemEntity itemEntity = mapper.itemToEntity(item, savedOrderEntity.getId());
                jpaItemRepository.save(itemEntity);
            });
        }
        
        // Recargar los ítems
        List<OrderItemEntity> itemEntities = jpaItemRepository.findByOrderId(savedOrderEntity.getId());
        
        return mapper.toDomain(savedOrderEntity, itemEntities);
    }
    
    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id)
            .map(orderEntity -> {
                List<OrderItemEntity> items = jpaItemRepository.findByOrderId(id);
                return mapper.toDomain(orderEntity, items);
            });
    }
    
    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return jpaRepository.findByOrderNumber(orderNumber)
            .map(orderEntity -> {
                List<OrderItemEntity> items = jpaItemRepository.findByOrderId(orderEntity.getId());
                return mapper.toDomain(orderEntity, items);
            });
    }
    
    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll().stream()
            .map(orderEntity -> {
                List<OrderItemEntity> items = jpaItemRepository.findByOrderId(orderEntity.getId());
                return mapper.toDomain(orderEntity, items);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
            .map(orderEntity -> {
                List<OrderItemEntity> items = jpaItemRepository.findByOrderId(orderEntity.getId());
                return mapper.toDomain(orderEntity, items);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Order> findByStatus(String status) {
        return jpaRepository.findByStatus(status).stream()
            .map(orderEntity -> {
                List<OrderItemEntity> items = jpaItemRepository.findByOrderId(orderEntity.getId());
                return mapper.toDomain(orderEntity, items);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Order> findByCustomerIdAndStatus(Long customerId, String status) {
        return jpaRepository.findByCustomerIdAndStatus(customerId, status).stream()
            .map(orderEntity -> {
                List<OrderItemEntity> items = jpaItemRepository.findByOrderId(orderEntity.getId());
                return mapper.toDomain(orderEntity, items);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByDateRange(startDate, endDate).stream()
            .map(orderEntity -> {
                List<OrderItemEntity> items = jpaItemRepository.findByOrderId(orderEntity.getId());
                return mapper.toDomain(orderEntity, items);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public void delete(Long id) {
        jpaItemRepository.deleteByOrderId(id);
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByOrderNumber(String orderNumber) {
        return jpaRepository.existsByOrderNumber(orderNumber);
    }
}
