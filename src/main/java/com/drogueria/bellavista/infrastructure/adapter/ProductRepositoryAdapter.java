package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.domain.repository.ProductRepository;
import com.drogueria.bellavista.infrastructure.mapper.ProductMapper;
import com.drogueria.bellavista.infrastructure.persistence.JpaProductRepository;
import com.drogueria.bellavista.infrastructure.persistence.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * <h2>ProductRepositoryAdapter</h2>
 *
 * <p>
 * Implementación del puerto {@link ProductRepository}
 * utilizando Spring Data JPA como mecanismo de persistencia.
 * </p>
 *
 * <p>
 * Este adaptador pertenece a la capa de infraestructura dentro de la
 * arquitectura hexagonal (Ports & Adapters) y es responsable de:
 * </p>
 *
 * <ul>
 *     <li>Delegar operaciones CRUD al repositorio JPA.</li>
 *     <li>Convertir objetos de dominio {@link Product} a entidades {@link ProductEntity}.</li>
 *     <li>Reconstruir el modelo de dominio desde las entidades persistidas.</li>
 * </ul>
 *
 * <p>
 * La conversión entre dominio y persistencia se realiza mediante
 * {@link ProductMapper}, asegurando que el núcleo del dominio
 * permanezca desacoplado de detalles tecnológicos.
 * </p>
 *
 * <p>
 * Esta clase actúa como adaptador de salida conectando
 * el dominio con la base de datos.
 * </p>
 * @author Daniel jurado & equipo de desarrollo
 * @since 1.0
 */
@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {
    /**
     * Repositorio JPA para la entidad de producto.
     */
    private final JpaProductRepository jpaRepository;
    /**
     * Mapper encargado de convertir entre modelo de dominio
     * y entidad de persistencia.
     */
    private final ProductMapper mapper;
    /**
     * Guarda o actualiza un producto en la base de datos.
     *
     * @param product producto del dominio a persistir
     * @return producto persistido convertido nuevamente a dominio
     */
    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    /**
     * Busca un producto por su identificador único.
     *
     * @param id identificador del producto
     * @return producto encontrado si existe
     */
    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    /**
     * Busca un producto por su código interno.
     *
     * @param code código del producto
     * @return producto encontrado si existe
     */
    @Override
    public Optional<Product> findByCode(String code) {
        return jpaRepository.findByCode(code)
                .map(mapper::toDomain);
    }
    /**
     * Obtiene todos los productos registrados.
     *
     * @return lista de productos
     */
    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    /**
     * Obtiene todos los productos activos.
     *
     * @return lista de productos activos
     */
    @Override
    public List<Product> findAllActive() {
        return jpaRepository.findByActiveTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    /**
     * Obtiene productos filtrados por categoría.
     *
     * @param category categoría del producto
     * @return lista de productos pertenecientes a la categoría
     */
    @Override
    public List<Product> findByCategory(String category) {
        return jpaRepository.findByCategory(category).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    /**
     * Obtiene productos cuyo stock está por debajo del mínimo
     * y requieren reposición.
     *
     * @return lista de productos que necesitan reabastecimiento
     */
    @Override
    public List<Product> findProductsNeedingRestock() {
        return jpaRepository.findProductsNeedingRestock().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    /**
     * Busca productos cuyo nombre contenga el texto indicado
     * (ignorando mayúsculas/minúsculas).
     *
     * @param name texto a buscar en el nombre
     * @return lista de productos coincidentes
     */
    @Override
    public List<Product> findByNameContaining(String name) {
        return jpaRepository.findByNameContainingIgnoreCase(name).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    /**
     * Elimina un producto por su identificador.
     *
     * @param id identificador del producto
     */
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
    /**
     * Verifica si existe un producto con el código indicado.
     *
     * @param code código del producto
     * @return true si existe
     */
    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
}
