package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Supplier;
import com.drogueria.bellavista.domain.repository.SupplierRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SupplierService.
 *
 * These tests validate:
 * - supplier creation rules
 * - uniqueness validations
 * - update logic
 * - activation state transitions
 * - repository interaction
 *
 * All dependencies are mocked to isolate service logic.
 */
class SupplierServiceTest {

    private static final Logger log =
            LoggerFactory.getLogger(SupplierServiceTest.class);

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    private Supplier supplier;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        supplier = Supplier.builder()
                .id(1L)
                .code("SUP-01")
                .name("Proveedor Test")
                .email("proveedor@test.com")
                .active(true)
                .build();

        log.info("✔ Setup SupplierServiceTest listo");
    }

    // =============================
    // CREATE
    // =============================

    @Test
    @DisplayName("Debe crear proveedor correctamente")
    void shouldCreateSupplierSuccessfully() {
        log.info("🧪 Test createSupplier SUCCESS");

        when(supplierRepository.existsByCode("SUP-01")).thenReturn(false);
        when(supplierRepository.existsByEmail("proveedor@test.com")).thenReturn(false);
        when(supplierRepository.save(any())).thenReturn(supplier);

        Supplier result = supplierService.createSupplier(supplier);

        assertNotNull(result);
        assertTrue(result.getActive());
        verify(supplierRepository).save(any());

        log.info("✅ Proveedor creado correctamente");
    }

    @Test
    @DisplayName("Debe fallar si código ya existe")
    void shouldFailIfCodeExists() {
        log.info("🧪 Test código duplicado");

        when(supplierRepository.existsByCode("SUP-01")).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> supplierService.createSupplier(supplier));

        log.info("✅ Excepción correcta por código duplicado");
    }

    @Test
    @DisplayName("Debe fallar si email ya existe")
    void shouldFailIfEmailExists() {
        log.info("🧪 Test email duplicado");

        when(supplierRepository.existsByCode("SUP-01")).thenReturn(false);
        when(supplierRepository.existsByEmail("proveedor@test.com")).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> supplierService.createSupplier(supplier));

        log.info("✅ Excepción correcta por email duplicado");
    }

    // =============================
    // UPDATE
    // =============================

    @Test
    @DisplayName("Debe actualizar proveedor correctamente")
    void shouldUpdateSupplier() {
        log.info("🧪 Test updateSupplier");

        Supplier newData = Supplier.builder()
                .code("SUP-01")
                .name("Proveedor Actualizado")
                .email("nuevo@test.com")
                .active(true)
                .build();

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.existsByCode("SUP-01")).thenReturn(false);
        when(supplierRepository.existsByEmail("nuevo@test.com")).thenReturn(false);
        when(supplierRepository.save(any())).thenReturn(supplier);

        Supplier result = supplierService.updateSupplier(1L, newData);

        assertEquals("Proveedor Actualizado", result.getName());
        verify(supplierRepository).save(any());

        log.info("✅ Proveedor actualizado correctamente");
    }

    @Test
    @DisplayName("Debe lanzar excepción si proveedor no existe")
    void shouldThrowIfSupplierNotFound() {
        log.info("🧪 Test supplier not found");

        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> supplierService.updateSupplier(1L, supplier));

        log.info("✅ Excepción correcta por proveedor inexistente");
    }

    // =============================
    // GETTERS
    // =============================

    @Test
    @DisplayName("Debe obtener proveedor por id")
    void shouldGetSupplierById() {
        log.info("🧪 Test getSupplierById");

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        Supplier result = supplierService.getSupplierById(1L);

        assertNotNull(result);
        assertEquals("SUP-01", result.getCode());

        log.info("✅ getSupplierById correcto");
    }

    @Test
    @DisplayName("Debe lanzar excepción si getById no existe")
    void shouldFailGetSupplierById() {
        log.info("🧪 Test getSupplierById NOT FOUND");

        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> supplierService.getSupplierById(1L));

        log.info("✅ Excepción correcta");
    }

    // =============================
    // ACTIVATE / DEACTIVATE
    // =============================

    @Test
    @DisplayName("Debe desactivar proveedor")
    void shouldDeactivateSupplier() {
        log.info("🧪 Test deactivateSupplier");

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any())).thenReturn(supplier);

        Supplier result = supplierService.deactivateSupplier(1L);

        assertFalse(result.getActive());
        verify(supplierRepository).save(supplier);

        log.info("✅ Proveedor desactivado correctamente");
    }

    @Test
    @DisplayName("Debe activar proveedor")
    void shouldActivateSupplier() {
        log.info("🧪 Test activateSupplier");

        supplier.setActive(false);

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any())).thenReturn(supplier);

        Supplier result = supplierService.activateSupplier(1L);

        assertTrue(result.getActive());
        verify(supplierRepository).save(supplier);

        log.info("✅ Proveedor activado correctamente");
    }
}
