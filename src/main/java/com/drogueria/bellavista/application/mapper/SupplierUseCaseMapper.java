package com.drogueria.bellavista.application.mapper;

import com.drogueria.bellavista.application.dto.SupplierDTO;
import com.drogueria.bellavista.domain.model.Supplier;
import org.springframework.stereotype.Component;

/**
 * Mapper - Convierte entre SupplierDTO (Request/Response) ↔ Supplier (Dominio)
 */
@Component
public class SupplierUseCaseMapper {
    
    public Supplier toDomain(SupplierDTO.CreateRequest request) {
        if (request == null) return null;
        return Supplier.builder()
            .code(request.getCode())
            .name(request.getName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .address(request.getAddress())
            .city(request.getCity())
            .postalCode(request.getPostalCode())
            .documentNumber(request.getDocumentNumber())
            .documentType(request.getDocumentType())
            .leadTimeDays(request.getLeadTimeDays())
            .averagePaymentDelay(request.getAveragePaymentDelay())
            .build();
    }
    
    public Supplier toDomain(SupplierDTO.UpdateRequest request) {
        if (request == null) return null;
        return Supplier.builder()
            .code(request.getCode())
            .name(request.getName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .address(request.getAddress())
            .city(request.getCity())
            .postalCode(request.getPostalCode())
            .documentNumber(request.getDocumentNumber())
            .documentType(request.getDocumentType())
            .leadTimeDays(request.getLeadTimeDays())
            .averagePaymentDelay(request.getAveragePaymentDelay())
            .active(request.getActive())
            .build();
    }
    
    public SupplierDTO.Response toResponse(Supplier domain) {
        if (domain == null) return null;
        return SupplierDTO.Response.builder()
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
