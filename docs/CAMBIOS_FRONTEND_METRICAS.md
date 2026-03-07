# 🔄 Cambios Necesarios en el Frontend

## Droguería Bellavista - Actualización por Métricas de Calidad

---

## 📋 Resumen de Cambios en el Backend

Se realizaron las siguientes modificaciones en el backend para implementar las métricas de calidad:

1. **Nuevo campo `createdBy` en órdenes** (Métrica 2.3 - Auditoría)
2. **Mensajes de error mejorados** (Métrica 2.2 - Claridad)
3. **Corrección de ruta del controlador de órdenes** (Fix de integración)
4. **🆕 Sistema de Gestión de Usuarios y Roles** (Métrica 4.3 - Control de Acceso)
5. **🆕 Sistema de Email - Bienvenida y Recuperación de Contraseña** (Métrica 4.2 - Seguridad)

**📧 NUEVO:** Ver detalles completos del sistema de email en [`CAMBIO_4_SISTEMA_EMAIL.md`](./CAMBIO_4_SISTEMA_EMAIL.md)

---

## ⚠️ CAMBIO CRÍTICO: Ruta del Endpoint de Órdenes

### Problema Detectado

Durante las pruebas de integración, se identificó un problema de **duplicación de ruta** en el endpoint de órdenes:

**ANTES (Incorrecto):**
```
URL completa: /api/api/orders
```

**Causa:** El `OrderController` tenía `@RequestMapping("/api/orders")` pero `application.yml` ya define `context-path: /api`

**DESPUÉS (Correcto):**
```
URL completa: /api/orders
```

### ✅ Solución Aplicada en Backend

```java
// OrderController.java
@RestController
@RequestMapping("/orders")  // ✅ Cambiado de "/api/orders" a "/orders"
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    // ...
}
```

### 🔧 Acción Requerida en Frontend

**SI tu frontend estaba usando rutas como:**
```javascript
// ❌ INCORRECTO (puede que funcionara antes por casualidad)
const response = await fetch('https://drogueria-bellavista-api.onrender.com/api/api/orders');

// ❌ INCORRECTO (sin /api)
const response = await fetch('https://drogueria-bellavista-api.onrender.com/orders');
```

**DEBES cambiar a:**
```javascript
// ✅ CORRECTO
const response = await fetch('https://drogueria-bellavista-api.onrender.com/api/orders');
```

**Verifica TODOS los endpoints de órdenes:**
- `POST /api/orders` - Crear orden
- `GET /api/orders` - Listar todas las órdenes
- `GET /api/orders/{id}` - Obtener orden por ID
- `GET /api/orders/number/{orderNumber}` - Obtener por número
- `GET /api/orders/customer/{customerId}` - Órdenes de un cliente
- `GET /api/orders/status/{status}` - Órdenes por estado
- `GET /api/orders/customer/{customerId}/pending` - Órdenes pendientes
- `PATCH /api/orders/{id}/complete` - Completar orden
- `PATCH /api/orders/{id}/cancel` - Cancelar orden
- `GET /api/orders/search?startDate=...&endDate=...` - Buscar por fechas

---

## 🆕 CAMBIO 1: Nuevo Campo `createdBy` en Órdenes

### Descripción

Se agregó el campo `createdBy` al modelo de órdenes para cumplir con la **Métrica 2.3 - Completitud de Auditoría** (meta: 100% de registros con información de auditoría).

### Cambios en el Backend

**Modelo de Dominio (`Order.java`):**
```java
public class Order {
    // ... otros campos
    private String createdBy;  // ✅ NUEVO CAMPO
}
```

**Entidad JPA (`OrderEntity.java`):**
```java
@Entity
@Table(name = "orders")
public class OrderEntity {
    // ... otros campos
    
    @Column(name = "created_by", length = 100)
    private String createdBy;  // ✅ NUEVO CAMPO
}
```

**Servicio (`OrderService.java`):**
```java
public Order createOrder(Order order) {
    // ... validaciones
    
    // Establecer createdBy si no está definido
    if (order.getCreatedBy() == null) {
        order.setCreatedBy("SYSTEM");  // ✅ Valor por defecto
    }
    
    // ... resto del código
}
```

### 🔧 Acción Requerida en Frontend

#### Opción 1: Enviar el Usuario Actual (RECOMENDADO)

Si tu frontend tiene información del usuario autenticado, envíala en el campo `createdBy`:

```javascript
// Ejemplo con React/JavaScript
const createOrder = async (orderData) => {
    const currentUser = getUserFromToken(); // Tu función para obtener el usuario
    
    const payload = {
        customerId: orderData.customerId,
        items: orderData.items,
        notes: orderData.notes,
        createdBy: currentUser.username  // ✅ NUEVO CAMPO
    };
    
    const response = await fetch('https://drogueria-bellavista-api.onrender.com/api/orders', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(payload)
    });
    
    return response.json();
};
```

#### Opción 2: No Enviar el Campo (Funciona pero no es óptimo)

Si no envías el campo, el backend automáticamente asignará `"SYSTEM"`:

```javascript
// Funciona, pero no es ideal para auditoría
const payload = {
    customerId: orderData.customerId,
    items: orderData.items,
    notes: orderData.notes
    // createdBy se asignará como "SYSTEM" automáticamente
};
```

#### Actualizar TypeScript Interfaces (si aplica)

```typescript
// types/Order.ts
export interface CreateOrderRequest {
    customerId: number;
    items: OrderItem[];
    notes?: string;
    createdBy?: string;  // ✅ NUEVO CAMPO OPCIONAL
}

export interface OrderResponse {
    id: number;
    orderNumber: string;
    customerId: number;
    customerName: string;
    status: string;
    total: number;
    items: OrderItem[];
    orderDate: string;
    createdAt: string;
    updatedAt: string;
    createdBy: string;  // ✅ NUEVO CAMPO en respuesta
}
```

---

## 📝 CAMBIO 2: Mensajes de Error Mejorados

### Descripción

Se mejoraron los mensajes de error para cumplir con la **Métrica 2.2 - Tasa de Pedidos Rechazados con Mensajes Claros** (meta: ≤ 5% de rechazos).

### Ejemplos de Nuevos Mensajes

#### Antes (Genérico):
```json
{
    "error": "Bad Request",
    "message": "Validation failed"
}
```

#### Después (Específico):
```json
{
    "error": "BusinessException",
    "message": "Stock insuficiente para el producto 'Acetaminofén 500mg' (código: MED-001). Disponible: 50, solicitado: 100"
}
```

### Tipos de Errores Mejorados

1. **Stock Insuficiente:**
```json
{
    "message": "Stock insuficiente para el producto 'Nombre' (código: XXX). Disponible: X, solicitado: Y"
}
```

2. **Crédito Insuficiente:**
```json
{
    "message": "El cliente no tiene crédito suficiente. Límite: 5000000, Pendiente: 3000000, Requerido: 2500000"
}
```

3. **Producto No Disponible:**
```json
{
    "message": "El producto 'Nombre' (código: XXX) no está disponible para la venta"
}
```

4. **Cliente Inactivo:**
```json
{
    "message": "El cliente está inactivo"
}
```

5. **Orden Sin Items:**
```json
{
    "message": "El campo 'items' es obligatorio. La orden debe contener al menos un producto"
}
```

### 🔧 Acción Requerida en Frontend

#### Mejorar el Manejo de Errores

```javascript
// Ejemplo con React/JavaScript
const createOrder = async (orderData) => {
    try {
        const response = await fetch('https://drogueria-bellavista-api.onrender.com/api/orders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(orderData)
        });
        
        if (!response.ok) {
            const error = await response.json();
            
            // ✅ Mostrar el mensaje específico del backend
            throw new Error(error.message || 'Error al crear la orden');
        }
        
        return await response.json();
        
    } catch (error) {
        // ✅ Mostrar el mensaje de error al usuario
        console.error('Error:', error.message);
        
        // Mostrar en UI (ejemplo con toast/alert)
        showErrorNotification(error.message);
        
        throw error;
    }
};
```

#### Ejemplo con React + Toast Notifications

```javascript
import { toast } from 'react-toastify';

const handleCreateOrder = async (orderData) => {
    try {
        const order = await createOrder(orderData);
        toast.success('Orden creada exitosamente');
        return order;
    } catch (error) {
        // ✅ El mensaje ya viene claro del backend
        toast.error(error.message);
    }
};
```

#### Ejemplo con Vue.js

```javascript
// En tu componente Vue
methods: {
    async createOrder(orderData) {
        try {
            const response = await this.$http.post('/api/orders', orderData);
            this.$notify({
                type: 'success',
                message: 'Orden creada exitosamente'
            });
            return response.data;
        } catch (error) {
            // ✅ Mostrar el mensaje específico
            this.$notify({
                type: 'error',
                message: error.response?.data?.message || 'Error al crear la orden'
            });
        }
    }
}
```

---

## 🆕 CAMBIO 3: Sistema de Gestión de Usuarios y Roles (ADMIN)

### Descripción

Se implementó un sistema completo de gestión de usuarios que permite al **ADMINISTRADOR** gestionar usuarios y asignar roles desde el frontend. Este cambio cumple con la **Métrica 4.3 - Control de Acceso Basado en Roles**.

### Flujo de Trabajo

1. **Registro de Nuevos Usuarios**: Cualquier persona puede registrarse, pero por defecto se asigna el rol `USER`
2. **Gestión por ADMIN**: Solo el ADMIN puede:
   - Ver lista de todos los usuarios
   - Cambiar roles de usuarios
   - Activar/desactivar usuarios
   - Eliminar usuarios (con protección del último admin)

### Nuevos Endpoints Disponibles

Todos estos endpoints requieren autenticación con rol `ADMIN`:

```
GET    /api/users              - Listar todos los usuarios
GET    /api/users/{id}         - Obtener usuario por ID
PATCH  /api/users/{id}/role    - Actualizar rol de usuario
PATCH  /api/users/{id}/status  - Activar/desactivar usuario
DELETE /api/users/{id}         - Eliminar usuario
```

### Roles Disponibles

```typescript
enum Role {
    ADMIN = 'ADMIN',      // Administrador del sistema
    MANAGER = 'MANAGER',  // Gerente
    SALES = 'SALES',      // Vendedor
    USER = 'USER'         // Usuario básico
}
```

### 🔧 Implementación en Frontend

#### 1. Interfaces TypeScript

```typescript
// types/User.ts
export interface User {
    id: number;
    username: string;
    email: string;
    firstName: string;
    lastName: string;
    role: 'ADMIN' | 'MANAGER' | 'SALES' | 'USER';
    active: boolean;
    createdAt: string;
    lastLogin?: string;
}

export interface UpdateRoleRequest {
    role: 'ADMIN' | 'MANAGER' | 'SALES' | 'USER';
}

export interface UpdateStatusRequest {
    active: boolean;
}
```

#### 2. Servicio de Gestión de Usuarios

```typescript
// services/userManagementService.ts
import axios from 'axios';

const API_BASE_URL = 'https://drogueria-bellavista-api.onrender.com/api';

class UserManagementService {
    private getAuthHeader() {
        const token = localStorage.getItem('token');
        return {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };
    }

    // Listar todos los usuarios (solo ADMIN)
    async getAllUsers(): Promise<User[]> {
        try {
            const response = await axios.get<User[]>(
                `${API_BASE_URL}/users`,
                { headers: this.getAuthHeader() }
            );
            return response.data;
        } catch (error: any) {
            if (error.response?.status === 403) {
                throw new Error('No tienes permisos para acceder a esta funcionalidad');
            }
            throw new Error(error.response?.data?.message || 'Error al obtener usuarios');
        }
    }

    // Obtener usuario por ID (solo ADMIN)
    async getUserById(id: number): Promise<User> {
        try {
            const response = await axios.get<User>(
                `${API_BASE_URL}/users/${id}`,
                { headers: this.getAuthHeader() }
            );
            return response.data;
        } catch (error: any) {
            throw new Error(error.response?.data?.message || 'Error al obtener usuario');
        }
    }

    // Actualizar rol de usuario (solo ADMIN)
    async updateUserRole(userId: number, role: string): Promise<User> {
        try {
            const response = await axios.patch<User>(
                `${API_BASE_URL}/users/${userId}/role`,
                { role },
                { headers: this.getAuthHeader() }
            );
            return response.data;
        } catch (error: any) {
            // El backend retorna mensajes específicos
            throw new Error(error.response?.data?.message || 'Error al actualizar rol');
        }
    }

    // Activar/desactivar usuario (solo ADMIN)
    async updateUserStatus(userId: number, active: boolean): Promise<User> {
        try {
            const response = await axios.patch<User>(
                `${API_BASE_URL}/users/${userId}/status`,
                { active },
                { headers: this.getAuthHeader() }
            );
            return response.data;
        } catch (error: any) {
            throw new Error(error.response?.data?.message || 'Error al actualizar estado');
        }
    }

    // Eliminar usuario (solo ADMIN)
    async deleteUser(userId: number): Promise<void> {
        try {
            await axios.delete(
                `${API_BASE_URL}/users/${userId}`,
                { headers: this.getAuthHeader() }
            );
        } catch (error: any) {
            throw new Error(error.response?.data?.message || 'Error al eliminar usuario');
        }
    }
}

export default new UserManagementService();
```

#### 3. Componente de Lista de Usuarios (React)

```typescript
// components/UserManagement.tsx
import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import userManagementService from '../services/userManagementService';

const UserManagement: React.FC = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadUsers();
    }, []);

    const loadUsers = async () => {
        try {
            setLoading(true);
            const data = await userManagementService.getAllUsers();
            setUsers(data);
        } catch (error: any) {
            toast.error(error.message);
        } finally {
            setLoading(false);
        }
    };

    const handleRoleChange = async (userId: number, newRole: string) => {
        try {
            await userManagementService.updateUserRole(userId, newRole);
            toast.success('Rol actualizado exitosamente');
            loadUsers(); // Recargar lista
        } catch (error: any) {
            // ✅ El backend envía mensajes específicos
            toast.error(error.message);
        }
    };

    const handleToggleStatus = async (userId: number, currentStatus: boolean) => {
        try {
            await userManagementService.updateUserStatus(userId, !currentStatus);
            toast.success(`Usuario ${!currentStatus ? 'activado' : 'desactivado'} exitosamente`);
            loadUsers();
        } catch (error: any) {
            toast.error(error.message);
        }
    };

    const handleDeleteUser = async (userId: number, username: string) => {
        if (!window.confirm(`¿Estás seguro de eliminar al usuario ${username}?`)) {
            return;
        }

        try {
            await userManagementService.deleteUser(userId);
            toast.success('Usuario eliminado exitosamente');
            loadUsers();
        } catch (error: any) {
            toast.error(error.message);
        }
    };

    if (loading) return <div>Cargando usuarios...</div>;

    return (
        <div className="user-management">
            <h2>Gestión de Usuarios</h2>
            
            <table className="users-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Usuario</th>
                        <th>Email</th>
                        <th>Nombre</th>
                        <th>Rol</th>
                        <th>Estado</th>
                        <th>Último Login</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map(user => (
                        <tr key={user.id}>
                            <td>{user.id}</td>
                            <td>{user.username}</td>
                            <td>{user.email}</td>
                            <td>{user.firstName} {user.lastName}</td>
                            <td>
                                <select
                                    value={user.role}
                                    onChange={(e) => handleRoleChange(user.id, e.target.value)}
                                    className="role-select"
                                >
                                    <option value="USER">Usuario</option>
                                    <option value="SALES">Vendedor</option>
                                    <option value="MANAGER">Gerente</option>
                                    <option value="ADMIN">Administrador</option>
                                </select>
                            </td>
                            <td>
                                <span className={`status-badge ${user.active ? 'active' : 'inactive'}`}>
                                    {user.active ? 'Activo' : 'Inactivo'}
                                </span>
                            </td>
                            <td>
                                {user.lastLogin 
                                    ? new Date(user.lastLogin).toLocaleString() 
                                    : 'Nunca'}
                            </td>
                            <td className="actions">
                                <button
                                    onClick={() => handleToggleStatus(user.id, user.active)}
                                    className="btn-toggle"
                                >
                                    {user.active ? 'Desactivar' : 'Activar'}
                                </button>
                                <button
                                    onClick={() => handleDeleteUser(user.id, user.username)}
                                    className="btn-delete"
                                >
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default UserManagement;
```

#### 4. Protección de Rutas (React Router)

```typescript
// components/ProtectedRoute.tsx
import React from 'react';
import { Navigate } from 'react-router-dom';

interface ProtectedRouteProps {
    children: React.ReactNode;
    requiredRole?: 'ADMIN' | 'MANAGER' | 'SALES' | 'USER';
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, requiredRole }) => {
    const token = localStorage.getItem('token');
    const userStr = localStorage.getItem('user');
    
    if (!token) {
        return <Navigate to="/login" />;
    }

    if (requiredRole && userStr) {
        const user = JSON.parse(userStr);
        if (user.role !== requiredRole) {
            return <Navigate to="/unauthorized" />;
        }
    }

    return <>{children}</>;
};

// Uso en App.tsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login />} />
                
                {/* Ruta protegida solo para ADMIN */}
                <Route 
                    path="/admin/users" 
                    element={
                        <ProtectedRoute requiredRole="ADMIN">
                            <UserManagement />
                        </ProtectedRoute>
                    } 
                />
                
                {/* Otras rutas */}
            </Routes>
        </BrowserRouter>
    );
}
```

#### 5. Ejemplo con Vue.js

```vue
<!-- components/UserManagement.vue -->
<template>
    <div class="user-management">
        <h2>Gestión de Usuarios</h2>
        
        <div v-if="loading">Cargando usuarios...</div>
        
        <table v-else class="users-table">
            <thead>
                <tr>
                    <th>Usuario</th>
                    <th>Email</th>
                    <th>Rol</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id">
                    <td>{{ user.username }}</td>
                    <td>{{ user.email }}</td>
                    <td>
                        <select 
                            v-model="user.role" 
                            @change="updateRole(user.id, user.role)"
                        >
                            <option value="USER">Usuario</option>
                            <option value="SALES">Vendedor</option>
                            <option value="MANAGER">Gerente</option>
                            <option value="ADMIN">Administrador</option>
                        </select>
                    </td>
                    <td>
                        <span :class="['badge', user.active ? 'active' : 'inactive']">
                            {{ user.active ? 'Activo' : 'Inactivo' }}
                        </span>
                    </td>
                    <td>
                        <button @click="toggleStatus(user)">
                            {{ user.active ? 'Desactivar' : 'Activar' }}
                        </button>
                        <button @click="deleteUser(user)">Eliminar</button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
export default {
    data() {
        return {
            users: [],
            loading: true
        };
    },
    
    async mounted() {
        await this.loadUsers();
    },
    
    methods: {
        async loadUsers() {
            try {
                this.loading = true;
                const response = await this.$http.get('/api/users');
                this.users = response.data;
            } catch (error) {
                this.$notify({
                    type: 'error',
                    message: error.response?.data?.message || 'Error al cargar usuarios'
                });
            } finally {
                this.loading = false;
            }
        },
        
        async updateRole(userId, newRole) {
            try {
                await this.$http.patch(`/api/users/${userId}/role`, { role: newRole });
                this.$notify({
                    type: 'success',
                    message: 'Rol actualizado exitosamente'
                });
            } catch (error) {
                this.$notify({
                    type: 'error',
                    message: error.response?.data?.message || 'Error al actualizar rol'
                });
                await this.loadUsers(); // Recargar para revertir cambio visual
            }
        },
        
        async toggleStatus(user) {
            try {
                await this.$http.patch(`/api/users/${user.id}/status`, { 
                    active: !user.active 
                });
                this.$notify({
                    type: 'success',
                    message: `Usuario ${!user.active ? 'activado' : 'desactivado'}`
                });
                await this.loadUsers();
            } catch (error) {
                this.$notify({
                    type: 'error',
                    message: error.response?.data?.message
                });
            }
        },
        
        async deleteUser(user) {
            if (!confirm(`¿Eliminar usuario ${user.username}?`)) return;
            
            try {
                await this.$http.delete(`/api/users/${user.id}`);
                this.$notify({
                    type: 'success',
                    message: 'Usuario eliminado'
                });
                await this.loadUsers();
            } catch (error) {
                this.$notify({
                    type: 'error',
                    message: error.response?.data?.message
                });
            }
        }
    }
};
</script>
```

### Mensajes de Error Específicos

El backend retorna mensajes claros para cada situación:

#### 1. Protección del Último Admin

```json
{
    "status": 400,
    "error": "Business Error",
    "message": "No se puede cambiar el rol del único administrador del sistema"
}
```

```json
{
    "status": 400,
    "error": "Business Error",
    "message": "No se puede desactivar el único administrador activo del sistema"
}
```

```json
{
    "status": 400,
    "error": "Business Error",
    "message": "No se puede eliminar el único administrador del sistema"
}
```

#### 2. Sin Permisos

```json
{
    "status": 403,
    "error": "Forbidden",
    "message": "No tiene permisos para acceder a este recurso"
}
```

#### 3. Usuario No Encontrado

```json
{
    "status": 404,
    "error": "Not Found",
    "message": "User not found with ID: 123"
}
```

### Validaciones Importantes

#### En el Frontend

```typescript
// Validar antes de cambiar rol
const canChangeRole = (user: User, currentUserRole: string) => {
    // Solo ADMIN puede cambiar roles
    if (currentUserRole !== 'ADMIN') {
        return false;
    }
    
    // No permitir cambiar el propio rol si eres el único admin
    // (el backend también valida esto)
    return true;
};

// Validar antes de desactivar
const canDeactivate = (user: User, allUsers: User[]) => {
    if (user.role === 'ADMIN') {
        const activeAdmins = allUsers.filter(
            u => u.role === 'ADMIN' && u.active
        ).length;
        
        if (activeAdmins <= 1) {
            toast.warning('No se puede desactivar el único administrador activo');
            return false;
        }
    }
    return true;
};
```

### Estilos CSS Sugeridos

```css
/* styles/UserManagement.css */
.user-management {
    padding: 20px;
}

.users-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
}

.users-table th,
.users-table td {
    padding: 12px;
    text-align: left;
    border-bottom: 1px solid #ddd;
}

.users-table th {
    background-color: #f5f5f5;
    font-weight: 600;
}

.role-select {
    padding: 6px 12px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
}

.status-badge {
    padding: 4px 12px;
    border-radius: 12px;
    font-size: 12px;
    font-weight: 500;
}

.status-badge.active {
    background-color: #d4edda;
    color: #155724;
}

.status-badge.inactive {
    background-color: #f8d7da;
    color: #721c24;
}

.actions {
    display: flex;
    gap: 8px;
}

.btn-toggle,
.btn-delete {
    padding: 6px 12px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
}

.btn-toggle {
    background-color: #007bff;
    color: white;
}

.btn-toggle:hover {
    background-color: #0056b3;
}

.btn-delete {
    background-color: #dc3545;
    color: white;
}

.btn-delete:hover {
    background-color: #c82333;
}
```

### Flujo Completo de Usuario

```
1. Usuario se registra → Rol: USER (por defecto)
   POST /api/auth/register
   
2. Usuario inicia sesión → Recibe token JWT
   POST /api/auth/login
   
3. ADMIN ve lista de usuarios → Identifica nuevo usuario
   GET /api/users
   
4. ADMIN cambia rol a SALES → Usuario ahora es vendedor
   PATCH /api/users/{id}/role
   Body: { "role": "SALES" }
   
5. Si usuario se porta mal → ADMIN lo desactiva
   PATCH /api/users/{id}/status
   Body: { "active": false }
   
6. Usuario desactivado intenta login → Recibe error
   POST /api/auth/login
   Response: 401 "User account is disabled"
```

### Testing en Frontend

```typescript
// __tests__/UserManagement.test.tsx
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import UserManagement from '../components/UserManagement';
import userManagementService from '../services/userManagementService';

jest.mock('../services/userManagementService');

describe('UserManagement', () => {
    const mockUsers = [
        {
            id: 1,
            username: 'admin',
            email: 'admin@test.com',
            firstName: 'Admin',
            lastName: 'User',
            role: 'ADMIN',
            active: true,
            createdAt: '2024-01-01',
            lastLogin: '2024-01-15'
        },
        {
            id: 2,
            username: 'user1',
            email: 'user1@test.com',
            firstName: 'Test',
            lastName: 'User',
            role: 'USER',
            active: true,
            createdAt: '2024-01-02',
            lastLogin: null
        }
    ];

    beforeEach(() => {
        (userManagementService.getAllUsers as jest.Mock).mockResolvedValue(mockUsers);
    });

    it('should load and display users', async () => {
        render(<UserManagement />);
        
        await waitFor(() => {
            expect(screen.getByText('admin')).toBeInTheDocument();
            expect(screen.getByText('user1')).toBeInTheDocument();
        });
    });

    it('should update user role', async () => {
        (userManagementService.updateUserRole as jest.Mock).mockResolvedValue({
            ...mockUsers[1],
            role: 'SALES'
        });

        render(<UserManagement />);
        
        await waitFor(() => {
            const selects = screen.getAllByRole('combobox');
            fireEvent.change(selects[1], { target: { value: 'SALES' } });
        });

        await waitFor(() => {
            expect(userManagementService.updateUserRole).toHaveBeenCalledWith(2, 'SALES');
        });
    });

    it('should handle error when updating role fails', async () => {
        const errorMessage = 'No se puede cambiar el rol del único administrador';
        (userManagementService.updateUserRole as jest.Mock).mockRejectedValue(
            new Error(errorMessage)
        );

        render(<UserManagement />);
        
        // Simular cambio de rol
        await waitFor(() => {
            const selects = screen.getAllByRole('combobox');
            fireEvent.change(selects[0], { target: { value: 'USER' } });
        });

        // Verificar que se muestra el error
        await waitFor(() => {
            expect(screen.getByText(errorMessage)).toBeInTheDocument();
        });
    });
});
```

---

## 🧪 Problema con Tests y JWT Secret

### Contexto del Problema

Durante la ejecución de tests, se encontró el error:
```
Could not resolve placeholder 'APP_JWT_SECRET'
```

### ✅ Solución Aplicada en Backend

Se creó `src/test/resources/application.yml` con un valor por defecto para tests:

```yaml
app:
  jwt:
    secret: ${APP_JWT_SECRET:test-secret-key-with-at-least-32-characters-for-testing-purposes-only}
    expiration: 86400000
```

### 🔧 Acción Requerida en Frontend

**NO SE REQUIERE NINGÚN CAMBIO EN EL FRONTEND** para este problema, ya que fue un issue interno de configuración de tests del backend.

Sin embargo, es importante que el frontend:

1. **Siempre envíe el token JWT en el header:**
```javascript
headers: {
    'Authorization': `Bearer ${token}`
}
```

2. **Maneje correctamente la expiración del token:**
```javascript
// Verificar si el token está expirado
const isTokenExpired = (token) => {
    try {
        const decoded = jwt_decode(token);
        return decoded.exp < Date.now() / 1000;
    } catch (error) {
        return true;
    }
};

// Renovar token si es necesario
if (isTokenExpired(currentToken)) {
    await refreshToken();
}
```

---

## 📋 Checklist de Cambios en Frontend

### Cambios Obligatorios

- [ ] **Verificar rutas de endpoints de órdenes** (deben ser `/api/orders`, no `/api/api/orders`)
- [ ] **Agregar campo `createdBy` al crear órdenes** (opcional pero recomendado)
- [ ] **Actualizar interfaces TypeScript** (si aplica)
- [ ] **Mejorar manejo de errores** para mostrar mensajes específicos del backend

### Cambios Recomendados

- [ ] **Implementar notificaciones toast** para errores y éxitos
- [ ] **Validar datos antes de enviar** (stock, crédito, etc.)
- [ ] **Agregar loading states** durante las peticiones
- [ ] **Implementar retry logic** para errores de red
- [ ] **Agregar logs de auditoría** en el frontend

### Testing

- [ ] **Probar creación de órdenes** con el nuevo campo `createdBy`
- [ ] **Verificar que los mensajes de error se muestren correctamente**
- [ ] **Probar todos los endpoints de órdenes** con las nuevas rutas
- [ ] **Validar manejo de tokens JWT**

---

## 🔍 Ejemplos de Código Completo

### Ejemplo Completo: Servicio de Órdenes (React + TypeScript)

```typescript
// services/orderService.ts
import axios from 'axios';

const API_BASE_URL = 'https://drogueria-bellavista-api.onrender.com/api';

export interface CreateOrderRequest {
    customerId: number;
    items: Array<{
        productId: number;
        quantity: number;
    }>;
    notes?: string;
    createdBy?: string;  // ✅ NUEVO
}

export interface OrderResponse {
    id: number;
    orderNumber: string;
    customerId: number;
    customerName: string;
    status: string;
    total: number;
    items: Array<{
        id: number;
        productId: number;
        productCode: string;
        productName: string;
        quantity: number;
        unitPrice: number;
        subtotal: number;
    }>;
    orderDate: string;
    createdAt: string;
    updatedAt: string;
    createdBy: string;  // ✅ NUEVO
}

class OrderService {
    private getAuthHeader() {
        const token = localStorage.getItem('token');
        return {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };
    }

    async createOrder(orderData: CreateOrderRequest): Promise<OrderResponse> {
        try {
            // ✅ Agregar usuario actual si está disponible
            const currentUser = this.getCurrentUser();
            if (currentUser && !orderData.createdBy) {
                orderData.createdBy = currentUser.username;
            }

            const response = await axios.post<OrderResponse>(
                `${API_BASE_URL}/orders`,  // ✅ Ruta correcta
                orderData,
                { headers: this.getAuthHeader() }
            );

            return response.data;
        } catch (error: any) {
            // ✅ Propagar el mensaje específico del backend
            const message = error.response?.data?.message || 'Error al crear la orden';
            throw new Error(message);
        }
    }

    async getOrderById(id: number): Promise<OrderResponse> {
        try {
            const response = await axios.get<OrderResponse>(
                `${API_BASE_URL}/orders/${id}`,
                { headers: this.getAuthHeader() }
            );
            return response.data;
        } catch (error: any) {
            const message = error.response?.data?.message || 'Error al obtener la orden';
            throw new Error(message);
        }
    }

    async completeOrder(id: number): Promise<OrderResponse> {
        try {
            const response = await axios.patch<OrderResponse>(
                `${API_BASE_URL}/orders/${id}/complete`,
                {},
                { headers: this.getAuthHeader() }
            );
            return response.data;
        } catch (error: any) {
            const message = error.response?.data?.message || 'Error al completar la orden';
            throw new Error(message);
        }
    }

    private getCurrentUser() {
        const userStr = localStorage.getItem('user');
        return userStr ? JSON.parse(userStr) : null;
    }
}

export default new OrderService();
```

### Ejemplo Completo: Componente de Creación de Orden (React)

```typescript
// components/CreateOrderForm.tsx
import React, { useState } from 'react';
import { toast } from 'react-toastify';
import orderService, { CreateOrderRequest } from '../services/orderService';

const CreateOrderForm: React.FC = () => {
    const [loading, setLoading] = useState(false);
    const [formData, setFormData] = useState<CreateOrderRequest>({
        customerId: 0,
        items: [],
        notes: ''
    });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);

        try {
            const order = await orderService.createOrder(formData);
            
            // ✅ Éxito
            toast.success(`Orden ${order.orderNumber} creada exitosamente`);
            
            // Resetear formulario o redirigir
            // navigate(`/orders/${order.id}`);
            
        } catch (error: any) {
            // ✅ Mostrar mensaje específico del backend
            toast.error(error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            {/* Campos del formulario */}
            <button type="submit" disabled={loading}>
                {loading ? 'Creando...' : 'Crear Orden'}
            </button>
        </form>
    );
};

export default CreateOrderForm;
```

---

## 🚀 Despliegue y Verificación

### Pasos para Verificar los Cambios

1. **Verificar que el backend esté desplegado:**
```bash
curl https://drogueria-bellavista-api.onrender.com/api/actuator/health
```

2. **Probar endpoint de órdenes:**
```bash
# Login primero
curl -X POST https://drogueria-bellavista-api.onrender.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Crear orden con el token
curl -X POST https://drogueria-bellavista-api.onrender.com/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "customerId": 1,
    "items": [{"productId": 1, "quantity": 5}],
    "createdBy": "admin"
  }'
```

3. **Verificar en el frontend:**
- Abrir DevTools (F12)
- Ir a la pestaña Network
- Crear una orden
- Verificar que la URL sea `/api/orders` (no `/api/api/orders`)
- Verificar que el payload incluya `createdBy`
- Verificar que los errores muestren mensajes claros

---

## 📞 Soporte

Si tienes dudas sobre estos cambios:

1. Revisa la documentación completa en `docs/RESUMEN_METRICAS_IMPLEMENTACION.md`
2. Consulta los ejemplos de Postman en `Postman_Collection_Metricas.json`
3. Revisa el código del backend en:
   - `src/main/java/com/drogueria/bellavista/controller/OrderController.java`
   - `src/main/java/com/drogueria/bellavista/domain/service/OrderService.java`
   - `src/main/java/com/drogueria/bellavista/domain/model/Order.java`

---

**Última actualización:** Marzo 2026  
**Versión:** 1.0  
**Autor:** Equipo Droguería Bellavista
