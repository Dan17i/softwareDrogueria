package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.infrastructure.persistence.ProductEntity;
import org.springframework.stereotype.Component;
/**
 * <h2>ProductMapper</h2>
 *
 * <p>
 * Componente encargado de convertir datos entre:
 * </p>
 *
 * <ul>
 *     <li>{@link ProductEntity} (capa de persistencia - JPA)</li>
 *     <li>{@link Product} (modelo de dominio)</li>
 * </ul>
 *
 * <p>
 * Este mapper forma parte de la capa de infraestructura dentro de la
 * arquitectura hexagonal (Ports & Adapters), permitiendo que el modelo
 * de dominio permanezca desacoplado de los detalles técnicos de la
 * base de datos.
 * </p>
 *
 * <p>
 * Su responsabilidad es exclusivamente la transformación de datos,
 * sin contener lógica de negocio, respetando el principio de
 * responsabilidad única (SRP).
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Component
public class ProductMapper {
    /**
     * Convierte una entidad JPA {@link ProductEntity}
     * en un modelo de dominio {@link Product}.
     *
     * <p>
     * Si la entidad recibida es {@code null}, se retorna {@code null}
     * para evitar {@link NullPointerException}.
     * </p>
     *
     * @param entity entidad obtenida desde la base de datos
     * @return modelo de dominio equivalente o {@code null} si la entidad es nula
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
     * Convierte un modelo de dominio {@link Product}
     * en su entidad JPA {@link ProductEntity}.
     *
     * <p>
     * Este método prepara el objeto para ser persistido
     * en la base de datos mediante Spring Data JPA.
     * </p>
     *
     * <p>
     * Si el objeto de dominio es {@code null}, retorna {@code null}.
     * </p>
     *
     * @param product modelo de dominio a convertir
     * @return entidad JPA lista para persistencia o {@code null} si el dominio es nulo
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
