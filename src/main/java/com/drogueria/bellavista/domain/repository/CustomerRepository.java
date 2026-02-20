package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.Customer;

import java.util.List;
import java.util.Optional;

/**
 * Puerto/Interfaz de Repositorio - Cliente
 * Define el contrato que debe cumplir cualquier implementación de persistencia
 */
public interface CustomerRepository {
    
    Customer save(Customer customer);
    
    Optional<Customer> findById(Long id);
    
    Optional<Customer> findByCode(String code);
    
    Optional<Customer> findByEmail(String email);
    
    Optional<Customer> findByDocumentNumber(String documentNumber);
    
    List<Customer> findAll();
    
    List<Customer> findAllActive();
    
    List<Customer> findByCustomerType(String customerType);
    
    List<Customer> findMorosos();
    
    void delete(Long id);
    
    boolean existsByCode(String code);
    
    boolean existsByEmail(String email);
    
    boolean existsByDocumentNumber(String documentNumber);
}
