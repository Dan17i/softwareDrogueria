package com.drogueria.bellavista.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTOs para Goods Receipt (Recepción de Mercancía)
 */
public class GoodsReceiptDTO {

    public record GoodsReceiptItemRequest(

            @NotNull(message = "Product ID is required")
            Long productId,

            @NotBlank(message = "Product code is required")
            @Size(max = 50, message = "Product code cannot exceed 50 characters")
            String productCode,

            @NotBlank(message = "Product name is required")
            @Size(max = 255, message = "Product name cannot exceed 255 characters")
            String productName,

            @NotNull(message = "Ordered quantity is required")
            @Min(value = 1, message = "Ordered quantity must be greater than 0")
            Integer orderedQuantity,

            @NotNull(message = "Received quantity is required")
            @Min(value = 0, message = "Received quantity cannot be negative")
            Integer receivedQuantity) {

        public Long getProductId() { return productId; }
        public String getProductCode() { return productCode; }
        public String getProductName() { return productName; }
        public Integer getOrderedQuantity() { return orderedQuantity; }
        public Integer getReceivedQuantity() { return receivedQuantity; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Long productId;
            private String productCode;
            private String productName;
            private Integer orderedQuantity;
            private Integer receivedQuantity;

            public Builder productId(Long productId) { this.productId = productId; return this; }
            public Builder productCode(String productCode) { this.productCode = productCode; return this; }
            public Builder productName(String productName) { this.productName = productName; return this; }
            public Builder orderedQuantity(Integer orderedQuantity) { this.orderedQuantity = orderedQuantity; return this; }
            public Builder receivedQuantity(Integer receivedQuantity) { this.receivedQuantity = receivedQuantity; return this; }

            public GoodsReceiptItemRequest build() {
                return new GoodsReceiptItemRequest(productId, productCode, productName, orderedQuantity, receivedQuantity);
            }
        }
    }

    public record CreateRequest(

            @NotNull(message = "Order ID is required")
            Long orderId,

            @NotEmpty(message = "Items list cannot be empty")
            @Valid
            List<GoodsReceiptItemRequest> items,

            @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
            String notes,

            LocalDateTime expectedDeliveryDate) {

        public Long getOrderId() { return orderId; }
        public List<GoodsReceiptItemRequest> getItems() { return items; }
        public String getNotes() { return notes; }
        public LocalDateTime getExpectedDeliveryDate() { return expectedDeliveryDate; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Long orderId;
            private List<GoodsReceiptItemRequest> items;
            private String notes;
            private LocalDateTime expectedDeliveryDate;

            public Builder orderId(Long orderId) { this.orderId = orderId; return this; }
            public Builder items(List<GoodsReceiptItemRequest> items) { this.items = items; return this; }
            public Builder notes(String notes) { this.notes = notes; return this; }
            public Builder expectedDeliveryDate(LocalDateTime expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; return this; }

            public CreateRequest build() {
                return new CreateRequest(orderId, items, notes, expectedDeliveryDate);
            }
        }
    }

    public record UpdateRequest(

            @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
            String notes,

            LocalDateTime expectedDeliveryDate) {

        public String getNotes() { return notes; }
        public LocalDateTime getExpectedDeliveryDate() { return expectedDeliveryDate; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String notes;
            private LocalDateTime expectedDeliveryDate;

            public Builder notes(String notes) { this.notes = notes; return this; }
            public Builder expectedDeliveryDate(LocalDateTime expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; return this; }

            public UpdateRequest build() {
                return new UpdateRequest(notes, expectedDeliveryDate);
            }
        }
    }

    public record GoodsReceiptItemResponse(
            Long id,
            Long productId,
            String productCode,
            String productName,
            Integer orderedQuantity,
            Integer receivedQuantity,
            Integer differenceQuantity) {

        public Long getId() { return id; }
        public Long getProductId() { return productId; }
        public String getProductCode() { return productCode; }
        public String getProductName() { return productName; }
        public Integer getOrderedQuantity() { return orderedQuantity; }
        public Integer getReceivedQuantity() { return receivedQuantity; }
        public Integer getDifferenceQuantity() { return differenceQuantity; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Long id;
            private Long productId;
            private String productCode;
            private String productName;
            private Integer orderedQuantity;
            private Integer receivedQuantity;
            private Integer differenceQuantity;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder productId(Long productId) { this.productId = productId; return this; }
            public Builder productCode(String productCode) { this.productCode = productCode; return this; }
            public Builder productName(String productName) { this.productName = productName; return this; }
            public Builder orderedQuantity(Integer orderedQuantity) { this.orderedQuantity = orderedQuantity; return this; }
            public Builder receivedQuantity(Integer receivedQuantity) { this.receivedQuantity = receivedQuantity; return this; }
            public Builder differenceQuantity(Integer differenceQuantity) { this.differenceQuantity = differenceQuantity; return this; }

            public GoodsReceiptItemResponse build() {
                return new GoodsReceiptItemResponse(id, productId, productCode, productName,
                        orderedQuantity, receivedQuantity, differenceQuantity);
            }
        }
    }

    public record Response(
            Long id,
            String receiptNumber,
            Long orderId,
            String orderNumber,
            Long supplierId,
            String supplierName,
            String status,
            String notes,
            LocalDateTime expectedDeliveryDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Integer totalLineItems,
            Integer totalReceivedQuantity,
            List<GoodsReceiptItemResponse> items) {

        public Long getId() { return id; }
        public String getReceiptNumber() { return receiptNumber; }
        public Long getOrderId() { return orderId; }
        public String getOrderNumber() { return orderNumber; }
        public Long getSupplierId() { return supplierId; }
        public String getSupplierName() { return supplierName; }
        public String getStatus() { return status; }
        public String getNotes() { return notes; }
        public LocalDateTime getExpectedDeliveryDate() { return expectedDeliveryDate; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public Integer getTotalLineItems() { return totalLineItems; }
        public Integer getTotalReceivedQuantity() { return totalReceivedQuantity; }
        public List<GoodsReceiptItemResponse> getItems() { return items; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Long id;
            private String receiptNumber;
            private Long orderId;
            private String orderNumber;
            private Long supplierId;
            private String supplierName;
            private String status;
            private String notes;
            private LocalDateTime expectedDeliveryDate;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;
            private Integer totalLineItems;
            private Integer totalReceivedQuantity;
            private List<GoodsReceiptItemResponse> items;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder receiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; return this; }
            public Builder orderId(Long orderId) { this.orderId = orderId; return this; }
            public Builder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
            public Builder supplierId(Long supplierId) { this.supplierId = supplierId; return this; }
            public Builder supplierName(String supplierName) { this.supplierName = supplierName; return this; }
            public Builder status(String status) { this.status = status; return this; }
            public Builder notes(String notes) { this.notes = notes; return this; }
            public Builder expectedDeliveryDate(LocalDateTime expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; return this; }
            public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
            public Builder totalLineItems(Integer totalLineItems) { this.totalLineItems = totalLineItems; return this; }
            public Builder totalReceivedQuantity(Integer totalReceivedQuantity) { this.totalReceivedQuantity = totalReceivedQuantity; return this; }
            public Builder items(List<GoodsReceiptItemResponse> items) { this.items = items; return this; }

            public Response build() {
                return new Response(id, receiptNumber, orderId, orderNumber, supplierId, supplierName,
                        status, notes, expectedDeliveryDate, createdAt, updatedAt, totalLineItems,
                        totalReceivedQuantity, items);
            }
        }
    }
}
