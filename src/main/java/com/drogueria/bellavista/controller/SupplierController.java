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
 * Controlador REST - Proveedores
 */
@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SupplierController {
    
    private final SupplierService supplierService;
    private final SupplierUseCaseMapper mapper;
    
    @PostMapping
    public ResponseEntity<SupplierDTO.Response> createSupplier(
            @Valid @RequestBody SupplierDTO.CreateRequest request) {
        Supplier supplier = mapper.toDomain(request);
        Supplier createdSupplier = supplierService.createSupplier(supplier);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(createdSupplier));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO.Response> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierDTO.UpdateRequest request) {
        Supplier supplierData = mapper.toDomain(request);
        Supplier updatedSupplier = supplierService.updateSupplier(id, supplierData);
        return ResponseEntity.ok(mapper.toResponse(updatedSupplier));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO.Response> getSupplierById(@PathVariable Long id) {
        Supplier supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(mapper.toResponse(supplier));
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<SupplierDTO.Response> getSupplierByCode(@PathVariable String code) {
        Supplier supplier = supplierService.getSupplierByCode(code);
        return ResponseEntity.ok(mapper.toResponse(supplier));
    }
    
    @GetMapping
    public ResponseEntity<List<SupplierDTO.Response>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        List<SupplierDTO.Response> responses = suppliers.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/status/active")
    public ResponseEntity<List<SupplierDTO.Response>> getActiveSuppliers() {
        List<Supplier> suppliers = supplierService.getActiveSuppliers();
        List<SupplierDTO.Response> responses = suppliers.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<SupplierDTO.Response> deactivateSupplier(@PathVariable Long id) {
        Supplier supplier = supplierService.deactivateSupplier(id);
        return ResponseEntity.ok(mapper.toResponse(supplier));
    }
    
    @PatchMapping("/{id}/activate")
    public ResponseEntity<SupplierDTO.Response> activateSupplier(@PathVariable Long id) {
        Supplier supplier = supplierService.activateSupplier(id);
        return ResponseEntity.ok(mapper.toResponse(supplier));
    }
}
