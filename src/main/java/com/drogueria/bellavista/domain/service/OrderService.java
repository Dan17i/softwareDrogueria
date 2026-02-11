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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Servicio de dominio - Casos de uso de Órdenes
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    
    /**
     * Crear nueva orden
     * Validaciones críticas:
     * - Cliente existe y está activo
     * - Cliente tiene crédito disponible
     * - Productos existen y tienen stock
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
     * Obtener orden por ID
     */
    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }
    
    /**
     * Obtener orden por número
     */
    @Transactional(readOnly = true)
    public Order getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));
    }
    
    /**
     * Listar todas las órdenes
     */
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    /**
     * Listar órdenes por cliente
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByCustomerId(Long customerId) {
        customerService.getCustomerById(customerId); // Validar que existe
        return orderRepository.findByCustomerId(customerId);
    }
    
    /**
     * Listar órdenes por estado
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
    
    /**
     * Listar órdenes pendientes de un cliente
     */
    @Transactional(readOnly = true)
    public List<Order> getPendingOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerIdAndStatus(customerId, "PENDING");
    }
    
    /**
     * Listar órdenes por rango de fechas
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByDateRange(startDate, endDate);
    }
    
    /**
     * Completar orden
     */
    public Order completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        order.complete();
        return orderRepository.save(order);
    }
    
    /**
     * Cancelar orden (reversión de operaciones)
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
     * Generar número de orden único
     */
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}
