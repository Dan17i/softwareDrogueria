package com.drogueria.bellavista.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * DTOs utilizados para la gestión de Productos dentro del sistema.
 *
 * Estas clases definen los contratos de comunicación entre:
 * Controller ↔ Application ↔ Cliente API
 *
 * Responsabilidades:
 * - Validar datos de entrada en operaciones CRUD
 * - Transportar información del producto sin exponer el dominio
 * - Estandarizar respuestas hacia clientes externos
 *
 * No contienen lógica de negocio.
 */
public class ProductDTO {
    /**
     * DTO utilizado para registrar un nuevo producto en el sistema.
     *
     * Contiene la información mínima necesaria para su creación
     * y reglas de validación que garantizan integridad básica.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        /**
         * Código único del producto.
         * Usado para identificación operativa y búsquedas.
         */
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 50, message = "El código no puede exceder 50 caracteres")
        private String code;
        /**
         * Nombre comercial del producto.
         */
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
        private String name;
        /**
         * Descripción del producto.
         * Campo opcional.
         */
        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        private String description;
        /**
         * Precio unitario del producto.
         * Debe ser mayor a cero.
         */
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        private BigDecimal price;
        /**
         * Stock inicial disponible.
         * No puede ser negativo.
         */
        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        private Integer stock;
        /**
         * Stock mínimo permitido antes de requerir reposición.
         */
        @NotNull(message = "El stock mínimo es obligatorio")
        @Min(value = 0, message = "El stock mínimo no puede ser negativo")
        private Integer minStock;
        /**
         * Categoría del producto.
         * Campo opcional utilizado para clasificación.
         */
        @Size(max = 100, message = "La categoría no puede exceder 100 caracteres")
        private String category;
    }
    /**
     * DTO utilizado para actualizar un producto existente.
     *
     * Incluye el estado activo del producto,
     * permitiendo habilitar o deshabilitar su uso operativo.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        /** Código del producto */
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 50, message = "El código no puede exceder 50 caracteres")
        private String code;
        /** Nombre del producto */
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
        private String name;
        /** Descripción del producto */
        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        private String description;
        /** Precio unitario */
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        private BigDecimal price;
        /** Stock disponible */
        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        private Integer stock;
        /** Stock mínimo permitido */
        @NotNull(message = "El stock mínimo es obligatorio")
        @Min(value = 0, message = "El stock mínimo no puede ser negativo")
        private Integer minStock;
        /** Categoría del producto */
        @Size(max = 100, message = "La categoría no puede exceder 100 caracteres")
        private String category;
        /**
         * Indica si el producto está activo en el sistema.
         * Si es false, no debería poder venderse ni incluirse en órdenes.
         */
        @NotNull(message = "El estado activo es obligatorio")
        private Boolean active;
    }
    /**
     * DTO de salida que representa un producto hacia el cliente API.
     *
     * Incluye:
     * - Datos persistidos
     * - Indicadores calculados por el dominio
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        /** Identificador único del producto */
        private Long id;
        /** Código del producto */
        private String code;
        /** Nombre del producto */
        private String name;
        /** Descripción del producto */
        private String description;
        /** Precio unitario */
        private BigDecimal price;
        /** Stock actual disponible */
        private Integer stock;
        /** Stock mínimo configurado */
        private Integer minStock;
        /** Categoría del producto */
        private String category;
        /** Indica si el producto está activo */
        private Boolean active;
        /**
         * Indica si el producto necesita reposición.
         * Calculado en la capa de dominio.
         */
        private Boolean needsRestock;
        /**
         * Indica si el producto puede venderse actualmente.
         * Depende de:
         * - stock > 0
         * - estado activo
         */
        private Boolean available;
        /** Fecha de creación del registro */
        private LocalDateTime createdAt;
        /** Fecha de última actualización */
        private LocalDateTime updatedAt;
    }
    /**
     * DTO utilizado para operaciones de ajuste de stock.
     *
     * Se emplea en endpoints especializados como:
     * - incrementar stock
     * - registrar reposición
     * - ajustes manuales controlados
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockAdjustment {
        /**
         * Cantidad a ajustar.
         * Debe ser al menos 1.
         */
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        private Integer quantity;
    }
}
