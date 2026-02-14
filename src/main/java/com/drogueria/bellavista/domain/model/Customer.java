package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * <h2>Entidad de Dominio - Customer</h2>
 *
 * <p>
 * Representa el modelo de negocio puro de un cliente dentro del dominio
 * de la droguería Bellavista.
 * </p>
 *
 * <p>
 * Esta clase encapsula tanto los atributos principales del cliente
 * como su comportamiento de negocio relacionado con:
 * </p>
 * <ul>
 *     <li>Gestión de crédito</li>
 *     <li>Control de saldo pendiente</li>
 *     <li>Validación de estado financiero</li>
 * </ul>
 *
 * <p>
 * No contiene lógica de persistencia ni dependencias externas,
 * cumpliendo con los principios de un modelo de dominio limpio.
 * </p>
 *
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    /**
     * Identificador único del cliente.
     */
    private Long id;
    /**
     * Código interno del cliente en el sistema.
     */
    private String code;
    /**
     * Nombre completo o razón social del cliente.
     */
    private String name;
    /**
     * Correo electrónico de contacto.
     */
    private String email;
    /**
     * Número telefónico del cliente.
     */
    private String phone;
    /**
     * Dirección física del cliente.
     */
    private String address;
    /**
     * Ciudad de residencia o ubicación comercial.
     */
    private String city;
    /**
     * Código postal de la dirección del cliente.
     */
    private String postalCode;
    /**
     * Número de documento de identificación (CC, NIT, etc.).
     */
    private String documentNumber;
    /**
     * Tipo de documento (RUT, CC, NIT, etc.).
     */
    private String documentType;
    /**
     * Tipo de cliente según clasificación comercial
     * (por ejemplo: MAYORISTA, MINORISTA).
     */
    private String customerType;
    /**
     * Límite máximo de crédito aprobado para el cliente.
     */
    private BigDecimal creditLimit;
    /**
     * Saldo pendiente actual del cliente.
     */
    private BigDecimal pendingBalance;
    /**
     * Indica si el cliente se encuentra activo en el sistema.
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
     * Verifica si el cliente tiene crédito disponible suficiente
     * para cubrir un monto determinado.
     *
     * @param amount monto que se desea validar
     * @return {@code true} si el cliente está activo y posee crédito disponible suficiente,
     *         {@code false} en caso contrario.
     */
    public boolean hasCreditAvailable(BigDecimal amount) {
        if (!this.active || this.creditLimit == null) {
            return false;
        }
        BigDecimal availableCredit = this.creditLimit.subtract(this.pendingBalance != null ? this.pendingBalance : BigDecimal.ZERO);
        return amount.compareTo(availableCredit) <= 0;
    }
    /**
     * Incrementa el saldo pendiente del cliente.
     *
     * <p>
     * Esta operación se utiliza cuando se registra una nueva venta
     * a crédito.
     * </p>
     *
     * @param amount monto a incrementar
     * @throws IllegalArgumentException si el monto es nulo o menor o igual a cero
     */
    public void increasePendingBalance(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        this.pendingBalance = (this.pendingBalance != null ? this.pendingBalance : BigDecimal.ZERO).add(amount);
        this.updatedAt = LocalDateTime.now();
    }
    /**
     * Reduce el saldo pendiente del cliente.
     *
     * <p>
     * Esta operación se utiliza cuando el cliente realiza un pago.
     * </p>
     *
     * @param amount monto a descontar
     * @throws IllegalArgumentException si el monto es nulo o menor o igual a cero
     * @throws IllegalStateException si el monto del pago excede el saldo pendiente actual
     */
    public void reducePendingBalance(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        if (this.pendingBalance == null) {
            this.pendingBalance = BigDecimal.ZERO;
        }
        if (this.pendingBalance.compareTo(amount) < 0) {
            throw new IllegalStateException("El pago excede el saldo pendiente");
        }
        this.pendingBalance = this.pendingBalance.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }
    /**
     * Determina si el cliente se encuentra en estado moroso.
     *
     * <p>
     * Se considera moroso si:
     * </p>
     * <ul>
     *     <li>Está activo</li>
     *     <li>Tiene saldo pendiente mayor a cero</li>
     * </ul>
     *
     * @return {@code true} si el cliente tiene deuda pendiente,
     *         {@code false} en caso contrario.
     */
    public boolean isMoroso() {
        return this.active && this.pendingBalance != null && this.pendingBalance.signum() > 0;
    }
}
