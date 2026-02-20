package com.drogueria.bellavista.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTOs para Orden
 */
public class OrderDTO {
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        @NotNull(message = "Product ID es requerido")
        private Long productId;
        
        @NotNull(message = "Cantidad es requerida")
        @Min(value = 1, message = "Cantidad debe ser mayor a 0")
        private Integer quantity;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotNull(message = "Customer ID es requerido")
        private Long customerId;
        
        @Valid
        @NotEmpty(message = "Debe contener al menos un producto")
        private List<OrderItemRequest> items;
        
        private String notes;
        
        private LocalDateTime expectedDeliveryDate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private Long id;
        private Long productId;
        private String productCode;
        private String productName;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal subtotal;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String orderNumber;
        private Long customerId;
        private String customerCode;
        private String customerName;
        private Long supplierId;
        private String supplierCode;
        private String supplierName;
        private String status;
        private BigDecimal total;
        private List<OrderItemResponse> items;
        private String notes;
        private LocalDateTime orderDate;
        private LocalDateTime expectedDeliveryDate;
        private LocalDateTime actualDeliveryDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
