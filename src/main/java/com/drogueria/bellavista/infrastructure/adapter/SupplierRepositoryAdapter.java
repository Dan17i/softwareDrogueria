package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.domain.model.Supplier;
import com.drogueria.bellavista.domain.repository.SupplierRepository;
import com.drogueria.bellavista.infrastructure.mapper.SupplierMapper;
import com.drogueria.bellavista.infrastructure.persistence.JpaSupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * <h2>SupplierRepositoryAdapter</h2>
 *
 * <p>
 * Implementación del puerto {@link SupplierRepository}
 * utilizando Spring Data JPA como mecanismo de persistencia.
 * </p>
 *
 * <p>
 * Este adaptador pertenece a la capa de infraestructura dentro de la
 * arquitectura hexagonal (Ports & Adapters) y actúa como puente entre:
 * </p>
 *
 * <ul>
 *     <li>El modelo de dominio {@link Supplier}</li>
 *     <li>El repositorio JPA {@link JpaSupplierRepository}</li>
 *     <li>La base de datos</li>
 * </ul>
 *
 * <p>
 * La conversión entre el modelo de dominio y la entidad de persistencia
 * se realiza mediante {@link SupplierMapper}, garantizando que el
 * dominio permanezca desacoplado de la infraestructura.
 * </p>
 *
 * <p>
 * Este adaptador implementa las operaciones CRUD y consultas
 * especializadas definidas en el puerto del dominio.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class SupplierRepositoryAdapter implements SupplierRepository {
    /**
     * Repositorio JPA para la entidad Supplier.
     */
    private final JpaSupplierRepository jpaRepository;
    /**
     * Mapper encargado de convertir entre dominio y entidad.
     */
    private final SupplierMapper mapper;
    /**
     * Guarda o actualiza un proveedor.
     *
     * @param supplier proveedor del dominio a persistir
     * @return proveedor persistido convertido nuevamente a dominio
     */
    @Override
    public Supplier save(Supplier supplier) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(supplier)));
    }
    /**
     * Busca un proveedor por su identificador único.
     *
     * @param id identificador del proveedor
     * @return proveedor encontrado si existe
     */
    @Override
    public Optional<Supplier> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
    /**
     * Busca un proveedor por su código interno.
     *
     * @param code código del proveedor
     * @return proveedor encontrado si existe
     */
    @Override
    public Optional<Supplier> findByCode(String code) {
        return jpaRepository.findByCode(code).map(mapper::toDomain);
    }
    /**
     * Busca un proveedor por su correo electrónico.
     *
     * @param email correo electrónico
     * @return proveedor encontrado si existe
     */
    @Override
    public Optional<Supplier> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }
    /**
     * Obtiene todos los proveedores registrados.
     *
     * @return lista de proveedores
     */
    @Override
    public List<Supplier> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }
    /**
     * Obtiene todos los proveedores activos.
     *
     * @return lista de proveedores activos
     */
    @Override
    public List<Supplier> findAllActive() {
        return jpaRepository.findAllActive().stream().map(mapper::toDomain).collect(Collectors.toList());
    }
    /**
     * Elimina un proveedor por su identificador.
     *
     * @param id identificador del proveedor
     */
    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }
    /**
     * Verifica si existe un proveedor con el código indicado.
     *
     * @param code código del proveedor
     * @return true si existe
     */
    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
    /**
     * Verifica si existe un proveedor con el correo electrónico indicado.
     *
     * @param email correo electrónico
     * @return true si existe
     */
    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
