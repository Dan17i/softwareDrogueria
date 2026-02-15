package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de dominio - Producto
 * Esta clase representa el modelo de negocio puro, sin dependencias de frameworks
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
