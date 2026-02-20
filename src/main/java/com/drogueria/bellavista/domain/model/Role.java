package com.drogueria.bellavista.domain.model;

public enum Role {
    ADMIN("ADMIN", "Administrador del sistema"),
    MANAGER("MANAGER", "Gerente de ventas"),
    SALES("SALES", "Representante de ventas"),
    WAREHOUSE("WAREHOUSE", "Personal de almacén"),
    USER("USER", "Usuario estándar");

    private final String code;
    private final String description;

    Role(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
