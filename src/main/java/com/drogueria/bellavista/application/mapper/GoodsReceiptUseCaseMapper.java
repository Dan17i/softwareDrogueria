package com.drogueria.bellavista.application.mapper;

import com.drogueria.bellavista.application.dto.GoodsReceiptDTO;
import com.drogueria.bellavista.domain.model.GoodsReceipt;
import com.drogueria.bellavista.domain.model.GoodsReceiptItem;
import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.model.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
/**
 * Mapper de casos de uso para recepciones de mercancía.
 * <p>
 * Responsable de transformar los DTOs utilizados por la capa de aplicación
 * en entidades del dominio {@link GoodsReceipt} y viceversa.
 * </p>
 *
 * <p>
 * Este mapper necesita información adicional del {@link Order} y del
 * {@link Supplier} para completar los datos del dominio durante la creación,
 * ya que el request no contiene toda la información derivada del pedido.
 * </p>
 *
 * Forma parte de la capa de aplicación dentro de la arquitectura hexagonal.
 */
@Component
@RequiredArgsConstructor
public class GoodsReceiptUseCaseMapper {
    /**
     * Convierte un DTO de creación en una entidad de dominio {@link GoodsReceipt}.
     * <p>
     * El número de recepción se genera en el servicio, por lo que aquí se deja nulo.
     * Se utilizan los datos del pedido y del proveedor para completar la información
     * derivada que no viene en el request.
     * </p>
     *
     * @param request   DTO con los datos enviados desde la API
     * @param order     pedido asociado a la recepción
     * @param supplier  proveedor asociado al pedido
     * @return instancia de {@link GoodsReceipt} lista para persistencia
     */
    public GoodsReceipt toDomain(GoodsReceiptDTO.CreateRequest request, Order order, Supplier supplier) {
        GoodsReceipt receipt = GoodsReceipt.builder()
            .receiptNumber(null) // Se genera en el servicio
            .orderId(request.getOrderId())
            .orderNumber(order.getOrderNumber())
            .supplierId(order.getSupplierId())
            .supplierName(supplier.getName())
            .status("PENDING")
            .notes(request.getNotes())
            .expectedDeliveryDate(request.getExpectedDeliveryDate())
            .createdAt(java.time.LocalDateTime.now())
            .updatedAt(java.time.LocalDateTime.now())
            .items(request.getItems().stream()
                .map(this::itemRequestToDomain)
                .collect(Collectors.toList()))
            .build();
        
        return receipt;
    }
    /**
     * Convierte un item del request en una entidad de dominio {@link GoodsReceiptItem}.
     *
     * @param request DTO del item recibido desde la API
     * @return instancia de {@link GoodsReceiptItem} con los valores del request
     */
    public GoodsReceiptItem itemRequestToDomain(GoodsReceiptDTO.GoodsReceiptItemRequest request) {
        return GoodsReceiptItem.builder()
            .productId(request.getProductId())
            .productCode(request.getProductCode())
            .productName(request.getProductName())
            .orderedQuantity(request.getOrderedQuantity())
            .receivedQuantity(request.getReceivedQuantity())
            .build();
    }
    /**
     * Convierte una entidad de dominio {@link GoodsReceipt}
     * en un DTO de respuesta para la API.
     *
     * @param receipt entidad proveniente del dominio
     * @return DTO {@link GoodsReceiptDTO.Response} listo para exposición
     */
    public GoodsReceiptDTO.Response toResponse(GoodsReceipt receipt) {
        return GoodsReceiptDTO.Response.builder()
            .id(receipt.getId())
            .receiptNumber(receipt.getReceiptNumber())
            .orderId(receipt.getOrderId())
            .orderNumber(receipt.getOrderNumber())
            .supplierId(receipt.getSupplierId())
            .supplierName(receipt.getSupplierName())
            .status(receipt.getStatus())
            .notes(receipt.getNotes())
            .expectedDeliveryDate(receipt.getExpectedDeliveryDate())
            .createdAt(receipt.getCreatedAt())
            .updatedAt(receipt.getUpdatedAt())
            .totalLineItems(receipt.getTotalLineItems())
            .totalReceivedQuantity(receipt.getTotalReceivedQuantity())
            .items(receipt.getItems().stream()
                .map(this::itemToResponse)
                .collect(Collectors.toList()))
            .build();
    }
    /**
     * Convierte un item del dominio {@link GoodsReceiptItem}
     * en su DTO de respuesta.
     *
     * @param item entidad del dominio
     * @return DTO {@link GoodsReceiptDTO.GoodsReceiptItemResponse}
     */
    public GoodsReceiptDTO.GoodsReceiptItemResponse itemToResponse(GoodsReceiptItem item) {
        return GoodsReceiptDTO.GoodsReceiptItemResponse.builder()
            .id(item.getId())
            .productId(item.getProductId())
            .productCode(item.getProductCode())
            .productName(item.getProductName())
            .orderedQuantity(item.getOrderedQuantity())
            .receivedQuantity(item.getReceivedQuantity())
            .differenceQuantity(item.getDifference())
            .build();
    }
}
