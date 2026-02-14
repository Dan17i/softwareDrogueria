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
 * Controlador REST para la gestión de órdenes de compra (Orders).
 * <p>
 * Expone endpoints para crear, consultar, listar, completar, cancelar y buscar órdenes de compra.
 * Funciona como adaptador de entrada en la arquitectura hexagonal, delegando la lógica de negocio
 * a {@link OrderService}.
 * </p>
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    
    private final OrderService orderService;
    private final OrderUseCaseMapper mapper;
    /**
     * Crea una nueva orden de compra.
     * <p>
     * Validaciones automáticas:
     * <ul>
     *   <li>Cliente existe y está activo</li>
     *   <li>Productos existen y tienen stock suficiente</li>
     *   <li>Cliente tiene crédito disponible</li>
     * </ul>
     *
     * @param request DTO con los datos de la orden a crear
     * @return {@link ResponseEntity} con {@link OrderDTO.Response} de la orden creada
     */
    @PostMapping
    public ResponseEntity<OrderDTO.Response> createOrder(
            @Valid @RequestBody OrderDTO.CreateRequest request) {
        Order order = mapper.toDomain(request);
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(createdOrder));
    }

    /**
     * Obtiene una orden por su ID.
     *
     * @param id Identificador de la orden
     * @return {@link ResponseEntity} con {@link OrderDTO.Response} de la orden
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO.Response> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(mapper.toResponse(order));
    }

    /**
     * Obtiene una orden por su número de orden.
     *
     * @param orderNumber Número único de la orden
     * @return {@link ResponseEntity} con {@link OrderDTO.Response} de la orden
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderDTO.Response> getOrderByOrderNumber(@PathVariable String orderNumber) {
        Order order = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(mapper.toResponse(order));
    }

    /**
     * Lista todas las órdenes de compra.
     *
     * @return {@link ResponseEntity} con lista de {@link OrderDTO.Response}
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
     * Lista las órdenes de un cliente específico.
     *
     * @param customerId ID del cliente
     * @return {@link ResponseEntity} con lista de {@link OrderDTO.Response} del cliente
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
     * Lista órdenes por estado (PENDING, COMPLETED, CANCELLED, etc.).
     *
     * @param status Estado de la orden
     * @return {@link ResponseEntity} con lista de {@link OrderDTO.Response}
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
     * Lista las órdenes pendientes de un cliente específico.
     *
     * @param customerId ID del cliente
     * @return {@link ResponseEntity} con lista de {@link OrderDTO.Response} pendientes
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
     * Completa una orden de compra.
     * <p>
     * Cambia el estado a COMPLETED y marca la fecha de entrega.
     *
     * @param id ID de la orden
     * @return {@link ResponseEntity} con {@link OrderDTO.Response} de la orden completada
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<OrderDTO.Response> completeOrder(@PathVariable Long id) {
        Order order = orderService.completeOrder(id);
        return ResponseEntity.ok(mapper.toResponse(order));
    }
    /**
     * Cancela una orden de compra.
     * <p>
     * Revierte los efectos de la orden:
     * <ul>
     *   <li>Stock de productos</li>
     *   <li>Saldo pendiente del cliente</li>
     * </ul>
     *
     * @param id ID de la orden
     * @return {@link ResponseEntity} con {@link OrderDTO.Response} de la orden cancelada
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO.Response> cancelOrder(@PathVariable Long id) {
        Order order = orderService.cancelOrder(id);
        return ResponseEntity.ok(mapper.toResponse(order));
    }

    /**
     * Busca órdenes por rango de fechas.
     *
     * @param startDate Fecha inicial en formato YYYY-MM-DD
     * @param endDate Fecha final en formato YYYY-MM-DD
     * @return {@link ResponseEntity} con lista de {@link OrderDTO.Response} que cumplen el rango
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
