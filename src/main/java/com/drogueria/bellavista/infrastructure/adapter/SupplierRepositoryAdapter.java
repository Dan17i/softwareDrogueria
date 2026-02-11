package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.domain.model.Supplier;
import com.drogueria.bellavista.domain.repository.SupplierRepository;
import com.drogueria.bellavista.infrastructure.mapper.SupplierMapper;
import com.drogueria.bellavista.infrastructure.persistence.JpaSupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador - Implementa SupplierRepository usando Spring Data JPA
 */
@Component
@RequiredArgsConstructor
public class SupplierRepositoryAdapter implements SupplierRepository {
    
    private final JpaSupplierRepository jpaRepository;
    private final SupplierMapper mapper;
    
    @Override
    public Supplier save(Supplier supplier) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(supplier)));
    }
    
    @Override
    public Optional<Supplier> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
    
    @Override
    public Optional<Supplier> findByCode(String code) {
        return jpaRepository.findByCode(code).map(mapper::toDomain);
    }
    
    @Override
    public Optional<Supplier> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }
    
    @Override
    public List<Supplier> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }
    
    @Override
    public List<Supplier> findAllActive() {
        return jpaRepository.findAllActive().stream().map(mapper::toDomain).collect(Collectors.toList());
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
}
