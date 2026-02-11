package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.OrderDTO;
import com.drogueria.bellavista.application.mapper.OrderUseCaseMapper;
import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST - Órdenes de Compra
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    
    private final OrderService orderService;
    private final OrderUseCaseMapper mapper;
    
    /**
     * Crear nueva orden
     * POST /orders
     * Validaciones automáticas:
     * - Cliente existe y está activo
     * - Productos existen y tienen stock
     * - Cliente tiene crédito disponible
     */
    @PostMapping
    public ResponseEntity<OrderDTO.Response> createOrder(
            @Valid @RequestBody OrderDTO.CreateRequest request) {
        Order order = mapper.toDomain(request);
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(createdOrder));
    }
    
    /**
     * Obtener orden por ID
     * GET /orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO.Response> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(mapper.toResponse(order));
    }
    
    /**
     * Obtener orden por número
     * GET /orders/number/{orderNumber}
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderDTO.Response> getOrderByOrderNumber(@PathVariable String orderNumber) {
        Order order = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(mapper.toResponse(order));
    }
    
    /**
     * Listar todas las órdenes
     * GET /orders
     */
    @GetMapping
    public ResponseEntity<List<OrderDTO.Response>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderDTO.Response> responses = orders.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Listar órdenes por cliente
     * GET /orders/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO.Response>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        List<OrderDTO.Response> responses = orders.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Listar órdenes por estado
     * GET /orders/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDTO.Response>> getOrdersByStatus(@PathVariable String status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        List<OrderDTO.Response> responses = orders.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Listar órdenes pendientes de un cliente
     * GET /orders/customer/{customerId}/pending
     */
    @GetMapping("/customer/{customerId}/pending")
    public ResponseEntity<List<OrderDTO.Response>> getPendingOrdersByCustomerId(@PathVariable Long customerId) {
        List<Order> orders = orderService.getPendingOrdersByCustomerId(customerId);
        List<OrderDTO.Response> responses = orders.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Completar orden
     * PATCH /orders/{id}/complete
     * Cambia estado a COMPLETED y marca la fecha de entrega
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<OrderDTO.Response> completeOrder(@PathVariable Long id) {
        Order order = orderService.completeOrder(id);
        return ResponseEntity.ok(mapper.toResponse(order));
    }
    
    /**
     * Cancelar orden
     * PATCH /orders/{id}/cancel
     * Revierte:
     * - Stock de productos
     * - Saldo pendiente del cliente
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO.Response> cancelOrder(@PathVariable Long id) {
        Order order = orderService.cancelOrder(id);
        return ResponseEntity.ok(mapper.toResponse(order));
    }
    
    /**
     * Buscar órdenes por rango de fechas
     * GET /orders/search?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
     */
    @GetMapping("/search")
    public ResponseEntity<List<OrderDTO.Response>> searchOrdersByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
        
        List<Order> orders = orderService.getOrdersByDateRange(start, end);
        List<OrderDTO.Response> responses = orders.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }
}
