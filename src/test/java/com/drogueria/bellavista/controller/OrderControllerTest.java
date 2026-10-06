package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.OrderDTO;
import com.drogueria.bellavista.application.mapper.OrderUseCaseMapper;
import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock private OrderService orderService;
    @Mock private OrderUseCaseMapper mapper;

    @InjectMocks
    private OrderController controller;

    private Order order;
    private OrderDTO.Response response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = Order.builder().id(1L).orderNumber("ORD-1").status("PENDING").build();
        response = mock(OrderDTO.Response.class);
        when(mapper.toResponse(any(Order.class))).thenReturn(response);
    }

    @Test
    @DisplayName("createOrder - 201 con orden creada")
    void shouldCreateOrder() {
        when(mapper.toDomain(any(OrderDTO.CreateRequest.class))).thenReturn(order);
        when(orderService.createOrder(order)).thenReturn(order);

        ResponseEntity<OrderDTO.Response> result = controller.createOrder(mock(OrderDTO.CreateRequest.class));

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertSame(response, result.getBody());
    }

    @Test
    @DisplayName("getOrderById / ByOrderNumber - 200")
    void shouldGetSingleOrder() {
        when(orderService.getOrderById(1L)).thenReturn(order);
        when(orderService.getOrderByOrderNumber("ORD-1")).thenReturn(order);

        assertSame(response, controller.getOrderById(1L).getBody());
        assertSame(response, controller.getOrderByOrderNumber("ORD-1").getBody());
    }

    @Test
    @DisplayName("listados - mapean cada orden")
    void shouldListOrders() {
        when(orderService.getAllOrders()).thenReturn(List.of(order, order));
        when(orderService.getOrdersByCustomerId(5L)).thenReturn(List.of(order));
        when(orderService.getOrdersByStatus("PENDING")).thenReturn(List.of(order));
        when(orderService.getPendingOrdersByCustomerId(5L)).thenReturn(List.of());

        assertEquals(2, controller.getAllOrders().getBody().size());
        assertEquals(1, controller.getOrdersByCustomerId(5L).getBody().size());
        assertEquals(1, controller.getOrdersByStatus("PENDING").getBody().size());
        assertTrue(controller.getPendingOrdersByCustomerId(5L).getBody().isEmpty());
    }

    @Test
    @DisplayName("complete / cancel - delegan al servicio")
    void shouldCompleteAndCancel() {
        when(orderService.completeOrder(1L)).thenReturn(order);
        when(orderService.cancelOrder(1L)).thenReturn(order);

        assertEquals(HttpStatus.OK, controller.completeOrder(1L).getStatusCode());
        assertEquals(HttpStatus.OK, controller.cancelOrder(1L).getStatusCode());
        verify(orderService).completeOrder(1L);
        verify(orderService).cancelOrder(1L);
    }

    @Test
    @DisplayName("searchOrdersByDateRange - cubre el día completo")
    void shouldSearchByDateRange() {
        LocalDateTime start = LocalDateTime.parse("2026-01-01T00:00:00");
        LocalDateTime end = LocalDateTime.parse("2026-01-31T23:59:59");
        when(orderService.getOrdersByDateRange(start, end)).thenReturn(List.of(order));

        List<OrderDTO.Response> body = controller.searchOrdersByDateRange("2026-01-01", "2026-01-31").getBody();

        assertEquals(1, body.size());
    }

    @Test
    @DisplayName("searchOrdersByDateRange - fecha inválida lanza DateTimeParseException")
    void shouldRejectInvalidDate() {
        assertThrows(DateTimeParseException.class,
            () -> controller.searchOrdersByDateRange("no-es-fecha", "2026-01-31"));
    }

    @Test
    @DisplayName("seguridad - todo endpoint público del controller exige @PreAuthorize")
    void everyEndpointShouldBeProtected() {
        for (Method m : OrderController.class.getDeclaredMethods()) {
            if (Modifier.isPublic(m.getModifiers()) && !m.isSynthetic()) {
                assertNotNull(m.getAnnotation(PreAuthorize.class), "Sin @PreAuthorize: " + m.getName());
            }
        }
    }
}
