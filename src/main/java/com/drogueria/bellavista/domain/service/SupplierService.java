package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Supplier;
import com.drogueria.bellavista.domain.repository.SupplierRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de dominio - Casos de uso de Proveedores
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SupplierService {
    
    private final SupplierRepository supplierRepository;
    
    /**
     * Crear nuevo proveedor
     */
    public Supplier createSupplier(Supplier supplier) {
        if (supplierRepository.existsByCode(supplier.getCode())) {
            throw new BusinessException("Ya existe un proveedor con el código: " + supplier.getCode());
        }
        
        if (supplier.getEmail() != null && supplierRepository.existsByEmail(supplier.getEmail())) {
            throw new BusinessException("Ya existe un proveedor con el email: " + supplier.getEmail());
        }
        
        supplier.setActive(true);
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        
        return supplierRepository.save(supplier);
    }
    
    /**
     * Actualizar proveedor
     */
    public Supplier updateSupplier(Long id, Supplier supplierData) {
        Supplier existingSupplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));
        
        if (!existingSupplier.getCode().equals(supplierData.getCode()) 
            && supplierRepository.existsByCode(supplierData.getCode())) {
            throw new BusinessException("Ya existe un proveedor con el código: " + supplierData.getCode());
        }
        
        if (supplierData.getEmail() != null 
            && !existingSupplier.getEmail().equals(supplierData.getEmail())
            && supplierRepository.existsByEmail(supplierData.getEmail())) {
            throw new BusinessException("Ya existe un proveedor con el email: " + supplierData.getEmail());
        }
        
        existingSupplier.setCode(supplierData.getCode());
        existingSupplier.setName(supplierData.getName());
        existingSupplier.setEmail(supplierData.getEmail());
        existingSupplier.setPhone(supplierData.getPhone());
        existingSupplier.setAddress(supplierData.getAddress());
        existingSupplier.setCity(supplierData.getCity());
        existingSupplier.setPostalCode(supplierData.getPostalCode());
        existingSupplier.setDocumentNumber(supplierData.getDocumentNumber());
        existingSupplier.setDocumentType(supplierData.getDocumentType());
        existingSupplier.setLeadTimeDays(supplierData.getLeadTimeDays());
        existingSupplier.setAveragePaymentDelay(supplierData.getAveragePaymentDelay());
        existingSupplier.setActive(supplierData.getActive());
        existingSupplier.setUpdatedAt(LocalDateTime.now());
        
        return supplierRepository.save(existingSupplier);
    }
    
    /**
     * Obtener proveedor por ID
     */
    @Transactional(readOnly = true)
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));
    }
    
    /**
     * Obtener proveedor por código
     */
    @Transactional(readOnly = true)
    public Supplier getSupplierByCode(String code) {
        return supplierRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier", "code", code));
    }
    
    /**
     * Listar todos
     */
    @Transactional(readOnly = true)
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
    
    /**
     * Listar activos
     */
    @Transactional(readOnly = true)
    public List<Supplier> getActiveSuppliers() {
        return supplierRepository.findAllActive();
    }
    
    /**
     * Desactivar
     */
    public Supplier deactivateSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));
        
        supplier.setActive(false);
        supplier.setUpdatedAt(LocalDateTime.now());
        
        return supplierRepository.save(supplier);
    }
    
    /**
     * Activar
     */
    public Supplier activateSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));
        
        supplier.setActive(true);
        supplier.setUpdatedAt(LocalDateTime.now());
        
        return supplierRepository.save(supplier);
    }
}
