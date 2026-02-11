package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.Supplier;
import com.drogueria.bellavista.infrastructure.persistence.SupplierEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper - Convierte entre SupplierEntity (BD) ↔ Supplier (Dominio)
 */
@Component
public class SupplierMapper {
    
    public Supplier toDomain(SupplierEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Supplier.builder()
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
            .leadTimeDays(entity.getLeadTimeDays())
            .averagePaymentDelay(entity.getAveragePaymentDelay())
            .active(entity.getActive())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
    
    public SupplierEntity toEntity(Supplier domain) {
        if (domain == null) {
            return null;
        }
        
        return SupplierEntity.builder()
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
            .leadTimeDays(domain.getLeadTimeDays())
            .averagePaymentDelay(domain.getAveragePaymentDelay())
            .active(domain.getActive())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .build();
    }
}
