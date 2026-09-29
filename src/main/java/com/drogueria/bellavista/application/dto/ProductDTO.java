package com.drogueria.bellavista.application.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTOs para la API de Productos
 */
public class ProductDTO {

    /**
     * DTO para crear un producto
     */
    public record CreateRequest(

            @NotBlank(message = "El código es obligatorio")
            @Size(max = 50, message = "El código no puede exceder 50 caracteres")
            String code,

            @NotBlank(message = "El nombre es obligatorio")
            @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
            String name,

            @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
            String description,

            @NotNull(message = "El precio es obligatorio")
            @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
            BigDecimal price,

            @NotNull(message = "El stock es obligatorio")
            @Min(value = 0, message = "El stock no puede ser negativo")
            Integer stock,

            @NotNull(message = "El stock mínimo es obligatorio")
            @Min(value = 0, message = "El stock mínimo no puede ser negativo")
            Integer minStock,

            @Size(max = 100, message = "La categoría no puede exceder 100 caracteres")
            String category) {

        public String getCode() { return code; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public BigDecimal getPrice() { return price; }
        public Integer getStock() { return stock; }
        public Integer getMinStock() { return minStock; }
        public String getCategory() { return category; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String code;
            private String name;
            private String description;
            private BigDecimal price;
            private Integer stock;
            private Integer minStock;
            private String category;

            public Builder code(String code) { this.code = code; return this; }
            public Builder name(String name) { this.name = name; return this; }
            public Builder description(String description) { this.description = description; return this; }
            public Builder price(BigDecimal price) { this.price = price; return this; }
            public Builder stock(Integer stock) { this.stock = stock; return this; }
            public Builder minStock(Integer minStock) { this.minStock = minStock; return this; }
            public Builder category(String category) { this.category = category; return this; }

            public CreateRequest build() {
                return new CreateRequest(code, name, description, price, stock, minStock, category);
            }
        }
    }

    /**
     * DTO para actualizar un producto
     */
    public record UpdateRequest(

            @NotBlank(message = "El código es obligatorio")
            @Size(max = 50, message = "El código no puede exceder 50 caracteres")
            String code,

            @NotBlank(message = "El nombre es obligatorio")
            @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
            String name,

            @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
            String description,

            @NotNull(message = "El precio es obligatorio")
            @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
            BigDecimal price,

            @NotNull(message = "El stock es obligatorio")
            @Min(value = 0, message = "El stock no puede ser negativo")
            Integer stock,

            @NotNull(message = "El stock mínimo es obligatorio")
            @Min(value = 0, message = "El stock mínimo no puede ser negativo")
            Integer minStock,

            @Size(max = 100, message = "La categoría no puede exceder 100 caracteres")
            String category,

            @NotNull(message = "El estado activo es obligatorio")
            Boolean active) {

        public String getCode() { return code; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public BigDecimal getPrice() { return price; }
        public Integer getStock() { return stock; }
        public Integer getMinStock() { return minStock; }
        public String getCategory() { return category; }
        public Boolean getActive() { return active; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String code;
            private String name;
            private String description;
            private BigDecimal price;
            private Integer stock;
            private Integer minStock;
            private String category;
            private Boolean active;

            public Builder code(String code) { this.code = code; return this; }
            public Builder name(String name) { this.name = name; return this; }
            public Builder description(String description) { this.description = description; return this; }
            public Builder price(BigDecimal price) { this.price = price; return this; }
            public Builder stock(Integer stock) { this.stock = stock; return this; }
            public Builder minStock(Integer minStock) { this.minStock = minStock; return this; }
            public Builder category(String category) { this.category = category; return this; }
            public Builder active(Boolean active) { this.active = active; return this; }

            public UpdateRequest build() {
                return new UpdateRequest(code, name, description, price, stock, minStock, category, active);
            }
        }
    }

    /**
     * DTO de respuesta con la información del producto
     */
    public record Response(
            Long id,
            String code,
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            Integer minStock,
            String category,
            Boolean active,
            Boolean needsRestock,
            Boolean available,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        public Long getId() { return id; }
        public String getCode() { return code; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public BigDecimal getPrice() { return price; }
        public Integer getStock() { return stock; }
        public Integer getMinStock() { return minStock; }
        public String getCategory() { return category; }
        public Boolean getActive() { return active; }
        public Boolean getNeedsRestock() { return needsRestock; }
        public Boolean getAvailable() { return available; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }

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
            private Boolean needsRestock;
            private Boolean available;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder code(String code) { this.code = code; return this; }
            public Builder name(String name) { this.name = name; return this; }
            public Builder description(String description) { this.description = description; return this; }
            public Builder price(BigDecimal price) { this.price = price; return this; }
            public Builder stock(Integer stock) { this.stock = stock; return this; }
            public Builder minStock(Integer minStock) { this.minStock = minStock; return this; }
            public Builder category(String category) { this.category = category; return this; }
            public Builder active(Boolean active) { this.active = active; return this; }
            public Builder needsRestock(Boolean needsRestock) { this.needsRestock = needsRestock; return this; }
            public Builder available(Boolean available) { this.available = available; return this; }
            public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

            public Response build() {
                return new Response(id, code, name, description, price, stock, minStock, category,
                        active, needsRestock, available, createdAt, updatedAt);
            }
        }
    }

    /**
     * DTO para ajustar stock
     */
    public record StockAdjustment(

            @NotNull(message = "La cantidad es obligatoria")
            @Min(value = 1, message = "La cantidad debe ser al menos 1")
            Integer quantity) {

        public Integer getQuantity() { return quantity; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Integer quantity;

            public Builder quantity(Integer quantity) { this.quantity = quantity; return this; }

            public StockAdjustment build() {
                return new StockAdjustment(quantity);
            }
        }
    }
}
