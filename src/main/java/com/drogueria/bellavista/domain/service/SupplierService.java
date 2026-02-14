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
 * Servicio de dominio encargado de la gestión de proveedores.
 *
 * <p>Centraliza la lógica de negocio relacionada con:</p>
 * <ul>
 *     <li>Registro y actualización de proveedores</li>
 *     <li>Validaciones de unicidad (código y email)</li>
 *     <li>Consultas por distintos criterios</li>
 *     <li>Activación y desactivación</li>
 * </ul>
 *
 * <p>Las operaciones de escritura se ejecutan dentro de una transacción
 * para garantizar consistencia en los datos.</p>
 */@Service
@RequiredArgsConstructor
@Transactional
public class SupplierService {
    
    private final SupplierRepository supplierRepository;
    /**
     * Crea un nuevo proveedor en el sistema.
     *
     * <p>Reglas de negocio:</p>
     * <ul>
     *     <li>El código del proveedor debe ser único</li>
     *     <li>El email debe ser único si se proporciona</li>
     *     <li>Se establecen valores iniciales por defecto</li>
     * </ul>
     *
     * @param supplier proveedor a registrar
     * @return proveedor guardado
     * @throws BusinessException si el código o email ya existen
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
     * Actualiza los datos de un proveedor existente.
     *
     * <p>Reglas de negocio:</p>
     * <ul>
     *     <li>El proveedor debe existir</li>
     *     <li>El código debe mantenerse único</li>
     *     <li>El email debe mantenerse único si se modifica</li>
     * </ul>
     *
     * @param id identificador del proveedor
     * @param supplierData datos nuevos del proveedor
     * @return proveedor actualizado
     * @throws ResourceNotFoundException si el proveedor no existe
     * @throws BusinessException si código o email ya están en uso
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
     * Obtiene un proveedor por su ID.
     *
     * @param id identificador del proveedor
     * @return proveedor encontrado
     * @throws ResourceNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));
    }
    /**
     * Obtiene un proveedor por su código.
     *
     * @param code código del proveedor
     * @return proveedor encontrado
     * @throws ResourceNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public Supplier getSupplierByCode(String code) {
        return supplierRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier", "code", code));
    }
    /**
     * Lista todos los proveedores registrados.
     */
    @Transactional(readOnly = true)
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
    /**
     * Lista únicamente los proveedores activos.
     */
    @Transactional(readOnly = true)
    public List<Supplier> getActiveSuppliers() {
        return supplierRepository.findAllActive();
    }
    /**
     * Desactiva un proveedor.
     *
     * @param id identificador del proveedor
     * @return proveedor actualizado
     * @throws ResourceNotFoundException si no existe
     */
    public Supplier deactivateSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));
        
        supplier.setActive(false);
        supplier.setUpdatedAt(LocalDateTime.now());
        
        return supplierRepository.save(supplier);
    }
    /**
     * Activa un proveedor previamente desactivado.
     *
     * @param id identificador del proveedor
     * @return proveedor actualizado
     * @throws ResourceNotFoundException si no existe
     */
    public Supplier activateSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));
        
        supplier.setActive(true);
        supplier.setUpdatedAt(LocalDateTime.now());
        
        return supplierRepository.save(supplier);
    }
}
