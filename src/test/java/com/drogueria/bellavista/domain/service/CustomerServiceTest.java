package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Customer;
import com.drogueria.bellavista.domain.repository.CustomerRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomerService.
 *
 * These tests validate business rules, repository interaction,
 * and exception handling for customer operations.
 *
 * This class uses Mockito to isolate the domain service
 * and ensure only business logic is tested.
 */
class CustomerServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        customer = Customer.builder()
                .id(1L)
                .code("C001")
                .name("Cliente Test")
                .email("test@mail.com")
                .documentNumber("123")
                .creditLimit(new BigDecimal("1000"))
                .pendingBalance(BigDecimal.ZERO)
                .active(true)
                .build();

        log.info("✔ Setup inicial completado");
    }

    @Test
    @DisplayName("Debe crear cliente correctamente")
    void shouldCreateCustomerSuccessfully() {
        log.info("🧪 Iniciando test: createCustomer success");

        when(customerRepository.existsByCode(any())).thenReturn(false);
        when(customerRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.existsByDocumentNumber(any())).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(customer);

        Customer result = customerService.createCustomer(customer);

        assertNotNull(result);
        verify(customerRepository).save(any());

        log.info("✅ Cliente creado correctamente");
    }

    @Test
    @DisplayName("Debe fallar si código ya existe")
    void shouldThrowIfCodeExists() {
        log.info("🧪 Iniciando test: código duplicado");

        when(customerRepository.existsByCode("C001")).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> customerService.createCustomer(customer));

        log.info("✅ Excepción lanzada correctamente por código duplicado");
    }

    @Test
    @DisplayName("Debe obtener cliente por ID")
    void shouldGetCustomerById() {
        log.info("🧪 Iniciando test: getCustomerById");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(1L);

        assertEquals("C001", result.getCode());

        log.info("✅ Cliente encontrado correctamente");
    }

    @Test
    @DisplayName("Debe lanzar excepción si cliente no existe")
    void shouldThrowIfCustomerNotFound() {
        log.info("🧪 Iniciando test: cliente no existe");

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> customerService.getCustomerById(1L));

        log.info("✅ Excepción lanzada correctamente");
    }

    @Test
    @DisplayName("Debe aumentar saldo correctamente")
    void shouldIncreasePendingBalance() {
        log.info("🧪 Iniciando test: increasePendingBalance");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);

        Customer result = customerService.increasePendingBalance(1L, new BigDecimal("200"));

        assertTrue(result.getPendingBalance().compareTo(new BigDecimal("200")) >= 0);

        log.info("✅ Saldo aumentado correctamente");
    }

    @Test
    @DisplayName("Debe reducir saldo correctamente")
    void shouldReducePendingBalance() {
        log.info("🧪 Iniciando test: reducePendingBalance");

        customer.setPendingBalance(new BigDecimal("500"));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);

        Customer result = customerService.reducePendingBalance(1L, new BigDecimal("200"));

        assertEquals(new BigDecimal("300"), result.getPendingBalance());

        log.info("✅ Saldo reducido correctamente");
    }

    @Test
    @DisplayName("Debe desactivar cliente")
    void shouldDeactivateCustomer() {
        log.info("🧪 Iniciando test: deactivateCustomer");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);

        Customer result = customerService.deactivateCustomer(1L);

        assertFalse(result.getActive());

        log.info("✅ Cliente desactivado correctamente");
    }

    @Test
    @DisplayName("Debe activar cliente")
    void shouldActivateCustomer() {
        log.info("🧪 Iniciando test: activateCustomer");

        customer.setActive(false);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);

        Customer result = customerService.activateCustomer(1L);

        assertTrue(result.getActive());

        log.info("✅ Cliente activado correctamente");
    }
}
