package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.Product;

import java.util.List;
import java.util.Optional;
/**
 * <h2>Puerto de Salida (Output Port) - ProductRepository</h2>
 *
 * <p>
 * Define el contrato que debe implementar cualquier mecanismo de persistencia
 * relacionado con la entidad {@link Product}.
 * </p>
 *
 * <p>
 * Esta interfaz pertenece a la capa de dominio y no depende de ningún
 * framework o tecnología específica. Actúa como un <b>puerto de salida</b>
 * dentro de la arquitectura hexagonal (Ports & Adapters), permitiendo
 * desacoplar la lógica de negocio de la infraestructura.
 * </p>
 *
 * <p>
 * Las implementaciones concretas deben ubicarse en la capa de infraestructura.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
public interface ProductRepository {
    /**
     * Guarda o actualiza un producto en el sistema.
     *
     * @param product entidad a persistir
     * @return producto persistido con posibles datos actualizados
     */
    Product save(Product product);
    /**
     * Busca un producto por su identificador único.
     *
     * @param id identificador del producto
     * @return {@link Optional} con el producto si existe,
     *         o vacío si no se encuentra
     */
    Optional<Product> findById(Long id);
    /**
     * Busca un producto por su código único.
     *
     * @param code código del producto
     * @return {@link Optional} con el producto si existe,
     *         o vacío si no se encuentra
     */
    Optional<Product> findByCode(String code);
    /**
     * Obtiene todos los productos registrados en el sistema.
     *
     * @return lista completa de productos
     */
    List<Product> findAll();
    /**
     * Obtiene únicamente los productos activos.
     *
     * @return lista de productos con estado activo
     */
    List<Product> findAllActive();
    /**
     * Obtiene productos filtrados por categoría.
     *
     * @param category categoría del producto
     * @return lista de productos que pertenecen a la categoría indicada
     */
    List<Product> findByCategory(String category);
    /**
     * Obtiene productos que requieren reabastecimiento,
     * generalmente cuando el stock es menor o igual al stock mínimo definido.
     *
     * @return lista de productos que necesitan reposición
     */
    List<Product> findProductsNeedingRestock();
    /**
     * Busca productos cuyo nombre contenga el texto indicado
     * (búsqueda parcial).
     *
     * @param name texto a buscar dentro del nombre del producto
     * @return lista de productos que coinciden con el criterio
     */
    List<Product> findByNameContaining(String name);
    /**
     * Elimina un producto por su identificador.
     *
     * @param id identificador del producto
     */
    void deleteById(Long id);
    /**
     * Verifica si ya existe un producto con el código indicado.
     *
     * @param code código del producto
     * @return {@code true} si existe,
     *         {@code false} en caso contrario
     */
    boolean existsByCode(String code);
}
