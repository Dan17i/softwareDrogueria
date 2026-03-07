# 📊 Análisis: Gestión de Usuarios y Roles

## Estado Actual del Sistema

### ✅ Lo que YA funciona:

1. **Registro de usuarios** (`POST /api/auth/register`)
   - Cualquiera puede registrarse
   - Por defecto se asigna rol `USER`
   - Password se encripta automáticamente

2. **Login** (`POST /api/auth/login`)
   - Autenticación con JWT
   - Retorna token y rol del usuario

3. **Crear usuario con rol específico** (`POST /api/auth/admin/create-user`)
   - Solo ADMIN puede usarlo
   - Permite asignar cualquier rol al crear

4. **Roles disponibles:**
   - ADMIN - Administrador del sistema
   - MANAGER - Gerente de ventas
   - SALES - Representante de ventas
   - WAREHOUSE - Personal de almacén
   - USER - Usuario estándar

### ❌ Lo que FALTA para tu requerimiento:

1. **Listar todos los usuarios** - No existe endpoint
2. **Actualizar rol de un usuario existente** - No existe endpoint
3. **Ver detalles de un usuario** - No existe endpoint
4. **Activar/Desactivar usuarios** - No existe endpoint
5. **Controlador dedicado para gestión de usuarios** - No existe

---

## 🎯 Requerimiento del Usuario

> "Quiero que cada vez que alguien se registre sea tomado como USER, y que luego cuando el ADMIN inicie sesión, le aparezcan todos los usuarios para que pueda asignarles roles o cambiarlos de USER a otro rol"

### Flujo Deseado:

1. Usuario nuevo se registra → Automáticamente es `USER` ✅ (Ya funciona)
2. ADMIN inicia sesión → Obtiene lista de usuarios ❌ (Falta)
3. ADMIN ve usuario con rol `USER` → Puede cambiar a MANAGER, SALES, etc. ❌ (Falta)
4. ADMIN puede activar/desactivar usuarios ❌ (Falta)

---

## 🔧 Solución Propuesta

### Nuevos Endpoints Necesarios:

#### 1. Listar Usuarios (Solo ADMIN)
```
GET /api/users
```
Retorna lista de todos los usuarios con su información básica y rol actual.

#### 2. Obtener Usuario por ID (Solo ADMIN)
```
GET /api/users/{id}
```
Retorna detalles completos de un usuario específico.

#### 3. Actualizar Rol de Usuario (Solo ADMIN)
```
PATCH /api/users/{id}/role
Body: { "role": "MANAGER" }
```
Permite cambiar el rol de un usuario existente.

#### 4. Activar/Desactivar Usuario (Solo ADMIN)
```
PATCH /api/users/{id}/status
Body: { "active": true }
```
Permite activar o desactivar una cuenta de usuario.

#### 5. Eliminar Usuario (Solo ADMIN) - Opcional
```
DELETE /api/users/{id}
```
Elimina un usuario del sistema (soft delete recomendado).

---

## 📝 Archivos a Crear/Modificar

### Nuevos Archivos:

1. **UserController.java** - Controlador REST para gestión de usuarios
2. **UserDTO.java** - DTO para respuestas de usuario
3. **UpdateRoleRequestDTO.java** - DTO para actualizar rol
4. **UpdateStatusRequestDTO.java** - DTO para activar/desactivar

### Archivos a Modificar:

1. **UserRepository.java** - Agregar método `findAll()`
2. **UserService.java** - Agregar métodos de gestión (updateRole, updateStatus, etc.)

---

## 🔒 Seguridad

Todos los nuevos endpoints estarán protegidos con:
```java
@PreAuthorize("hasRole('ADMIN')")
```

Solo usuarios con rol ADMIN podrán:
- Ver lista de usuarios
- Cambiar roles
- Activar/desactivar usuarios

---

## 📋 Ejemplo de Respuesta

### GET /api/users
```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@bellavista.com",
    "firstName": "Admin",
    "lastName": "Sistema",
    "role": "ADMIN",
    "active": true,
    "createdAt": "2024-03-01T10:00:00",
    "lastLogin": "2024-03-07T15:30:00"
  },
  {
    "id": 2,
    "username": "juan.perez",
    "email": "juan@example.com",
    "firstName": "Juan",
    "lastName": "Pérez",
    "role": "USER",
    "active": true,
    "createdAt": "2024-03-05T14:20:00",
    "lastLogin": "2024-03-06T09:15:00"
  }
]
```

### PATCH /api/users/2/role
Request:
```json
{
  "role": "SALES"
}
```

Response:
```json
{
  "id": 2,
  "username": "juan.perez",
  "email": "juan@example.com",
  "firstName": "Juan",
  "lastName": "Pérez",
  "role": "SALES",
  "active": true,
  "updatedAt": "2024-03-07T16:00:00"
}
```

---

## ✅ Conclusión

**El código actual NO puede hacer lo que necesitas.**

Necesitamos:
1. Crear un nuevo `UserController`
2. Agregar métodos en `UserService` para gestión de usuarios
3. Agregar método `findAll()` en `UserRepository`
4. Crear DTOs para las nuevas operaciones

¿Procedo con la implementación?
