package com.drogueria.bellavista.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTOs para Orden
 */
public class OrderDTO {

    public record OrderItemRequest(

            @NotNull(message = "Product ID es requerido")
            Long productId,

            @NotNull(message = "Cantidad es requerida")
            @Min(value = 1, message = "Cantidad debe ser mayor a 0")
            Integer quantity) {

        public Long getProductId() { return productId; }
        public Integer getQuantity() { return quantity; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Long productId;
            private Integer quantity;

            public Builder productId(Long productId) { this.productId = productId; return this; }
            public Builder quantity(Integer quantity) { this.quantity = quantity; return this; }

            public OrderItemRequest build() {
                return new OrderItemRequest(productId, quantity);
            }
        }
    }

    public record CreateRequest(

            @NotNull(message = "Customer ID es requerido")
            Long customerId,

            Long supplierId,

            @Valid
            @NotEmpty(message = "Debe contener al menos un producto")
            List<OrderItemRequest> items,

            String notes,

            LocalDateTime expectedDeliveryDate) {

        public Long getCustomerId() { return customerId; }
        public Long getSupplierId() { return supplierId; }
        public List<OrderItemRequest> getItems() { return items; }
        public String getNotes() { return notes; }
        public LocalDateTime getExpectedDeliveryDate() { return expectedDeliveryDate; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Long customerId;
            private Long supplierId;
            private List<OrderItemRequest> items;
            private String notes;
            private LocalDateTime expectedDeliveryDate;

            public Builder customerId(Long customerId) { this.customerId = customerId; return this; }
            public Builder supplierId(Long supplierId) { this.supplierId = supplierId; return this; }
            public Builder items(List<OrderItemRequest> items) { this.items = items; return this; }
            public Builder notes(String notes) { this.notes = notes; return this; }
            public Builder expectedDeliveryDate(LocalDateTime expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; return this; }

            public CreateRequest build() {
                return new CreateRequest(customerId, supplierId, items, notes, expectedDeliveryDate);
            }
        }
    }

    public record OrderItemResponse(
            Long id,
            Long productId,
            String productCode,
            String productName,
            BigDecimal unitPrice,
            Integer quantity,
            BigDecimal subtotal) {

        public Long getId() { return id; }
        public Long getProductId() { return productId; }
        public String getProductCode() { return productCode; }
        public String getProductName() { return productName; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public Integer getQuantity() { return quantity; }
        public BigDecimal getSubtotal() { return subtotal; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Long id;
            private Long productId;
            private String productCode;
            private String productName;
            private BigDecimal unitPrice;
            private Integer quantity;
            private BigDecimal subtotal;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder productId(Long productId) { this.productId = productId; return this; }
            public Builder productCode(String productCode) { this.productCode = productCode; return this; }
            public Builder productName(String productName) { this.productName = productName; return this; }
            public Builder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
            public Builder quantity(Integer quantity) { this.quantity = quantity; return this; }
            public Builder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }

            public OrderItemResponse build() {
                return new OrderItemResponse(id, productId, productCode, productName, unitPrice, quantity, subtotal);
            }
        }
    }

    public record Response(
            Long id,
            String orderNumber,
            Long customerId,
            String customerCode,
            String customerName,
            Long supplierId,
            String supplierCode,
            String supplierName,
            String status,
            BigDecimal total,
            List<OrderItemResponse> items,
            String notes,
            LocalDateTime orderDate,
            LocalDateTime expectedDeliveryDate,
            LocalDateTime actualDeliveryDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        public Long getId() { return id; }
        public String getOrderNumber() { return orderNumber; }
        public Long getCustomerId() { return customerId; }
        public String getCustomerCode() { return customerCode; }
        public String getCustomerName() { return customerName; }
        public Long getSupplierId() { return supplierId; }
        public String getSupplierCode() { return supplierCode; }
        public String getSupplierName() { return supplierName; }
        public String getStatus() { return status; }
        public BigDecimal getTotal() { return total; }
        public List<OrderItemResponse> getItems() { return items; }
        public String getNotes() { return notes; }
        public LocalDateTime getOrderDate() { return orderDate; }
        public LocalDateTime getExpectedDeliveryDate() { return expectedDeliveryDate; }
        public LocalDateTime getActualDeliveryDate() { return actualDeliveryDate; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
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

            public Builder id(Long id) { this.id = id; return this; }
            public Builder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
            public Builder customerId(Long customerId) { this.customerId = customerId; return this; }
            public Builder customerCode(String customerCode) { this.customerCode = customerCode; return this; }
            public Builder customerName(String customerName) { this.customerName = customerName; return this; }
            public Builder supplierId(Long supplierId) { this.supplierId = supplierId; return this; }
            public Builder supplierCode(String supplierCode) { this.supplierCode = supplierCode; return this; }
            public Builder supplierName(String supplierName) { this.supplierName = supplierName; return this; }
            public Builder status(String status) { this.status = status; return this; }
            public Builder total(BigDecimal total) { this.total = total; return this; }
            public Builder items(List<OrderItemResponse> items) { this.items = items; return this; }
            public Builder notes(String notes) { this.notes = notes; return this; }
            public Builder orderDate(LocalDateTime orderDate) { this.orderDate = orderDate; return this; }
            public Builder expectedDeliveryDate(LocalDateTime expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; return this; }
            public Builder actualDeliveryDate(LocalDateTime actualDeliveryDate) { this.actualDeliveryDate = actualDeliveryDate; return this; }
            public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

            public Response build() {
                return new Response(id, orderNumber, customerId, customerCode, customerName,
                        supplierId, supplierCode, supplierName, status, total, items, notes,
                        orderDate, expectedDeliveryDate, actualDeliveryDate, createdAt, updatedAt);
            }
        }
    }
}
