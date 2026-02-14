package com.drogueria.bellavista.application.mapper;

import com.drogueria.bellavista.application.dto.OrderDTO;
import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.model.OrderItem;
import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
/**
 * Mapper de casos de uso para órdenes de compra.
 * <p>
 * Responsable de convertir entre los DTOs utilizados por la capa de aplicación
 * y las entidades del dominio {@link Order}.
 * </p>
 *
 * <p>
 * Durante la conversión a dominio, este mapper consulta el
 * {@link ProductService} para enriquecer los ítems con datos reales del producto
 * (código, nombre, precio). Esto evita confiar en información enviada por el cliente
 * y garantiza consistencia con el catálogo del sistema.
 * </p>
 *
 * Forma parte de la capa de aplicación dentro de la arquitectura hexagonal.
 */
@Component
@RequiredArgsConstructor
public class OrderUseCaseMapper {
    
    private final ProductService productService;
    /**
     * Convierte un DTO de creación en una entidad de dominio {@link Order}.
     * <p>
     * Además de mapear los campos básicos, este método:
     * <ul>
     *   <li>Obtiene cada {@link Product} desde el servicio de dominio</li>
     *   <li>Construye los {@link OrderItem} con datos confiables</li>
     *   <li>Calcula subtotales por ítem</li>
     *   <li>Agrega los ítems a la orden</li>
     * </ul>
     *
     * @param request DTO enviado desde la API
     * @return instancia de {@link Order} lista para procesamiento en dominio
     */
    public Order toDomain(OrderDTO.CreateRequest request) {
        if (request == null) return null;
        
        Order order = Order.builder()
            .customerId(request.getCustomerId())
            .notes(request.getNotes())
            .expectedDeliveryDate(request.getExpectedDeliveryDate())
            .build();
        
        // Convertir items: obtener datos del producto
        if (request.getItems() != null) {
            request.getItems().forEach(itemRequest -> {
                Product product = productService.getProductById(itemRequest.getProductId());
                
                OrderItem item = OrderItem.builder()
                    .productId(product.getId())
                    .productCode(product.getCode())
                    .productName(product.getName())
                    .unitPrice(product.getPrice())
                    .quantity(itemRequest.getQuantity())
                    .build();
                
                item.calculateSubtotal();
                order.addItem(item);
            });
        }
        
        return order;
    }
    /**
     * Convierte una entidad de dominio {@link Order}
     * en su DTO de respuesta para la API.
     *
     * @param domain entidad proveniente del dominio
     * @return DTO {@link OrderDTO.Response} listo para exposición
     */
    public OrderDTO.Response toResponse(Order domain) {
        if (domain == null) return null;
        
        return OrderDTO.Response.builder()
            .id(domain.getId())
            .orderNumber(domain.getOrderNumber())
            .customerId(domain.getCustomerId())
            .customerCode(domain.getCustomerCode())
            .customerName(domain.getCustomerName())
            .supplierId(domain.getSupplierId())
            .supplierCode(domain.getSupplierCode())
            .supplierName(domain.getSupplierName())
            .status(domain.getStatus())
            .total(domain.getTotal())
            .items(domain.getItems().stream()
                .map(this::itemToResponse)
                .collect(Collectors.toList()))
            .notes(domain.getNotes())
            .orderDate(domain.getOrderDate())
            .expectedDeliveryDate(domain.getExpectedDeliveryDate())
            .actualDeliveryDate(domain.getActualDeliveryDate())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .build();
    }
    /**
     * Convierte un {@link OrderItem} del dominio
     * en su DTO de respuesta.
     *
     * @param item ítem perteneciente a la orden
     * @return DTO {@link OrderDTO.OrderItemResponse}
     */
    private OrderDTO.OrderItemResponse itemToResponse(OrderItem item) {
        return OrderDTO.OrderItemResponse.builder()
            .id(item.getId())
            .productId(item.getProductId())
            .productCode(item.getProductCode())
            .productName(item.getProductName())
            .unitPrice(item.getUnitPrice())
            .quantity(item.getQuantity())
            .subtotal(item.getSubtotal())
            .build();
    }
}
