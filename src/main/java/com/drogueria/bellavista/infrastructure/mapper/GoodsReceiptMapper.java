package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.GoodsReceipt;
import com.drogueria.bellavista.domain.model.GoodsReceiptItem;
import com.drogueria.bellavista.infrastructure.persistence.GoodsReceiptEntity;
import com.drogueria.bellavista.infrastructure.persistence.GoodsReceiptItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h2>GoodsReceiptMapper</h2>
 *
 * <p>
 * Componente encargado de transformar datos entre:
 * </p>
 *
 * <ul>
 *     <li>{@link GoodsReceiptEntity} y {@link GoodsReceiptItemEntity} (capa de persistencia)</li>
 *     <li>{@link GoodsReceipt} y {@link GoodsReceiptItem} (modelo de dominio)</li>
 * </ul>
 *
 * <p>
 * Este mapper pertenece a la capa de infraestructura dentro de la
 * arquitectura hexagonal (Ports & Adapters) y permite desacoplar
 * completamente el dominio de los detalles de base de datos.
 * </p>
 *
 * <p>
 * Maneja la conversión tanto del agregado principal (GoodsReceipt)
 * como de su colección de ítems asociados, respetando el principio
 * de responsabilidad única (SRP).
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Component
public class GoodsReceiptMapper {
    /**
     * Convierte una entidad {@link GoodsReceiptEntity} junto con su lista de
     * {@link GoodsReceiptItemEntity} en un modelo de dominio {@link GoodsReceipt}.
     *
     * <p>
     * Si la entidad principal es {@code null}, retorna {@code null}.
     * Si la lista de ítems es {@code null}, se asigna una lista vacía.
     * </p>
     *
     * @param entity entidad principal obtenida de la base de datos
     * @param itemEntities lista de entidades de detalle asociadas
     * @return modelo de dominio completamente construido o {@code null}
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
     * Convierte un modelo de dominio {@link GoodsReceipt}
     * en su entidad de persistencia {@link GoodsReceiptEntity}.
     *
     * <p>
     * No incluye la conversión de los ítems; estos deben mapearse
     * individualmente mediante {@link #itemToEntity(GoodsReceiptItem, Long)}.
     * </p>
     *
     * @param domain objeto de dominio a convertir
     * @return entidad lista para persistencia o {@code null} si el dominio es nulo
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
     * Convierte una entidad de detalle {@link GoodsReceiptItemEntity}
     * en un modelo de dominio {@link GoodsReceiptItem}.
     *
     * @param entity entidad de detalle
     * @return modelo de dominio equivalente o {@code null} si la entidad es nula
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
     * Convierte un modelo de dominio {@link GoodsReceiptItem}
     * en su entidad de persistencia {@link GoodsReceiptItemEntity}.
     *
     * <p>
     * Requiere el identificador del comprobante (goodsReceiptId)
     * para mantener la relación en la base de datos.
     * </p>
     *
     * @param domain ítem del dominio
     * @param goodsReceiptId identificador del comprobante padre
     * @return entidad lista para persistencia o {@code null} si el dominio es nulo
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
