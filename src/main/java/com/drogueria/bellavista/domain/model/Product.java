package com.drogueria.bellavista.domain.model;

import java.util.Objects;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de dominio - Producto
 * Esta clase representa el modelo de negocio puro, sin dependencias de frameworks
 */
public class Product {
    
    private Long id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Integer minStock;
    private String category;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product() {
    }

    public Product(Long id, String code, String name, String description, BigDecimal price, Integer stock, Integer minStock, String category, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.minStock = minStock;
        this.category = category;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getMinStock() {
        return minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        if (!(o instanceof Product other)) return false;
        return Objects.equals(id, other.id) && Objects.equals(code, other.code) && Objects.equals(name, other.name) && Objects.equals(description, other.description) && Objects.equals(price, other.price) && Objects.equals(stock, other.stock) && Objects.equals(minStock, other.minStock) && Objects.equals(category, other.category) && Objects.equals(active, other.active) && Objects.equals(createdAt, other.createdAt) && Objects.equals(updatedAt, other.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, description, price, stock, minStock, category, active, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Product(" + "id=" + id + ", code=" + code + ", name=" + name + ", description=" + description + ", price=" + price + ", stock=" + stock + ", minStock=" + minStock + ", category=" + category + ", active=" + active + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ")";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String code;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer stock;
        private Integer minStock;
        private String category;
        private Boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder stock(Integer stock) {
            this.stock = stock;
            return this;
        }

        public Builder minStock(Integer minStock) {
            this.minStock = minStock;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder active(Boolean active) {
            this.active = active;
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

        public Product build() {
            return new Product(id, code, name, description, price, stock, minStock, category, active, createdAt, updatedAt);
        }
    }

    
    /**
     * Lógica de negocio: Validar si el producto necesita reabastecimiento
     */
    public boolean needsRestock() {
        return this.stock != null && this.minStock != null && this.stock <= this.minStock;
    }
    
    /**
     * Lógica de negocio: Verificar disponibilidad
     */
    public boolean isAvailable() {
        return this.active && this.stock != null && this.stock > 0;
    }
    
    /**
     * Lógica de negocio: Reducir stock
     */
    public void reduceStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (this.stock < quantity) {
            throw new IllegalStateException("Stock insuficiente");
        }
        this.stock -= quantity;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Lógica de negocio: Aumentar stock
     */
    public void increaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.stock += quantity;
        this.updatedAt = LocalDateTime.now();
    }
}
