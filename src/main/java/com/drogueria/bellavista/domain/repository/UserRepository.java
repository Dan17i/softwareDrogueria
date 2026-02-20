package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.User;

import java.util.Optional;

/**
 * Port definition for User persistence operations.
 * This interface defines the contract for user data access,
 * independent of implementation details.
 */
public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void delete(User user);
}
