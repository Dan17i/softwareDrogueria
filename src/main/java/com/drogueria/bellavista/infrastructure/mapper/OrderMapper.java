package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.model.OrderItem;
import com.drogueria.bellavista.infrastructure.persistence.OrderEntity;
import com.drogueria.bellavista.infrastructure.persistence.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
/**
 * <h2>OrderMapper</h2>
 *
 * <p>
 * Componente responsable de convertir datos entre:
 * </p>
 *
 * <ul>
 *     <li>{@link OrderEntity} y {@link OrderItemEntity} (capa de persistencia)</li>
 *     <li>{@link Order} y {@link OrderItem} (modelo de dominio)</li>
 * </ul>
 *
 * <p>
 * Este mapper forma parte de la capa de infraestructura dentro de la
 * arquitectura hexagonal (Ports & Adapters), garantizando el desacoplamiento
 * entre el dominio y la base de datos.
 * </p>
 *
 * <p>
 * Gestiona la conversión del agregado principal {@link Order} y su colección
 * de ítems asociados, manteniendo separada la lógica de negocio de los
 * detalles técnicos de persistencia.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Component
public class OrderMapper {
    /**
     * Convierte una entidad {@link OrderEntity} junto con su lista de
     * {@link OrderItemEntity} en un modelo de dominio {@link Order}.
     *
     * <p>
     * Si la entidad principal es {@code null}, retorna {@code null}.
     * La lista de ítems debe ser proporcionada por la capa de persistencia.
     * </p>
     *
     * @param entity entidad principal de la base de datos
     * @param itemEntities lista de entidades de detalle asociadas
     * @return modelo de dominio completamente construido o {@code null}
     */
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
    /**
     * Convierte un modelo de dominio {@link Order}
     * en su entidad de persistencia {@link OrderEntity}.
     *
     * <p>
     * No incluye la conversión de los ítems; estos deben transformarse
     * individualmente utilizando {@link #itemToEntity(OrderItem, Long)}.
     * </p>
     *
     * @param domain objeto del dominio a convertir
     * @return entidad lista para persistencia o {@code null} si el dominio es nulo
     */
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
    /**
     * Convierte una entidad de detalle {@link OrderItemEntity}
     * en un modelo de dominio {@link OrderItem}.
     *
     * @param entity entidad de detalle
     * @return modelo de dominio equivalente o {@code null} si la entidad es nula
     */
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
    /**
     * Convierte un modelo de dominio {@link OrderItem}
     * en su entidad de persistencia {@link OrderItemEntity}.
     *
     * <p>
     * Requiere el identificador del pedido (orderId) para mantener
     * la relación entre el pedido y sus ítems en la base de datos.
     * </p>
     *
     * @param domain ítem del dominio
     * @param orderId identificador del pedido padre
     * @return entidad lista para persistencia o {@code null} si el dominio es nulo
     */
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
