package com.drogueria.bellavista.domain.model;

import java.util.Objects;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de dominio - Recepción de Mercancía
 * Vincula un Order con un Supplier para recibir mercancía
 */
public class GoodsReceipt {
    
    private Long id;
    private String receiptNumber; // Único, ej: GR-2024-001
    private Long orderId;
    private String orderNumber;
    private Long supplierId;
    private String supplierCode;
    private String supplierName;
    private String status; // PENDING, RECEIVED, PARTIALLY_RECEIVED, REJECTED
    private List<GoodsReceiptItem> items = new ArrayList<>();
    private String notes;
    private LocalDateTime receiptDate;
    private LocalDateTime expectedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GoodsReceipt() {
    }

    public GoodsReceipt(Long id, String receiptNumber, Long orderId, String orderNumber, Long supplierId, String supplierCode, String supplierName, String status, List<GoodsReceiptItem> items, String notes, LocalDateTime receiptDate, LocalDateTime expectedDeliveryDate, LocalDateTime actualDeliveryDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.receiptNumber = receiptNumber;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.supplierId = supplierId;
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.status = status;
        this.items = items;
        this.notes = notes;
        this.receiptDate = receiptDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.actualDeliveryDate = actualDeliveryDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public List<GoodsReceiptItem> getItems() {
        return items;
    }

    public void setItems(List<GoodsReceiptItem> items) {
        this.items = items;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDateTime receiptDate) {
        this.receiptDate = receiptDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GoodsReceipt other)) return false;
        return Objects.equals(id, other.id) && Objects.equals(receiptNumber, other.receiptNumber) && Objects.equals(orderId, other.orderId) && Objects.equals(orderNumber, other.orderNumber) && Objects.equals(supplierId, other.supplierId) && Objects.equals(supplierCode, other.supplierCode) && Objects.equals(supplierName, other.supplierName) && Objects.equals(status, other.status) && Objects.equals(items, other.items) && Objects.equals(notes, other.notes) && Objects.equals(receiptDate, other.receiptDate) && Objects.equals(expectedDeliveryDate, other.expectedDeliveryDate) && Objects.equals(actualDeliveryDate, other.actualDeliveryDate) && Objects.equals(createdAt, other.createdAt) && Objects.equals(updatedAt, other.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, receiptNumber, orderId, orderNumber, supplierId, supplierCode, supplierName, status, items, notes, receiptDate, expectedDeliveryDate, actualDeliveryDate, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "GoodsReceipt(" + "id=" + id + ", receiptNumber=" + receiptNumber + ", orderId=" + orderId + ", orderNumber=" + orderNumber + ", supplierId=" + supplierId + ", supplierCode=" + supplierCode + ", supplierName=" + supplierName + ", status=" + status + ", items=" + items + ", notes=" + notes + ", receiptDate=" + receiptDate + ", expectedDeliveryDate=" + expectedDeliveryDate + ", actualDeliveryDate=" + actualDeliveryDate + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ")";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String receiptNumber;
        private Long orderId;
        private String orderNumber;
        private Long supplierId;
        private String supplierCode;
        private String supplierName;
        private String status;
        private List<GoodsReceiptItem> items = new ArrayList<>();
        private String notes;
        private LocalDateTime receiptDate;
        private LocalDateTime expectedDeliveryDate;
        private LocalDateTime actualDeliveryDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder receiptNumber(String receiptNumber) {
            this.receiptNumber = receiptNumber;
            return this;
        }

        public Builder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder orderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
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

        public Builder items(List<GoodsReceiptItem> items) {
            this.items = items;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder receiptDate(LocalDateTime receiptDate) {
            this.receiptDate = receiptDate;
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

        public GoodsReceipt build() {
            return new GoodsReceipt(id, receiptNumber, orderId, orderNumber, supplierId, supplierCode, supplierName, status, items, notes, receiptDate, expectedDeliveryDate, actualDeliveryDate, createdAt, updatedAt);
        }
    }

    
    /**
     * Agregar línea a la recepción
     */
    public void addItem(GoodsReceiptItem item) {
        if (item != null && item.isValidQuantity()) {
            this.items.add(item);
        }
    }
    
    /**
     * Validar que todas las cantidades recibidas son válidas
     */
    public boolean isValidReceipt() {
        return items != null && items.stream().allMatch(GoodsReceiptItem::isValidQuantity);
    }
    
    /**
     * Validar si fue completamente recibida (todas las cantidades coinciden)
     */
    public boolean isFullyReceived() {
        return items != null && items.stream()
                .allMatch(item -> item.getReceivedQuantity() != null && 
                        item.getReceivedQuantity().equals(item.getOrderedQuantity()));
    }
    
    /**
     * Validar si fue parcialmente recibida
     */
    public boolean isPartiallyReceived() {
        return items != null && items.stream().anyMatch(item -> 
                item.getReceivedQuantity() != null && item.getReceivedQuantity() > 0 && 
                !item.getReceivedQuantity().equals(item.getOrderedQuantity()));
    }
    
    /**
     * Marcar como recibida
     */
    public void receive() {
        if (isFullyReceived()) {
            this.status = "RECEIVED";
            this.actualDeliveryDate = LocalDateTime.now();
        } else if (isPartiallyReceived()) {
            this.status = "PARTIALLY_RECEIVED";
            this.actualDeliveryDate = LocalDateTime.now();
        } else {
            throw new IllegalStateException(
                "No se recibió ninguna cantidad; no se puede confirmar la recepción");
        }
    }
    
    /**
     * Obtener total de productos diferentes
     */
    public Integer getTotalLineItems() {
        return items != null ? items.size() : 0;
    }
    
    /**
     * Obtener cantidad total recibida
     */
    public Integer getTotalReceivedQuantity() {
        return items != null ? items.stream()
                .mapToInt(item -> item.getReceivedQuantity() != null ? item.getReceivedQuantity() : 0)
                .sum() : 0;
    }
}
