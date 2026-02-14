package com.drogueria.bellavista.application.mapper;

import com.drogueria.bellavista.application.dto.ProductDTO;
import com.drogueria.bellavista.domain.model.Product;
import org.springframework.stereotype.Component;
/**
 * Mapper de casos de uso para productos.
 * <p>
 * Responsable de transformar los DTOs utilizados por la capa de aplicación
 * en entidades del dominio {@link Product} y viceversa.
 * </p>
 *
 * <p>
 * Este mapper no contiene lógica de negocio; únicamente realiza
 * conversiones de datos entre la API y el modelo de dominio.
 * Forma parte de la capa de aplicación en la arquitectura hexagonal.
 * </p>
 */
@Component
public class ProductUseCaseMapper {
    /**
     * Convierte un DTO de creación de producto en una entidad de dominio.
     *
     * @param request DTO recibido desde la API para crear un producto
     * @return instancia de {@link Product} lista para ser procesada por el dominio,
     *         o {@code null} si el request es nulo
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
     * Convierte un DTO de actualización en una entidad de dominio.
     * <p>
     * Incluye el estado activo del producto, ya que en operaciones
     * de actualización el estado puede modificarse.
     * </p>
     *
     * @param request DTO recibido desde la API para actualizar un producto
     * @return instancia de {@link Product} con los datos actualizados,
     *         o {@code null} si el request es nulo
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
     * Convierte una entidad del dominio {@link Product}
     * en su DTO de respuesta para la API.
     * <p>
     * Además de los campos persistidos, incluye propiedades derivadas
     * del dominio como:
     * <ul>
     *   <li>{@code needsRestock} – indica si el stock está por debajo del mínimo</li>
     *   <li>{@code available} – indica si el producto está disponible para venta</li>
     * </ul>
     *
     * @param product entidad proveniente del dominio
     * @return DTO {@link ProductDTO.Response} listo para exposición,
     *         o {@code null} si el producto es nulo
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
