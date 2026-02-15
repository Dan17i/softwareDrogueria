package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.infrastructure.persistence.ProductEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper entre entidad de dominio y entidad JPA
 * Convierte Product <-> ProductEntity
 */
@Component
public class ProductMapper {
    
    /**
     * Convierte de entidad JPA a modelo de dominio
     */
    public Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Product.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .minStock(entity.getMinStock())
                .category(entity.getCategory())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    /**
     * Convierte de modelo de dominio a entidad JPA
     */
    public ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        
        return ProductEntity.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .minStock(product.getMinStock())
                .category(product.getCategory())
                .active(product.getActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
