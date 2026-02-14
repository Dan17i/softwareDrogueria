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
 * Servicio de dominio responsable de la lógica de negocio asociada a los clientes.
 *
 * <p>Este servicio encapsula los casos de uso del dominio relacionados con:
 * creación, actualización, consulta, control de crédito y gestión del estado
 * de los clientes.</p>
 *
 * <p>Garantiza las invariantes del dominio, como unicidad de código, email
 * y documento, así como el manejo del saldo pendiente y validación de crédito.</p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    /**
     * Crea un nuevo cliente en el sistema.
     *
     * <p>Valida:
     * <ul>
     *     <li>Unicidad del código</li>
     *     <li>Unicidad del email (si se proporciona)</li>
     *     <li>Unicidad del documento</li>
     * </ul>
     * Además establece valores iniciales del cliente.</p>
     *
     * @param customer entidad cliente a crear
     * @return cliente persistido
     * @throws BusinessException si se violan reglas de negocio
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
     * Actualiza la información de un cliente existente.
     *
     * <p>Valida que el código y el email sigan siendo únicos en caso
     * de haber sido modificados.</p>
     *
     * @param id identificador del cliente
     * @param customerData nuevos datos del cliente
     * @return cliente actualizado
     * @throws ResourceNotFoundException si el cliente no existe
     * @throws BusinessException si se violan reglas de unicidad
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
     * Obtiene un cliente por su identificador.
     *
     * @param id identificador del cliente
     * @return cliente encontrado
     * @throws ResourceNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
    }
    /**
     * Obtiene un cliente por su código único.
     *
     * @param code código del cliente
     * @return cliente encontrado
     * @throws ResourceNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public Customer getCustomerByCode(String code) {
        return customerRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "code", code));
    }
    /**
     * Obtiene un cliente por su email.
     *
     * @param email correo electrónico
     * @return cliente encontrado
     * @throws ResourceNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));
    }
    /**
     * Retorna todos los clientes registrados.
     *
     * @return lista de clientes
     */
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    /**
     * Retorna únicamente los clientes activos.
     *
     * @return lista de clientes activos
     */
    @Transactional(readOnly = true)
    public List<Customer> getActiveCustomers() {
        return customerRepository.findAllActive();
    }
    /**
     * Obtiene clientes filtrados por tipo.
     *
     * @param customerType tipo de cliente
     * @return lista filtrada
     */
    @Transactional(readOnly = true)
    public List<Customer> getCustomersByType(String customerType) {
        return customerRepository.findByCustomerType(customerType);
    }
    /**
     * Retorna los clientes con saldo pendiente vencido o alto riesgo.
     *
     * @return lista de clientes morosos
     */
    @Transactional(readOnly = true)
    public List<Customer> getMorosos() {
        return customerRepository.findMorosos();
    }
    /**
     * Incrementa el saldo pendiente del cliente.
     * Se utiliza cuando el cliente realiza una compra a crédito.
     *
     * @param customerId identificador del cliente
     * @param amount monto a aumentar
     * @return cliente actualizado
     */
    public Customer increasePendingBalance(Long customerId, BigDecimal amount) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        
        customer.increasePendingBalance(amount);
        return customerRepository.save(customer);
    }
    /**
     * Reduce el saldo pendiente del cliente.
     * Se utiliza cuando el cliente realiza un pago.
     *
     * @param customerId identificador del cliente
     * @param amount monto a reducir
     * @return cliente actualizado
     */
    public Customer reducePendingBalance(Long customerId, BigDecimal amount) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        
        customer.reducePendingBalance(amount);
        return customerRepository.save(customer);
    }
    /**
     * Verifica si el cliente tiene crédito disponible para una compra.
     *
     * @param customerId identificador del cliente
     * @param amount monto a validar
     * @return true si puede comprar, false si supera el límite
     */
    @Transactional(readOnly = true)
    public boolean hasCreditAvailable(Long customerId, BigDecimal amount) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        
        return customer.hasCreditAvailable(amount);
    }
    /**
     * Desactiva un cliente en el sistema.
     *
     * @param id identificador del cliente
     * @return cliente actualizado
     */
    public Customer deactivateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        customer.setActive(false);
        customer.setUpdatedAt(LocalDateTime.now());
        
        return customerRepository.save(customer);
    }
    /**
     * Activa nuevamente un cliente.
     *
     * @param id identificador del cliente
     * @return cliente actualizado
     */
    public Customer activateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        customer.setActive(true);
        customer.setUpdatedAt(LocalDateTime.now());
        
        return customerRepository.save(customer);
    }
}
