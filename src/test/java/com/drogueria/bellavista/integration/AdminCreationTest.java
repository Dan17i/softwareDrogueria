package com.drogueria.bellavista.integration;

import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.domain.repository.UserRepository;
import com.drogueria.bellavista.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@org.springframework.context.annotation.Import(com.drogueria.bellavista.config.TestMailConfig.class)
public class AdminCreationTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testCreateAdminAndVerifyPassword() {
        // Crear admin
        User admin = userService.createUser(
                "admin",
                "admin@bellavista.com",
                "admin123",
                "Admin",
                "Sistema",
                Role.ADMIN
        );

        System.out.println("✅ Admin creado con ID: " + admin.getId());
        System.out.println("✅ Username: " + admin.getUsername());
        System.out.println("✅ Email: " + admin.getEmail());
        System.out.println("✅ Role: " + admin.getRole());
        System.out.println("✅ Active: " + admin.isActive());
        System.out.println("✅ Password hash: " + admin.getPassword());

        // Verificar que se guardó correctamente
        User savedAdmin = userRepository.findByUsername("admin").orElseThrow();
        assertThat(savedAdmin).isNotNull();
        assertThat(savedAdmin.getUsername()).isEqualTo("admin");
        assertThat(savedAdmin.getRole()).isEqualTo(Role.ADMIN);
        assertThat(savedAdmin.isActive()).isTrue();

        // Verificar que la contraseña coincide
        boolean passwordMatches = passwordEncoder.matches("admin123", savedAdmin.getPassword());
        System.out.println("✅ Password matches: " + passwordMatches);
        assertThat(passwordMatches).isTrue();

        // Imprimir el hash para usarlo en producción
        System.out.println("\n🔑 HASH PARA USAR EN PRODUCCIÓN:");
        System.out.println(savedAdmin.getPassword());
        System.out.println("\nEjecuta este SQL en DBeaver:");
        System.out.println("UPDATE users SET password = '" + savedAdmin.getPassword() + "' WHERE username = 'admin';");
    }
}
