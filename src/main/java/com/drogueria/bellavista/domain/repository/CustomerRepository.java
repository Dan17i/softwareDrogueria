package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.Customer;

import java.util.List;
import java.util.Optional;
/**
 * <h2>Puerto de Dominio - CustomerRepository</h2>
 *
 * <p>
 * Define el contrato que debe cumplir cualquier implementación
 * de persistencia relacionada con la entidad {@link Customer}.
 * </p>
 *
 * <p>
 * Esta interfaz pertenece al dominio y actúa como un <b>puerto</b>
 * en términos de Clean Architecture, permitiendo desacoplar
 * la lógica de negocio de la tecnología de almacenamiento
 * (base de datos, API externa, memoria, etc.).
 * </p>
 *
 * <p>
 * Las implementaciones concretas de esta interfaz se encuentran
 * en la capa de infraestructura.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
public interface CustomerRepository {
    /**
     * Guarda o actualiza un cliente en el sistema.
     *
     * @param customer entidad a persistir
     * @return cliente persistido con posibles datos actualizados
     */
    Customer save(Customer customer);
    /**
     * Busca un cliente por su identificador único.
     *
     * @param id identificador del cliente
     * @return {@link Optional} con el cliente si existe,
     *         o vacío si no se encuentra
     */
    Optional<Customer> findById(Long id);
    /**
     * Busca un cliente por su código interno.
     *
     * @param code código único del cliente
     * @return {@link Optional} con el cliente si existe,
     *         o vacío si no se encuentra
     */
    Optional<Customer> findByCode(String code);
    /**
     * Busca un cliente por su correo electrónico.
     *
     * @param email correo electrónico del cliente
     * @return {@link Optional} con el cliente si existe,
     *         o vacío si no se encuentra
     */
    Optional<Customer> findByEmail(String email);
    /**
     * Busca un cliente por su número de documento.
     *
     * @param documentNumber número de identificación
     * @return {@link Optional} con el cliente si existe,
     *         o vacío si no se encuentra
     */
    Optional<Customer> findByDocumentNumber(String documentNumber);
    /**
     * Obtiene la lista completa de clientes registrados.
     *
     * @return lista de clientes
     */
    List<Customer> findAll();
    /**
     * Obtiene todos los clientes activos.
     *
     * @return lista de clientes con estado activo
     */
    List<Customer> findAllActive();
    /**
     * Obtiene clientes según su tipo (ej. MAYORISTA, MINORISTA).
     *
     * @param customerType tipo de cliente
     * @return lista de clientes que coinciden con el tipo indicado
     */
    List<Customer> findByCustomerType(String customerType);
    /**
     * Obtiene los clientes en estado moroso
     * (con saldo pendiente mayor a cero).
     *
     * @return lista de clientes morosos
     */
    List<Customer> findMorosos();
    /**
     * Elimina un cliente por su identificador.
     *
     * @param id identificador del cliente
     */
    void delete(Long id);
    /**
     * Verifica si ya existe un cliente con el código indicado.
     *
     * @param code código del cliente
     * @return {@code true} si existe,
     *         {@code false} en caso contrario
     */
    boolean existsByCode(String code);
    /**
     * Verifica si ya existe un cliente con el correo indicado.
     *
     * @param email correo del cliente
     * @return {@code true} si existe,
     *         {@code false} en caso contrario
     */
    boolean existsByEmail(String email);
    /**
     * Verifica si ya existe un cliente con el número de documento indicado.
     *
     * @param documentNumber número de documento
     * @return {@code true} si existe,
     *         {@code false} en caso contrario
     */
    boolean existsByDocumentNumber(String documentNumber);
}
