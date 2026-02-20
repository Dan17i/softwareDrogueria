package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.GoodsReceipt;
import com.drogueria.bellavista.domain.model.GoodsReceiptItem;
import com.drogueria.bellavista.infrastructure.persistence.GoodsReceiptEntity;
import com.drogueria.bellavista.infrastructure.persistence.GoodsReceiptItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper: Entity ↔ Domain Model
 */
@Component
public class GoodsReceiptMapper {
    
    /**
     * Convertir Entity + Items → Domain Model
     */
    public GoodsReceipt toDomain(GoodsReceiptEntity entity, List<GoodsReceiptItemEntity> itemEntities) {
        if (entity == null) return null;
        
        List<GoodsReceiptItem> items = itemEntities != null ? 
                itemEntities.stream().map(this::itemToDomain).collect(Collectors.toList()) : 
                List.of();
        
        return GoodsReceipt.builder()
                .id(entity.getId())
                .receiptNumber(entity.getReceiptNumber())
                .orderId(entity.getOrderId())
                .orderNumber(entity.getOrderNumber())
                .supplierId(entity.getSupplierId())
                .supplierCode(entity.getSupplierCode())
                .supplierName(entity.getSupplierName())
                .status(entity.getStatus())
                .items(items)
                .notes(entity.getNotes())
                .receiptDate(entity.getReceiptDate())
                .expectedDeliveryDate(entity.getExpectedDeliveryDate())
                .actualDeliveryDate(entity.getActualDeliveryDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    /**
     * Convertir Domain Model → Entity
     */
    public GoodsReceiptEntity toEntity(GoodsReceipt domain) {
        if (domain == null) return null;
        
        return GoodsReceiptEntity.builder()
                .id(domain.getId())
                .receiptNumber(domain.getReceiptNumber())
                .orderId(domain.getOrderId())
                .orderNumber(domain.getOrderNumber())
                .supplierId(domain.getSupplierId())
                .supplierCode(domain.getSupplierCode())
                .supplierName(domain.getSupplierName())
                .status(domain.getStatus())
                .notes(domain.getNotes())
                .receiptDate(domain.getReceiptDate())
                .expectedDeliveryDate(domain.getExpectedDeliveryDate())
                .actualDeliveryDate(domain.getActualDeliveryDate())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
    
    /**
     * Convertir Item Entity → Domain Model
     */
    public GoodsReceiptItem itemToDomain(GoodsReceiptItemEntity entity) {
        if (entity == null) return null;
        
        return GoodsReceiptItem.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .productCode(entity.getProductCode())
                .productName(entity.getProductName())
                .orderedQuantity(entity.getOrderedQuantity())
                .receivedQuantity(entity.getReceivedQuantity())
                .unitPrice(entity.getUnitPrice())
                .notes(entity.getNotes())
                .build();
    }
    
    /**
     * Convertir Item Domain Model → Entity
     */
    public GoodsReceiptItemEntity itemToEntity(GoodsReceiptItem domain, Long goodsReceiptId) {
        if (domain == null) return null;
        
        return GoodsReceiptItemEntity.builder()
                .id(domain.getId())
                .goodsReceiptId(goodsReceiptId)
                .productId(domain.getProductId())
                .productCode(domain.getProductCode())
                .productName(domain.getProductName())
                .orderedQuantity(domain.getOrderedQuantity())
                .receivedQuantity(domain.getReceivedQuantity())
                .unitPrice(domain.getUnitPrice())
                .notes(domain.getNotes())
                .build();
    }
}
