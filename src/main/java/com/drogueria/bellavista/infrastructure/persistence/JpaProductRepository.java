package com.drogueria.bellavista.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA - Spring Data JPA
 * Interactúa con la base de datos
 */
@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
    
    Optional<ProductEntity> findByCode(String code);
    
    List<ProductEntity> findByActiveTrue();
    
    List<ProductEntity> findByCategory(String category);
    
    @Query("SELECT p FROM ProductEntity p WHERE p.stock <= p.minStock")
    List<ProductEntity> findProductsNeedingRestock();
    
    List<ProductEntity> findByNameContainingIgnoreCase(String name);
    
    boolean existsByCode(String code);
}
