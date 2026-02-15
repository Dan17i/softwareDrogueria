package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.domain.repository.UserRepository;
import com.drogueria.bellavista.infrastructure.mapper.UserMapper;
import com.drogueria.bellavista.infrastructure.persistence.JpaUserRepository;
import com.drogueria.bellavista.infrastructure.persistence.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
/**
 * <h2>UserRepositoryAdapter</h2>
 *
 * <p>
 * Implementación del puerto {@link UserRepository}
 * utilizando Spring Data JPA como mecanismo de persistencia.
 * </p>
 *
 * <p>
 * Este adaptador forma parte de la capa de infraestructura dentro de la
 * arquitectura hexagonal (Ports & Adapters) y actúa como puente entre:
 * </p>
 *
 * <ul>
 *     <li>El modelo de dominio {@link User}</li>
 *     <li>La entidad de persistencia {@link UserEntity}</li>
 *     <li>El repositorio JPA {@link JpaUserRepository}</li>
 * </ul>
 *
 * <p>
 * La conversión entre el modelo de dominio y la entidad de base de datos
 * se realiza mediante {@link UserMapper}, garantizando el desacoplamiento
 * del dominio respecto a la infraestructura.
 * </p>
 *
 * <p>
 * Este adaptador permite gestionar la persistencia de usuarios,
 * incluyendo búsquedas por credenciales y validaciones de existencia,
 * fundamentales para procesos de autenticación y autorización.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Component
public class UserRepositoryAdapter implements UserRepository {
    /**
     * Repositorio JPA para la entidad User.
     */
    private final JpaUserRepository jpaUserRepository;
    /**
     * Mapper encargado de convertir entre dominio y entidad.
     */
    private final UserMapper userMapper;
    /**
     * Constructor con inyección de dependencias.
     *
     * @param jpaUserRepository repositorio JPA
     * @param userMapper mapper de conversión dominio-entidad
     */
    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository, UserMapper userMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.userMapper = userMapper;
    }
    /**
     * Guarda o actualiza un usuario en la base de datos.
     *
     * @param user usuario del dominio a persistir
     * @return usuario persistido convertido nuevamente a dominio
     */
    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity saved = jpaUserRepository.save(entity);
        return userMapper.toDomain(saved);
    }
    /**
     * Busca un usuario por su identificador único.
     *
     * @param id identificador del usuario
     * @return usuario encontrado si existe
     */
    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id)
            .map(userMapper::toDomain);
    }
    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario
     * @return usuario encontrado si existe
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username)
            .map(userMapper::toDomain);
    }
    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email correo electrónico
     * @return usuario encontrado si existe
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
            .map(userMapper::toDomain);
    }
    /**
     * Verifica si existe un usuario con el nombre de usuario indicado.
     *
     * @param username nombre de usuario
     * @return true si existe
     */
    @Override
    public boolean existsByUsername(String username) {
        return jpaUserRepository.existsByUsername(username);
    }
    /**
     * Verifica si existe un usuario con el correo electrónico indicado.
     *
     * @param email correo electrónico
     * @return true si existe
     */
    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }
    /**
     * Elimina un usuario de la base de datos.
     *
     * @param user usuario a eliminar
     */
    @Override
    public void delete(User user) {
        UserEntity entity = userMapper.toEntity(user);
        jpaUserRepository.delete(entity);
    }
}
