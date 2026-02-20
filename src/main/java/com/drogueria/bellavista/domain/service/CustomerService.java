package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Customer;
import com.drogueria.bellavista.domain.repository.CustomerRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de dominio - Casos de uso de Clientes
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    /**
     * Crear un nuevo cliente
     */
    public Customer createCustomer(Customer customer) {
        // Validar que no exista un cliente con el mismo código
        if (customerRepository.existsByCode(customer.getCode())) {
            throw new BusinessException("Ya existe un cliente con el código: " + customer.getCode());
        }
        
        // Validar email único si se proporciona
        if (customer.getEmail() != null && customerRepository.existsByEmail(customer.getEmail())) {
            throw new BusinessException("Ya existe un cliente con el email: " + customer.getEmail());
        }
        
        // Validar documento único
        if (customer.getDocumentNumber() != null && customerRepository.existsByDocumentNumber(customer.getDocumentNumber())) {
            throw new BusinessException("Ya existe un cliente con este número de documento");
        }
        
        // Establecer valores por defecto
        customer.setActive(true);
        customer.setPendingBalance(BigDecimal.ZERO);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        
        return customerRepository.save(customer);
    }
    
    /**
     * Actualizar un cliente existente
     */
    public Customer updateCustomer(Long id, Customer customerData) {
        Customer existingCustomer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        // Validar código único si cambió
        if (!existingCustomer.getCode().equals(customerData.getCode()) 
            && customerRepository.existsByCode(customerData.getCode())) {
            throw new BusinessException("Ya existe un cliente con el código: " + customerData.getCode());
        }
        
        // Validar email único si cambió
        if (customerData.getEmail() != null 
            && !existingCustomer.getEmail().equals(customerData.getEmail())
            && customerRepository.existsByEmail(customerData.getEmail())) {
            throw new BusinessException("Ya existe un cliente con el email: " + customerData.getEmail());
        }
        
        // Actualizar campos
        existingCustomer.setCode(customerData.getCode());
        existingCustomer.setName(customerData.getName());
        existingCustomer.setEmail(customerData.getEmail());
        existingCustomer.setPhone(customerData.getPhone());
        existingCustomer.setAddress(customerData.getAddress());
        existingCustomer.setCity(customerData.getCity());
        existingCustomer.setPostalCode(customerData.getPostalCode());
        existingCustomer.setDocumentNumber(customerData.getDocumentNumber());
        existingCustomer.setDocumentType(customerData.getDocumentType());
        existingCustomer.setCustomerType(customerData.getCustomerType());
        existingCustomer.setCreditLimit(customerData.getCreditLimit());
        existingCustomer.setActive(customerData.getActive());
        existingCustomer.setUpdatedAt(LocalDateTime.now());
        
        return customerRepository.save(existingCustomer);
    }
    
    /**
     * Obtener cliente por ID
     */
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
    }
    
    /**
     * Obtener cliente por código
     */
    @Transactional(readOnly = true)
    public Customer getCustomerByCode(String code) {
        return customerRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "code", code));
    }
    
    /**
     * Obtener cliente por email
     */
    @Transactional(readOnly = true)
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));
    }
    
    /**
     * Listar todos los clientes
     */
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    /**
     * Listar clientes activos
     */
    @Transactional(readOnly = true)
    public List<Customer> getActiveCustomers() {
        return customerRepository.findAllActive();
    }
    
    /**
     * Listar clientes por tipo
     */
    @Transactional(readOnly = true)
    public List<Customer> getCustomersByType(String customerType) {
        return customerRepository.findByCustomerType(customerType);
    }
    
    /**
     * Listar clientes morosos
     */
    @Transactional(readOnly = true)
    public List<Customer> getMorosos() {
        return customerRepository.findMorosos();
    }
    
    /**
     * Aumentar saldo pendiente (cuando hace una compra)
     */
    public Customer increasePendingBalance(Long customerId, BigDecimal amount) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        try {
            customer.increasePendingBalance(amount);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(e.getMessage());
        }
        return customerRepository.save(customer);
    }
    
    /**
     * Reducir saldo pendiente (cuando hace un pago)
     */
    public Customer reducePendingBalance(Long customerId, BigDecimal amount) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        try {
            customer.reducePendingBalance(amount);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new BusinessException(e.getMessage());
        }
        return customerRepository.save(customer);
    }
    
    /**
     * Validar si tiene crédito disponible
     */
    @Transactional(readOnly = true)
    public boolean hasCreditAvailable(Long customerId, BigDecimal amount) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        
        return customer.hasCreditAvailable(amount);
    }
    
    /**
     * Desactivar cliente
     */
    public Customer deactivateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        customer.setActive(false);
        customer.setUpdatedAt(LocalDateTime.now());
        
        return customerRepository.save(customer);
    }
    
    /**
     * Activar cliente
     */
    public Customer activateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        customer.setActive(true);
        customer.setUpdatedAt(LocalDateTime.now());
        
        return customerRepository.save(customer);
    }
}
