package com.drogueria.bellavista.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA - Cliente
 * Proporciona operaciones CRUD contra la base de datos
 */
@Repository
public interface JpaCustomerRepository extends JpaRepository<CustomerEntity, Long> {
    
    Optional<CustomerEntity> findByCode(String code);
    
    Optional<CustomerEntity> findByEmail(String email);
    
    Optional<CustomerEntity> findByDocumentNumber(String documentNumber);
    
    @Query("SELECT c FROM CustomerEntity c WHERE c.active = true ORDER BY c.name ASC")
    List<CustomerEntity> findAllActive();
    
    @Query("SELECT c FROM CustomerEntity c WHERE c.customerType = :type ORDER BY c.name ASC")
    List<CustomerEntity> findByCustomerType(@Param("type") String type);
    
    @Query("SELECT c FROM CustomerEntity c WHERE c.active = true AND c.pendingBalance > 0 ORDER BY c.pendingBalance DESC")
    List<CustomerEntity> findMorosos();
    
    boolean existsByCode(String code);
    
    boolean existsByEmail(String email);
    
    boolean existsByDocumentNumber(String documentNumber);
}
