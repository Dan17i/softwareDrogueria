package com.drogueria.bellavista.domain.model;
/**
 * <h2>Enumeración de Dominio - Role</h2>
 *
 * <p>
 * Define los roles disponibles dentro del sistema de la droguería.
 * Cada rol representa un perfil de usuario con responsabilidades
 * y permisos específicos dentro de la aplicación.
 * </p>
 *
 * <p>
 * Esta enumeración forma parte del modelo de dominio y permite
 * controlar el acceso y las funcionalidades disponibles según
 * el tipo de usuario autenticado.
 * </p>
 *
 * <p>
 * Cada rol contiene:
 * </p>
 * <ul>
 *     <li><b>code</b>: Identificador interno del rol.</li>
 *     <li><b>description</b>: Descripción funcional del rol.</li>
 * </ul>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
public enum Role {
    /**
     * Administrador del sistema.
     *
     * <p>
     * Tiene acceso total a la configuración, gestión de usuarios,
     * inventario, ventas y reportes.
     * </p>
     */
    ADMIN("ADMIN", "Administrador del sistema"),
    /**
     * Gerente de ventas.
     *
     * <p>
     * Supervisa procesos comerciales, reportes y desempeño de ventas.
     * </p>
     */
    MANAGER("MANAGER", "Gerente de ventas"),
    /**
     * Representante de ventas.
     *
     * <p>
     * Responsable de registrar ventas, gestionar clientes
     * y generar órdenes.
     * </p>
     */
    SALES("SALES", "Representante de ventas"),
    /**
     * Personal de almacén.
     *
     * <p>
     * Encargado de la gestión de inventario,
     * recepción de mercancía y control de stock.
     * </p>
     */
    WAREHOUSE("WAREHOUSE", "Personal de almacén"),
    /**
     * Usuario estándar.
     *
     * <p>
     * Perfil con permisos limitados dentro del sistema.
     * </p>
     */
    USER("USER", "Usuario estándar");
    /**
     * Código interno del rol.
     */
    private final String code;
    /**
     * Descripción funcional del rol.
     */
    private final String description;
    /**
     * Constructor del rol.
     *
     * @param code identificador interno
     * @param description descripción del rol
     */
    Role(String code, String description) {
        this.code = code;
        this.description = description;
    }
    /**
     * Obtiene el código interno del rol.
     *
     * @return código del rol
     */
    public String getCode() {
        return code;
    }
    /**
     * Obtiene la descripción del rol.
     *
     * @return descripción funcional
     */
    public String getDescription() {
        return description;
    }
}
