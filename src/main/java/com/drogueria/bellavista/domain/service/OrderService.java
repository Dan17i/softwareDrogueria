package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Customer;
import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.domain.repository.OrderRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
/**
 * Servicio de dominio encargado de la gestión del ciclo de vida de las órdenes.
 *
 * <p>Responsabilidades principales:</p>
 * <ul>
 *     <li>Crear órdenes validando cliente, productos, stock y crédito</li>
 *     <li>Consultar órdenes por distintos criterios</li>
 *     <li>Gestionar cambios de estado (completar, cancelar)</li>
 *     <li>Aplicar efectos colaterales de negocio:
 *          actualización de stock y saldo del cliente</li>
 * </ul>
 *
 * <p>Este servicio contiene lógica crítica del negocio. Las operaciones que
 * modifican estado se ejecutan dentro de una transacción para garantizar
 * consistencia entre inventario, órdenes y crédito del cliente.</p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    /**
     * Crea una nueva orden de venta.
     *
     * <p>Validaciones realizadas:</p>
     * <ul>
     *     <li>El cliente debe existir y estar activo</li>
     *     <li>La orden debe contener al menos un ítem</li>
     *     <li>Los productos deben existir, estar disponibles y tener stock suficiente</li>
     *     <li>Se recalcula el total de la orden</li>
     *     <li>El cliente debe tener crédito disponible</li>
     * </ul>
     *
     * <p>Efectos de negocio:</p>
     * <ul>
     *     <li>Se genera un número único de orden</li>
     *     <li>Se reduce el stock de los productos</li>
     *     <li>Se incrementa el saldo pendiente del cliente</li>
     * </ul>
     *
     * @param order orden a registrar
     * @return orden guardada
     * @throws ResourceNotFoundException si el cliente no existe
     * @throws BusinessException si se incumple alguna regla de negocio
     */
    public Order createOrder(Order order) {
        // Validar cliente
        Customer customer = customerService.getCustomerById(order.getCustomerId());
        if (!customer.getActive()) {
            throw new BusinessException("El cliente está inactivo");
        }
        
        // Validar que la orden tenga ítems
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new BusinessException("La orden debe contener al menos un producto");
        }
        
        // Validar productos y stock
        order.getItems().forEach(item -> {
            Product product = productService.getProductById(item.getProductId());
            
            if (!product.isAvailable()) {
                throw new BusinessException("El producto " + product.getCode() + " no está disponible");
            }
            
            if (product.getStock() < item.getQuantity()) {
                throw new BusinessException("Stock insuficiente para el producto " + product.getCode() 
                    + ". Disponible: " + product.getStock());
            }
            
            // Actualizar datos del producto en la línea
            item.setProductCode(product.getCode());
            item.setProductName(product.getName());
            item.setUnitPrice(product.getPrice());
            item.calculateSubtotal();
        });
        
        // Recalcular total
        order.recalculateTotal();
        
        // Validar crédito del cliente
        if (!customer.hasCreditAvailable(order.getTotal())) {
            throw new BusinessException("El cliente no tiene crédito suficiente. "
                + "Límite: " + customer.getCreditLimit() 
                + ", Pendiente: " + customer.getPendingBalance()
                + ", Requerido: " + order.getTotal());
        }
        
        // Establecer valores
        order.setOrderNumber(generateOrderNumber());
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        // Guardar orden
        Order savedOrder = orderRepository.save(order);
        
        // Reducir stock de productos
        order.getItems().forEach(item -> {
            productService.reduceStockInternal(item.getProductId(), item.getQuantity());
        });
        
        // Aumentar saldo del cliente
        customerService.increasePendingBalance(customer.getId(), order.getTotal());
        
        return savedOrder;
    }
    /**
     * Obtiene una orden por su identificador.
     *
     * @param id ID de la orden
     * @return orden encontrada
     * @throws ResourceNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }
    /**
     * Obtiene una orden por su número único.
     *
     * @param orderNumber número de orden
     * @return orden encontrada
     * @throws ResourceNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public Order getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));
    }
    /**
     * Obtiene todas las órdenes registradas.
     */
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    /**
     * Obtiene las órdenes asociadas a un cliente.
     *
     * @param customerId ID del cliente
     * @return lista de órdenes del cliente
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByCustomerId(Long customerId) {
        customerService.getCustomerById(customerId); // Validar que existe
        return orderRepository.findByCustomerId(customerId);
    }
    /**
     * Obtiene órdenes filtradas por estado.
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
    /**
     * Obtiene las órdenes pendientes de un cliente.
     */
    @Transactional(readOnly = true)
    public List<Order> getPendingOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerIdAndStatus(customerId, "PENDING");
    }
    /**
     * Obtiene órdenes dentro de un rango de fechas.
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByDateRange(startDate, endDate);
    }
    /**
     * Marca una orden como completada.
     *
     * @param orderId ID de la orden
     * @return orden actualizada
     * @throws ResourceNotFoundException si no existe
     */
    public Order completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        order.complete();
        return orderRepository.save(order);
    }
    /**
     * Cancela una orden.
     *
     * <p>Si la orden estaba en estado PENDING se revierten
     * los efectos de negocio:</p>
     * <ul>
     *     <li>Se devuelve el stock a los productos</li>
     *     <li>Se reduce el saldo pendiente del cliente</li>
     * </ul>
     *
     * @param orderId ID de la orden
     * @return orden cancelada
     * @throws ResourceNotFoundException si no existe
     */
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        if ("PENDING".equals(order.getStatus())) {
            // Revertir stock
            order.getItems().forEach(item -> {
                productService.increaseStockInternal(item.getProductId(), item.getQuantity());
            });
            
            // Revertir saldo del cliente
            customerService.reducePendingBalance(order.getCustomerId(), order.getTotal());
        }
        
        order.cancel();
        return orderRepository.save(order);
    }
    /**
     * Genera un número único de orden.
     *
     * @return identificador único de orden
     */

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}
