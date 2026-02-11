package com.drogueria.bellavista.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTOs para la API de Productos
 */
public class ProductDTO {
    
    /**
     * DTO para crear un producto
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 50, message = "El código no puede exceder 50 caracteres")
        private String code;
        
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
        private String name;
        
        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        private String description;
        
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        private BigDecimal price;
        
        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        private Integer stock;
        
        @NotNull(message = "El stock mínimo es obligatorio")
        @Min(value = 0, message = "El stock mínimo no puede ser negativo")
        private Integer minStock;
        
        @Size(max = 100, message = "La categoría no puede exceder 100 caracteres")
        private String category;
    }
    
    /**
     * DTO para actualizar un producto
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 50, message = "El código no puede exceder 50 caracteres")
        private String code;
        
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
        private String name;
        
        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        private String description;
        
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        private BigDecimal price;
        
        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        private Integer stock;
        
        @NotNull(message = "El stock mínimo es obligatorio")
        @Min(value = 0, message = "El stock mínimo no puede ser negativo")
        private Integer minStock;
        
        @Size(max = 100, message = "La categoría no puede exceder 100 caracteres")
        private String category;
        
        @NotNull(message = "El estado activo es obligatorio")
        private Boolean active;
    }
    
    /**
     * DTO de respuesta con la información del producto
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String code;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer stock;
        private Integer minStock;
        private String category;
        private Boolean active;
        private Boolean needsRestock;
        private Boolean available;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    
    /**
     * DTO para ajustar stock
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockAdjustment {
        
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        private Integer quantity;
    }
}
