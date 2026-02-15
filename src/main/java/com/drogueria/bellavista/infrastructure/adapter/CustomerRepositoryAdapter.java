package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.domain.model.Customer;
import com.drogueria.bellavista.domain.repository.CustomerRepository;
import com.drogueria.bellavista.infrastructure.mapper.CustomerMapper;
import com.drogueria.bellavista.infrastructure.persistence.CustomerEntity;
import com.drogueria.bellavista.infrastructure.persistence.JpaCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * <h2>CustomerRepositoryAdapter</h2>
 *
 * <p>
 * Implementación del puerto {@link CustomerRepository}
 * utilizando Spring Data JPA como mecanismo de persistencia.
 * </p>
 *
 * <p>
 * Este adaptador pertenece a la capa de infraestructura dentro
 * de la arquitectura hexagonal (Ports & Adapters) y es responsable de:
 * </p>
 *
 * <ul>
 *     <li>Delegar operaciones CRUD al repositorio JPA.</li>
 *     <li>Convertir objetos de dominio {@link Customer} a entidades {@link CustomerEntity}.</li>
 *     <li>Convertir entidades JPA nuevamente a objetos de dominio.</li>
 * </ul>
 *
 * <p>
 * La conversión entre modelo de dominio y modelo de persistencia
 * se realiza mediante {@link CustomerMapper}, garantizando que
 * el dominio permanezca desacoplado de detalles tecnológicos.
 * </p>
 *
 * <p>
 * Esta clase actúa como un puente entre el núcleo del dominio
 * y la infraestructura de base de datos.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {
    /**
     * Repositorio JPA para operaciones de persistencia.
     */
    private final JpaCustomerRepository jpaRepository;
    /**
     * Mapper encargado de convertir entre dominio y entidad.
     */
    private final CustomerMapper mapper;
    /**
     * Guarda o actualiza un cliente en la base de datos.
     *
     * @param customer objeto de dominio a persistir
     * @return cliente persistido convertido nuevamente a dominio
     */
    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = mapper.toEntity(customer);
        CustomerEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    /**
     * Busca un cliente por su identificador único.
     *
     * @param id identificador del cliente
     * @return cliente encontrado envuelto en {@link Optional}
     */
    @Override
    public Optional<Customer> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }
    /**
     * Busca un cliente por su código interno.
     *
     * @param code código del cliente
     * @return cliente encontrado si existe
     */
    @Override
    public Optional<Customer> findByCode(String code) {
        return jpaRepository.findByCode(code)
            .map(mapper::toDomain);
    }
    /**
     * Busca un cliente por su correo electrónico.
     *
     * @param email correo electrónico
     * @return cliente encontrado si existe
     */
    @Override
    public Optional<Customer> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
            .map(mapper::toDomain);
    }
    /**
     * Busca un cliente por su número de documento.
     *
     * @param documentNumber número de documento
     * @return cliente encontrado si existe
     */
    @Override
    public Optional<Customer> findByDocumentNumber(String documentNumber) {
        return jpaRepository.findByDocumentNumber(documentNumber)
            .map(mapper::toDomain);
    }
    /**
     * Obtiene todos los clientes registrados.
     *
     * @return lista de clientes en modelo de dominio
     */
    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAll()
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    /**
     * Obtiene todos los clientes activos.
     *
     * @return lista de clientes activos
     */
    @Override
    public List<Customer> findAllActive() {
        return jpaRepository.findAllActive()
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    /**
     * Busca clientes por tipo (ej. MAYORISTA, MINORISTA).
     *
     * @param customerType tipo de cliente
     * @return lista de clientes que cumplen el criterio
     */
    @Override
    public List<Customer> findByCustomerType(String customerType) {
        return jpaRepository.findByCustomerType(customerType)
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    /**
     * Obtiene clientes con cartera vencida (morosos).
     *
     * @return lista de clientes morosos
     */
    @Override
    public List<Customer> findMorosos() {
        return jpaRepository.findMorosos()
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    /**
     * Elimina un cliente por su ID.
     *
     * @param id identificador del cliente
     */
    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }
    /**
     * Verifica si existe un cliente con el código indicado.
     *
     * @param code código del cliente
     * @return true si existe
     */
    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
    /**
     * Verifica si existe un cliente con el correo indicado.
     *
     * @param email correo electrónico
     * @return true si existe
     */
    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
    /**
     * Verifica si existe un cliente con el número de documento indicado.
     *
     * @param documentNumber número de documento
     * @return true si existe
     */
    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return jpaRepository.existsByDocumentNumber(documentNumber);
    }
}
