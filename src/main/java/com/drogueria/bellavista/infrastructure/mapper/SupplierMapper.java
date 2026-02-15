package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.Supplier;
import com.drogueria.bellavista.infrastructure.persistence.SupplierEntity;
import org.springframework.stereotype.Component;
/**
 * <h2>SupplierMapper</h2>
 *
 * <p>
 * Componente encargado de realizar la conversión entre:
 * </p>
 *
 * <ul>
 *     <li>{@link SupplierEntity} → Representación de persistencia (Base de Datos)</li>
 *     <li>{@link Supplier} → Modelo de dominio</li>
 * </ul>
 *
 * <p>
 * Este mapper pertenece a la capa de infraestructura dentro de la
 * arquitectura hexagonal (Ports & Adapters), permitiendo mantener
 * desacoplado el modelo de dominio de los detalles técnicos de
 * la base de datos.
 * </p>
 *
 * <p>
 * Su única responsabilidad es transformar datos entre ambas capas,
 * sin contener lógica de negocio, respetando el principio de
 * responsabilidad única (SRP).
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Component
public class SupplierMapper {
    /**
     * Convierte una entidad de persistencia {@link SupplierEntity}
     * en un modelo de dominio {@link Supplier}.
     *
     * <p>
     * Si la entidad recibida es {@code null}, retorna {@code null}
     * para prevenir {@link NullPointerException}.
     * </p>
     *
     * @param entity entidad obtenida desde la base de datos
     * @return modelo de dominio equivalente o {@code null} si la entidad es nula
     */
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
    /**
     * Convierte un modelo de dominio {@link Supplier}
     * en su entidad de persistencia {@link SupplierEntity}.
     *
     * <p>
     * Este método prepara el objeto para su almacenamiento
     * mediante mecanismos de persistencia como Spring Data JPA.
     * </p>
     *
     * <p>
     * Si el objeto de dominio es {@code null}, retorna {@code null}.
     * </p>
     *
     * @param domain modelo de dominio a convertir
     * @return entidad lista para persistencia o {@code null} si el dominio es nulo
     */
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
