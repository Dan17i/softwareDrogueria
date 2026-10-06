package com.drogueria.bellavista.domain.model;

import java.util.Objects;

import java.math.BigDecimal;

/**
 * Entidad de dominio - Línea de Recepción de Mercancía
 */
public class GoodsReceiptItem {
    
    private Long id;
    private Long productId;
    private String productCode;
    private String productName;
    private Integer orderedQuantity; // Cantidad ordenada
    private Integer receivedQuantity; // Cantidad recibida
    private BigDecimal unitPrice;
    private String notes;

    public GoodsReceiptItem() {
    }

    public GoodsReceiptItem(Long id, Long productId, String productCode, String productName, Integer orderedQuantity, Integer receivedQuantity, BigDecimal unitPrice, String notes) {
        this.id = id;
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
        if (!(o instanceof GoodsReceiptItem other)) return false;
        return Objects.equals(id, other.id) && Objects.equals(productId, other.productId) && Objects.equals(productCode, other.productCode) && Objects.equals(productName, other.productName) && Objects.equals(orderedQuantity, other.orderedQuantity) && Objects.equals(receivedQuantity, other.receivedQuantity) && Objects.equals(unitPrice, other.unitPrice) && Objects.equals(notes, other.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, productCode, productName, orderedQuantity, receivedQuantity, unitPrice, notes);
    }

    @Override
    public String toString() {
        return "GoodsReceiptItem(" + "id=" + id + ", productId=" + productId + ", productCode=" + productCode + ", productName=" + productName + ", orderedQuantity=" + orderedQuantity + ", receivedQuantity=" + receivedQuantity + ", unitPrice=" + unitPrice + ", notes=" + notes + ")";
    }

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
        private BigDecimal unitPrice;
        private String notes;

        public Builder id(Long id) {
            this.id = id;
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

        public GoodsReceiptItem build() {
            return new GoodsReceiptItem(id, productId, productCode, productName, orderedQuantity, receivedQuantity, unitPrice, notes);
        }
    }

    
    /**
     * Validar que la cantidad recibida no supera la ordenada
     */
    public boolean isValidQuantity() {
        return receivedQuantity != null && receivedQuantity <= orderedQuantity;
    }
    
    /**
     * Calcular diferencia entre ordenado y recibido
     */
    public Integer getDifference() {
        if (receivedQuantity == null) return 0;
        return orderedQuantity - receivedQuantity;
    }
}
