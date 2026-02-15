package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.Supplier;

import java.util.List;
import java.util.Optional;
/**
 * <h2>Puerto de Dominio - SupplierRepository</h2>
 *
 * <p>
 * Define el contrato que debe cumplir cualquier implementación
 * de persistencia relacionada con la entidad {@link Supplier}.
 * </p>
 *
 * <p>
 * Esta interfaz forma parte de la capa de dominio y actúa como un
 * <b>puerto</b> dentro de la arquitectura hexagonal (Ports & Adapters),
 * permitiendo desacoplar la lógica de negocio de los detalles técnicos
 * de almacenamiento.
 * </p>
 *
 * <p>
 * Las implementaciones concretas deben residir en la capa de infraestructura.
 * </p>
 *
 * @since 1.0
 */
public interface SupplierRepository {
    /**
     * Guarda o actualiza un proveedor en el sistema.
     *
     * @param supplier entidad a persistir
     * @return proveedor persistido con posibles datos actualizados
     */
    Supplier save(Supplier supplier);
    /**
     * Busca un proveedor por su identificador único.
     *
     * @param id identificador del proveedor
     * @return {@link Optional} con el proveedor si existe,
     *         o vacío si no se encuentra
     */
    Optional<Supplier> findById(Long id);
    /**
     * Busca un proveedor por su código único.
     *
     * @param code código del proveedor
     * @return {@link Optional} con el proveedor si existe,
     *         o vacío si no se encuentra
     */
    Optional<Supplier> findByCode(String code);
    /**
     * Busca un proveedor por su correo electrónico.
     *
     * @param email correo electrónico del proveedor
     * @return {@link Optional} con el proveedor si existe,
     *         o vacío si no se encuentra
     */
    Optional<Supplier> findByEmail(String email);
    /**
     * Obtiene todos los proveedores registrados en el sistema.
     *
     * @return lista completa de proveedores
     */
    List<Supplier> findAll();
    /**
     * Obtiene únicamente los proveedores activos.
     *
     * @return lista de proveedores con estado activo
     */
    List<Supplier> findAllActive();
    /**
     * Elimina un proveedor por su identificador.
     *
     * @param id identificador del proveedor
     */
    void delete(Long id);
    /**
     * Verifica si ya existe un proveedor con el código indicado.
     *
     * @param code código del proveedor
     * @return {@code true} si existe,
     *         {@code false} en caso contrario
     */
    boolean existsByCode(String code);
    /**
     * Verifica si ya existe un proveedor con el correo electrónico indicado.
     *
     * @param email correo electrónico del proveedor
     * @return {@code true} si existe,
     *         {@code false} en caso contrario
     */
    boolean existsByEmail(String email);
}
