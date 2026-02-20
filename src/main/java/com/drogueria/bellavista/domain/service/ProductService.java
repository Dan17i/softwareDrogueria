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
 * Servicio de dominio - Casos de uso de Productos
 * Contiene toda la lógica de negocio de productos
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    
    private final ProductRepository productRepository;
    
    /**
     * Crear un nuevo producto
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
     * Actualizar un producto existente
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
     * Obtener producto por ID
     */
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }
    
    /**
     * Obtener producto por código
     */
    @Transactional(readOnly = true)
    public Product getProductByCode(String code) {
        return productRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "code", code));
    }
    
    /**
     * Listar todos los productos
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    /**
     * Listar productos activos
     */
    @Transactional(readOnly = true)
    public List<Product> getActiveProducts() {
        return productRepository.findAllActive();
    }
    
    /**
     * Listar productos por categoría
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    /**
     * Buscar productos por nombre
     */
    @Transactional(readOnly = true)
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }
    
    /**
     * Obtener productos que necesitan reabastecimiento
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsNeedingRestock() {
        return productRepository.findProductsNeedingRestock();
    }
    
    /**
     * Reducir stock de un producto
     */
    public Product reduceStock(Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a 0");
        }
        Product product = getProductById(productId);
        try {
            product.reduceStock(quantity);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new BusinessException(e.getMessage());
        }
        return productRepository.save(product);
    }
    
    /**
     * Aumentar stock de un producto
     */
    public Product increaseStock(Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a 0");
        }
        Product product = getProductById(productId);
        try {
            product.increaseStock(quantity);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(e.getMessage());
        }
        return productRepository.save(product);
    }
    
    /**
     * Activar/Desactivar producto
     */
    public Product toggleProductStatus(Long productId) {
        Product product = getProductById(productId);
        product.setActive(!product.getActive());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }
    
    /**
     * Eliminar producto
     */
    public void deleteProduct(Long id) {
        if (!productRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
    }
    
    /**
     * Reducir stock (uso interno desde OrderService)
     * NO inicia transacción porque ya está en una
     */
    protected void reduceStockInternal(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        try {
            product.reduceStock(quantity);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new BusinessException(e.getMessage());
        }
        productRepository.save(product);
    }
    
    /**
     * Aumentar stock (uso interno desde OrderService)
     * NO inicia transacción porque ya está en una
     */
    protected void increaseStockInternal(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        try {
            product.increaseStock(quantity);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(e.getMessage());
        }
        productRepository.save(product);
    }
}
