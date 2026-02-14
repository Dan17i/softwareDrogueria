package com.drogueria.bellavista.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * Data Transfer Objects related to supplier management.
 *
 * <p>This class groups all DTOs used by the supplier API, including
 * creation requests, update requests, and response representations.</p>
 *
 * <p>Suppliers represent external entities that provide products to the system.
 * These DTOs are used to transfer validated data between the presentation layer
 * (REST controllers) and the application/service layer.</p>
 *
 * <p>Validation rules ensure correct supplier identification, contact data,
 * and operational attributes such as delivery time and payment metrics.</p>
 *
 * @author Daniel Jurado & equipo se desarrollo
 * @version 1.0
 * @since 2026
 */
public class SupplierDTO {
    /**
     * DTO used to create a new supplier.
     *
     * <p>Contains the required identification data and optional
     * contact and operational information.</p>
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        /**
         * Unique supplier code.
         * Used as business identifier and must not be blank.
         */
        @NotBlank(message = "El código es requerido")
        @Size(min = 1, max = 50)
        private String code;
        /**
         * Supplier business or legal name.
         * Must contain between 3 and 100 characters.
         */
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 3, max = 100)
        private String name;
        /**
         * Contact email address.
         * Must be valid if provided.
         */
        @Email(message = "Email debe ser válido")
        private String email;
        /**
         * Contact phone number.
         */
        @Size(max = 20)
        private String phone;
        /**
         * Physical or fiscal address of the supplier.
         */
        @Size(max = 255)
        private String address;
        /**
         * City where the supplier operates.
         */
        @Size(max = 100)
        private String city;
        /**
         * Postal or ZIP code.
         */
        @Size(max = 10)
        private String postalCode;
        /**
         * Legal document number (NIT, ID, etc.).
         */
        @Size(max = 50)
        private String documentNumber;
        /**
         * Type of legal document (NIT, CC, Passport, etc.).
         */
        @Size(max = 50)
        private String documentType;
        /**
         * Average delivery time in days.
         * Must not be negative.
         */
        @Min(value = 0, message = "Lead time no puede ser negativo")
        private Integer leadTimeDays;
        /**
         * Average payment delay metric used for supplier evaluation.
         * Must be zero or positive.
         */
        @DecimalMin(value = "0.00")
        private BigDecimal averagePaymentDelay;
    }
    /**
     * DTO used to update an existing supplier.
     *
     * <p>Contains the same fields as creation plus the active flag,
     * allowing logical activation or deactivation.</p>
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        /**
         * Unique supplier code.
         */
        @NotBlank(message = "El código es requerido")
        @Size(min = 1, max = 50)
        private String code;
        /**
         * Supplier name.
         */
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 3, max = 100)
        private String name;
        /**
         * Contact email.
         */
        @Email
        private String email;
        /**
         * Contact phone.
         */
        @Size(max = 20)
        private String phone;
        /**
         * Supplier address.
         */
        @Size(max = 255)
        private String address;
        /**
         * Supplier city.
         */
        @Size(max = 100)
        private String city;
        /**
         * Postal code.
         */
        @Size(max = 10)
        private String postalCode;
        /**
         * Legal document number.
         */
        @Size(max = 50)
        private String documentNumber;
        /**
         * Legal document type.
         */
        @Size(max = 50)
        private String documentType;
        /**
         * Delivery lead time in days.
         */
        @Min(value = 0)
        private Integer leadTimeDays;
        /**
         * Average payment delay indicator.
         */
        @DecimalMin(value = "0.00")
        private BigDecimal averagePaymentDelay;
        /**
         * Indicates whether the supplier is active in the system.
         */
        private Boolean active;
    }
    /**
     * DTO returned to the client with supplier information.
     *
     * <p>This object represents the persisted supplier state and
     * is typically returned in API responses.</p>
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        /**
         * Internal database identifier.
         */
        private Long id;
        /**
         * Business code.
         */
        private String code;
        /**
         * Supplier name.
         */
        private String name;
        /**
         * Email address.
         */
        private String email;
        /**
         * Phone number.
         */
        private String phone;
        /**
         * Address.
         */
        private String address;
        /**
         * City.
         */
        private String city;
        /**
         * Postal code.
         */
        private String postalCode;
        /**
         * Document number.
         */
        private String documentNumber;
        /**
         * Document type.
         */
        private String documentType;
        /**
         * Delivery lead time.
         */
        private Integer leadTimeDays;
        /**
         * Average payment delay metric.
         */
        private BigDecimal averagePaymentDelay;
        /**
         * Indicates if supplier is active.
         */
        private Boolean active;
        /**
         * Record creation timestamp.
         */
        private LocalDateTime createdAt;
        /**
         * Last update timestamp.
         */
        private LocalDateTime updatedAt;
    }
}
