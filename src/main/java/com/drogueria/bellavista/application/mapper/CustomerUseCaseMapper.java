package com.drogueria.bellavista.application.mapper;

import com.drogueria.bellavista.application.dto.CustomerDTO;
import com.drogueria.bellavista.domain.model.Customer;
import org.springframework.stereotype.Component;

/**
 * Mapper - Convierte entre CustomerDTO (Request/Response) ↔ Customer (Dominio)
 * Capa de aplicación
 */
@Component
public class CustomerUseCaseMapper {
    
    /**
     * Convierte CreateRequest → Customer (Dominio)
     */
    public Customer toDomain(CustomerDTO.CreateRequest request) {
        if (request == null) {
            return null;
        }
        
        return Customer.builder()
            .code(request.getCode())
            .name(request.getName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .address(request.getAddress())
            .city(request.getCity())
            .postalCode(request.getPostalCode())
            .documentNumber(request.getDocumentNumber())
            .documentType(request.getDocumentType())
            .customerType(request.getCustomerType())
            .creditLimit(request.getCreditLimit())
            .build();
    }
    
    /**
     * Convierte UpdateRequest → Customer (Dominio)
     */
    public Customer toDomain(CustomerDTO.UpdateRequest request) {
        if (request == null) {
            return null;
        }
        
        return Customer.builder()
            .code(request.getCode())
            .name(request.getName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .address(request.getAddress())
            .city(request.getCity())
            .postalCode(request.getPostalCode())
            .documentNumber(request.getDocumentNumber())
            .documentType(request.getDocumentType())
            .customerType(request.getCustomerType())
            .creditLimit(request.getCreditLimit())
            .active(request.getActive())
            .build();
    }
    
    /**
     * Convierte Customer (Dominio) → Response DTO
     */
    public CustomerDTO.Response toResponse(Customer domain) {
        if (domain == null) {
            return null;
        }
        
        return CustomerDTO.Response.builder()
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
