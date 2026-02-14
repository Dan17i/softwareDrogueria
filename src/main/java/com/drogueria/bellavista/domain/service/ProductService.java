package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.domain.repository.ProductRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Servicio de dominio encargado de la gestión de productos.
 *
 * <p>Centraliza la lógica de negocio relacionada con:</p>
 * <ul>
 *     <li>Creación y actualización de productos</li>
 *     <li>Consultas por distintos criterios</li>
 *     <li>Control de stock</li>
 *     <li>Activación, desactivación y eliminación</li>
 * </ul>
 *
 * <p>Las operaciones que modifican estado se ejecutan dentro de una
 * transacción para garantizar consistencia en inventario.</p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    
    private final ProductRepository productRepository;
    /**
     * Crea un nuevo producto en el sistema.
     *
     * <p>Reglas de negocio:</p>
     * <ul>
     *     <li>El código del producto debe ser único</li>
     *     <li>Se establecen valores iniciales por defecto</li>
     * </ul>
     *
     * @param product producto a registrar
     * @return producto guardado
     * @throws BusinessException si el código ya existe
     */
    public Product createProduct(Product product) {
        // Validar que no exista un producto con el mismo código
        if (productRepository.existsByCode(product.getCode())) {
            throw new BusinessException("Ya existe un producto con el código: " + product.getCode());
        }
        
        // Establecer valores por defecto
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        return productRepository.save(product);
    }
    /**
     * Actualiza un producto existente.
     *
     * <p>Reglas de negocio:</p>
     * <ul>
     *     <li>El producto debe existir</li>
     *     <li>El código debe seguir siendo único</li>
     * </ul>
     *
     * @param id identificador del producto
     * @param productData datos nuevos del producto
     * @return producto actualizado
     * @throws ResourceNotFoundException si no existe
     * @throws BusinessException si el código ya está en uso
     */
    public Product updateProduct(Long id, Product productData) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        
        // Validar código único si cambió
        if (!existingProduct.getCode().equals(productData.getCode()) 
            && productRepository.existsByCode(productData.getCode())) {
            throw new BusinessException("Ya existe un producto con el código: " + productData.getCode());
        }
        
        // Actualizar campos
        existingProduct.setCode(productData.getCode());
        existingProduct.setName(productData.getName());
        existingProduct.setDescription(productData.getDescription());
        existingProduct.setPrice(productData.getPrice());
        existingProduct.setStock(productData.getStock());
        existingProduct.setMinStock(productData.getMinStock());
        existingProduct.setCategory(productData.getCategory());
        existingProduct.setActive(productData.getActive());
        existingProduct.setUpdatedAt(LocalDateTime.now());
        
        return productRepository.save(existingProduct);
    }
    /**
     * Obtiene un producto por su ID.
     *
     * @param id identificador del producto
     * @return producto encontrado
     * @throws ResourceNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }
    /**
     * Obtiene un producto por su código.
     *
     * @param code código del producto
     * @return producto encontrado
     * @throws ResourceNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public Product getProductByCode(String code) {
        return productRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "code", code));
    }
    /**
     * Lista todos los productos registrados.
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    /**
     * Lista únicamente los productos activos.
     */
    @Transactional(readOnly = true)
    public List<Product> getActiveProducts() {
        return productRepository.findAllActive();
    }
    /**
     * Obtiene productos filtrados por categoría.
     *
     * @param category categoría del producto
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    /**
     * Busca productos cuyo nombre contenga el texto indicado.
     *
     * @param name texto de búsqueda
     */
    @Transactional(readOnly = true)
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }
    /**
     * Obtiene los productos que necesitan reabastecimiento,
     * normalmente aquellos con stock menor o igual al mínimo.
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsNeedingRestock() {
        return productRepository.findProductsNeedingRestock();
    }
    /**
     * Reduce el stock de un producto.
     *
     * @param productId ID del producto
     * @param quantity cantidad a descontar
     * @return producto actualizado
     */
    public Product reduceStock(Long productId, Integer quantity) {
        Product product = getProductById(productId);
        product.reduceStock(quantity);
        return productRepository.save(product);
    }
    /**
     * Aumenta el stock de un producto.
     *
     * @param productId ID del producto
     * @param quantity cantidad a agregar
     * @return producto actualizado
     */

    public Product increaseStock(Long productId, Integer quantity) {
        Product product = getProductById(productId);
        product.increaseStock(quantity);
        return productRepository.save(product);
    }
    /**
     * Cambia el estado activo/inactivo del producto.
     *
     * @param productId ID del producto
     * @return producto actualizado
     */
    public Product toggleProductStatus(Long productId) {
        Product product = getProductById(productId);
        product.setActive(!product.getActive());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }
    /**
     * Elimina un producto del sistema.
     *
     * @param id ID del producto
     * @throws ResourceNotFoundException si no existe
     */
    public void deleteProduct(Long id) {
        if (!productRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
    }
    /**
     * Reduce el stock de forma interna.
     *
     * <p>Este método está pensado para ser usado por otros servicios
     * dentro de la misma transacción (por ejemplo, órdenes o recepciones).</p>
     *
     * @param productId ID del producto
     * @param quantity cantidad a descontar
     */
    protected void reduceStockInternal(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        product.reduceStock(quantity);
        productRepository.save(product);
    }
    /**
     * Aumenta el stock de forma interna.
     *
     * <p>Utilizado por otros servicios de dominio dentro de una transacción
     * existente para mantener consistencia del inventario.</p>
     *
     * @param productId ID del producto
     * @param quantity cantidad a agregar
     */
    protected void increaseStockInternal(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        product.increaseStock(quantity);
        productRepository.save(product);
    }
}
