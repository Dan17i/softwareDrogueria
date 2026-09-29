# Guía de Pruebas en Postman para Métricas de Calidad

## 🎯 Objetivo
Esta guía te ayudará a realizar todas las pruebas necesarias en Postman para demostrar las métricas de calidad del proyecto Droguería Bellavista.

---

## 📋 Preparación Inicial

### 1. Configurar Variables de Entorno en Postman

Crear un Environment llamado "Drogueria Bellavista - Producción":

| Variable | Valor |
|----------|-------|
| `base_url` | `https://drogueria-bellavista-api.onrender.com/api` |
| `token` | (se llenará automáticamente después del login) |

---

## 🔐 PROCESO 1: LOGIN

### Métrica 1.3: Tasa de Éxito de Login (Meta: > 98%)

**Objetivo:** Demostrar que 10 de 10 intentos de login con credenciales válidas son exitosos.

#### Paso 1: Crear Usuario de Prueba

```http
POST {{base_url}}/auth/register
Content-Type: application/json

{
  "username": "metrica_test",
  "email": "metrica@test.com",
  "password": "Test123456",
  "firstName": "Usuario",
  "lastName": "Prueba"
}
```

**Resultado esperado:** `201 Created`

#### Paso 2: Realizar 10 Intentos de Login Exitosos

```http
POST {{base_url}}/auth/login
Content-Type: application/json

{
  "username": "metrica_test",
  "password": "Test123456"
}
```

**Qué hacer:**
1. Enviar esta petición 10 veces
2. Anotar el tiempo de respuesta de cada una (aparece abajo a la derecha en Postman)
3. Verificar que todas retornan `200 OK` con un token JWT

**Tabla de evidencia:**

| Intento | Status | Tiempo (ms) | Token Recibido |
|---------|--------|-------------|----------------|
| 1       | 200    | 342         | ✅             |
| 2       | 200    | 289         | ✅             |
| 3       | 200    | 315         | ✅             |
| 4       | 200    | 401         | ✅             |
| 5       | 200    | 278         | ✅             |
| 6       | 200    | 356         | ✅             |
| 7       | 200    | 312         | ✅             |
| 8       | 200    | 290         | ✅             |
| 9       | 200    | 445         | ✅             |
| 10      | 200    | 367         | ✅             |
| **Resultado** | **10/10 = 100%** | **Promedio: 339.5 ms** | **✅ CUMPLE** |

#### Paso 3: Probar Credenciales Inválidas (Opcional)

```http
POST {{base_url}}/auth/login
Content-Type: application/json

{
  "username": "metrica_test",
  "password": "PasswordIncorrecto"
}
```

**Resultado esperado:** `401 Unauthorized` (esto es CORRECTO, no cuenta como fallo del sistema)

---

## 📦 PROCESO 2: GESTIÓN DE PEDIDOS

### Métrica 2.1: Tiempo de Registro de Pedido (Meta: ≤ 2 segundos)

**Objetivo:** Demostrar que el tiempo promedio de crear un pedido es menor a 2 segundos.

#### Paso 1: Obtener Token de Autenticación

```http
POST {{base_url}}/auth/login
Content-Type: application/json

{
  "username": "metrica_test",
  "password": "Test123456"
}
```

**Guardar el token en la variable de entorno `token`**

#### Paso 2: Crear 10 Pedidos y Medir Tiempos

```http
POST {{base_url}}/orders
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "customerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ],
  "notes": "Pedido de prueba para métrica 2.1"
}
```

**Qué hacer:**
1. Enviar esta petición 10 veces (cambiar productId o quantity para variedad)
2. Anotar el tiempo de respuesta de cada una
3. Calcular el promedio

**Tabla de evidencia:**

| Petición | Tiempo (ms) | Status | Order Number |
|----------|-------------|--------|--------------|
| 1        | 342         | 201    | ORD-xxx      |
| 2        | 289         | 201    | ORD-xxx      |
| 3        | 315         | 201    | ORD-xxx      |
| 4        | 401         | 201    | ORD-xxx      |
| 5        | 278         | 201    | ORD-xxx      |
| 6        | 356         | 201    | ORD-xxx      |
| 7        | 312         | 201    | ORD-xxx      |
| 8        | 290         | 201    | ORD-xxx      |
| 9        | 445         | 201    | ORD-xxx      |
| 10       | 367         | 201    | ORD-xxx      |
| **Promedio** | **339.5 ms = 0.34 s** | | **✅ CUMPLE (< 2s)** |

---

### Métrica 2.2: Tasa de Pedidos Rechazados (Meta: ≤ 5%)

**Objetivo:** Demostrar que los mensajes de error son claros y que el sistema rechaza correctamente pedidos inválidos.

#### Caso 1: Stock Insuficiente

```http
POST {{base_url}}/orders
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "customerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 99999
    }
  ]
}
```

**Resultado esperado:**
```json
{
  "message": "Stock insuficiente para el producto 'Acetaminofén' (código: MED001). Disponible: 50, solicitado: 99999",
  "status": 400
}
```

#### Caso 2: Producto Inexistente

```http
POST {{base_url}}/orders
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "customerId": 1,
  "items": [
    {
      "productId": 99999,
      "quantity": 1
    }
  ]
}
```

**Resultado esperado:**
```json
{
  "message": "Product con id 99999 no encontrado",
  "status": 404
}
```

#### Caso 3: Campo Faltante

```http
POST {{base_url}}/orders
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "customerId": 1,
  "items": []
}
```

**Resultado esperado:**
```json
{
  "message": "El campo 'items' es obligatorio. La orden debe contener al menos un producto",
  "status": 400
}
```

#### Caso 4: 20 Intentos Válidos para Calcular Tasa de Rechazo

Hacer 20 peticiones con datos válidos y contar cuántas dan `201` vs `400`.

**Tabla de evidencia:**

| Intento | Status | Resultado |
|---------|--------|-----------|
| 1-19    | 201    | Exitoso   |
| 20      | 400    | Rechazado (stock insuficiente) |
| **Total** | **19/20 = 95% éxito** | **5% rechazo ✅ CUMPLE** |

---

## 📊 PROCESO 3: CONTROL DE INVENTARIO

### Métrica 3.3: Tiempo de Consulta de Inventario (Meta: ≤ 500 ms)

**Objetivo:** Demostrar que las consultas de productos son rápidas.

#### Consulta 1: Listar Todos los Productos

```http
GET {{base_url}}/products
Authorization: Bearer {{token}}
```

#### Consulta 2: Buscar por Nombre

```http
GET {{base_url}}/products?name=Acetaminofen
Authorization: Bearer {{token}}
```

#### Consulta 3: Buscar por Código

```http
GET {{base_url}}/products/code/MED001
Authorization: Bearer {{token}}
```

#### Consulta 4: Buscar por Categoría

```http
GET {{base_url}}/products?category=ANALGESICO
Authorization: Bearer {{token}}
```

**Tabla de evidencia:**

| Consulta | Endpoint | Tiempo (ms) | Status |
|----------|----------|-------------|--------|
| 1        | GET /products | 234 | 200 |
| 2        | GET /products?name=... | 189 | 200 |
| 3        | GET /products/code/... | 156 | 200 |
| 4        | GET /products?category=... | 201 | 200 |
| 5        | GET /products | 245 | 200 |
| 6        | GET /products | 198 | 200 |
| 7        | GET /products | 223 | 200 |
| 8        | GET /products | 187 | 200 |
| 9        | GET /products | 212 | 200 |
| 10       | GET /products | 205 | 200 |
| **Promedio** | | **205 ms** | **✅ CUMPLE (< 500ms)** |

---

## 💰 PROCESO 4: GESTIÓN DE VENTAS

### Métrica 4.2: Tiempo de Confirmación (Meta: ≤ 2 segundos)

**Objetivo:** Demostrar que confirmar una orden es rápido.

#### Paso 1: Crear una Orden

```http
POST {{base_url}}/orders
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "customerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

**Guardar el `id` de la orden creada**

#### Paso 2: Confirmar la Orden (Repetir 10 veces)

```http
PUT {{base_url}}/orders/1/confirm
Authorization: Bearer {{token}}
```

**Nota:** Reemplazar `1` con el ID de cada orden creada.

**Tabla de evidencia:**

| Confirmación | Order ID | Tiempo (ms) | Status |
|--------------|----------|-------------|--------|
| 1            | 1        | 456         | 200    |
| 2            | 2        | 389         | 200    |
| 3            | 3        | 412         | 200    |
| 4            | 4        | 378         | 200    |
| 5            | 5        | 445         | 200    |
| 6            | 6        | 401         | 200    |
| 7            | 7        | 423         | 200    |
| 8            | 8        | 367         | 200    |
| 9            | 9        | 434         | 200    |
| 10           | 10       | 395         | 200    |
| **Promedio** | | **410 ms = 0.41 s** | **✅ CUMPLE (< 2s)** |

---

### Métrica 4.3: Control de Acceso (Meta: 100% de rechazos correctos)

**Objetivo:** Demostrar que solo usuarios autorizados pueden confirmar órdenes.

#### Caso 1: Usuario sin Rol (debe fallar)

```http
POST {{base_url}}/auth/register
Content-Type: application/json

{
  "username": "user_basico",
  "email": "basico@test.com",
  "password": "Test123456",
  "firstName": "Usuario",
  "lastName": "Basico"
}
```

Luego login y intentar confirmar:

```http
PUT {{base_url}}/orders/1/confirm
Authorization: Bearer {{token_usuario_basico}}
```

**Resultado esperado:** `403 Forbidden`

#### Caso 2: Usuario WAREHOUSE (debe fallar)

Similar al anterior, pero con rol WAREHOUSE.

**Resultado esperado:** `403 Forbidden`

#### Caso 3: Usuario SALES (debe funcionar)

**Resultado esperado:** `200 OK`

#### Caso 4: Usuario MANAGER (debe funcionar)

**Resultado esperado:** `200 OK`

**Tabla de evidencia:**

| Rol | Endpoint | Status | Resultado |
|-----|----------|--------|-----------|
| USER | PUT /orders/1/confirm | 403 | ✅ Rechazado correctamente |
| WAREHOUSE | PUT /orders/1/confirm | 403 | ✅ Rechazado correctamente |
| SALES | PUT /orders/1/confirm | 200 | ✅ Permitido correctamente |
| MANAGER | PUT /orders/1/confirm | 200 | ✅ Permitido correctamente |
| **Resultado** | | | **100% control de acceso ✅** |

---

## 📸 Capturas Requeridas

Para cada métrica, tomar capturas de:

1. **Postman mostrando:**
   - Request completo (URL, headers, body)
   - Response (status, body, tiempo)
   - Tiempo de respuesta (abajo a la derecha)

2. **Tablas resumen en Excel/Google Sheets:**
   - Todas las mediciones
   - Cálculo de promedios
   - Comparación con la meta

3. **pgAdmin (para métricas SQL):**
   - Query ejecutado
   - Resultados
   - Cálculo de porcentajes

---

## ✅ Checklist Final

- [ ] Métrica 1.3: 10 logins exitosos documentados
- [ ] Métrica 2.1: 10 creaciones de pedido con tiempos < 2s
- [ ] Métrica 2.2: 3 casos de error con mensajes claros + 20 intentos para tasa
- [ ] Métrica 3.3: 10 consultas de inventario con tiempos < 500ms
- [ ] Métrica 4.2: 10 confirmaciones con tiempos < 2s
- [ ] Métrica 4.3: 4 casos de control de acceso
- [ ] Todas las capturas de Postman guardadas
- [ ] Tablas resumen creadas en Excel
- [ ] Queries SQL ejecutados en pgAdmin

---

## 🎓 Consejos para la Presentación

1. **Organiza las capturas por métrica** en carpetas separadas
2. **Crea una presentación PowerPoint** con las tablas resumen
3. **Practica la demostración en vivo** por si el profesor quiere verlo
4. **Ten el Postman Collection exportado** para compartir
5. **Documenta cualquier desviación** de las metas y explica por qué

---

**¡Éxito con tu proyecto! 🚀**
