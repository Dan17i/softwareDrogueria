package com.drogueria.bellavista.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA - Línea de Orden
 */
@Repository
public interface JpaOrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    
    @Query("SELECT oi FROM OrderItemEntity oi WHERE oi.orderId = :orderId")
    List<OrderItemEntity> findByOrderId(@Param("orderId") Long orderId);
    
    void deleteByOrderId(Long orderId);
}
