package com.drogueria.bellavista.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA - Orden
 */
@Repository
public interface JpaOrderRepository extends JpaRepository<OrderEntity, Long> {
    
    Optional<OrderEntity> findByOrderNumber(String orderNumber);
    
    @Query("SELECT o FROM OrderEntity o WHERE o.customerId = :customerId ORDER BY o.orderDate DESC")
    List<OrderEntity> findByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT o FROM OrderEntity o WHERE o.status = :status ORDER BY o.orderDate DESC")
    List<OrderEntity> findByStatus(@Param("status") String status);
    
    @Query("SELECT o FROM OrderEntity o WHERE o.customerId = :customerId AND o.status = :status ORDER BY o.orderDate DESC")
    List<OrderEntity> findByCustomerIdAndStatus(@Param("customerId") Long customerId, @Param("status") String status);
    
    @Query("SELECT o FROM OrderEntity o WHERE o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    List<OrderEntity> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    boolean existsByOrderNumber(String orderNumber);
}
