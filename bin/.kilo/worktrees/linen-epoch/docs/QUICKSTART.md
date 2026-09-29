# 🚀 Guía de Inicio Rápido - Droguería Bellavista Backend

## ⚡ Inicio Rápido (5 minutos)

### 1. Descomprimir el proyecto
```bash
tar -xzf drogueria-bellavista-backend.tar.gz
cd drogueria-bellavista-backend
```

### 2. Ejecutar en modo desarrollo (con H2)
```bash
chmod +x scripts/run-dev.sh
./scripts/run-dev.sh
```

O manualmente:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 3. Verificar que funciona
Abre tu navegador en:
- **API**: http://localhost:8080/api/products
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - User: `sa`
  - Password: (dejar vacío)

### 4. Probar la API

**Crear un producto:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "code": "TEST001",
    "name": "Producto de Prueba",
    "description": "Mi primer producto",
    "price": 10000,
    "stock": 50,
    "minStock": 10,
    "category": "Pruebas"
  }'
```

**Listar productos:**
```bash
curl http://localhost:8080/api/products
```

## 📋 Estructura del Proyecto

```
drogueria-bellavista-backend/
├── pom.xml                          # Configuración Maven
├── README.md                        # Documentación completa
├── ARCHITECTURE.md                  # Diagrama de arquitectura
├── Postman_Collection.json          # Colección para pruebas
├── scripts/run-dev.sh               # Script de inicio rápido
│
└── src/
    ├── main/
    │   ├── java/com/drogueria/bellavista/
    │   │   ├── domain/              # ⭐ Lógica de negocio
    │   │   ├── application/         # ⭐ DTOs y casos de uso
    │   │   ├── infrastructure/      # ⭐ JPA y persistencia
    │   │   ├── controller/          # ⭐ API REST
    │   │   ├── config/              # Configuración
    │   │   └── exception/           # Excepciones
    │   │
    │   └── resources/
    │       ├── application.yml      # Config principal
    │       ├── application-dev.yml  # Config desarrollo (H2)
    │       ├── application-prod.yml # Config producción (PostgreSQL)
    │       └── data.sql             # Datos de prueba
    │
    └── test/
        └── java/                    # Tests unitarios
```

## 🎯 Endpoints Principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST   | `/api/products` | Crear producto |
| GET    | `/api/products` | Listar todos |
| GET    | `/api/products/{id}` | Ver uno |
| PUT    | `/api/products/{id}` | Actualizar |
| DELETE | `/api/products/{id}` | Eliminar |
| GET    | `/api/products/search?name=xxx` | Buscar |
| GET    | `/api/products/restock-needed` | Stock bajo |
| POST   | `/api/products/{id}/reduce-stock` | Vender |
| POST   | `/api/products/{id}/increase-stock` | Abastecer |

## 🔧 Configuración para Producción

### Opción 1: Con PostgreSQL Local

1. Crear base de datos:
```sql
CREATE DATABASE drogueria_db;
```

2. Editar `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/drogueria_db
    username: tu_usuario
    password: tu_password
```

3. Ejecutar:
```bash
mvn spring-boot:run
```

### Opción 2: Con Variables de Entorno (Recomendado)

```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/drogueria_db
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=tu_password

mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## 📦 Compilar para Producción

```bash
# Compilar
mvn clean package

# Ejecutar JAR
java -jar target/bellavista-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## 🧪 Ejecutar Tests

```bash
mvn test
```

## 📚 Importar Colección de Postman

1. Abre Postman
2. File → Import
3. Selecciona `Postman_Collection.json`
4. ¡Listo! Tienes todos los endpoints configurados

## 🏗️ Arquitectura

Este proyecto usa **Arquitectura Hexagonal (Clean Architecture)**:

- ✅ **Dominio independiente** de frameworks
- ✅ **Fácil de testear** sin base de datos
- ✅ **Flexible** para cambios futuros
- ✅ **Código limpio** y mantenible

Ver `ARCHITECTURE.md` para más detalles.

## 🔍 Siguiente Paso Recomendado

1. **Leer** `README.md` para entender la arquitectura completa
2. **Revisar** el código en `domain/` para ver la lógica de negocio
3. **Personalizar** según tus necesidades
4. **Agregar** nuevas entidades siguiendo el mismo patrón

## 💡 Ejemplo: Agregar Nueva Entidad "Cliente"

Sigue la misma estructura que `Product`:

1. `domain/model/Customer.java` - Entidad de dominio
2. `domain/repository/CustomerRepository.java` - Interface
3. `domain/service/CustomerService.java` - Lógica de negocio
4. `infrastructure/persistence/CustomerEntity.java` - JPA Entity
5. `infrastructure/adapter/CustomerRepositoryAdapter.java` - Implementación
6. `application/dto/CustomerDTO.java` - DTOs
7. `controller/CustomerController.java` - REST API

## ❓ Preguntas Frecuentes

**P: ¿Puedo usar MySQL en lugar de PostgreSQL?**
R: Sí, solo cambia el driver en `pom.xml` y la URL de conexión.

**P: ¿Cómo agrego autenticación?**
R: Agrega Spring Security y JWT. Hay muchos tutoriales disponibles.

**P: ¿Por qué usar arquitectura hexagonal?**
R: Facilita el testing, mantenimiento y evolución del código.

## 📧 Soporte

Si tienes dudas:
1. Revisa `README.md` y `ARCHITECTURE.md`
2. Busca en los comentarios del código
3. Revisa los tests en `src/test/`

## ✨ Características Incluidas

✅ CRUD completo de productos
✅ Validaciones de negocio
✅ Manejo de excepciones
✅ Tests unitarios
✅ Datos de prueba
✅ Configuración multi-ambiente
✅ Documentación completa
✅ Colección Postman
✅ Scripts de inicio

¡Listo para comenzar! 🎉
