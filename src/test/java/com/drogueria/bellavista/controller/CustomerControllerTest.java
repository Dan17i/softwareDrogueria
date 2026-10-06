package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.CustomerDTO;
import com.drogueria.bellavista.application.mapper.CustomerUseCaseMapper;
import com.drogueria.bellavista.domain.model.Customer;
import com.drogueria.bellavista.domain.service.CustomerService;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
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
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock private CustomerService customerService;
    @Mock private CustomerUseCaseMapper mapper;

    @InjectMocks
    private CustomerController controller;

    private Customer customer;
    private CustomerDTO.Response response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = Customer.builder()
            .id(1L).code("C-001").name("Cliente")
            .creditLimit(new BigDecimal("1000")).pendingBalance(new BigDecimal("300"))
            .active(true).build();
        response = mock(CustomerDTO.Response.class);
        when(mapper.toResponse(any(Customer.class))).thenReturn(response);
    }

    @Test
    @DisplayName("createCustomer - 201 con cliente creado")
    void shouldCreateCustomer() {
        when(mapper.toDomain(any(CustomerDTO.CreateRequest.class))).thenReturn(customer);
        when(customerService.createCustomer(customer)).thenReturn(customer);

        ResponseEntity<CustomerDTO.Response> result = controller.createCustomer(mock(CustomerDTO.CreateRequest.class));

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertSame(response, result.getBody());
    }

    @Test
    @DisplayName("updateCustomer - 200 con cliente actualizado")
    void shouldUpdateCustomer() {
        when(mapper.toDomain(any(CustomerDTO.UpdateRequest.class))).thenReturn(customer);
        when(customerService.updateCustomer(1L, customer)).thenReturn(customer);

        ResponseEntity<CustomerDTO.Response> result = controller.updateCustomer(1L, mock(CustomerDTO.UpdateRequest.class));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertSame(response, result.getBody());
    }

    @Test
    @DisplayName("getCustomerById / ByCode / ByEmail - 200")
    void shouldGetSingleCustomer() {
        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(customerService.getCustomerByCode("C-001")).thenReturn(customer);
        when(customerService.getCustomerByEmail("a@b.com")).thenReturn(customer);

        assertSame(response, controller.getCustomerById(1L).getBody());
        assertSame(response, controller.getCustomerByCode("C-001").getBody());
        assertSame(response, controller.getCustomerByEmail("a@b.com").getBody());
    }

    @Test
    @DisplayName("getCustomerById - propaga ResourceNotFoundException")
    void shouldPropagateNotFound() {
        when(customerService.getCustomerById(99L)).thenThrow(new ResourceNotFoundException("Customer", "id", 99L));

        assertThrows(ResourceNotFoundException.class, () -> controller.getCustomerById(99L));
    }

    @Test
    @DisplayName("listados - mapean cada cliente")
    void shouldListCustomers() {
        when(customerService.getAllCustomers()).thenReturn(List.of(customer, customer));
        when(customerService.getActiveCustomers()).thenReturn(List.of(customer));
        when(customerService.getCustomersByType("RETAIL")).thenReturn(List.of(customer));
        when(customerService.getMorosos()).thenReturn(List.of());

        assertEquals(2, controller.getAllCustomers().getBody().size());
        assertEquals(1, controller.getActiveCustomers().getBody().size());
        assertEquals(1, controller.getCustomersByType("RETAIL").getBody().size());
        assertTrue(controller.getMorosos().getBody().isEmpty());
    }

    @Test
    @DisplayName("activate / deactivate - delegan al servicio")
    void shouldActivateAndDeactivate() {
        when(customerService.activateCustomer(1L)).thenReturn(customer);
        when(customerService.deactivateCustomer(1L)).thenReturn(customer);

        assertEquals(HttpStatus.OK, controller.activateCustomer(1L).getStatusCode());
        assertEquals(HttpStatus.OK, controller.deactivateCustomer(1L).getStatusCode());
        verify(customerService).activateCustomer(1L);
        verify(customerService).deactivateCustomer(1L);
    }

    @Test
    @DisplayName("hasCreditAvailable - retorna booleano del servicio")
    void shouldCheckCredit() {
        when(customerService.hasCreditAvailable(1L, BigDecimal.TEN)).thenReturn(true);

        assertTrue(controller.hasCreditAvailable(1L, BigDecimal.TEN).getBody());
    }

    @Test
    @DisplayName("getCustomerBalance - calcula crédito disponible")
    void shouldComputeBalance() {
        when(customerService.getCustomerById(1L)).thenReturn(customer);

        CustomerController.BalanceInfo info = controller.getCustomerBalance(1L).getBody();

        assertEquals(new BigDecimal("700"), info.availableCredit());
        assertEquals(new BigDecimal("1000"), info.creditLimit());
        assertTrue(info.isMoroso());
    }

    @Test
    @DisplayName("getCustomerBalance - saldo pendiente null se trata como cero")
    void shouldTreatNullPendingBalanceAsZero() {
        customer.setPendingBalance(null);
        when(customerService.getCustomerById(1L)).thenReturn(customer);

        CustomerController.BalanceInfo info = controller.getCustomerBalance(1L).getBody();

        assertEquals(new BigDecimal("1000"), info.availableCredit());
        assertFalse(info.isMoroso());
    }

    @Test
    @DisplayName("seguridad - todo endpoint público del controller exige @PreAuthorize")
    void everyEndpointShouldBeProtected() {
        for (Method m : CustomerController.class.getDeclaredMethods()) {
            if (Modifier.isPublic(m.getModifiers()) && !m.isSynthetic()) {
                assertNotNull(m.getAnnotation(PreAuthorize.class), "Sin @PreAuthorize: " + m.getName());
            }
        }
    }
}
