package com.drogueria.bellavista.integration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica las migraciones Flyway sobre PostgreSQL real:
 * BD vacía (V1 se ejecuta) y BD existente creada con ddl-auto=update (se baselinea en V1).
 */
@Testcontainers
@DisplayName("Flyway - migraciones")
class FlywayMigrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("flyway_test")
            .withUsername("test")
            .withPassword("test");

    private Flyway flyway(String schema) {
        return Flyway.configure()
                .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .schemas(schema)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .baselineVersion("1")
                .load();
    }

    private int countTables(String schema) throws Exception {
        try (Connection c = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT count(*) FROM information_schema.tables WHERE table_schema = '" + schema
                     + "' AND table_name <> 'flyway_schema_history'")) {
            rs.next();
            return rs.getInt(1);
        }
    }

    @Test
    @DisplayName("BD vacía - ejecuta V1 y crea las 11 tablas")
    void shouldCreateSchemaOnEmptyDatabase() throws Exception {
        MigrateResult result = flyway("empty_db").migrate();

        assertThat(result.success).isTrue();
        assertThat(result.migrationsExecuted).isEqualTo(1);
        assertThat(countTables("empty_db")).isEqualTo(11);
    }

    @Test
    @DisplayName("BD existente sin historial - baselinea en V1 sin re-ejecutar el script")
    void shouldBaselineExistingDatabase() throws Exception {
        String ddl = StreamUtils.copyToString(
                new ClassPathResource("db/migration/V1__baseline_schema.sql").getInputStream(), StandardCharsets.UTF_8);
        try (Connection c = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
             Statement st = c.createStatement()) {
            st.execute("CREATE SCHEMA existing_db");
            st.execute("SET search_path TO existing_db");
            st.execute(ddl);
        }

        Flyway flyway = flyway("existing_db");
        MigrateResult result = flyway.migrate();

        assertThat(result.success).isTrue();
        assertThat(result.migrationsExecuted).isZero();
        assertThat(flyway.info().current().getVersion()).isEqualTo(MigrationVersion.fromVersion("1"));
        assertThat(countTables("existing_db")).isEqualTo(11);
    }
}
