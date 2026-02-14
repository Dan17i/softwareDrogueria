package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.domain.repository.UserRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
/**
 * Servicio de dominio encargado de la gestión de usuarios.
 *
 * <p>Contiene la lógica de negocio relacionada con:
 * <ul>
 *     <li>Creación de usuarios</li>
 *     <li>Búsqueda por distintos criterios</li>
 *     <li>Validaciones de disponibilidad</li>
 *     <li>Verificación de contraseñas</li>
 *     <li>Actualización de metadatos del usuario</li>
 * </ul>
 *
 * <p>Aplica buenas prácticas de seguridad como el cifrado de contraseñas
 * mediante {@link PasswordEncoder} y validaciones previas de negocio.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    /**
     * Constructor del servicio de usuarios.
     *
     * @param userRepository repositorio de persistencia de usuarios
     * @param passwordEncoder codificador de contraseñas utilizado para cifrar y verificar passwords
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    /**
     * Crea un nuevo usuario aplicando validaciones de negocio.
     *
     * <p>Incluye:
     * <ul>
     *     <li>Validación de campos obligatorios</li>
     *     <li>Validación de formato de email</li>
     *     <li>Validación de longitud de username y password</li>
     *     <li>Verificación de unicidad de username y email</li>
     *     <li>Cifrado automático de la contraseña</li>
     * </ul>
     *
     * @param username nombre de usuario único
     * @param email correo electrónico del usuario
     * @param rawPassword contraseña en texto plano
     * @param firstName nombre del usuario
     * @param lastName apellido del usuario
     * @param role rol asignado al usuario (si es null se asigna USER por defecto)
     * @return usuario persistido en base de datos
     * @throws BusinessException si alguna validación de negocio falla
     */
    public User createUser(String username, String email, String rawPassword, String firstName, String lastName, Role role) {
        // Validation
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException("Username is required");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new BusinessException("Username must be between 3 and 50 characters");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("Email is required");
        }
        if (!isValidEmail(email)) {
            throw new BusinessException("Invalid email format");
        }

        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new BusinessException("Password is required");
        }
        if (rawPassword.length() < 8) {
            throw new BusinessException("Password must be at least 8 characters");
        }

        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("Email already exists");
        }

        // Create user with encoded password
        User user = User.builder()
            .username(username)
            .email(email)
            .password(passwordEncoder.encode(rawPassword))
            .firstName(firstName)
            .lastName(lastName)
            .role(role != null ? role : Role.USER)
            .active(true)
            .createdAt(LocalDateTime.now())
            .build();

        return userRepository.save(user);
    }
    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario a buscar
     * @return usuario encontrado
     * @throws ResourceNotFoundException si no existe un usuario con ese username
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
    /**
     * Obtiene un usuario por su identificador.
     *
     * @param userId identificador del usuario
     * @return usuario encontrado
     * @throws ResourceNotFoundException si no existe un usuario con ese ID
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }
    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * @param email correo del usuario
     * @return usuario encontrado
     * @throws ResourceNotFoundException si no existe un usuario con ese email
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
    /**
     * Indica si un nombre de usuario está disponible.
     *
     * @param username nombre de usuario a validar
     * @return {@code true} si está disponible, {@code false} si ya existe
     */
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
    /**
     * Indica si un correo electrónico está disponible.
     *
     * @param email correo a validar
     * @return {@code true} si está disponible, {@code false} si ya existe
     */
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
    /**
     * Verifica si una contraseña en texto plano coincide con la contraseña cifrada.
     *
     * @param rawPassword contraseña ingresada por el usuario
     * @param encodedPassword contraseña almacenada cifrada
     * @return {@code true} si coinciden, {@code false} en caso contrario
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    /**
     * Actualiza la fecha del último inicio de sesión del usuario.
     *
     * @param userId identificador del usuario
     * @throws ResourceNotFoundException si el usuario no existe
     */
    public void updateLastLogin(Long userId) {
        User user = getUserById(userId);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }
    /**
     * Valida el formato del correo electrónico mediante una expresión regular simple.
     *
     * @param email correo a validar
     * @return {@code true} si cumple el formato, {@code false} en caso contrario
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}
