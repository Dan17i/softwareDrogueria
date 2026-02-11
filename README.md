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

- **Java 17**
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
