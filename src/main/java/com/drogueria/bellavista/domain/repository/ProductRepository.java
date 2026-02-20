package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (Output Port) - Repositorio de Productos
 * Define el contrato que debe implementar la infraestructura
 * No tiene dependencias de frameworks, solo del dominio
 */
public interface ProductRepository {
    
    /**
     * Guardar un producto
     */
    Product save(Product product);
    
    /**
     * Buscar producto por ID
     */
    Optional<Product> findById(Long id);
    
    /**
     * Buscar producto por código
     */
    Optional<Product> findByCode(String code);
    
    /**
     * Listar todos los productos
     */
    List<Product> findAll();
    
    /**
     * Listar productos activos
     */
    List<Product> findAllActive();
    
    /**
     * Listar productos por categoría
     */
    List<Product> findByCategory(String category);
    
    /**
     * Listar productos que necesitan reabastecimiento
     */
    List<Product> findProductsNeedingRestock();
    
    /**
     * Buscar productos por nombre (búsqueda parcial)
     */
    List<Product> findByNameContaining(String name);
    
    /**
     * Eliminar producto
     */
    void deleteById(Long id);
    
    /**
     * Verificar si existe un producto con el código dado
     */
    boolean existsByCode(String code);
}
