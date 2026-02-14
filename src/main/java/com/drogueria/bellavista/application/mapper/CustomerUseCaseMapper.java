package com.drogueria.bellavista.application.mapper;

import com.drogueria.bellavista.application.dto.CustomerDTO;
import com.drogueria.bellavista.domain.model.Customer;
import org.springframework.stereotype.Component;
/**
 * Mapper de casos de uso para clientes.
 * <p>
 * Se encarga de convertir entre los DTOs utilizados por la capa de aplicación
 * y la entidad de dominio {@link Customer}.
 * </p>
 *
 * <p>
 * Responsabilidades:
 * <ul>
 *     <li>Transformar DTOs de entrada en objetos del dominio.</li>
 *     <li>Transformar entidades del dominio en DTOs de salida.</li>
 *     <li>Mantener aislado el dominio de detalles de transporte (API).</li>
 * </ul>
 * </p>
 *
 * Forma parte de la capa de aplicación dentro de la arquitectura hexagonal.
 */
@Component
public class CustomerUseCaseMapper {
    /**
     * Convierte un DTO de creación en una entidad de dominio.
     *
     * @param request DTO con los datos para crear un cliente
     * @return instancia de {@link Customer} con los valores del request,
     *         o {@code null} si el request es nulo
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
     * Convierte un DTO de actualización en una entidad de dominio.
     *
     * @param request DTO con los datos para actualizar un cliente
     * @return instancia de {@link Customer} con los valores del request,
     *         o {@code null} si el request es nulo
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
     * Convierte una entidad del dominio en un DTO de respuesta.
     *
     * @param domain entidad {@link Customer} proveniente del dominio
     * @return DTO {@link CustomerDTO.Response} listo para ser expuesto en la API,
     *         o {@code null} si el dominio es nulo
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
