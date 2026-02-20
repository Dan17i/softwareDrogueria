package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto/Interfaz de Repositorio - Orden
 */
public interface OrderRepository {
    
    Order save(Order order);
    
    Optional<Order> findById(Long id);
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    List<Order> findAll();
    
    List<Order> findByCustomerId(Long customerId);
    
    List<Order> findByStatus(String status);
    
    List<Order> findByCustomerIdAndStatus(Long customerId, String status);
    
    List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    void delete(Long id);
    
    boolean existsByOrderNumber(String orderNumber);
}
