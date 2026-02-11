# Droguería Bellavista - Backend

Sistema de gestión para droguería construido con Spring Boot siguiendo arquitectura hexagonal (Clean Architecture).

## 🏗️ Arquitectura

El proyecto sigue el patrón de Arquitectura Hexagonal (Ports & Adapters):

```
src/main/java/com/drogueria/bellavista/
│
├── domain/                          # Capa de Dominio (Lógica de Negocio)
│   ├── model/                       # Entidades de dominio
│   ├── repository/                  # Puertos de salida (interfaces)
│   └── service/                     # Casos de uso / Servicios de dominio
│
├── application/                     # Capa de Aplicación (Casos de Uso)
│   ├── dto/                         # DTOs para API
│   ├── mapper/                      # Mappers DTO ↔ Domain
│   └── usecase/                     # Casos de uso específicos
│
├── infrastructure/                  # Capa de Infraestructura (Detalles técnicos)
│   ├── persistence/                 # Entidades JPA y repositorios
│   ├── adapter/                     # Adaptadores de repositorio
│   └── mapper/                      # Mappers Entity ↔ Domain
│
├── controller/                      # Controladores REST (Puerto de entrada)
├── config/                          # Configuraciones
├── exception/                       # Excepciones personalizadas
└── util/                           # Utilidades
```

## 🚀 Tecnologías

- **Java 21**
- **Spring Boot 3.2.2**
- **Spring Data JPA**
- **PostgreSQL** (producción)
- **H2** (desarrollo/testing)
- **Lombok**
- **Maven**

## 📦 Instalación

### Prerequisitos

- Java 17 o superior
- Maven 3.8+
- PostgreSQL 12+ (opcional, puede usar H2)

### Pasos

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd bellavista
```

2. **Configurar la base de datos**

Para PostgreSQL, edita `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/drogueria_db
    username: tu_usuario
    password: tu_password
```

Para H2 (desarrollo), cambia a `application-dev.yml`:
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  h2:
    console:
      enabled: true
```

3. **Compilar el proyecto**
```bash
mvn clean install
```

4. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

La API estará disponible en: `http://localhost:8080/api`

## 📚 API Endpoints

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
| PATCH | `/api/products/{id}/toggle-status` | Activar/Desactivar |
| DELETE | `/api/products/{id}` | Eliminar producto |

### Recepciones de Mercancía (Goods Receipt)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/goods-receipts` | Crear recepción de mercancía vinculada a una orden |
| GET | `/api/goods-receipts/{id}` | Obtener recepción por ID |
| GET | `/api/goods-receipts/number/{receiptNumber}` | Obtener por número de recepción |
| GET | `/api/goods-receipts/order/{orderId}` | Listar recepciones de una orden |
| GET | `/api/goods-receipts/supplier/{supplierId}` | Listar recepciones de un proveedor |
| GET | `/api/goods-receipts/status/{status}` | Filtrar por estado (PENDING, RECEIVED, ...) |
| GET | `/api/goods-receipts/pending` | Listar recepciones pendientes |
| PATCH | `/api/goods-receipts/{id}/receive` | Confirmar recepción y actualizar stock |
| PATCH | `/api/goods-receipts/{id}/reject` | Rechazar recepción (no actualiza stock) |
| DELETE | `/api/goods-receipts/{id}` | Eliminar recepción (solo PENDING) |

### Roles y Autorización

El sistema ahora incluye una definición de roles en `domain.model.Role` para control de acceso y autorización.

- Roles disponibles (enum `Role`):
  - `ADMIN` — Administrador del sistema (acceso completo).
  - `MANAGER` — Gerente de ventas (reportes, gestión avanzada).
  - `SALES` — Representante de ventas (crear/gestionar órdenes y clientes).
  - `WAREHOUSE` — Personal de almacén (gestión de inventario y recepciones).
  - `USER` — Usuario estándar (permisos limitados).

Buenas prácticas y notas de implementación:

- El `Role` se persiste en la entidad `UserEntity` como `EnumType.STRING`.
- Para mapear a Spring Security, usamos la convención `ROLE_<ROLE_NAME>` al construir `GrantedAuthority`. Por ejemplo:
  - `ROLE_ADMIN`, `ROLE_MANAGER`, etc.
- Se recomienda usar el helper `toAuthority()` o `"ROLE_" + role.name()` al construir autoridades.
- No se recomienda almacenar la definición de roles en texto plano en múltiples lugares; centraliza la lógica en `domain.model.Role`.
- Para internacionalización utiliza claves (i18n) en lugar de descripciones literales en el enum.

Autenticación y JWT

- El proyecto incluye utilidades para JWT en `infrastructure.security.JwtUtils`.
- Configuración recomendable para desarrollo: establecer la variable de entorno `APP_JWT_SECRET` con una clave segura (mínimo 32 bytes). Ejemplo (PowerShell):

```powershell
$env:APP_JWT_SECRET = 'a-very-long-dev-secret-with-at-least-32-chars-123456'
mvn spring-boot:run
```

- Para producción, almacena secretos en un gestor (Vault, KeyVault) y no en `application.yml`.
- Endpoints de autenticación (próximamente):
  - `POST /api/auth/register` — Registro de usuario
  - `POST /api/auth/login` — Inicio de sesión (devuelve JWT)
  - Endpoints protegidos deben recibir `Authorization: Bearer <token>`

Creación de usuario administrador inicial (dev)

Puedes crear un admin manualmente en base de datos o añadir un script de inicialización. Ejemplo SQL mínimo (H2/Postgres):

```sql
INSERT INTO users (username, email, password, role, active, created_at)
VALUES ('admin', 'admin@example.com', '<bcrypt-hash>', 'ADMIN', true, CURRENT_TIMESTAMP);
```

Usa BCrypt para generar el hash de la contraseña (por ejemplo con `PasswordEncoder` de Spring Security).


### Ejemplos de Uso

**Crear un producto:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
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
```bash
curl http://localhost:8080/api/products
```

**Buscar productos:**
```bash
curl http://localhost:8080/api/products/search?name=acetaminofen
```

**Reducir stock:**
```bash
curl -X POST http://localhost:8080/api/products/1/reduce-stock \
  -H "Content-Type: application/json" \
  -d '{"quantity": 5}'
```

## 🏛️ Principios de Arquitectura Hexagonal

### 1. **Dominio (Core)**
- Contiene la lógica de negocio pura
- No tiene dependencias externas (frameworks, librerías)
- Define interfaces (puertos) para comunicación

### 2. **Aplicación**
- Orquesta los casos de uso
- Convierte entre DTOs y modelos de dominio
- Maneja validaciones de entrada

### 3. **Infraestructura**
- Implementa los puertos definidos en el dominio
- Maneja detalles técnicos (BD, APIs externas)
- Adaptadores de persistencia (JPA)

### 4. **Controladores (Adaptadores de Entrada)**
- Exponen la API REST
- Convierten requests HTTP a llamadas de dominio

## 🔍 Ventajas de esta Arquitectura

✅ **Independencia de frameworks**: El dominio no depende de Spring
✅ **Testeable**: Fácil de hacer unit tests sin infraestructura
✅ **Mantenible**: Separación clara de responsabilidades
✅ **Flexible**: Fácil cambiar BD o exponer otra API
✅ **Escalable**: Cada capa puede evolucionar independientemente

## 🧪 Testing

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar tests con coverage
mvn test jacoco:report
```

Se han añadido pruebas unitarias básicas para `GoodsReceiptService` en `src/test/java/com/drogueria/bellavista/domain/service/GoodsReceiptServiceTest.java`.

## 📝 Estructura de Carpetas Completa

```
bellavista/
├── src/
│   ├── main/
│   │   ├── java/com/drogueria/bellavista/
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   └── Product.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── ProductRepository.java
│   │   │   │   └── service/
│   │   │   │       └── ProductService.java
│   │   │   │
│   │   │   ├── application/
│   │   │   │   ├── dto/
│   │   │   │   │   └── ProductDTO.java
│   │   │   │   └── mapper/
│   │   │   │       └── ProductUseCaseMapper.java
│   │   │   │
│   │   │   ├── infrastructure/
│   │   │   │   ├── persistence/
│   │   │   │   │   ├── ProductEntity.java
│   │   │   │   │   └── JpaProductRepository.java
│   │   │   │   ├── adapter/
│   │   │   │   │   └── ProductRepositoryAdapter.java
│   │   │   │   └── mapper/
│   │   │   │       └── ProductMapper.java
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   └── ProductController.java
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── WebConfig.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── BusinessException.java
│   │   │   │
│   │   │   └── BellavistaApplication.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       └── data.sql (opcional)
│   │
│   └── test/
│       └── java/com/drogueria/bellavista/
│
├── pom.xml
└── README.md
```

## 🔐 Seguridad (Próximos pasos)

Para producción, considera agregar:
- Spring Security
- JWT Authentication
- Rate Limiting
- HTTPS

## 📧 Contacto

Para más información o soporte, contacta al equipo de desarrollo.

## 📄 Licencia

Este proyecto es propiedad de Droguería Bellavista.
