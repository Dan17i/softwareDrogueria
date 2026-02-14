package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * <h2>Entidad de Dominio - Product</h2>
 *
 * <p>
 * Representa un producto dentro del sistema de la droguería.
 * Esta entidad forma parte del núcleo del dominio y encapsula
 * las reglas de negocio relacionadas con la gestión de inventario.
 * </p>
 *
 * <p>
 * No contiene dependencias de frameworks ni lógica de persistencia,
 * cumpliendo con los principios de Clean Architecture y DDD.
 * </p>
 *
 * <p>
 * Incluye comportamientos asociados a:
 * </p>
 * <ul>
 *     <li>Control de stock</li>
 *     <li>Validación de disponibilidad</li>
 *     <li>Detección de necesidad de reabastecimiento</li>
 * </ul>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    /**
     * Identificador único del producto.
     */
    private Long id;
    /**
     * Código interno del producto.
     */
    private String code;
    /**
     * Nombre comercial del producto.
     */
    private String name;
    /**
     * Descripción detallada del producto.
     */
    private String description;
    /**
     * Precio unitario de venta.
     */
    private BigDecimal price;
    /**
     * Cantidad disponible en inventario.
     */
    private Integer stock;
    /**
     * Nivel mínimo permitido de inventario.
     * Cuando el stock es menor o igual a este valor,
     * el producto requiere reabastecimiento.
     */
    private Integer minStock;
    /**
     * Categoría a la que pertenece el producto.
     */
    private String category;
    /**
     * Indica si el producto está activo y disponible
     * para operaciones comerciales.
     */
    private Boolean active;
    /**
     * Fecha y hora de creación del registro.
     */
    private LocalDateTime createdAt;
    /**
     * Fecha y hora de la última actualización del registro.
     */
    private LocalDateTime updatedAt;
    /**
     * Determina si el producto necesita reabastecimiento.
     *
     * <p>
     * Se considera que necesita reabastecimiento cuando
     * el stock actual es menor o igual al stock mínimo definido.
     * </p>
     *
     * @return {@code true} si debe reabastecerse,
     *         {@code false} en caso contrario.
     */
    public boolean needsRestock() {
        return this.stock != null && this.minStock != null && this.stock <= this.minStock;
    }
    /**
     * Verifica si el producto está disponible para venta.
     *
     * <p>
     * Un producto está disponible cuando:
     * </p>
     * <ul>
     *     <li>Está activo.</li>
     *     <li>Tiene stock mayor a cero.</li>
     * </ul>
     *
     * @return {@code true} si está disponible,
     *         {@code false} en caso contrario.
     */
    public boolean isAvailable() {
        return this.active && this.stock != null && this.stock > 0;
    }
    /**
     * Reduce el stock del producto.
     *
     * <p>
     * Se utiliza generalmente al registrar una venta.
     * </p>
     *
     * @param quantity cantidad a descontar del inventario
     * @throws IllegalArgumentException si la cantidad es nula o menor o igual a cero
     * @throws IllegalStateException si el stock disponible es insuficiente
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
     * Incrementa el stock del producto.
     *
     * <p>
     * Se utiliza cuando se recibe mercancía o se realiza
     * un ajuste positivo de inventario.
     * </p>
     *
     * @param quantity cantidad a agregar al inventario
     * @throws IllegalArgumentException si la cantidad es nula o menor o igual a cero
     */
    public void increaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.stock += quantity;
        this.updatedAt = LocalDateTime.now();
    }
}
