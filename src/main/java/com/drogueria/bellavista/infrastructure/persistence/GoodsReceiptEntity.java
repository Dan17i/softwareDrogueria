package com.drogueria.bellavista.infrastructure.persistence;

import java.util.Objects;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * JPA Entity - Recepción de Mercancía
 */
@Entity
@Table(name = "goods_receipts",
        indexes = {
                @Index(name = "idx_receipt_number", columnList = "receipt_number", unique = true),
                @Index(name = "idx_order_id", columnList = "order_id"),
                @Index(name = "idx_supplier_id", columnList = "supplier_id"),
                @Index(name = "idx_status", columnList = "status")
        })
public class GoodsReceiptEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "receipt_number", nullable = false, unique = true, length = 100)
    private String receiptNumber;
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "order_number", length = 100)
    private String orderNumber;
    
    @Column(name = "supplier_id")
    private Long supplierId;
    
    @Column(name = "supplier_code", length = 50)
    private String supplierCode;
    
    @Column(name = "supplier_name", length = 255)
    private String supplierName;
    
    @Column(name = "status", nullable = false, length = 50)
    private String status;
    
    @Column(name = "notes", length = 1000)
    private String notes;
    
    @Column(name = "receipt_date")
    private LocalDateTime receiptDate;
    
    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;
    
    @Column(name = "actual_delivery_date")
    private LocalDateTime actualDeliveryDate;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public GoodsReceiptEntity() {
    }

    public GoodsReceiptEntity(Long id, String receiptNumber, Long orderId, String orderNumber, Long supplierId, String supplierCode, String supplierName, String status, String notes, LocalDateTime receiptDate, LocalDateTime expectedDeliveryDate, LocalDateTime actualDeliveryDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.receiptNumber = receiptNumber;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.supplierId = supplierId;
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.status = status;
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
        if (!(o instanceof GoodsReceiptEntity other)) return false;
        return Objects.equals(id, other.id) && Objects.equals(receiptNumber, other.receiptNumber) && Objects.equals(orderId, other.orderId) && Objects.equals(orderNumber, other.orderNumber) && Objects.equals(supplierId, other.supplierId) && Objects.equals(supplierCode, other.supplierCode) && Objects.equals(supplierName, other.supplierName) && Objects.equals(status, other.status) && Objects.equals(notes, other.notes) && Objects.equals(receiptDate, other.receiptDate) && Objects.equals(expectedDeliveryDate, other.expectedDeliveryDate) && Objects.equals(actualDeliveryDate, other.actualDeliveryDate) && Objects.equals(createdAt, other.createdAt) && Objects.equals(updatedAt, other.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, receiptNumber, orderId, orderNumber, supplierId, supplierCode, supplierName, status, notes, receiptDate, expectedDeliveryDate, actualDeliveryDate, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "GoodsReceiptEntity(" + "id=" + id + ", receiptNumber=" + receiptNumber + ", orderId=" + orderId + ", orderNumber=" + orderNumber + ", supplierId=" + supplierId + ", supplierCode=" + supplierCode + ", supplierName=" + supplierName + ", status=" + status + ", notes=" + notes + ", receiptDate=" + receiptDate + ", expectedDeliveryDate=" + expectedDeliveryDate + ", actualDeliveryDate=" + actualDeliveryDate + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ")";
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

        public GoodsReceiptEntity build() {
            return new GoodsReceiptEntity(id, receiptNumber, orderId, orderNumber, supplierId, supplierCode, supplierName, status, notes, receiptDate, expectedDeliveryDate, actualDeliveryDate, createdAt, updatedAt);
        }
    }

}
