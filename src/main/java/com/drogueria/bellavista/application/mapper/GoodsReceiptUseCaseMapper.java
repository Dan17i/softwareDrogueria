package com.drogueria.bellavista.application.mapper;

import com.drogueria.bellavista.application.dto.GoodsReceiptDTO;
import com.drogueria.bellavista.domain.model.GoodsReceipt;
import com.drogueria.bellavista.domain.model.GoodsReceiptItem;
import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.model.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Mapper: Convierte entre DTOs y Modelos de Dominio de Goods Receipt
 */
@Component
@RequiredArgsConstructor
public class GoodsReceiptUseCaseMapper {
    
    /**
     * Convierte CreateRequest a GoodsReceipt (Domain Model)
     * Requiere información del Order para completar datos
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
     * Convierte GoodsReceiptItemRequest a GoodsReceiptItem (Domain Model)
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
     * Convierte GoodsReceipt (Domain Model) a Response DTO
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
     * Convierte GoodsReceiptItem a GoodsReceiptItemResponse DTO
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
