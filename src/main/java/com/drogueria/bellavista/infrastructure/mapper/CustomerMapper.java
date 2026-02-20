package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.Customer;
import com.drogueria.bellavista.infrastructure.persistence.CustomerEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper - Convierte entre CustomerEntity (BD) ↔ Customer (Dominio)
 */
@Component
public class CustomerMapper {
    
    /**
     * Convierte CustomerEntity (BD) → Customer (Dominio)
     */
    public Customer toDomain(CustomerEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Customer.builder()
            .id(entity.getId())
            .code(entity.getCode())
            .name(entity.getName())
            .email(entity.getEmail())
            .phone(entity.getPhone())
            .address(entity.getAddress())
            .city(entity.getCity())
            .postalCode(entity.getPostalCode())
            .documentNumber(entity.getDocumentNumber())
            .documentType(entity.getDocumentType())
            .customerType(entity.getCustomerType())
            .creditLimit(entity.getCreditLimit())
            .pendingBalance(entity.getPendingBalance())
            .active(entity.getActive())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
    
    /**
     * Convierte Customer (Dominio) → CustomerEntity (BD)
     */
    public CustomerEntity toEntity(Customer domain) {
        if (domain == null) {
            return null;
        }
        
        return CustomerEntity.builder()
            .id(domain.getId())
            .code(domain.getCode())
            .name(domain.getName())
            .email(domain.getEmail())
            .phone(domain.getPhone())
            .address(domain.getAddress())
            .city(domain.getCity())
            .postalCode(domain.getPostalCode())
            .documentNumber(domain.getDocumentNumber())
            .documentType(domain.getDocumentType())
            .customerType(domain.getCustomerType())
            .creditLimit(domain.getCreditLimit())
            .pendingBalance(domain.getPendingBalance())
            .active(domain.getActive())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .build();
    }
}
