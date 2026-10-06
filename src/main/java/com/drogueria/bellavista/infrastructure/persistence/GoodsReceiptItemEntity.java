package com.drogueria.bellavista.infrastructure.persistence;

import java.util.Objects;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * JPA Entity - Línea de Recepción de Mercancía
 */
@Entity
@Table(name = "goods_receipt_items")
public class GoodsReceiptItemEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "goods_receipt_id", nullable = false)
    private Long goodsReceiptId;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", nullable = false, length = 50)
    private String productCode;
    
    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;
    
    @Column(name = "ordered_quantity", nullable = false)
    private Integer orderedQuantity;
    
    @Column(name = "received_quantity")
    private Integer receivedQuantity;
    
    @Column(name = "unit_price", precision = 19, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "notes", length = 500)
    private String notes;

    public GoodsReceiptItemEntity() {
    }

    public GoodsReceiptItemEntity(Long id, Long goodsReceiptId, Long productId, String productCode, String productName, Integer orderedQuantity, Integer receivedQuantity, BigDecimal unitPrice, String notes) {
        this.id = id;
        this.goodsReceiptId = goodsReceiptId;
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.orderedQuantity = orderedQuantity;
        this.receivedQuantity = receivedQuantity;
        this.unitPrice = unitPrice;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsReceiptId() {
        return goodsReceiptId;
    }

    public void setGoodsReceiptId(Long goodsReceiptId) {
        this.goodsReceiptId = goodsReceiptId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(Integer orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public Integer getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(Integer receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GoodsReceiptItemEntity other)) return false;
        return Objects.equals(id, other.id) && Objects.equals(goodsReceiptId, other.goodsReceiptId) && Objects.equals(productId, other.productId) && Objects.equals(productCode, other.productCode) && Objects.equals(productName, other.productName) && Objects.equals(orderedQuantity, other.orderedQuantity) && Objects.equals(receivedQuantity, other.receivedQuantity) && Objects.equals(unitPrice, other.unitPrice) && Objects.equals(notes, other.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, goodsReceiptId, productId, productCode, productName, orderedQuantity, receivedQuantity, unitPrice, notes);
    }

    @Override
    public String toString() {
        return "GoodsReceiptItemEntity(" + "id=" + id + ", goodsReceiptId=" + goodsReceiptId + ", productId=" + productId + ", productCode=" + productCode + ", productName=" + productName + ", orderedQuantity=" + orderedQuantity + ", receivedQuantity=" + receivedQuantity + ", unitPrice=" + unitPrice + ", notes=" + notes + ")";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long goodsReceiptId;
        private Long productId;
        private String productCode;
        private String productName;
        private Integer orderedQuantity;
        private Integer receivedQuantity;
        private BigDecimal unitPrice;
        private String notes;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder goodsReceiptId(Long goodsReceiptId) {
            this.goodsReceiptId = goodsReceiptId;
            return this;
        }

        public Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder productCode(String productCode) {
            this.productCode = productCode;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder orderedQuantity(Integer orderedQuantity) {
            this.orderedQuantity = orderedQuantity;
            return this;
        }

        public Builder receivedQuantity(Integer receivedQuantity) {
            this.receivedQuantity = receivedQuantity;
            return this;
        }

        public Builder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public GoodsReceiptItemEntity build() {
            return new GoodsReceiptItemEntity(id, goodsReceiptId, productId, productCode, productName, orderedQuantity, receivedQuantity, unitPrice, notes);
        }
    }

}
