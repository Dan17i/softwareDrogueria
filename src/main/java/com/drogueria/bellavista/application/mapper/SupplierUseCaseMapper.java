package com.drogueria.bellavista.application.mapper;

import com.drogueria.bellavista.application.dto.SupplierDTO;
import com.drogueria.bellavista.domain.model.Supplier;
import org.springframework.stereotype.Component;
/**
 * Mapper de casos de uso para proveedores.
 * <p>
 * Se encarga de transformar los DTOs utilizados por la capa de aplicación
 * en entidades del dominio {@link Supplier} y viceversa.
 * </p>
 *
 * <p>
 * Este componente pertenece a la capa de aplicación dentro de la
 * arquitectura hexagonal y no contiene lógica de negocio, únicamente
 * conversión de estructuras de datos.
 * </p>
 */
@Component
public class SupplierUseCaseMapper {
    /**
     * Convierte un DTO de creación de proveedor en una entidad de dominio.
     *
     * @param request DTO recibido desde la API para registrar un proveedor
     * @return instancia de {@link Supplier} con los datos iniciales,
     *         o {@code null} si el request es nulo
     */
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
    /**
     * Convierte un DTO de actualización de proveedor en una entidad de dominio.
     * <p>
     * A diferencia del método de creación, este incluye el estado activo
     * del proveedor, ya que puede modificarse durante una actualización.
     * </p>
     *
     * @param request DTO recibido desde la API para actualizar un proveedor
     * @return entidad {@link Supplier} con los datos actualizados,
     *         o {@code null} si el request es nulo
     */
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
    /**
     * Convierte una entidad del dominio {@link Supplier}
     * en su DTO de respuesta para exposición en la API.
     *
     * @param domain entidad proveniente del dominio
     * @return DTO {@link SupplierDTO.Response} listo para serialización,
     *         o {@code null} si la entidad es nula
     */
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
