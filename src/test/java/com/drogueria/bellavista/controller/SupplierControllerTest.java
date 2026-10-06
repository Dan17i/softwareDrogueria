package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.SupplierDTO;
import com.drogueria.bellavista.application.mapper.SupplierUseCaseMapper;
import com.drogueria.bellavista.domain.model.Supplier;
import com.drogueria.bellavista.domain.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SupplierControllerTest {

    @Mock private SupplierService supplierService;
    @Mock private SupplierUseCaseMapper mapper;

    @InjectMocks
    private SupplierController controller;

    private Supplier supplier;
    private SupplierDTO.Response response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        supplier = Supplier.builder().id(1L).code("S-001").name("Proveedor").build();
        response = mock(SupplierDTO.Response.class);
        when(mapper.toResponse(any(Supplier.class))).thenReturn(response);
    }

    @Test
    @DisplayName("createSupplier - 201 con proveedor creado")
    void shouldCreateSupplier() {
        when(mapper.toDomain(any(SupplierDTO.CreateRequest.class))).thenReturn(supplier);
        when(supplierService.createSupplier(supplier)).thenReturn(supplier);

        var result = controller.createSupplier(mock(SupplierDTO.CreateRequest.class));

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertSame(response, result.getBody());
    }

    @Test
    @DisplayName("updateSupplier - 200 con proveedor actualizado")
    void shouldUpdateSupplier() {
        when(mapper.toDomain(any(SupplierDTO.UpdateRequest.class))).thenReturn(supplier);
        when(supplierService.updateSupplier(1L, supplier)).thenReturn(supplier);

        var result = controller.updateSupplier(1L, mock(SupplierDTO.UpdateRequest.class));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertSame(response, result.getBody());
    }

    @Test
    @DisplayName("getSupplierById / ByCode - 200")
    void shouldGetSingleSupplier() {
        when(supplierService.getSupplierById(1L)).thenReturn(supplier);
        when(supplierService.getSupplierByCode("S-001")).thenReturn(supplier);

        assertSame(response, controller.getSupplierById(1L).getBody());
        assertSame(response, controller.getSupplierByCode("S-001").getBody());
    }

    @Test
    @DisplayName("listados - todos y activos")
    void shouldListSuppliers() {
        when(supplierService.getAllSuppliers()).thenReturn(List.of(supplier, supplier));
        when(supplierService.getActiveSuppliers()).thenReturn(List.of(supplier));

        assertEquals(2, controller.getAllSuppliers().getBody().size());
        assertEquals(1, controller.getActiveSuppliers().getBody().size());
    }

    @Test
    @DisplayName("activate / deactivate - delegan al servicio")
    void shouldActivateAndDeactivate() {
        when(supplierService.activateSupplier(1L)).thenReturn(supplier);
        when(supplierService.deactivateSupplier(1L)).thenReturn(supplier);

        assertEquals(HttpStatus.OK, controller.activateSupplier(1L).getStatusCode());
        assertEquals(HttpStatus.OK, controller.deactivateSupplier(1L).getStatusCode());
        verify(supplierService).activateSupplier(1L);
        verify(supplierService).deactivateSupplier(1L);
    }

    @Test
    @DisplayName("seguridad - el controller completo exige rol ADMIN, WAREHOUSE o MANAGER")
    void controllerShouldBeProtected() {
        PreAuthorize pre = SupplierController.class.getAnnotation(PreAuthorize.class);

        assertNotNull(pre);
        assertEquals("hasAnyRole('ADMIN','WAREHOUSE','MANAGER')", pre.value());
    }
}
