package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Customer;
import com.drogueria.bellavista.domain.model.OrderItem;
import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.domain.model.Order;
import com.drogueria.bellavista.domain.repository.OrderRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderService.
 *
 * These tests validate orchestration logic:
 * - business validations
 * - dependency interaction
 * - state transitions
 * - stock and balance side effects
 *
 * All dependencies are mocked to isolate domain behavior.
 */
class OrderServiceTest {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceTest.class);

    @Mock private OrderRepository orderRepository;
    @Mock private CustomerService customerService;
    @Mock private ProductService productService;

    @InjectMocks private OrderService orderService;

    private Order order;
    private Customer customer;
    private Product product;
    private OrderItem item;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        customer = Customer.builder()
                .id(1L)
                .active(true)
                .creditLimit(new BigDecimal("1000"))
                .pendingBalance(BigDecimal.ZERO)
                .build();

        product = new Product();
        product.setId(10L);
        product.setCode("P001");
        product.setName("Producto test");
        product.setPrice(new BigDecimal("100"));
        product.setStock(50);
        product.setActive(true);

        item = new OrderItem();
        item.setProductId(10L);
        item.setQuantity(2);

        order = Order.builder()
                .id(1L)
                .customerId(1L)
                .status("PENDING")
                .items(new ArrayList<>(List.of(item)))
                .build();

        log.info("✔ Setup OrderServiceTest listo");
    }

    // =============================
    // CREATE ORDER
    // =============================

    @Test
    @DisplayName("Debe crear orden correctamente")
    void shouldCreateOrderSuccessfully() {
        log.info("🧪 Test createOrder SUCCESS");

        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(productService.getProductById(10L)).thenReturn(product);
        when(orderRepository.save(any())).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertNotNull(result);
        verify(orderRepository).save(any());
        verify(productService).reduceStockInternal(10L, 2);
        verify(customerService).increasePendingBalance(eq(1L), any());

        log.info("✅ Orden creada correctamente");
    }

    @Test
    @DisplayName("Debe fallar si cliente está inactivo")
    void shouldFailIfCustomerInactive() {
        log.info("🧪 Test cliente inactivo");

        customer.setActive(false);
        when(customerService.getCustomerById(1L)).thenReturn(customer);

        assertThrows(BusinessException.class,
                () -> orderService.createOrder(order));

        log.info("✅ Excepción correcta por cliente inactivo");
    }

    @Test
    @DisplayName("Debe fallar si no tiene items")
    void shouldFailIfNoItems() {
        log.info("🧪 Test sin items");

        order.setItems(Collections.emptyList());
        when(customerService.getCustomerById(1L)).thenReturn(customer);

        assertThrows(BusinessException.class,
                () -> orderService.createOrder(order));

        log.info("✅ Excepción correcta por orden sin items");
    }

    @Test
    @DisplayName("Debe fallar si stock insuficiente")
    void shouldFailIfStockInsufficient() {
        log.info("🧪 Test stock insuficiente");

        product.setStock(1);

        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(productService.getProductById(10L)).thenReturn(product);

        assertThrows(BusinessException.class,
                () -> orderService.createOrder(order));

        log.info("✅ Excepción correcta por stock insuficiente");
    }

    @Test
    @DisplayName("Debe fallar si cliente sin crédito")
    void shouldFailIfNoCredit() {
        log.info("🧪 Test sin crédito");

        customer.setCreditLimit(new BigDecimal("50"));

        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(productService.getProductById(10L)).thenReturn(product);

        assertThrows(BusinessException.class,
                () -> orderService.createOrder(order));

        log.info("✅ Excepción correcta por crédito insuficiente");
    }

    // =============================
    // COMPLETE ORDER
    // =============================

    @Test
    @DisplayName("Debe completar orden correctamente")
    void shouldCompleteOrder() {
        log.info("🧪 Test completeOrder");

        order.setTotal(new BigDecimal("100"));
        order.setStatus("PENDING");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        Order result = orderService.completeOrder(1L);

        assertEquals("COMPLETED", result.getStatus());
        verify(orderRepository).save(order);

        log.info("✅ Orden completada correctamente");
    }

    // =============================
    // CANCEL ORDER
    // =============================

    @Test
    @DisplayName("Debe cancelar orden pendiente y revertir operaciones")
    void shouldCancelPendingOrder() {
        log.info("🧪 Test cancelOrder PENDING");

        order.setTotal(new BigDecimal("200"));
        order.setStatus("PENDING");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        Order result = orderService.cancelOrder(1L);

        assertEquals("CANCELLED", result.getStatus());

        verify(productService).increaseStockInternal(10L, 2);
        verify(customerService).reducePendingBalance(1L, new BigDecimal("200"));

        log.info("✅ Orden cancelada y revertida correctamente");
    }

    @Test
    @DisplayName("Debe lanzar excepción si orden no existe")
    void shouldThrowIfOrderNotFound() {
        log.info("🧪 Test order not found");

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.getOrderById(1L));

        log.info("✅ Excepción correcta por orden inexistente");
    }
}