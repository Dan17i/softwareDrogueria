# 🏥 Droguería Bellavista - Backend API

Sistema de gestión para droguería construido con **Spring Boot 3.2.2** y **Java 21**, siguiendo arquitectura hexagonal (Clean Architecture).

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Tests](https://img.shields.io/badge/Tests-109%20passed-success.svg)]()
[![Deploy](https://img.shields.io/badge/Deploy-Render-purple.svg)](https://drogueria-bellavista-api.onrender.com/api/actuator/health)

---

## 📋 Tabla de Contenidos

- [Demo en Producción](#-demo-en-producción)
- [Características Principales](#-características-principales)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Instalación Local](#-instalación-local)
- [Configuración](#-configuración)
- [API Endpoints](#-api-endpoints)
- [Seguridad](#-seguridad)
- [Sistema de Email](#-sistema-de-email)
- [Testing](#-testing)
- [Despliegue](#-despliegue)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Contribución](#-contribución)

---

## 🌐 Demo en Producción

La API está desplegada y disponible en:

```
https://drogueria-bellavista-api.onrender.com/api
```

### Endpoints públicos para probar:

| Endpoint | URL |
|----------|-----|
| Health Check | [/api/actuator/health](https://drogueria-bellavista-api.onrender.com/api/actuator/health) |
| Registro | POST `/api/auth/register` |
| Login | POST `/api/auth/login` |

> ⚠️ **Nota**: El plan gratuito de Render apaga la app tras 15 min de inactividad. El primer request puede tardar ~30-60 segundos.

---

## ✨ Características Principales

### 🔐 Gestión de Usuarios y Roles
- Sistema completo de autenticación con JWT
- 5 roles disponibles: ADMIN, MANAGER, SALES, WAREHOUSE, USER
- Panel de administración para gestionar usuarios (solo ADMIN)
- Activar/desactivar usuarios
- Cambiar roles dinámicamente
- Protección del último administrador del sistema

### 📧 Sistema de Email
- Email de bienvenida automático al registrarse
- Recuperación de contraseña mediante token temporal (1 hora)
- Notificaciones de cambios importantes
- Tokens seguros de un solo uso
- Protección contra enumeración de emails

### 📦 Gestión de Inventario
- Control de productos con stock en tiempo real
- Recepción de mercancía de proveedores
- Órdenes de venta con validación de stock
- Auditoría completa de movimientos

### 👥 Gestión de Clientes
- Registro de clientes con límite de crédito
- Control de saldo pendiente
- Historial de órdenes por cliente

---

## 🚀 Tecnologías

| Categoría | Tecnología |
|-----------|------------|
| **Lenguaje** | Java 21 |
| **Framework** | Spring Boot 3.2.2 |
| **Seguridad** | Spring Security + JWT |
| **Persistencia** | Spring Data JPA |
| **Base de Datos** | PostgreSQL 15 (prod) / H2 (dev) |
| **Build** | Maven |
| **Testing** | JUnit 5, Testcontainers |
| **Contenedores** | Docker |
| **Deploy** | Render |

---

## 🏗️ Arquitectura

El proyecto implementa **Arquitectura Hexagonal** (Ports & Adapters):

```
┌─────────────────────────────────────────────────────────────┐
│                      CONTROLLERS                             │
│                   (Adaptadores de Entrada)                   │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                      APPLICATION                             │
│              (DTOs, Mappers, Servicios de App)               │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                        DOMAIN                                │
│            (Modelos, Servicios, Puertos/Interfaces)          │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                    INFRASTRUCTURE                            │
│         (JPA Entities, Repositories, Security, Adapters)     │
└─────────────────────────────────────────────────────────────┘
```

### Principios aplicados:

- ✅ **Independencia de frameworks**: El dominio no depende de Spring
- ✅ **Testeable**: Fácil de hacer unit tests sin infraestructura
- ✅ **Mantenible**: Separación clara de responsabilidades
- ✅ **Flexible**: Fácil cambiar BD o exponer otra API
- ✅ **Escalable**: Cada capa puede evolucionar independientemente

---

## 💻 Instalación Local

### Prerrequisitos

- Java 21
- Maven 3.8+
- Docker (para PostgreSQL)

### Pasos

**1. Clonar el repositorio**

```bash
git clone https://github.com/Dan17i/softwareDrogueria.git
cd softwareDrogueria
```

**2. Levantar PostgreSQL con Docker**

```bash
docker compose up -d
```

**3. Configurar variable de entorno JWT**

```bash
# Windows (PowerShell)
$env:APP_JWT_SECRET="dev-secret-key-with-at-least-32-characters"

# Linux / macOS
export APP_JWT_SECRET="dev-secret-key-with-at-least-32-characters"
```

**4. Ejecutar la aplicación**

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**5. Verificar**

Abrir: http://localhost:8080/api/actuator/health

---

## ⚙️ Configuración

### Perfiles disponibles

| Perfil | Base de Datos | Uso |
|--------|---------------|-----|
| `dev` | PostgreSQL (localhost:5433) | Desarrollo local |
| `prod` | PostgreSQL (Render) | Producción |

### Variables de entorno

| Variable | Descripción | Requerida |
|----------|-------------|-----------|
| `APP_JWT_SECRET` | Clave secreta para JWT (mín. 32 caracteres) | ✅ Sí |
| `MAIL_HOST` | Servidor SMTP (ej: smtp.gmail.com) | ✅ Sí |
| `MAIL_PORT` | Puerto SMTP (ej: 587) | ✅ Sí |
| `MAIL_USERNAME` | Usuario del servidor de email | ✅ Sí |
| `MAIL_PASSWORD` | Contraseña del servidor de email | ✅ Sí |
| `MAIL_FROM` | Email remitente | ✅ Sí |
| `FRONTEND_URL` | URL del frontend (para links en emails) | ✅ Sí |
| `SPRING_DATASOURCE_URL` | URL de conexión JDBC | Solo en prod |
| `SPRING_DATASOURCE_USERNAME` | Usuario de BD | Solo en prod |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de BD | Solo en prod |
| `PORT` | Puerto del servidor | Solo en prod |

---

## 📚 API Endpoints

### Autenticación (públicos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/register` | Registrar usuario (rol USER por defecto) |
| POST | `/api/auth/login` | Iniciar sesión (retorna JWT) |
| POST | `/api/auth/forgot-password` | Solicitar recuperación de contraseña |
| POST | `/api/auth/reset-password` | Restablecer contraseña con token |

### Gestión de Usuarios (solo ADMIN)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/users` | Listar todos los usuarios |
| GET | `/api/users/{id}` | Obtener usuario por ID |
| PATCH | `/api/users/{id}/role` | Cambiar rol de usuario |
| PATCH | `/api/users/{id}/status` | Activar/desactivar usuario |
| DELETE | `/api/users/{id}` | Eliminar usuario |

### Productos (protegidos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/products` | Listar todos |
| GET | `/api/products/{id}` | Obtener por ID |
| GET | `/api/products/code/{code}` | Obtener por código |
| POST | `/api/products` | Crear producto |
| PUT | `/api/products/{id}` | Actualizar producto |
| DELETE | `/api/products/{id}` | Eliminar producto |
| POST | `/api/products/{id}/reduce-stock` | Reducir stock |
| POST | `/api/products/{id}/increase-stock` | Aumentar stock |
| PATCH | `/api/products/{id}/toggle-status` | Cambiar estado |

### Clientes (protegidos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/customers` | Listar todos |
| GET | `/api/customers/{id}` | Obtener por ID |
| POST | `/api/customers` | Crear cliente |
| PUT | `/api/customers/{id}` | Actualizar cliente |
| DELETE | `/api/customers/{id}` | Eliminar cliente |

### Proveedores (protegidos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/suppliers` | Listar todos |
| GET | `/api/suppliers/{id}` | Obtener por ID |
| POST | `/api/suppliers` | Crear proveedor |
| PUT | `/api/suppliers/{id}` | Actualizar proveedor |
| DELETE | `/api/suppliers/{id}` | Eliminar proveedor |

### Órdenes (protegidos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/orders` | Listar todas |
| GET | `/api/orders/{id}` | Obtener por ID |
| POST | `/api/orders` | Crear orden |
| PUT | `/api/orders/{id}` | Actualizar orden |
| DELETE | `/api/orders/{id}` | Eliminar orden |

### Recepción de Mercancía (protegidos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/goods-receipts` | Listar todas |
| GET | `/api/goods-receipts/{id}` | Obtener por ID |
| POST | `/api/goods-receipts` | Crear recepción |
| PATCH | `/api/goods-receipts/{id}/receive` | Confirmar recepción |
| PATCH | `/api/goods-receipts/{id}/reject` | Rechazar recepción |

---

## 🔐 Seguridad

### Autenticación JWT

Todos los endpoints protegidos requieren el header:

```
Authorization: Bearer <token>
```

### Flujo de autenticación

```
1. POST /api/auth/register  →  Crear cuenta
2. POST /api/auth/login     →  Obtener token JWT
3. Usar token en headers    →  Acceder a endpoints protegidos
```

### Roles disponibles

| Rol | Descripción |
|-----|-------------|
| `ADMIN` | Acceso completo |
| `MANAGER` | Reportes y gestión |
| `SALES` | Ventas y clientes |
| `WAREHOUSE` | Inventario |
| `USER` | Acceso básico |

### Ejemplo de uso

```bash
# 1. Registrar
curl -X POST https://drogueria-bellavista-api.onrender.com/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","email":"admin@test.com","password":"password123","firstName":"Admin","lastName":"User"}'

# 2. Login
curl -X POST https://drogueria-bellavista-api.onrender.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'

# 3. Usar token
curl https://drogueria-bellavista-api.onrender.com/api/products \
  -H "Authorization: Bearer <tu-token>"
```

---

## 🧪 Testing

### Resumen de cobertura

| Tipo | Tests | Estado |
|------|-------|--------|
| Unit Tests (Servicios) | 69 | ✅ |
| Integration Tests | 40 | ✅ |
| **Total** | **109** | ✅ |

### Desglose de Integration Tests

| Test Suite | Tests |
|-------------|-------|
| SecurityIntegrationTest | 11 |
| ProductIntegrationTest | 15 |
| AuthOrderIntegrationTest | 1 |
| UserManagementIntegrationTest | 13 |
| **Subtotal Integration** | **40** |

### Ejecutar tests

```bash
# Todos los tests
mvn test

# Test específico
mvn -Dtest=SecurityIntegrationTest test

# Con reporte de cobertura
mvn test jacoco:report
```

### Tests de integración

Utilizan **Testcontainers** con PostgreSQL 15 para simular el entorno de producción.

---

## 🚀 Despliegue

### Plataforma: Render

La aplicación está desplegada en [Render](https://render.com) con:

- **Web Service**: Docker container con JRE 21
- **Base de datos**: PostgreSQL 15

### Archivos de configuración

| Archivo | Descripción |
|---------|-------------|
| `Dockerfile` | Build multi-stage con Maven + JRE Alpine |
| `render.yaml` | Blueprint para despliegue automático |

### URL de producción

```
https://drogueria-bellavista-api.onrender.com/api
```

### Desplegar cambios

Los cambios en la rama `main` se despliegan automáticamente.

---

## 📁 Estructura del Proyecto

<details>
<summary>Click para expandir</summary>

```
softwareDrogueria/
├── src/
│   ├── main/
│   │   ├── java/com/drogueria/bellavista/
│   │   │   ├── domain/
│   │   │   │   ├── model/          # Entidades de dominio
│   │   │   │   ├── repository/     # Puertos (interfaces)
│   │   │   │   └── service/        # Lógica de negocio
│   │   │   │
│   │   │   ├── application/
│   │   │   │   ├── dto/            # Data Transfer Objects
│   │   │   │   ├── mapper/         # Mappers DTO ↔ Domain
│   │   │   │   └── service/        # Servicios de aplicación
│   │   │   │
│   │   │   ├── infrastructure/
│   │   │   │   ├── persistence/    # Entidades JPA + Repositorios
│   │   │   │   ├── adapter/        # Implementación de puertos
│   │   │   │   ├── mapper/         # Mappers Entity ↔ Domain
│   │   │   │   └── security/       # JWT Filter + Utils
│   │   │   │
│   │   │   ├── controller/         # REST Controllers
│   │   │   ├── config/             # Configuraciones
│   │   │   └── exception/          # Excepciones personalizadas
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── application-prod.yml
│   │
│   └── test/
│       └── java/.../integration/   # Tests de integración
│
├── http/                           # Archivos .http para pruebas
├── Dockerfile
├── render.yaml
├── docker-compose.yml
├── pom.xml
└── README.md
```

</details>

---

## 👥 Contribución

1. Fork el proyecto
2. Crea tu rama (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'feat: agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

---

## 📄 Licencia

Este proyecto es propiedad de **Droguería Bellavista** - Proyecto Académico.

---

## 📧 Contacto

Para más información o soporte, contacta al equipo de desarrollo.

---

<p align="center">
  Desarrollado con ❤️ usando Spring Boot
</p>