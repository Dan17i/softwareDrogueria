# 🔄 Cambios Necesarios en el Frontend

## Droguería Bellavista - Actualización por Métricas de Calidad

---

## 📋 Resumen de Cambios en el Backend

Se realizaron las siguientes modificaciones en el backend para implementar las métricas de calidad:

1. **Nuevo campo `createdBy` en órdenes** (Métrica 2.3 - Auditoría)
2. **Mensajes de error mejorados** (Métrica 2.2 - Claridad)
3. **Corrección de ruta del controlador de órdenes** (Fix de integración)

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
