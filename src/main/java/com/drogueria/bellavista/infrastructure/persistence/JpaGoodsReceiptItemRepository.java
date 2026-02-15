package com.drogueria.bellavista.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA - Goods Receipt Items
 */
@Repository
public interface JpaGoodsReceiptItemRepository extends JpaRepository<GoodsReceiptItemEntity, Long> {
    
    List<GoodsReceiptItemEntity> findByGoodsReceiptId(Long goodsReceiptId);
    
    void deleteByGoodsReceiptId(Long goodsReceiptId);
}
