package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.model.OrderItem;
import com.drogueria.bellavista.infrastructure.persistence.OrderEntity;
import com.drogueria.bellavista.infrastructure.persistence.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper - Convierte entre Order (Dominio) ↔ OrderEntity (BD)
 */
@Component
public class OrderMapper {
    
    public Order toDomain(OrderEntity entity, List<OrderItemEntity> itemEntities) {
        if (entity == null) {
            return null;
        }
        
        List<OrderItem> items = itemEntities.stream()
            .map(this::itemToDomain)
            .collect(Collectors.toList());
        
        return Order.builder()
            .id(entity.getId())
            .orderNumber(entity.getOrderNumber())
            .customerId(entity.getCustomerId())
            .customerCode(entity.getCustomerCode())
            .customerName(entity.getCustomerName())
            .supplierId(entity.getSupplierId())
            .supplierCode(entity.getSupplierCode())
            .supplierName(entity.getSupplierName())
            .status(entity.getStatus())
            .total(entity.getTotal())
            .items(items)
            .notes(entity.getNotes())
            .orderDate(entity.getOrderDate())
            .expectedDeliveryDate(entity.getExpectedDeliveryDate())
            .actualDeliveryDate(entity.getActualDeliveryDate())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
    
    public OrderEntity toEntity(Order domain) {
        if (domain == null) {
            return null;
        }
        
        return OrderEntity.builder()
            .id(domain.getId())
            .orderNumber(domain.getOrderNumber())
            .customerId(domain.getCustomerId())
            .customerCode(domain.getCustomerCode())
            .customerName(domain.getCustomerName())
            .supplierId(domain.getSupplierId())
            .supplierCode(domain.getSupplierCode())
            .supplierName(domain.getSupplierName())
            .status(domain.getStatus())
            .total(domain.getTotal())
            .notes(domain.getNotes())
            .orderDate(domain.getOrderDate())
            .expectedDeliveryDate(domain.getExpectedDeliveryDate())
            .actualDeliveryDate(domain.getActualDeliveryDate())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .build();
    }
    
    public OrderItem itemToDomain(OrderItemEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return OrderItem.builder()
            .id(entity.getId())
            .productId(entity.getProductId())
            .productCode(entity.getProductCode())
            .productName(entity.getProductName())
            .unitPrice(entity.getUnitPrice())
            .quantity(entity.getQuantity())
            .subtotal(entity.getSubtotal())
            .build();
    }
    
    public OrderItemEntity itemToEntity(OrderItem domain, Long orderId) {
        if (domain == null) {
            return null;
        }
        
        return OrderItemEntity.builder()
            .id(domain.getId())
            .orderId(orderId)
            .productId(domain.getProductId())
            .productCode(domain.getProductCode())
            .productName(domain.getProductName())
            .unitPrice(domain.getUnitPrice())
            .quantity(domain.getQuantity())
            .subtotal(domain.getSubtotal())
            .build();
    }
}
