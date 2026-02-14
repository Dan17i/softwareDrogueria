package com.drogueria.bellavista.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
/**
 * DTOs relacionados con el proceso de Recepción de Mercancía (Goods Receipt).
 *
 * Estos objetos se utilizan para:
 * - Registrar la recepción de productos provenientes de una orden de compra
 * - Actualizar información de la recepción
 * - Transportar datos hacia el cliente (responses)
 *
 * Se diseñan como clases estáticas internas para mantener cohesión funcional
 * dentro del mismo contexto de negocio.
 */
public class GoodsReceiptDTO {
    /**
     * DTO utilizado para registrar cada ítem recibido dentro de la recepción.
     * Representa un producto individual y sus cantidades ordenadas vs recibidas.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoodsReceiptItemRequest {
        /** ID del producto en el sistema */
        @NotNull(message = "Product ID is required")
        private Long productId;
        /** Código del producto (útil para visualización rápida o auditoría) */
        @NotBlank(message = "Product code is required")
        @Size(max = 50, message = "Product code cannot exceed 50 characters")
        private String productCode;
        /** Nombre descriptivo del producto */
        @NotBlank(message = "Product name is required")
        @Size(max = 255, message = "Product name cannot exceed 255 characters")
        private String productName;
        /** Cantidad que se esperaba recibir según la orden */
        @NotNull(message = "Ordered quantity is required")
        @Min(value = 1, message = "Ordered quantity must be greater than 0")
        private Integer orderedQuantity;
        /** Cantidad realmente recibida físicamente */
        @NotNull(message = "Received quantity is required")
        @Min(value = 0, message = "Received quantity cannot be negative")
        private Integer receivedQuantity;
    }
    /**
     * DTO usado para crear una nueva recepción de mercancía.
     *
     * Reglas de negocio implícitas:
     * - Debe estar asociada a una orden existente
     * - Debe contener al menos un ítem
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        /** ID de la orden de compra asociada */
        @NotNull(message = "Order ID is required")
        private Long orderId;
        /** Lista de productos recibidos */
        @NotEmpty(message = "Items list cannot be empty")
        @Valid
        private List<GoodsReceiptItemRequest> items;
        /** Observaciones del usuario sobre la recepción */
        @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
        private String notes;
        /** Fecha esperada de entrega definida en la orden */
        private java.time.LocalDateTime expectedDeliveryDate;
    }
    /**
     * DTO utilizado para actualizar información editable de la recepción.
     * No permite modificar los ítems ni la orden asociada.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        /** Notas u observaciones actualizadas */
        @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
        private String notes;
        /** Fecha esperada de entrega modificada */
        private LocalDateTime expectedDeliveryDate;
    }
    /**
     * DTO de respuesta para cada ítem recibido.
     * Incluye información calculada por el backend.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoodsReceiptItemResponse {
        /** ID interno del registro del ítem */
        private Long id;
        /** ID del producto */
        private Long productId;
        /** Código del producto */
        private String productCode;
        /** Nombre del producto */
        private String productName;
        /** Cantidad ordenada */
        private Integer orderedQuantity;
        /** Cantidad recibida */
        private Integer receivedQuantity;
        /**
         * Diferencia entre lo ordenado y lo recibido.
         * Valor positivo indica faltante.
         * Valor negativo indicaría sobre-recepción.
         */
        private Integer differenceQuantity;
    }
    /**
     * DTO principal de respuesta para una recepción de mercancía.
     * Contiene información de cabecera + ítems recibidos.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        /** ID interno de la recepción */
        private Long id;
        /** Número único de recepción generado por el sistema */
        private String receiptNumber;
        /** ID de la orden asociada */
        private Long orderId;
        /** Número visible de la orden */
        private String orderNumber;
        /** ID del proveedor */
        private Long supplierId;
        /** Nombre del proveedor */
        private String supplierName;
        /**
         * Estado actual de la recepción:
         * PENDING, RECEIVED, PARTIALLY_RECEIVED, REJECTED
         */
        private String status;
        /** Notas registradas */
        private String notes;
        /** Fecha esperada de entrega */
        private LocalDateTime expectedDeliveryDate;
        /** Fecha de creación del registro */
        private java.time.LocalDateTime createdAt;
        /** Fecha de última actualización */
        private java.time.LocalDateTime updatedAt;
        /** Número total de líneas de productos */
        private Integer totalLineItems;
        /** Cantidad total de unidades recibidas */
        private Integer totalReceivedQuantity;
        /** Detalle de ítems recibidos */
        private List<GoodsReceiptItemResponse> items;
    }
}
