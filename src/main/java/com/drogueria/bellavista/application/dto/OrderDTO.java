package com.drogueria.bellavista.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
/**
 * DTOs utilizados para la gestión de Órdenes del sistema.
 *
 * Este conjunto de clases define los contratos de entrada y salida
 * para los casos de uso relacionados con órdenes de venta o compra.
 *
 * Responsabilidades:
 * - Transportar datos entre Controller ↔ Application
 * - Validar estructura de requests entrantes
 * - Estandarizar responses hacia el cliente
 *
 * No contiene lógica de negocio.
 */
public class OrderDTO {
    /**
     * DTO utilizado para representar cada producto incluido en una orden
     * durante la creación de la misma.
     *
     * Solo contiene información mínima necesaria para el backend,
     * el resto de datos del producto se resuelven en la capa de dominio.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        /**
         * Identificador del producto a incluir en la orden.
         * Debe existir previamente en el sistema.
         */
        @NotNull(message = "Product ID es requerido")
        private Long productId;
        /**
         * Cantidad solicitada del producto.
         * Debe ser mayor a cero.
         */
        @NotNull(message = "Cantidad es requerida")
        @Min(value = 1, message = "Cantidad debe ser mayor a 0")
        private Integer quantity;
    }
    /**
     * DTO utilizado para crear una nueva orden.
     *
     * Contiene:
     * - Cliente asociado
     * - Lista de productos
     * - Información opcional logística
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        /**
         * ID del cliente que realiza la orden.
         */
        @NotNull(message = "Customer ID es requerido")
        private Long customerId;
        /**
         * Lista de productos incluidos en la orden.
         *
         * Reglas:
         * - Debe contener al menos un item
         * - Cada item se valida individualmente
         */
        @Valid
        @NotEmpty(message = "Debe contener al menos un producto")
        private List<OrderItemRequest> items;
        /**
         * Observaciones adicionales de la orden.
         * Campo opcional.
         */
        private String notes;
        /**
         * Fecha estimada de entrega de la orden.
         * Opcional.
         */
        private LocalDateTime expectedDeliveryDate;
    }
    /**
     * DTO de salida que representa cada item de la orden.
     *
     * Incluye información enriquecida del producto y cálculos
     * realizados por la capa de dominio.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        /** ID interno del item de la orden */
        private Long id;
        /** ID del producto */
        private Long productId;
        /** Código del producto */
        private String productCode;
        /** Nombre del producto */
        private String productName;
        /** Precio unitario aplicado en la orden */
        private BigDecimal unitPrice;
        /** Cantidad solicitada */
        private Integer quantity;
        /** Subtotal calculado (precio * cantidad) */
        private BigDecimal subtotal;
    }
    /**
     * DTO de respuesta principal para consultas de órdenes.
     *
     * Incluye:
     * - Información del cliente
     * - Información del proveedor asociado (si aplica)
     * - Estado de la orden
     * - Totales calculados
     * - Fechas de control
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        /** Identificador único de la orden */
        private Long id;
        /** Número de orden generado por el sistema */
        private String orderNumber;
        /** ID del cliente */
        private Long customerId;
        /** Código del cliente */
        private String customerCode;
        /** Nombre del cliente */
        private String customerName;
        /** ID del proveedor asociado */
        private Long supplierId;
        /** Código del proveedor */
        private String supplierCode;
        /** Nombre del proveedor */
        private String supplierName;
        /**
         * Estado actual de la orden.
         * Ejemplos:
         * - PENDING
         * - CONFIRMED
         * - SHIPPED
         * - DELIVERED
         * - CANCELLED
         */
        private String status;
        /** Total monetario de la orden */
        private BigDecimal total;
        /** Lista de items incluidos en la orden */
        private List<OrderItemResponse> items;
        /** Observaciones registradas */
        private String notes;
        /** Fecha en la que se registró la orden */
        private LocalDateTime orderDate;
        /** Fecha estimada de entrega */
        private LocalDateTime expectedDeliveryDate;
        /** Fecha real de entrega */
        private LocalDateTime actualDeliveryDate;
        /** Fecha de creación del registro */
        private LocalDateTime createdAt;
        /** Fecha de última actualización */
        private LocalDateTime updatedAt;
    }
}
