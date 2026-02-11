package com.drogueria.bellavista.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTOs para Goods Receipt (Recepción de Mercancía)
 */
public class GoodsReceiptDTO {
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoodsReceiptItemRequest {
        
        @NotNull(message = "Product ID is required")
        private Long productId;
        
        @NotBlank(message = "Product code is required")
        @Size(max = 50, message = "Product code cannot exceed 50 characters")
        private String productCode;
        
        @NotBlank(message = "Product name is required")
        @Size(max = 255, message = "Product name cannot exceed 255 characters")
        private String productName;
        
        @NotNull(message = "Ordered quantity is required")
        @Min(value = 1, message = "Ordered quantity must be greater than 0")
        private Integer orderedQuantity;
        
        @NotNull(message = "Received quantity is required")
        @Min(value = 0, message = "Received quantity cannot be negative")
        private Integer receivedQuantity;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        
        @NotNull(message = "Order ID is required")
        private Long orderId;
        
        @NotEmpty(message = "Items list cannot be empty")
        @Valid
        private List<GoodsReceiptItemRequest> items;
        
        @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
        private String notes;
        
        private java.time.LocalDateTime expectedDeliveryDate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        
        @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
        private String notes;
        
        private LocalDateTime expectedDeliveryDate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoodsReceiptItemResponse {
        private Long id;
        private Long productId;
        private String productCode;
        private String productName;
        private Integer orderedQuantity;
        private Integer receivedQuantity;
        private Integer differenceQuantity;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String receiptNumber;
        private Long orderId;
        private String orderNumber;
        private Long supplierId;
        private String supplierName;
        private String status; // PENDING, RECEIVED, PARTIALLY_RECEIVED, REJECTED
        private String notes;
        private LocalDateTime expectedDeliveryDate;
        private java.time.LocalDateTime createdAt;
        private java.time.LocalDateTime updatedAt;
        private Integer totalLineItems;
        private Integer totalReceivedQuantity;
        private List<GoodsReceiptItemResponse> items;
    }
}
