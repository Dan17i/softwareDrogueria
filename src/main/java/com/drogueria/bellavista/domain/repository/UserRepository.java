package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.User;

import java.util.Optional;
/**
 * <h2>Puerto de Dominio - UserRepository</h2>
 *
 * <p>
 * Define el contrato que debe implementar cualquier mecanismo
 * de persistencia relacionado con la entidad {@link User}.
 * </p>
 *
 * <p>
 * Esta interfaz actúa como un <b>puerto de salida</b> dentro de la
 * arquitectura hexagonal (Ports & Adapters), permitiendo desacoplar
 * la lógica de autenticación y gestión de usuarios de los detalles
 * de infraestructura (base de datos, LDAP, servicio externo, etc.).
 * </p>
 *
 * <p>
 * No contiene dependencias de frameworks ni tecnologías específicas.
 * Las implementaciones concretas deben ubicarse en la capa de infraestructura.
 * </p>
 *
 * @since 1.0
 */
public interface UserRepository {
    /**
     * Guarda o actualiza un usuario en el sistema.
     *
     * @param user entidad a persistir
     * @return usuario persistido con posibles datos actualizados
     */
    User save(User user);
    /**
     * Busca un usuario por su identificador único.
     *
     * @param id identificador del usuario
     * @return {@link Optional} con el usuario si existe,
     *         o vacío si no se encuentra
     */
    Optional<User> findById(Long id);
    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario
     * @return {@link Optional} con el usuario si existe,
     *         o vacío si no se encuentra
     */
    Optional<User> findByUsername(String username);
    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email correo electrónico del usuario
     * @return {@link Optional} con el usuario si existe,
     *         o vacío si no se encuentra
     */
    Optional<User> findByEmail(String email);
    /**
     * Verifica si ya existe un usuario con el nombre de usuario indicado.
     *
     * @param username nombre de usuario
     * @return {@code true} si existe,
     *         {@code false} en caso contrario
     */
    boolean existsByUsername(String username);
    /**
     * Verifica si ya existe un usuario con el correo electrónico indicado.
     *
     * @param email correo electrónico del usuario
     * @return {@code true} si existe,
     *         {@code false} en caso contrario
     */
    boolean existsByEmail(String email);
    /**
     * Elimina un usuario del sistema.
     *
     * @param user entidad del usuario a eliminar
     */
    void delete(User user);
}
