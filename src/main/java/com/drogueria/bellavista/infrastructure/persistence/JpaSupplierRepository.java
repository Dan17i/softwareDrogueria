package com.drogueria.bellavista.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA - Proveedor
 */
@Repository
public interface JpaSupplierRepository extends JpaRepository<SupplierEntity, Long> {
    
    Optional<SupplierEntity> findByCode(String code);
    
    Optional<SupplierEntity> findByEmail(String email);
    
    @Query("SELECT s FROM SupplierEntity s WHERE s.active = true ORDER BY s.name ASC")
    List<SupplierEntity> findAllActive();
    
    boolean existsByCode(String code);
    
    boolean existsByEmail(String email);
}
