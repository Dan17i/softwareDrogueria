package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.SupplierDTO;
import com.drogueria.bellavista.application.mapper.SupplierUseCaseMapper;
import com.drogueria.bellavista.domain.model.Supplier;
import com.drogueria.bellavista.domain.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Controlador REST para la gestión de proveedores.
 * <p>
 * Expone endpoints para crear, actualizar, consultar, listar y activar/desactivar proveedores.
 * Funciona como puerto de entrada (Input Adapter) delegando la lógica de negocio a {@link SupplierService}.
 * </p>
 */
@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SupplierController {
    
    private final SupplierService supplierService;
    private final SupplierUseCaseMapper mapper;
    /**
     * Crea un nuevo proveedor.
     * POST /suppliers
     *
     * @param request DTO con los datos del proveedor a crear
     * @return {@link ResponseEntity} con {@link SupplierDTO.Response} del proveedor creado
     */
    @PostMapping
    public ResponseEntity<SupplierDTO.Response> createSupplier(
            @Valid @RequestBody SupplierDTO.CreateRequest request) {
        Supplier supplier = mapper.toDomain(request);
        Supplier createdSupplier = supplierService.createSupplier(supplier);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(createdSupplier));
    }
    /**
     * Actualiza un proveedor existente.
     * PUT /suppliers/{id}
     *
     * @param id ID del proveedor a actualizar
     * @param request DTO con los datos a actualizar
     * @return {@link ResponseEntity} con {@link SupplierDTO.Response} del proveedor actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO.Response> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierDTO.UpdateRequest request) {
        Supplier supplierData = mapper.toDomain(request);
        Supplier updatedSupplier = supplierService.updateSupplier(id, supplierData);
        return ResponseEntity.ok(mapper.toResponse(updatedSupplier));
    }
    /**
     * Obtiene un proveedor por su ID.
     * GET /suppliers/{id}
     *
     * @param id ID del proveedor
     * @return {@link ResponseEntity} con {@link SupplierDTO.Response} del proveedor
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO.Response> getSupplierById(@PathVariable Long id) {
        Supplier supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(mapper.toResponse(supplier));
    }
    /**
     * Obtiene un proveedor por su código.
     * GET /suppliers/code/{code}
     *
     * @param code Código único del proveedor
     * @return {@link ResponseEntity} con {@link SupplierDTO.Response} del proveedor
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<SupplierDTO.Response> getSupplierByCode(@PathVariable String code) {
        Supplier supplier = supplierService.getSupplierByCode(code);
        return ResponseEntity.ok(mapper.toResponse(supplier));
    }
    /**
     * Lista todos los proveedores.
     * GET /suppliers
     *
     * @return {@link ResponseEntity} con lista de {@link SupplierDTO.Response}
     */
    @GetMapping
    public ResponseEntity<List<SupplierDTO.Response>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        List<SupplierDTO.Response> responses = suppliers.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    /**
     * Lista todos los proveedores activos.
     * GET /suppliers/status/active
     *
     * @return {@link ResponseEntity} con lista de {@link SupplierDTO.Response} activos
     */
    @GetMapping("/status/active")
    public ResponseEntity<List<SupplierDTO.Response>> getActiveSuppliers() {
        List<Supplier> suppliers = supplierService.getActiveSuppliers();
        List<SupplierDTO.Response> responses = suppliers.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    /**
     * Desactiva un proveedor.
     * PATCH /suppliers/{id}/deactivate
     *
     * @param id ID del proveedor
     * @return {@link ResponseEntity} con {@link SupplierDTO.Response} actualizado
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<SupplierDTO.Response> deactivateSupplier(@PathVariable Long id) {
        Supplier supplier = supplierService.deactivateSupplier(id);
        return ResponseEntity.ok(mapper.toResponse(supplier));
    }
    /**
     * Activa un proveedor.
     * PATCH /suppliers/{id}/activate
     *
     * @param id ID del proveedor
     * @return {@link ResponseEntity} con {@link SupplierDTO.Response} actualizado
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<SupplierDTO.Response> activateSupplier(@PathVariable Long id) {
        Supplier supplier = supplierService.activateSupplier(id);
        return ResponseEntity.ok(mapper.toResponse(supplier));
    }
}
