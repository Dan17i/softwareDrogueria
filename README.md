# 🏥 Droguería Bellavista - Backend API

Sistema de gestión para droguería construido con **Spring Boot 3.2.2** y **Java 21**, siguiendo arquitectura hexagonal (Clean Architecture).

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Tests](https://img.shields.io/badge/Tests-150%20passed-success.svg)]()
[![Deploy](https://img.shields.io/badge/Deploy-AWS%20EC2-orange.svg)](http://3.83.244.10:8080/api/actuator/health)

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
- [Configuración de Stripe](#-configuración-de-stripe)
- [Testing](#-testing)
- [Despliegue](#-despliegue)
- [Documentación](#-documentación)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Contribución](#-contribución)

---

## 🌐 Demo en Producción

La API está desplegada y disponible en:

```
http://3.83.244.10:8080/api
```

### Endpoints públicos para probar:

| Endpoint | URL |
|----------|-----|
| Health Check | [/api/actuator/health](http://3.83.244.10:8080/api/actuator/health) |
| Registro | POST `/api/auth/register` |
| Login | POST `/api/auth/login` |

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

### 💳 Sistema de Pagos con Stripe
- Integración completa con Stripe para pagos seguros
- Procesamiento de tarjetas de crédito/débito
- Estados de pago en tiempo real (pendiente, procesando, exitoso, fallido)
- Reembolsos automáticos para devoluciones
- Validación automática de fondos y seguridad PCI DSS
- Historial completo de transacciones por cliente

### 🔔 Sistema de Notificaciones
- Alertas automáticas de inventario bajo
- Notificaciones específicas por roles (ADMIN, WAREHOUSE, etc.)
- Estados de lectura y timestamps
- Scheduler automático cada 6 horas
- API REST completa para gestión de notificaciones
- Integración con productos que necesitan reabastecimiento

---

## 🚀 Tecnologías

| Categoría | Tecnología |
|-----------|------------|
| **Lenguaje** | Java 21 |
| **Framework** | Spring Boot 3.2.2 |
| **Seguridad** | Spring Security + JWT |
| **Persistencia** | Spring Data JPA |
| **Base de Datos** | PostgreSQL 15 (prod) / H2 (dev) |
| **Documentación** | SpringDoc OpenAPI (Swagger) |
| **Testing** | JUnit 5, Testcontainers, Spring Security Test |
| **Build** | Maven |
| **Contenedores** | Docker |
| **Deploy** | AWS EC2 |

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

### Desarrolladores

En modo desarrollo (`dev`), tienes acceso adicional a:

- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - User: `sa`
  - Password: (vacío)
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

---

## ⚙️ Configuración

### Perfiles disponibles

| Perfil | Base de Datos | Uso |
|--------|---------------|-----|
| `dev` | PostgreSQL (localhost:5433) | Desarrollo local |
| `prod` | PostgreSQL (AWS EC2) | Producción |

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
| `STRIPE_SECRET_KEY` | Clave secreta de Stripe (`sk_test_...` o `sk_live_...`) | ✅ Sí |
| `STRIPE_PUBLIC_KEY` | Clave publicable de Stripe (`pk_test_...` o `pk_live_...`) | ✅ Sí |
| `SPRING_DATASOURCE_URL` | URL de conexión JDBC | Solo en prod |
| `SPRING_DATASOURCE_USERNAME` | Usuario de BD | Solo en prod |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de BD | Solo en prod |
| `PORT` | Puerto del servidor | Solo en prod |

---

## 💳 Configuración de Stripe

### Modo Test vs Producción

| Modo | Prefijo de claves | Descripción |
|------|-------------------|-------------|
| Test | `sk_test_` / `pk_test_` | Sin cobros reales. Usar en desarrollo. |
| Live | `sk_live_` / `pk_live_` | Cobros reales. Solo en producción. |

### Variables de entorno requeridas

```bash
# Backend (clave secreta — nunca exponer al frontend)
STRIPE_SECRET_KEY=sk_test_...

# Frontend (clave publicable — puede ser pública)
STRIPE_PUBLIC_KEY=pk_test_...
```

### Configuración local (desarrollo)

Las claves se cargan desde el archivo `.env` en la raíz del proyecto (gitignoreado). Copia la plantilla y rellena tus valores:

```bash
cp .env.example .env
# Edita .env con tus claves reales de Stripe
```

Para que Spring Boot cargue el `.env` al ejecutar con Maven:

```bash
# Windows (PowerShell)
Get-Content .env | ForEach-Object { $var = $_ -split '=', 2; [System.Environment]::SetEnvironmentVariable($var[0], $var[1]) }
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Linux / macOS
export $(cat .env | xargs)
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Configuración por perfil

| Perfil | Archivo | Comportamiento |
|--------|---------|----------------|
| `dev` | `application-dev.yml` | Usa valores por defecto del YAML (clave test embebida) |
| `prod` | `application-prod.yml` | Lee obligatoriamente desde variables de entorno |

> **Seguridad:** `application-dev.yml` está en `.gitignore` para evitar exponer claves en el repositorio.

### Mejores prácticas

- En producción, usa siempre una **Clave Restringida** (`rk_live_...`) con solo los permisos necesarios (Payments, Checkout Sessions, Customers) en lugar de la clave secreta estándar.
- Nunca cometas claves `sk_live_` ni `rk_live_` en el código fuente.
- En producción (AWS EC2), configura las variables de entorno directamente en el servidor o en el `docker-compose.yml` usando un archivo `.env` que esté en `.gitignore`.

### Tarjetas de prueba (modo test)

| Número | Resultado |
|--------|-----------|
| `4242 4242 4242 4242` | Pago exitoso |
| `4000 0000 0000 0002` | Tarjeta rechazada |
| `4000 0025 0000 3155` | Requiere autenticación 3D Secure |

Fecha: cualquier fecha futura. CVC: cualquier 3 dígitos.

---

## ⏰ Tareas Programadas

El sistema incluye tareas automáticas que se ejecutan periódicamente:

### Scheduler de Notificaciones
- **Frecuencia**: Cada 6 horas
- **Función**: Verifica automáticamente el inventario de productos
- **Acción**: Crea notificaciones de alerta para productos con stock bajo
- **Configuración**: `@EnableScheduling` en `BellavistaApplication.java`
- **Productos monitoreados**: Aquellos con `minStockLevel` definido y stock actual ≤ nivel mínimo

---

## 📚 API Endpoints

### Autenticación (públicos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/register` | Registrar usuario (rol USER por defecto) |
| POST | `/api/auth/login` | Iniciar sesión (retorna JWT) |
| POST | `/api/auth/forgot-password` | Solicitar recuperación de contraseña |
| POST | `/api/auth/reset-password` | Restablecer contraseña con token |
| POST | `/api/auth/dev-create-admin` | Crear admin por defecto (solo desarrollo) |

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
| GET | `/api/products` | Listar todos los productos |
| GET | `/api/products?active=true` | Listar solo productos activos |
| GET | `/api/products/{id}` | Obtener producto por ID |
| GET | `/api/products/code/{code}` | Obtener producto por código |
| GET | `/api/products/search?name=xxx` | Buscar productos por nombre |
| GET | `/api/products/category/{category}` | Listar productos por categoría |
| GET | `/api/products/restock-needed` | Productos que necesitan reabastecimiento |
| POST | `/api/products` | Crear producto |
| PUT | `/api/products/{id}` | Actualizar producto |
| POST | `/api/products/{id}/reduce-stock` | Reducir stock |
| POST | `/api/products/{id}/increase-stock` | Aumentar stock |
| PATCH | `/api/products/{id}/toggle-status` | Cambiar estado (activo/inactivo) |
| DELETE | `/api/products/{id}` | Eliminar producto |

### Clientes (protegidos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/customers` | Listar todos los clientes |
| GET | `/api/customers/{id}` | Obtener cliente por ID |
| POST | `/api/customers` | Crear cliente |
| PUT | `/api/customers/{id}` | Actualizar cliente |
| DELETE | `/api/customers/{id}` | Eliminar cliente |

### Proveedores (protegidos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/suppliers` | Listar todos los proveedores |
| GET | `/api/suppliers/{id}` | Obtener proveedor por ID |
| POST | `/api/suppliers` | Crear proveedor |
| PUT | `/api/suppliers/{id}` | Actualizar proveedor |
| DELETE | `/api/suppliers/{id}` | Eliminar proveedor |

### Órdenes (protegidos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/orders` | Listar todas las órdenes |
| GET | `/api/orders/{id}` | Obtener orden por ID |
| GET | `/api/orders/number/{orderNumber}` | Obtener orden por número |
| GET | `/api/orders/customer/{customerId}` | Órdenes de un cliente |
| GET | `/api/orders/status/{status}` | Órdenes por estado |
| GET | `/api/orders/customer/{customerId}/pending` | Órdenes pendientes de un cliente |
| GET | `/api/orders/search?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` | Órdenes por rango de fechas |
| POST | `/api/orders` | Crear orden |
| PATCH | `/api/orders/{id}/complete` | Completar orden |
| PATCH | `/api/orders/{id}/cancel` | Cancelar orden |

### Recepción de Mercancía (protegidos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/goods-receipts` | Listar todas las recepciones |
| GET | `/api/goods-receipts/{id}` | Obtener recepción por ID |
| GET | `/api/goods-receipts/number/{receiptNumber}` | Obtener por número de recepción |
| GET | `/api/goods-receipts/order/{orderId}` | Recepciones de una orden |
| GET | `/api/goods-receipts/supplier/{supplierId}` | Recepciones de un proveedor |
| GET | `/api/goods-receipts/status/{status}` | Recepciones por estado |
| GET | `/api/goods-receipts/pending` | Recepciones pendientes |
| POST | `/api/goods-receipts` | Crear recepción |
| PATCH | `/api/goods-receipts/{id}/receive` | Confirmar recepción |
| PATCH | `/api/goods-receipts/{id}/reject` | Rechazar recepción |
| DELETE | `/api/goods-receipts/{id}` | Eliminar recepción (solo PENDING) |

### 💳 Pagos (protegidos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/payments/process` | Procesar pago con Stripe |
| GET | `/api/payments/{intentId}` | Verificar estado de pago |

### 🔔 Notificaciones (protegidos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/notifications` | Listar todas las notificaciones |
| GET | `/api/notifications/unread` | Listar notificaciones no leídas |
| GET | `/api/notifications/{id}` | Obtener notificación por ID |
| GET | `/api/notifications/role/{role}` | Notificaciones por rol |
| PATCH | `/api/notifications/{id}/read` | Marcar notificación como leída |
| PATCH | `/api/notifications/mark-all-read` | Marcar todas como leídas |
| DELETE | `/api/notifications/{id}` | Eliminar notificación |
| POST | `/api/notifications/check-inventory` | Verificar inventario y crear alertas |

### Monitoreo y Documentación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/actuator/health` | Estado de salud de la aplicación |
| GET | `/api/swagger-ui.html` | Documentación interactiva de la API |
| GET | `/api/v3/api-docs` | Especificación OpenAPI JSON |

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
curl -X POST http://3.83.244.10:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","email":"admin@test.com","password":"password123","firstName":"Admin","lastName":"User"}'

# 2. Login
curl -X POST http://3.83.244.10:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'

# 3. Usar token
curl http://3.83.244.10:8080/api/products \
  -H "Authorization: Bearer <tu-token>"
```

---

### Resumen de cobertura
## 🧪 Testing

### Resumen de cobertura

| Tipo | Tests | Estado |
|------|-------|--------|
| Integration Tests | 60 | ✅ |
| Domain Services | 140 | ✅ |
| Controllers | 41 | ✅ |
| Domain Models/Conditions | 241 | ✅ |
| Infrastructure | 49 | ✅ |
| Application Layer | 32 | ✅ |
| Configuration | 2 | ✅ |
| **Total** | **565** | ✅ |

### Desglose por categoría

<details>
<summary><b>Integration Tests (60 tests)</b></summary>

| Test Suite | Tests |
|-------------|-------|
| ProductIntegrationTest | 17 |
| UserManagementIntegrationTest | 14 |
| SecurityIntegrationTest | 13 |
| NotificationIntegrationTest | 11 |
| AuthOrderIntegrationTest | 3 |
| AdminCreationTest | 2 |

</details>

<details>
<summary><b>Tests de mayor cobertura</b></summary>

| Test Suite | Tests | Categoría |
|-------------|-------|-----------|
| NotificationDTOConditionsTest | 61 | Validaciones |
| PaymentConditionsTest | 47 | Validaciones |
| PaymentEntityConditionsTest | 45 | Validaciones |
| NotificationConditionsTest | 41 | Validaciones |
| NotificationEntityConditionsTest | 38 | Validaciones |
| AuthServiceTest | 26 | Servicios |
| PaymentServiceTest | 20 | Servicios |
| GoodsReceiptControllerTest | 20 | Controllers |

</details>

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


## 🚀 Despliegue

### Plataforma: AWS EC2

La aplicación está desplegada en una instancia **AWS EC2 t2.micro** (Ubuntu 24.04) usando Docker Compose:

- **App**: Spring Boot en contenedor Docker (JRE 21 Alpine)
- **Base de datos**: PostgreSQL 15 en contenedor local (sin RDS)
- **Cache/Blacklist**: Redis 7 en contenedor local

### Archivos de configuración

| Archivo | Descripción |
|---------|-------------|
| `Dockerfile` | Build multi-stage con Maven + JRE Alpine |
| `docker-compose.yml` | Orquestación de servicios (app, postgres, redis) |
| `scripts/deploy-aws.sh` | Script de instalación y despliegue automático en EC2 |
| `docs/AWS_DEPLOYMENT.md` | Guía completa de arquitectura y despliegue |

### URL de producción

```
http://3.83.244.10:8080/api
```

### Desplegar cambios

```bash
cd ~/softwareDrogueria
git pull
docker compose up -d --build app
```

---

## 📚 Documentación

La documentación completa del proyecto está organizada en varios archivos especializados:

### 📋 Documentos Principales

| Documento | Descripción |
|-----------|-------------|
| **[README.md](README.md)** | Guía de inicio rápido y referencia general |
| **[QUICKSTART.md](QUICKSTART.md)** | Inicio rápido en 5 minutos |
| **[ARCHITECTURE.md](docs/Arquitectura%20y%20proyecto/ARCHITECTURE.md)** | Arquitectura hexagonal detallada |

### 🗄️ Base de Datos

| Documento | Descripción |
|-----------|-------------|
| **[DATABASE.md](docs/DATABASE.md)** | Esquema completo, relaciones y migraciones |
| **[schema.sql](src/main/resources/schema.sql)** | Script de creación de tablas |
| **[data.sql](src/main/resources/data.sql)** | Datos de prueba |

### 🔧 Operaciones y Mantenimiento

| Documento | Descripción |
|-----------|-------------|
| **[OPERATIONS.md](docs/OPERATIONS.md)** | Monitoreo, troubleshooting y mantenimiento |
| **[IMPLEMENTACION_METRICAS_COMPLETADA.md](docs/IMPLEMENTACIONES%20COMPLETADAS/IMPLEMENTACION_METRICAS_COMPLETADA.md)** | Métricas de calidad implementadas |
| **[TASK_COMPLETED_USER_MANAGEMENT.md](docs/IMPLEMENTACIONES%20COMPLETADAS/TASK_COMPLETED_USER_MANAGEMENT.md)** | Gestión de usuarios completada |
| **[IMPLEMENTACION_METRICAS_COMPLETADA.md](docs/IMPLEMENTACIONES%20COMPLETADAS/IMPLEMENTACION_METRICAS_COMPLETADA.md)** | Métricas de calidad implementadas |
| **[TASK_COMPLETED_USER_MANAGEMENT.md](docs/IMPLEMENTACIONES%20COMPLETADAS/TASK_COMPLETED_USER_MANAGEMENT.md)** | Gestión de usuarios completada |
| **[CAMBIO_4_SISTEMA_EMAIL.md](docs/IMPLEMENTACIONES%20COMPLETADAS/CAMBIO_4_SISTEMA_EMAIL.md)** | Sistema de email implementado |
| **[PAGOS_STRIPE_IMPLEMENTACION.md](docs/IMPLEMENTACIONES%20COMPLETADAS/PAGOS_STRIPE_IMPLEMENTACION.md)** | Implementación completa de pagos con Stripe |
| **[IMPLEMENTACION_PAGOS_COMPLETA.md](docs/IMPLEMENTACION_PAGOS_COMPLETA.md)** | Guía detallada de sistema de pagos |

### 🎨 Frontend

| Documento | Descripción |
|-----------|-------------|
| **[FRONTEND_INTEGRATION.md](docs/FRONTEND_INTEGRATION.md)** | Guía completa de integración frontend |
| **[Postman_Collection.json](Postman_Collection.json)** | Colección de Postman con ejemplos |
| **[Postman_Collection_Metricas.json](Postman_Collection_Metricas.json)** | Colección para pruebas de métricas |

### 🔒 Seguridad y Calidad

| Documento | Descripción |
|-----------|-------------|
| **[Plan_de_Gestion_de_la_Calidad_FINAL.md](docs/Arquitectura%20y%20proyecto/Plan_de_Gestion_de_la_Calidad_FINAL.md)** | Plan de calidad completo |
| **[SOLID_AND_PATTERNS_ANALYSIS.md](docs/Arquitectura%20y%20proyecto/SOLID_AND_PATTERNS_ANALYSIS.md)** | Análisis SOLID y patrones |
| **[1. Invetoryrx - Plan de Gestión de la Calidad del Proyecto.pdf](docs/1.%20Invetoryrx%20-%20Plan%20de%20Gesti%C3%B3n%20de%20la%20Calidad%20del%20Proyecto.pdf)** | Documento PDF del plan de calidad |

### 🚀 Despliegue y Configuración

| Documento | Descripción |
|-----------|-------------|
| **[render.yaml](infra/render.yaml)** | Configuración de despliegue en Render |
| **[Dockerfile](Dockerfile)** | Configuración de Docker |
| **[docker-compose.yml](docker-compose.yml)** | Configuración local con Docker |
| **[DEPLOY_RENDER.md](docs/DESPLIEGUE%20Y%20CONFIGURACIÓN/DEPLOY_RENDER.md)** | Guía de despliegue en Render (histórico) |
| **[AWS_DEPLOYMENT.md](docs/AWS_DEPLOYMENT.md)** | Arquitectura y guía de despliegue en AWS EC2 |
| **[CONFIGURAR_GMAIL_RAPIDO.md](docs/DESPLIEGUE%20Y%20CONFIGURACIÓN/CONFIGURAR_GMAIL_RAPIDO.md)** | Configuración rápida de Gmail |

### 🧪 Testing y Calidad

| Documento | Descripción |
|-----------|-------------|
| **[Configuracion_SonarCloud.md](docs/Pruebas%20y%20Calidad/Configuracion_SonarCloud.md)** | Configuración de análisis de vulnerabilidades |
| **[Configuracion_Uptime_Monitoring.md](docs/Pruebas%20y%20Calidad/Configuracion_Uptime_Monitoring.md)** | Monitoreo de disponibilidad |
| **[INSTRUCCIONES_SONARCLOUD.md](docs/Pruebas%20y%20Calidad/INSTRUCCIONES_SONARCLOUD.md)** | Guía completa de SonarCloud |

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
├── infra/
│   └── render.yaml
├── scripts/
│   ├── deploy-aws.sh
│   ├── run-dev.sh
│   └── reset-admin.sql
├── docker-compose.yml
├── pom.xml
└── README.md
```

</details>

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
