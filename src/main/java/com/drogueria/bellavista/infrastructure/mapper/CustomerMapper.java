package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.Customer;
import com.drogueria.bellavista.infrastructure.persistence.CustomerEntity;
import org.springframework.stereotype.Component;
/**
 * <h2>CustomerMapper</h2>
 *
 * <p>
 * Componente encargado de realizar la conversión entre:
 * </p>
 *
 * <ul>
 *     <li>{@link CustomerEntity} → Representación de persistencia (Base de Datos)</li>
 *     <li>{@link Customer} → Modelo de dominio</li>
 * </ul>
 *
 * <p>
 * Este mapper forma parte de la capa de infraestructura dentro de la
 * arquitectura hexagonal (Ports & Adapters), permitiendo mantener el
 * desacoplamiento entre el dominio y los detalles de persistencia.
 * </p>
 *
 * <p>
 * Su responsabilidad principal es transformar datos entre ambas capas
 * sin introducir lógica de negocio, garantizando así el principio
 * de responsabilidad única (SRP).
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Component
public class CustomerMapper {
    /**
     * Convierte una entidad de persistencia {@link CustomerEntity}
     * en un modelo de dominio {@link Customer}.
     *
     * <p>
     * Si la entidad recibida es {@code null}, se retorna {@code null}
     * para evitar {@link NullPointerException}.
     * </p>
     *
     * @param entity entidad obtenida desde la base de datos
     * @return objeto del dominio equivalente o {@code null} si la entidad es nula
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
     * Convierte un modelo de dominio {@link Customer}
     * en una entidad de persistencia {@link CustomerEntity}.
     *
     * <p>
     * Si el objeto de dominio recibido es {@code null}, se retorna {@code null}
     * para evitar errores en la capa de persistencia.
     * </p>
     *
     * @param domain objeto del dominio a convertir
     * @return entidad lista para ser persistida o {@code null} si el dominio es nulo
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
