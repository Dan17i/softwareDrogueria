# Arquitectura Hexagonal - Droguería Bellavista

## 🏗️ Diagrama de Capas

```
┌─────────────────────────────────────────────────────────────────┐
│                    CAPA DE PRESENTACIÓN                          │
│                     (Puerto de Entrada)                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌──────────────┐         ┌──────────────┐                     │
│  │ Controller   │────────▶│ DTO Request  │                     │
│  │ (REST API)   │◀────────│ DTO Response │                     │
│  └──────────────┘         └──────────────┘                     │
│         │                         │                              │
└─────────┼─────────────────────────┼──────────────────────────────┘
          │                         │
          ▼                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    CAPA DE APLICACIÓN                            │
│                   (Casos de Uso / Orquestación)                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌──────────────────────┐       ┌──────────────────────┐       │
│  │  ProductDTO          │       │ ProductUseCaseMapper │       │
│  │  - CreateRequest     │◀──────│ toDomain()           │       │
│  │  - UpdateRequest     │       │ toResponse()         │       │
│  │  - Response          │       └──────────────────────┘       │
│  └──────────────────────┘                  │                    │
│                                            │                    │
└────────────────────────────────────────────┼─────────────────────┘
                                             │
                                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                      CAPA DE DOMINIO                             │
│                    (Lógica de Negocio PURA)                      │
│                   ⚠️ SIN DEPENDENCIAS EXTERNAS                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌────────────────────┐        ┌──────────────────────┐        │
│  │ Product (Model)    │        │ ProductService       │        │
│  │ ─────────────────  │        │ ──────────────────── │        │
│  │ + id               │◀───────│ + createProduct()    │        │
│  │ + code             │        │ + updateProduct()    │        │
│  │ + name             │        │ + getProductById()   │        │
│  │ + price            │        │ + getAllProducts()   │        │
│  │ + stock            │        │ + reduceStock()      │        │
│  │ + minStock         │        │ + increaseStock()    │        │
│  │ ───────────────    │        └──────────────────────┘        │
│  │ + needsRestock()   │                   │                     │
│  │ + isAvailable()    │                   │                     │
│  │ + reduceStock()    │                   ▼                     │
│  │ + increaseStock()  │        ┌──────────────────────┐        │
│  └────────────────────┘        │ ProductRepository    │        │
│                                │ (INTERFACE/PORT)     │        │
│                                │ ──────────────────── │        │
│                                │ + save()             │        │
│                                │ + findById()         │        │
│                                │ + findAll()          │        │
│                                │ + delete()           │        │
│                                └──────────────────────┘        │
│                                          ▲                      │
└──────────────────────────────────────────┼──────────────────────┘
                                           │
                                           │ implements
                                           │
┌──────────────────────────────────────────┼──────────────────────┐
│                  CAPA DE INFRAESTRUCTURA │                      │
│                    (Detalles Técnicos)   │                      │
├──────────────────────────────────────────┼──────────────────────┤
│                                          │                      │
│  ┌─────────────────────────────┐        │                      │
│  │ ProductRepositoryAdapter    │────────┘                      │
│  │ (Implementación del Puerto) │                               │
│  └─────────────────────────────┘                               │
│                 │                                               │
│                 │ usa                                           │
│                 ▼                                               │
│  ┌────────────────────────┐      ┌──────────────────────┐     │
│  │ JpaProductRepository   │      │ ProductMapper        │     │
│  │ (Spring Data JPA)      │      │ Entity ↔ Domain      │     │
│  └────────────────────────┘      └──────────────────────┘     │
│                 │                            │                 │
│                 ▼                            ▼                 │
│  ┌────────────────────────┐                                   │
│  │ ProductEntity (JPA)    │                                   │
│  │ @Entity @Table         │                                   │
│  │ ─────────────────────  │                                   │
│  │ Mapea a tabla DB       │                                   │
│  └────────────────────────┘                                   │
│                 │                                               │
└─────────────────┼───────────────────────────────────────────────┘
                  │
                  ▼
         ┌────────────────┐
         │   PostgreSQL   │
         │   (Database)   │
         └────────────────┘
```

## 🔄 Flujo de Datos

### Ejemplo: Crear un Producto

```
1. Cliente HTTP
   │
   │ POST /api/products
   │ { "code": "MED001", "name": "Acetaminofen", ... }
   │
   ▼
2. ProductController
   │
   │ - Recibe ProductDTO.CreateRequest
   │ - Valida con @Valid
   │
   ▼
3. ProductUseCaseMapper
   │
   │ - Convierte DTO → Product (domain)
   │
   ▼
4. ProductService
   │
   │ - Valida lógica de negocio
   │ - Verifica código único
   │ - Establece valores por defecto
   │
   ▼
5. ProductRepository (Interface)
   │
   │ - Define contrato save(Product)
   │
   ▼
6. ProductRepositoryAdapter
   │
   │ - Implementa el contrato
   │
   ▼
7. ProductMapper
   │
   │ - Convierte Product → ProductEntity
   │
   ▼
8. JpaProductRepository
   │
   │ - Guarda en base de datos
   │
   ▼
9. PostgreSQL
   │
   │ - Persiste datos
   │
   ◀── Retorna ProductEntity
   │
10. ProductMapper
   │
   │ - Convierte ProductEntity → Product
   │
   ▼
11. ProductService
   │
   │ - Retorna Product con ID
   │
   ▼
12. ProductUseCaseMapper
   │
   │ - Convierte Product → ProductDTO.Response
   │
   ▼
13. ProductController
   │
   │ - Retorna ResponseEntity<ProductDTO.Response>
   │
   ▼
Cliente HTTP recibe:
{
  "id": 1,
  "code": "MED001",
  "name": "Acetaminofen",
  ...
}
```

## 🎯 Principios SOLID Aplicados

### Single Responsibility Principle (SRP)
- **ProductController**: Solo maneja HTTP requests/responses
- **ProductService**: Solo contiene lógica de negocio
- **ProductRepository**: Solo define operaciones de persistencia
- **ProductMapper**: Solo convierte entre modelos

### Open/Closed Principle (OCP)
- Interfaces (ProductRepository) abiertas para extensión
- Implementaciones cerradas para modificación

### Liskov Substitution Principle (LSP)
- ProductRepositoryAdapter puede sustituir a ProductRepository

### Interface Segregation Principle (ISP)
- Interfaces específicas por funcionalidad

### Dependency Inversion Principle (DIP)
- ProductService depende de ProductRepository (abstracción)
- NO depende de JpaProductRepository (implementación)

## 📦 Ventajas de esta Arquitectura

✅ **Testabilidad**
- Dominio se testea sin infraestructura
- Mocks de repositorios fáciles de crear

✅ **Mantenibilidad**
- Cambios en UI no afectan dominio
- Cambios en BD no afectan lógica de negocio

✅ **Flexibilidad**
- Fácil cambiar PostgreSQL por MongoDB
- Fácil agregar GraphQL sin tocar dominio

✅ **Independencia de Frameworks**
- Dominio no conoce Spring
- Fácil migrar a otro framework

## 🔍 Dependencias entre Capas

```
Presentación ────▶ Aplicación ────▶ Dominio ◀──── Infraestructura
                                      ▲
                                      │
                                   (solo interfaces)
```

**Regla de Oro**: Las dependencias SIEMPRE apuntan hacia el DOMINIO.
La infraestructura IMPLEMENTA las interfaces definidas en el dominio.
