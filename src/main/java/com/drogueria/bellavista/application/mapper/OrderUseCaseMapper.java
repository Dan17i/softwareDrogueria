package com.drogueria.bellavista.application.mapper;

import com.drogueria.bellavista.application.dto.OrderDTO;
import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.model.OrderItem;
import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper - Convierte entre OrderDTO (Request/Response) ↔ Order (Dominio)
 */
@Component
@RequiredArgsConstructor
public class OrderUseCaseMapper {
    
    private final ProductService productService;
    
    public Order toDomain(OrderDTO.CreateRequest request) {
        if (request == null) return null;
        
        Order order = Order.builder()
            .customerId(request.getCustomerId())
            .notes(request.getNotes())
            .expectedDeliveryDate(request.getExpectedDeliveryDate())
            .build();
        
        // Convertir items: obtener datos del producto
        if (request.getItems() != null) {
            request.getItems().forEach(itemRequest -> {
                Product product = productService.getProductById(itemRequest.getProductId());
                
                OrderItem item = OrderItem.builder()
                    .productId(product.getId())
                    .productCode(product.getCode())
                    .productName(product.getName())
                    .unitPrice(product.getPrice())
                    .quantity(itemRequest.getQuantity())
                    .build();
                
                item.calculateSubtotal();
                order.addItem(item);
            });
        }
        
        return order;
    }
    
    public OrderDTO.Response toResponse(Order domain) {
        if (domain == null) return null;
        
        return OrderDTO.Response.builder()
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
            .items(domain.getItems().stream()
                .map(this::itemToResponse)
                .collect(Collectors.toList()))
            .notes(domain.getNotes())
            .orderDate(domain.getOrderDate())
            .expectedDeliveryDate(domain.getExpectedDeliveryDate())
            .actualDeliveryDate(domain.getActualDeliveryDate())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .build();
    }
    
    private OrderDTO.OrderItemResponse itemToResponse(OrderItem item) {
        return OrderDTO.OrderItemResponse.builder()
            .id(item.getId())
            .productId(item.getProductId())
            .productCode(item.getProductCode())
            .productName(item.getProductName())
            .unitPrice(item.getUnitPrice())
            .quantity(item.getQuantity())
            .subtotal(item.getSubtotal())
            .build();
    }
}
