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

    // =============================
    // CREATE ORDER - ERROR CASES
    // =============================

    @Test
    @DisplayName("No debe crear orden si cliente está inactivo")
    void shouldNotCreateOrderIfCustomerInactive() {
        log.info("🧪 Test createOrder with inactive customer");

        Customer inactiveCustomer = Customer.builder()
                .id(1L)
                .active(false)
                .build();

        when(customerService.getCustomerById(1L)).thenReturn(inactiveCustomer);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> orderService.createOrder(order));

        assertTrue(exception.getMessage().contains("cliente está inactivo"));
        verify(orderRepository, never()).save(any());
        verify(productService, never()).reduceStockInternal(any(), any());
        verify(customerService, never()).increasePendingBalance(any(), any());

        log.info("✅ Orden rechazada por cliente inactivo");
    }

    @Test
    @DisplayName("No debe crear orden si no tiene items")
    void shouldNotCreateOrderIfNoItems() {
        log.info("🧪 Test createOrder with no items");

        Order orderWithoutItems = Order.builder()
                .customerId(1L)
                .items(new ArrayList<>())
                .build();

        when(customerService.getCustomerById(1L)).thenReturn(customer);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> orderService.createOrder(orderWithoutItems));

        assertTrue(exception.getMessage().contains("campo 'items' es obligatorio"));
        verify(orderRepository, never()).save(any());
        verify(productService, never()).reduceStockInternal(any(), any());
        verify(customerService, never()).increasePendingBalance(any(), any());

        log.info("✅ Orden rechazada por falta de items");
    }

    @Test
    @DisplayName("No debe crear orden si producto no está disponible")
    void shouldNotCreateOrderIfProductNotAvailable() {
        log.info("🧪 Test createOrder with unavailable product");

        Product unavailableProduct = new Product();
        unavailableProduct.setId(10L);
        unavailableProduct.setCode("P001");
        unavailableProduct.setName("Producto no disponible");
        unavailableProduct.setActive(false); // No disponible

        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(productService.getProductById(10L)).thenReturn(unavailableProduct);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> orderService.createOrder(order));

        assertTrue(exception.getMessage().contains("no está disponible para la venta"));
        verify(orderRepository, never()).save(any());
        verify(productService, never()).reduceStockInternal(any(), any());
        verify(customerService, never()).increasePendingBalance(any(), any());

        log.info("✅ Orden rechazada por producto no disponible");
    }

    @Test
    @DisplayName("No debe crear orden si stock insuficiente")
    void shouldNotCreateOrderIfInsufficientStock() {
        log.info("🧪 Test createOrder with insufficient stock");

        Product lowStockProduct = new Product();
        lowStockProduct.setId(10L);
        lowStockProduct.setCode("P001");
        lowStockProduct.setName("Producto con poco stock");
        lowStockProduct.setPrice(new BigDecimal("100"));
        lowStockProduct.setStock(1); // Solo 1 disponible
        lowStockProduct.setActive(true);

        OrderItem highQuantityItem = new OrderItem();
        highQuantityItem.setProductId(10L);
        highQuantityItem.setQuantity(5); // Pide 5, pero solo hay 1

        Order orderWithHighQuantity = Order.builder()
                .customerId(1L)
                .items(List.of(highQuantityItem))
                .build();

        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(productService.getProductById(10L)).thenReturn(lowStockProduct);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> orderService.createOrder(orderWithHighQuantity));

        assertTrue(exception.getMessage().contains("Stock insuficiente"));
        assertTrue(exception.getMessage().contains("Disponible: 1"));
        assertTrue(exception.getMessage().contains("solicitado: 5"));
        verify(orderRepository, never()).save(any());
        verify(productService, never()).reduceStockInternal(any(), any());
        verify(customerService, never()).increasePendingBalance(any(), any());

        log.info("✅ Orden rechazada por stock insuficiente");
    }

    @Test
    @DisplayName("No debe crear orden si cliente no tiene crédito suficiente")
    void shouldNotCreateOrderIfInsufficientCredit() {
        log.info("🧪 Test createOrder with insufficient credit");

        Customer lowCreditCustomer = Customer.builder()
                .id(1L)
                .active(true)
                .creditLimit(new BigDecimal("100")) // Solo 100 de límite
                .pendingBalance(BigDecimal.ZERO)
                .build();

        Order expensiveOrder = Order.builder()
                .customerId(1L)
                .items(List.of(item)) // Item cuesta 200 (2 * 100)
                .build();

        when(customerService.getCustomerById(1L)).thenReturn(lowCreditCustomer);
        when(productService.getProductById(10L)).thenReturn(product);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> orderService.createOrder(expensiveOrder));

        assertTrue(exception.getMessage().contains("no tiene crédito suficiente"));
        assertTrue(exception.getMessage().contains("Límite: 100"));
        verify(orderRepository, never()).save(any());
        verify(productService, never()).reduceStockInternal(any(), any());
        verify(customerService, never()).increasePendingBalance(any(), any());

        log.info("✅ Orden rechazada por crédito insuficiente");
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

    @Test
    @DisplayName("No debe completar orden inexistente")
    void shouldNotCompleteNonExistentOrder() {
        log.info("🧪 Test completeOrder with non-existent order");

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.completeOrder(999L));

        assertTrue(exception.getMessage().contains("Order"));
        verify(orderRepository, never()).save(any());

        log.info("✅ Completar orden inexistente lanza excepción correcta");
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
    @DisplayName("No debe cancelar orden inexistente")
    void shouldNotCancelNonExistentOrder() {
        log.info("🧪 Test cancelOrder with non-existent order");

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.cancelOrder(999L));

        assertTrue(exception.getMessage().contains("Order"));
        verify(orderRepository, never()).save(any());
        verify(productService, never()).increaseStockInternal(any(), any());
        verify(customerService, never()).reducePendingBalance(any(), any());

        log.info("✅ Cancelar orden inexistente lanza excepción correcta");
    }

    @Test
    @DisplayName("Debe obtener orden por número correctamente")
    void shouldGetOrderByOrderNumber() {
        log.info("🧪 Test getOrderByOrderNumber");

        when(orderRepository.findByOrderNumber("ORD-123")).thenReturn(Optional.of(order));

        Order result = orderService.getOrderByOrderNumber("ORD-123");

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        verify(orderRepository).findByOrderNumber("ORD-123");

        log.info("✅ Obtener orden por número funciona correctamente");
    }

    @Test
    @DisplayName("Debe lanzar excepción si orden por número no existe")
    void shouldThrowIfOrderByNumberNotFound() {
        log.info("🧪 Test getOrderByOrderNumber not found");

        when(orderRepository.findByOrderNumber("ORD-INEXISTENTE")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.getOrderByOrderNumber("ORD-INEXISTENTE"));

        assertTrue(exception.getMessage().contains("Order"));
        assertTrue(exception.getMessage().contains("orderNumber"));

        log.info("✅ Excepción correcta por orden inexistente por número");
    }
}