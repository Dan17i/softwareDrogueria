package com.drogueria.bellavista.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * DTOs del módulo de clientes.
 *
 * <p>Contiene las estructuras utilizadas por la API para:
 * <ul>
 *     <li>Crear clientes</li>
 *     <li>Actualizar clientes</li>
 *     <li>Responder información del cliente</li>
 * </ul>
 *
 * <p>Estos DTOs pertenecen a la capa de aplicación y funcionan como
 * contrato entre la API REST y los casos de uso del dominio.
 */
public class CustomerDTO {
    /**
     * DTO utilizado para crear un cliente.
     *
     * <p>Se recibe desde el cliente HTTP al invocar el endpoint
     * de creación de clientes. Incluye validaciones de formato,
     * tamaño y obligatoriedad.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        /**
         * Código único del cliente dentro del sistema.
         */
        @NotBlank(message = "El código es requerido")
        @Size(min = 1, max = 50, message = "El código debe tener entre 1 y 50 caracteres")
        private String code;
        /**
         * Nombre completo o razón social del cliente.
         */
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        private String name;
        /**
         * Correo electrónico del cliente.
         */
        @Email(message = "El email debe ser válido")
        private String email;
        /**
         * Número telefónico del cliente.
         */
        @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
        private String phone;
        /**
         * Dirección física del cliente.
         */
        @Size(max = 255, message = "La dirección no debe exceder 255 caracteres")
        private String address;
        /**
         * Ciudad del cliente.
         */
        @Size(max = 100, message = "La ciudad no debe exceder 100 caracteres")
        private String city;
        /**
         * Código postal del cliente.
         */
        @Size(max = 10, message = "El código postal no debe exceder 10 caracteres")
        private String postalCode;
        /**
         * Número de documento del cliente.
         */
        @Size(max = 50, message = "El documento no debe exceder 50 caracteres")
        private String documentNumber;
        /**
         * Tipo de documento del cliente.
         */
        @Size(max = 50, message = "El tipo de documento no debe exceder 50 caracteres")
        private String documentType;
        /**
         * Tipo de cliente dentro del negocio.
         * Ejemplo: MAYORISTA, MINORISTA.
         */
        @NotBlank(message = "El tipo de cliente es requerido")
        @Size(max = 50, message = "El tipo de cliente no debe exceder 50 caracteres")
        private String customerType; // MAYORISTA, MINORISTA
        /**
         * Límite de crédito asignado al cliente.
         */
        @DecimalMin(value = "0.00", inclusive = false, message = "El límite de crédito debe ser mayor a 0")
        private BigDecimal creditLimit;
    }
    /**
     * DTO utilizado para actualizar un cliente existente.
     *
     * <p>Similar al DTO de creación, pero permite modificar
     * el estado activo del cliente.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        /**
         * Código único del cliente.
         */
        @NotBlank(message = "El código es requerido")
        @Size(min = 1, max = 50, message = "El código debe tener entre 1 y 50 caracteres")
        private String code;
        /**
         * Nombre del cliente.
         */
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        private String name;
        /**
         * Email del cliente.
         */
        @Email(message = "El email debe ser válido")
        private String email;
        /**
         * Teléfono del cliente.
         */
        @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
        private String phone;
        /**
         * Dirección del cliente.
         */
        @Size(max = 255, message = "La dirección no debe exceder 255 caracteres")
        private String address;
        /**
         * Ciudad del cliente.
         */
        @Size(max = 100, message = "La ciudad no debe exceder 100 caracteres")
        private String city;
        /**
         * Código postal del cliente.
         */
        @Size(max = 10, message = "El código postal no debe exceder 10 caracteres")
        private String postalCode;
        /**
         * Documento del cliente.
         */
        @Size(max = 50, message = "El documento no debe exceder 50 caracteres")
        private String documentNumber;
        /**
         * Tipo de documento.
         */
        @Size(max = 50, message = "El tipo de documento no debe exceder 50 caracteres")
        private String documentType;
        /**
         * Tipo de cliente.
         */
        @Size(max = 50, message = "El tipo de cliente no debe exceder 50 caracteres")
        private String customerType;
        /**
         * Límite de crédito actualizado.
         */
        @DecimalMin(value = "0.00", message = "El límite de crédito no puede ser negativo")
        private BigDecimal creditLimit;
        /**
         * Indica si el cliente está activo en el sistema.
         */
        private Boolean active;
    }
    /**
     * DTO de respuesta del cliente.
     *
     * <p>Se devuelve en las respuestas de la API con toda la
     * información del cliente persistido en el sistema.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        /**
         * Identificador único del cliente.
         */
        private Long id;

        private String code;
        private String name;
        private String email;
        private String phone;
        private String address;
        private String city;
        private String postalCode;
        private String documentNumber;
        private String documentType;
        private String customerType;
        private BigDecimal creditLimit;
        /**
         * Saldo pendiente del cliente.
         */
        @JsonProperty("pendingBalance")
        private BigDecimal pendingBalance;
        /**
         * Indica si el cliente está activo.
         */
        private Boolean active;
        /**
         * Fecha de creación del registro.
         */
        private LocalDateTime createdAt;
        /**
         * Fecha de última actualización.
         */
        private LocalDateTime updatedAt;
    }
}
