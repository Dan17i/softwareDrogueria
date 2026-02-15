package com.drogueria.bellavista.application.mapper;

import com.drogueria.bellavista.application.dto.ProductDTO;
import com.drogueria.bellavista.domain.model.Product;
import org.springframework.stereotype.Component;

/**
 * Mapper de casos de uso - Convierte entre DTOs y modelos de dominio
 */
@Component
public class ProductUseCaseMapper {
    
    /**
     * Convierte CreateRequest a modelo de dominio
     */
    public Product toDomain(ProductDTO.CreateRequest request) {
        if (request == null) {
            return null;
        }
        
        return Product.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .minStock(request.getMinStock())
                .category(request.getCategory())
                .build();
    }
    
    /**
     * Convierte UpdateRequest a modelo de dominio
     */
    public Product toDomain(ProductDTO.UpdateRequest request) {
        if (request == null) {
            return null;
        }
        
        return Product.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .minStock(request.getMinStock())
                .category(request.getCategory())
                .active(request.getActive())
                .build();
    }
    
    /**
     * Convierte modelo de dominio a Response DTO
     */
    public ProductDTO.Response toResponse(Product product) {
        if (product == null) {
            return null;
        }
        
        return ProductDTO.Response.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .minStock(product.getMinStock())
                .category(product.getCategory())
                .active(product.getActive())
                .needsRestock(product.needsRestock())
                .available(product.isAvailable())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
