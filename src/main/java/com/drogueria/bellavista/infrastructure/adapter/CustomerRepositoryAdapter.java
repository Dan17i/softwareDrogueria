package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.domain.model.Customer;
import com.drogueria.bellavista.domain.repository.CustomerRepository;
import com.drogueria.bellavista.infrastructure.mapper.CustomerMapper;
import com.drogueria.bellavista.infrastructure.persistence.CustomerEntity;
import com.drogueria.bellavista.infrastructure.persistence.JpaCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador - Implementa CustomerRepository usando Spring Data JPA
 * Convierte entre objetos de dominio y entidades JPA
 */
@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {
    
    private final JpaCustomerRepository jpaRepository;
    private final CustomerMapper mapper;
    
    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = mapper.toEntity(customer);
        CustomerEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Customer> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Customer> findByCode(String code) {
        return jpaRepository.findByCode(code)
            .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Customer> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
            .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Customer> findByDocumentNumber(String documentNumber) {
        return jpaRepository.findByDocumentNumber(documentNumber)
            .map(mapper::toDomain);
    }
    
    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAll()
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Customer> findAllActive() {
        return jpaRepository.findAllActive()
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Customer> findByCustomerType(String customerType) {
        return jpaRepository.findByCustomerType(customerType)
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Customer> findMorosos() {
        return jpaRepository.findMorosos()
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
    
    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return jpaRepository.existsByDocumentNumber(documentNumber);
    }
}
