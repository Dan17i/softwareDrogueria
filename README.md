
```markdown
# Droguería Bella vista - Backend

Sistema de gestión para droguería construido con Spring Boot siguiendo arquitectura hexagonal (Clean Architecture).

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/Dan17i/softwareDrogueria)

## 🏗️ Arquitectura

El proyecto sigue el patrón de Arquitectura Hexagonal (Ports & Adapters):
```

src/main/java/com/drogueria/bellavista/
│
├── domain/                          # Capa de Dominio (Lógica de Negocio)
│   ├── model/                       # Entidades de dominio
│   ├── repository/                  # Puertos de salida (interfaces)
│   └── service/                     # Servicios de dominio (lógica de negocio pura)
│
├── application/                     # Capa de Aplicación (Orquestación)
│   ├── dto/                         # DTOs para la API
│   ├── mapper/                      # Mappers DTO ↔ Domain
│   └── service/                     # Servicios de aplicación (e.g. AuthService)
│
├── infrastructure/                  # Capa de Infraestructura (Detalles técnicos)
│   ├── persistence/                 # Entidades JPA y repositorios Spring Data
│   ├── adapter/                     # Adaptadores que implementan los puertos de dominio
│   ├── mapper/                      # Mappers Entity ↔ Domain
│   └── security/                    # Filtros y utilidades JWT
│
├── controller/                      # Controladores REST (Puertos de entrada)
├── config/                          # Configuraciones (Security, CORS, PasswordEncoder)
└── exception/                       # Excepciones personalizadas
```
## 🚀 Tecnologías

- **Java 21**
- **Spring Boot 3.2.2**
- **Spring Security** (JWT con `JwtAuthenticationFilter` + `JwtUtils`)
- **Spring Data JPA**
- **PostgreSQL** (producción)
- **H2** (desarrollo/testing)
- **Lombok**
- **Maven**
- **Testcontainers** (pruebas de integración)

## 📦 Instalación

### Prerrequisitos

- Java 21
- Maven 3.8+
- Docker (para PostgreSQL con Docker Compose o para Testcontainers)

### Pasos

1. **Clonar el repositorio**
```
bash
git clone <repository-url>
cd softwareDrogueria
```
2. **Configurar la base de datos**

Para PostgreSQL, edita `src/main/resources/application.yml`:
```
yaml
spring:
datasource:
url: jdbc:postgresql://localhost:5432/drogueria_db
username: tu_usuario
password: tu_password
```
Para H2 (desarrollo), cambia el perfil a `application-dev.yml`:
```
yaml
spring:
datasource:
url: jdbc:h2:mem:testdb
driver-class-name: org.h2.Driver
h2:
console:
enabled: true
```
3. **Compilar el proyecto**
```
bash
mvn clean install
```
4. **Levantar servicios con Docker Compose**
```
bash
docker compose up -d
```
5. **Verificar que PostgreSQL esté corriendo**
```
bash
docker ps
```
6. **Configurar el secreto JWT**

Establece la variable de entorno `APP_JWT_SECRET` con una clave segura (mínimo 32 caracteres). Ejemplo (PowerShell):
```
powershell
$env:APP_JWT_SECRET = 'a-very-long-dev-secret-with-at-least-32-chars-123456'
mvn spring-boot:run
```
En Linux/macOS:
```
bash
export APP_JWT_SECRET='a-very-long-dev-secret-with-at-least-32-chars-123456'
mvn spring-boot:run
```
> ⚠️ Para producción, almacena los secretos en un gestor seguro (Vault, Azure KeyVault, etc.) y nunca en `application.yml`.

7. **Ejecutar la aplicación**
```
bash
mvn spring-boot:run
```
La API estará disponible en: `http://localhost:8080/api`

## 📚 API Endpoints

### Autenticación

| Método | Endpoint | Descripción | Auth requerida |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Registrar nuevo usuario | No |
| POST | `/api/auth/login` | Iniciar sesión (devuelve JWT) | No |
| POST | `/api/auth/dev-create-admin` | Crear usuario admin por defecto (**solo desarrollo**) | No |

**Registro:**
```
bash
curl -X POST http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{
"username": "juan123",
"email": "juan@test.com",
"password": "12345678",
"firstName": "Juan",
"lastName": "Perez"
}'
```
**Login:**
```
bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{
"username": "juan123",
"password": "12345678"
}'
```
Los endpoints protegidos deben incluir el token en cada request:
```

Authorization: Bearer <token>
```
---

### Productos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/products` | Crear producto |
| PUT | `/api/products/{id}` | Actualizar producto |
| GET | `/api/products/{id}` | Obtener producto por ID |
| GET | `/api/products/code/{code}` | Obtener producto por código |
| GET | `/api/products` | Listar todos los productos |
| GET | `/api/products?active=true` | Listar productos activos |
| GET | `/api/products/search?name=xxx` | Buscar por nombre |
| GET | `/api/products/category/{category}` | Listar por categoría |
| GET | `/api/products/restock-needed` | Productos con stock bajo |
| POST | `/api/products/{id}/reduce-stock` | Reducir stock |
| POST | `/api/products/{id}/increase-stock` | Aumentar stock |
| PATCH | `/api/products/{id}/toggle-status` | Activar/Desactivar producto |
| DELETE | `/api/products/{id}` | Eliminar producto |

---

### Clientes

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/customers` | Crear cliente |
| PUT | `/api/customers/{id}` | Actualizar cliente |
| GET | `/api/customers/{id}` | Obtener cliente por ID |
| GET | `/api/customers` | Listar todos los clientes |
| DELETE | `/api/customers/{id}` | Eliminar cliente |

---

### Proveedores

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/suppliers` | Crear proveedor |
| PUT | `/api/suppliers/{id}` | Actualizar proveedor |
| GET | `/api/suppliers/{id}` | Obtener proveedor por ID |
| GET | `/api/suppliers` | Listar todos los proveedores |
| DELETE | `/api/suppliers/{id}` | Eliminar proveedor |

---

### Órdenes

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/orders` | Crear orden |
| PUT | `/api/orders/{id}` | Actualizar orden |
| GET | `/api/orders/{id}` | Obtener orden por ID |
| GET | `/api/orders` | Listar todas las órdenes |
| DELETE | `/api/orders/{id}` | Eliminar orden |

---

### Recepciones de Mercancía (Goods Receipt)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/goods-receipts` | Crear recepción vinculada a una orden |
| GET | `/api/goods-receipts/{id}` | Obtener recepción por ID |
| GET | `/api/goods-receipts/number/{receiptNumber}` | Obtener por número de recepción |
| GET | `/api/goods-receipts/order/{orderId}` | Listar recepciones de una orden |
| GET | `/api/goods-receipts/supplier/{supplierId}` | Listar recepciones de un proveedor |
| GET | `/api/goods-receipts/status/{status}` | Filtrar por estado (`PENDING`, `RECEIVED`, ...) |
| GET | `/api/goods-receipts/pending` | Listar recepciones pendientes |
| PATCH | `/api/goods-receipts/{id}/receive` | Confirmar recepción y actualizar stock |
| PATCH | `/api/goods-receipts/{id}/reject` | Rechazar recepción (no actualiza stock) |
| DELETE | `/api/goods-receipts/{id}` | Eliminar recepción (solo si está `PENDING`) |

---

### Ejemplos de Uso

**Crear un producto:**
```
bash
curl -X POST http://localhost:8080/api/products \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <token>" \
-d '{
"code": "MED001",
"name": "Acetaminofén 500mg",
"description": "Analgésico y antipirético",
"price": 5000,
"stock": 100,
"minStock": 20,
"category": "Medicamentos"
}'
```
**Listar productos:**
```
bash
curl http://localhost:8080/api/products \
-H "Authorization: Bearer <token>"
```
**Reducir stock:**
```
bash
curl -X POST http://localhost:8080/api/products/1/reduce-stock \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <token>" \
-d '{"quantity": 5}'
```
## 🔐 Seguridad

El proyecto tiene Spring Security completamente integrado con autenticación basada en JWT.

### Componentes de seguridad

| Componente | Ubicación | Descripción |
|---|---|---|
| `JwtUtils` | `infrastructure.security` | Generación y validación de tokens JWT |
| `JwtAuthenticationFilter` | `infrastructure.security` | Filtro que intercepta y valida el JWT en cada request |
| `SecurityConfig` | `config` | Configuración de cadena de filtros y rutas públicas/protegidas |
| `PasswordEncoderConfig` | `config` | Bean de `BCryptPasswordEncoder` |
| `AuthService` | `application.service` | Orquesta registro, login y creación del admin |

### Roles disponibles (`domain.model.Role`)

| Rol | Descripción |
|-----|-------------|
| `ADMIN` | Administrador del sistema (acceso completo) |
| `MANAGER` | Gerente (reportes y gestión avanzada) |
| `SALES` | Representante de ventas (órdenes y clientes) |
| `WAREHOUSE` | Almacén (inventario y recepciones de mercancía) |
| `USER` | Usuario estándar (permisos limitados) |

- El rol se persiste en `UserEntity` como `EnumType.STRING`.
- Al construir `GrantedAuthority` se usa la convención `ROLE_<ROLE_NAME>` (ej. `ROLE_ADMIN`).
- La lógica de roles está centralizada en `domain.model.Role`.

### Crear usuario administrador inicial

**Opción 1 — Endpoint de desarrollo:**
```
bash
curl -X POST http://localhost:8080/api/auth/dev-create-admin
```
**Opción 2 — SQL directo (H2 / PostgreSQL):**
```
 SQL
INSERT INTO users (username, email, password, role, active, created_at)
VALUES ('admin', 'admin@example.com', '<bcrypt-hash>', 'ADMIN', true, CURRENT_TIMESTAMP);
```
> Genera el hash con `BCryptPasswordEncoder` de Spring Security.

## 🧪 Testing

### Ejecutar todos los tests
```bash
mvn test
```
```


### Ejecutar tests con reporte de cobertura
```shell script
mvn test jacoco:report
```


### Ejecutar prueba de integración específica
```shell script
mvn -Dtest=AuthOrderIntegrationTest test
```


Las pruebas de integración utilizan **Testcontainers** para levantar una instancia de PostgreSQL aislada (`postgres:15-alpine`) y validar el flujo completo de autenticación + creación de pedido.

> Si no tienes Docker disponible, ejecuta las pruebas con el perfil H2.

Se incluyen pruebas unitarias para `GoodsReceiptService` en:
`src/test/java/com/drogueria/bellavista/domain/service/GoodsReceiptServiceTest.java`

## 🏛️ Principios de Arquitectura Hexagonal

### 1. **Dominio (Core)**
- Contiene la lógica de negocio pura
- No tiene dependencias externas (frameworks, librerías)
- Define interfaces (puertos) para comunicación hacia afuera

### 2. **Aplicación**
- Orquesta los casos de uso
- Convierte entre DTO y modelos de dominio
- Maneja validaciones de entrada

### 3. **Infraestructura**
- Implementa los puertos definidos en el dominio
- Maneja detalles técnicos (BD, seguridad, API externas)
- Adaptadores de persistencia JPA y filtros de seguridad

### 4. **Controladores (Adaptadores de Entrada)**
- Exponen la API REST
- Convierten requests HTTP a llamadas de dominio

## 🔍 Ventajas de esta Arquitectura

✅ **Independencia de frameworks**: El dominio no depende de Spring  
✅ **Testeable**: Fácil de hacer unit tests sin infraestructura  
✅ **Mantenible**: Separación clara de responsabilidades  
✅ **Flexible**: Fácil cambiar BD o exponer otra API  
✅ **Escalable**: Cada capa puede evolucionar independientemente

## 📝 Estructura de Carpetas Completa

```
softwareDrogueria/
├── src/
│   ├── main/
│   │   ├── java/com/drogueria/bellavista/
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   ├── Customer.java
│   │   │   │   │   ├── GoodsReceipt.java
│   │   │   │   │   ├── GoodsReceiptItem.java
│   │   │   │   │   ├── Order.java
│   │   │   │   │   ├── OrderItem.java
│   │   │   │   │   ├── Product.java
│   │   │   │   │   ├── Role.java
│   │   │   │   │   ├── Supplier.java
│   │   │   │   │   └── User.java
│   │   │   │   ├── repository/
│   │   │   │   │   ├── CustomerRepository.java
│   │   │   │   │   ├── GoodsReceiptRepository.java
│   │   │   │   │   ├── OrderRepository.java
│   │   │   │   │   ├── ProductRepository.java
│   │   │   │   │   ├── SupplierRepository.java
│   │   │   │   │   └── UserRepository.java
│   │   │   │   └── service/
│   │   │   │       ├── CustomerService.java
│   │   │   │       ├── GoodsReceiptService.java
│   │   │   │       ├── OrderService.java
│   │   │   │       ├── ProductService.java
│   │   │   │       ├── SupplierService.java
│   │   │   │       └── UserService.java
│   │   │   │
│   │   │   ├── application/
│   │   │   │   ├── dto/
│   │   │   │   │   ├── AuthResponseDTO.java
│   │   │   │   │   ├── CustomerDTO.java
│   │   │   │   │   ├── GoodsReceiptDTO.java
│   │   │   │   │   ├── LoginRequestDTO.java
│   │   │   │   │   ├── OrderDTO.java
│   │   │   │   │   ├── ProductDTO.java
│   │   │   │   │   ├── RegisterRequestDTO.java
│   │   │   │   │   ├── SupplierDTO.java
│   │   │   │   │   └── UserResponseDTO.java
│   │   │   │   ├── mapper/
│   │   │   │   │   ├── CustomerUseCaseMapper.java
│   │   │   │   │   ├── GoodsReceiptUseCaseMapper.java
│   │   │   │   │   ├── OrderUseCaseMapper.java
│   │   │   │   │   ├── ProductUseCaseMapper.java
│   │   │   │   │   └── SupplierUseCaseMapper.java
│   │   │   │   └── service/
│   │   │   │       └── AuthService.java
│   │   │   │
│   │   │   ├── infrastructure/
│   │   │   │   ├── persistence/
│   │   │   │   │   ├── CustomerEntity.java
│   │   │   │   │   ├── GoodsReceiptEntity.java
│   │   │   │   │   ├── GoodsReceiptItemEntity.java
│   │   │   │   │   ├── JpaCustomerRepository.java
│   │   │   │   │   ├── JpaGoodsReceiptItemRepository.java
│   │   │   │   │   ├── JpaGoodsReceiptRepository.java
│   │   │   │   │   ├── JpaOrderItemRepository.java
│   │   │   │   │   ├── JpaOrderRepository.java
│   │   │   │   │   ├── JpaProductRepository.java
│   │   │   │   │   ├── JpaSupplierRepository.java
│   │   │   │   │   ├── JpaUserRepository.java
│   │   │   │   │   ├── OrderEntity.java
│   │   │   │   │   ├── OrderItemEntity.java
│   │   │   │   │   ├── ProductEntity.java
│   │   │   │   │   ├── SupplierEntity.java
│   │   │   │   │   └── UserEntity.java
│   │   │   │   ├── adapter/
│   │   │   │   │   ├── CustomerRepositoryAdapter.java
│   │   │   │   │   ├── GoodsReceiptRepositoryAdapter.java
│   │   │   │   │   ├── OrderRepositoryAdapter.java
│   │   │   │   │   ├── ProductRepositoryAdapter.java
│   │   │   │   │   ├── SupplierRepositoryAdapter.java
│   │   │   │   │   └── UserRepositoryAdapter.java
│   │   │   │   ├── mapper/
│   │   │   │   │   ├── CustomerMapper.java
│   │   │   │   │   ├── GoodsReceiptMapper.java
│   │   │   │   │   ├── OrderMapper.java
│   │   │   │   │   ├── ProductMapper.java
│   │   │   │   │   ├── SupplierMapper.java
│   │   │   │   │   └── UserMapper.java
│   │   │   │   └── security/
│   │   │   │       ├── JwtAuthenticationFilter.java
│   │   │   │       └── JwtUtils.java
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CustomerController.java
│   │   │   │   ├── GoodsReceiptController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── ProductController.java
│   │   │   │   └── SupplierController.java
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── PasswordEncoderConfig.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── WebConfig.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── AuthenticationException.java
│   │   │   │   ├── BusinessException.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   │
│   │   │   └── BellavistaApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.yml
│   │
│   └── test/
│       └── java/com/drogueria/bellavista/
│           └── domain/service/
│               └── GoodsReceiptServiceTest.java
│
├── http/
│   └── auth.http
├── docker-compose.yml
├── pom.xml
├── ARCHITECTURE.md
├── QUICKSTART.md
├── SOLID_AND_PATTERNS_ANALYSIS.md
└── README.md
```


## 📧 Contacto

Para más información o soporte, contacta al equipo de desarrollo.

## 📄 Licencia

Este proyecto es propiedad de Droguería Bellavista.
```
---

### Resumen de cambios aplicados

| # | Sección | Cambio |
|---|---------|--------|
| 1 | Arquitectura | `usecase/` → `service/` en capa `application`; agregada `infrastructure/security/` |
| 2 | Tecnologías | Agregados Spring Security, Testcontainers |
| 3 | Prerrequisitos | `Java 17` → `Java 21`; Docker como requisito explícito |
| 4 | Instalación | Agregado paso de configuración de `APP_JWT_SECRET` con ejemplo Linux y PowerShell |
| 5 | API Endpoints | Nueva tabla de `/api/auth/*` como **ya implementados**; tablas de Clientes, Proveedores y Órdenes agregadas |
| 6 | Ejemplos cURL | Agregado header `Authorization` en los ejemplos |
| 7 | Seguridad | Sección reescrita: de "Próximos pasos" a "Implementado", con tabla de componentes y roles |
| 8 | Admin inicial | Agregado endpoint `dev-create-admin` como opción rápida |
| 9 | Estructura | Árbol completo con **todos** los archivos reales del proyecto |
| 10 | Excepciones | Agregada `AuthenticationException` que existía pero no estaba documentada |
```
