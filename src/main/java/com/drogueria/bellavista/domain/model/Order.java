package com.drogueria.bellavista.domain.model;

import java.util.Objects;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de dominio - Orden de Compra
 */
public class Order {
    
    private Long id;
    private String orderNumber;
    private Long customerId;
    private String customerCode;
    private String customerName;
    private Long supplierId;
    private String supplierCode;
    private String supplierName;
    private String status; // PENDING, COMPLETED, CANCELLED
    private BigDecimal total;
    private List<OrderItem> items = new ArrayList<>();
    private String notes;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy; // Usuario que creó la orden (para auditoría - Métrica 2.3)

    public Order() {
    }

    public Order(Long id, String orderNumber, Long customerId, String customerCode, String customerName, Long supplierId, String supplierCode, String supplierName, String status, BigDecimal total, List<OrderItem> items, String notes, LocalDateTime orderDate, LocalDateTime expectedDeliveryDate, LocalDateTime actualDeliveryDate, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.supplierId = supplierId;
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.status = status;
        this.total = total;
        this.items = items;
        this.notes = notes;
        this.orderDate = orderDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.actualDeliveryDate = actualDeliveryDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDateTime expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public LocalDateTime getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    public void setActualDeliveryDate(LocalDateTime actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order other)) return false;
        return Objects.equals(id, other.id) && Objects.equals(orderNumber, other.orderNumber) && Objects.equals(customerId, other.customerId) && Objects.equals(customerCode, other.customerCode) && Objects.equals(customerName, other.customerName) && Objects.equals(supplierId, other.supplierId) && Objects.equals(supplierCode, other.supplierCode) && Objects.equals(supplierName, other.supplierName) && Objects.equals(status, other.status) && Objects.equals(total, other.total) && Objects.equals(items, other.items) && Objects.equals(notes, other.notes) && Objects.equals(orderDate, other.orderDate) && Objects.equals(expectedDeliveryDate, other.expectedDeliveryDate) && Objects.equals(actualDeliveryDate, other.actualDeliveryDate) && Objects.equals(createdAt, other.createdAt) && Objects.equals(updatedAt, other.updatedAt) && Objects.equals(createdBy, other.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderNumber, customerId, customerCode, customerName, supplierId, supplierCode, supplierName, status, total, items, notes, orderDate, expectedDeliveryDate, actualDeliveryDate, createdAt, updatedAt, createdBy);
    }

    @Override
    public String toString() {
        return "Order(" + "id=" + id + ", orderNumber=" + orderNumber + ", customerId=" + customerId + ", customerCode=" + customerCode + ", customerName=" + customerName + ", supplierId=" + supplierId + ", supplierCode=" + supplierCode + ", supplierName=" + supplierName + ", status=" + status + ", total=" + total + ", items=" + items + ", notes=" + notes + ", orderDate=" + orderDate + ", expectedDeliveryDate=" + expectedDeliveryDate + ", actualDeliveryDate=" + actualDeliveryDate + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", createdBy=" + createdBy + ")";
    }

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
        private List<OrderItem> items = new ArrayList<>();
        private String notes;
        private LocalDateTime orderDate;
        private LocalDateTime expectedDeliveryDate;
        private LocalDateTime actualDeliveryDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder orderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
            return this;
        }

        public Builder customerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder customerCode(String customerCode) {
            this.customerCode = customerCode;
            return this;
        }

        public Builder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder supplierId(Long supplierId) {
            this.supplierId = supplierId;
            return this;
        }

        public Builder supplierCode(String supplierCode) {
            this.supplierCode = supplierCode;
            return this;
        }

        public Builder supplierName(String supplierName) {
            this.supplierName = supplierName;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder total(BigDecimal total) {
            this.total = total;
            return this;
        }

        public Builder items(List<OrderItem> items) {
            this.items = items;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder orderDate(LocalDateTime orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Builder expectedDeliveryDate(LocalDateTime expectedDeliveryDate) {
            this.expectedDeliveryDate = expectedDeliveryDate;
            return this;
        }

        public Builder actualDeliveryDate(LocalDateTime actualDeliveryDate) {
            this.actualDeliveryDate = actualDeliveryDate;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Order build() {
            return new Order(id, orderNumber, customerId, customerCode, customerName, supplierId, supplierCode, supplierName, status, total, items, notes, orderDate, expectedDeliveryDate, actualDeliveryDate, createdAt, updatedAt, createdBy);
        }
    }

    
    /**
     * Agregar línea a la orden
     */
    public void addItem(OrderItem item) {
        if (item != null) {
            item.calculateSubtotal();
            this.items.add(item);
            recalculateTotal();
        }
    }
    
    /**
     * Recalcular total
     */
    public void recalculateTotal() {
        this.total = this.items.stream()
            .map(OrderItem::getSubtotal)
            .filter(sub -> sub != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Validar si orden puede ser completada
     */
    public boolean canBeCompleted() {
        if (this.status == null || this.total == null) return false;
        return "PENDING".equals(this.status) && this.total.signum() > 0 && !this.items.isEmpty();
    }
    
    /**
     * Marcar como completada
     */
    public void complete() {
        if (!canBeCompleted()) {
            throw new IllegalStateException("La orden no puede ser completada en su estado actual");
        }
        this.status = "COMPLETED";
        this.actualDeliveryDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Cancelar orden
     */
    public void cancel() {
        if ("COMPLETED".equals(this.status)) {
            throw new IllegalStateException("No se pueden cancelar órdenes completadas");
        }
        this.status = "CANCELLED";
        this.updatedAt = LocalDateTime.now();
    }
}
