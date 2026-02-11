package com.drogueria.bellavista.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA - Goods Receipt
 */
@Repository
public interface JpaGoodsReceiptRepository extends JpaRepository<GoodsReceiptEntity, Long> {
    
    Optional<GoodsReceiptEntity> findByReceiptNumber(String receiptNumber);
    
    List<GoodsReceiptEntity> findByOrderId(Long orderId);
    
    List<GoodsReceiptEntity> findBySupplierId(Long supplierId);
    
    @Query("SELECT gr FROM GoodsReceiptEntity gr WHERE gr.status = :status")
    List<GoodsReceiptEntity> findByStatus(@Param("status") String status);
    
    @Query("SELECT gr FROM GoodsReceiptEntity gr WHERE gr.status = 'PENDING' ORDER BY gr.createdAt DESC")
    List<GoodsReceiptEntity> findPendingReceipts();
    
    boolean existsByReceiptNumber(String receiptNumber);
}
