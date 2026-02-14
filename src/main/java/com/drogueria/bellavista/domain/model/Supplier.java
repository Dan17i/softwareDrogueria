package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * <h2>Entidad de Dominio - Supplier</h2>
 *
 * <p>
 * Representa un proveedor dentro del sistema de la droguería.
 * Un proveedor es la entidad responsable de suministrar productos
 * que posteriormente serán almacenados o comercializados.
 * </p>
 *
 * <p>
 * Esta clase pertenece al núcleo del dominio y encapsula
 * información relevante para la gestión comercial y logística,
 * como tiempos de entrega y comportamiento de pago.
 * </p>
 *
 * <p>
 * No contiene lógica de persistencia ni dependencias externas,
 * respetando los principios de Clean Architecture.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    /**
     * Identificador único del proveedor.
     */
    private Long id;
    /**
     * Código interno del proveedor.
     */
    private String code;
    /**
     * Nombre o razón social del proveedor.
     */
    private String name;
    /**
     * Correo electrónico de contacto.
     */
    private String email;
    /**
     * Número telefónico del proveedor.
     */
    private String phone;
    /**
     * Dirección física del proveedor.
     */
    private String address;
    /**
     * Ciudad de ubicación del proveedor.
     */
    private String city;
    /**
     * Código postal de la dirección.
     */
    private String postalCode;
    /**
     * Número de documento de identificación (NIT, RUT, etc.).
     */
    private String documentNumber;
    /**
     * Tipo de documento del proveedor.
     */
    private String documentType;
    /**
     * Tiempo promedio de entrega en días.
     *
     * <p>
     * Representa el número estimado de días que tarda el proveedor
     * en entregar un pedido.
     * </p>
     */
    private Integer leadTimeDays;
    /**
     * Promedio histórico de retraso en pagos.
     *
     * <p>
     * Permite evaluar el comportamiento financiero asociado
     * al proveedor.
     * </p>
     */
    private BigDecimal averagePaymentDelay; // Promedio de retraso en pago
    /**
     * Indica si el proveedor está activo y disponible
     * para realizar operaciones comerciales.
     */
    private Boolean active;
    /**
     * Fecha y hora de creación del registro.
     */
    private LocalDateTime createdAt;
    /**
     * Fecha y hora de la última actualización del registro.
     */
    private LocalDateTime updatedAt;
    /**
     * Verifica si el proveedor se encuentra disponible
     * para operaciones comerciales.
     *
     * <p>
     * Un proveedor está disponible cuando se encuentra activo.
     * </p>
     *
     * @return {@code true} si está activo,
     *         {@code false} en caso contrario.
     */
    public boolean isAvailable() {
        return this.active;
    }
}
