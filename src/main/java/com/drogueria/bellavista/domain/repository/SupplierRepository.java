package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.Supplier;

import java.util.List;
import java.util.Optional;

/**
 * Puerto/Interfaz de Repositorio - Proveedor
 */
public interface SupplierRepository {
    
    Supplier save(Supplier supplier);
    
    Optional<Supplier> findById(Long id);
    
    Optional<Supplier> findByCode(String code);
    
    Optional<Supplier> findByEmail(String email);
    
    List<Supplier> findAll();
    
    List<Supplier> findAllActive();
    
    void delete(Long id);
    
    boolean existsByCode(String code);
    
    boolean existsByEmail(String email);
}
