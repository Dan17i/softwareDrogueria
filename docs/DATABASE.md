# 📊 Esquema de Base de Datos - Droguería Bellavista

## Descripción General

La base de datos utiliza **PostgreSQL 15** en producción y **H2** en desarrollo. El esquema está diseñado para manejar las operaciones de una droguería con control de inventario, órdenes de compra y recepción de mercancía.

## Tablas Principales

### 1. Users (Usuarios)
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    active BOOLEAN NOT NULL DEFAULT true,
    email_verified BOOLEAN NOT NULL DEFAULT false,
    email_verification_token VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    last_login TIMESTAMP
);
```

**Índices:**
- `idx_users_username` (UNIQUE)
- `idx_users_email` (UNIQUE)
- `idx_users_active`
- `idx_users_role`
- `idx_users_email_verified`

**Roles disponibles:** ADMIN, MANAGER, SALES, WAREHOUSE, USER

### 2. Password Reset Tokens (Tokens de Recuperación)
```sql
CREATE TABLE password_reset_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

**Índices:**
- `idx_password_reset_token` (UNIQUE)
- `idx_password_reset_email`
- `idx_password_reset_expiry`

### 3. Customers (Clientes)
```sql
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(100),
    postal_code VARCHAR(10),
    document_number VARCHAR(50) UNIQUE,
    document_type VARCHAR(50),
    customer_type VARCHAR(50) NOT NULL,
    credit_limit NUMERIC(12, 2),
    pending_balance NUMERIC(12, 2) DEFAULT 0.00,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);
```

**Índices:**
- `idx_customers_code` (UNIQUE)
- `idx_customers_email` (UNIQUE)
- `idx_customers_document` (UNIQUE)
- `idx_customers_active`
- `idx_customers_type`
- `idx_customers_pending_balance`

**Tipos de cliente:** MAYORISTA, MINORISTA

### 4. Suppliers (Proveedores)
```sql
CREATE TABLE suppliers (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(100),
    postal_code VARCHAR(10),
    document_number VARCHAR(50),
    document_type VARCHAR(50),
    lead_time_days INTEGER,
    average_payment_delay NUMERIC(5, 2),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);
```

**Índices:**
- `idx_suppliers_code` (UNIQUE)
- `idx_suppliers_email` (UNIQUE)
- `idx_suppliers_active`

### 5. Products (Productos)
```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    price NUMERIC(10, 2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    min_stock INTEGER NOT NULL DEFAULT 10,
    category VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);
```

**Índices:**
- `idx_products_code` (UNIQUE)
- `idx_products_active`
- `idx_products_category`
- `idx_products_stock`

### 6. Orders (Órdenes de Compra)
```sql
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    customer_code VARCHAR(50),
    customer_name VARCHAR(100),
    supplier_id BIGINT,
    supplier_code VARCHAR(50),
    supplier_name VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    total NUMERIC(14, 2),
    notes TEXT,
    order_date TIMESTAMP,
    expected_delivery_date TIMESTAMP,
    actual_delivery_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_order_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);
```

**Estados disponibles:** PENDING, COMPLETED, CANCELLED

**Índices:**
- `idx_order_number` (UNIQUE)
- `idx_order_customer`
- `idx_order_status`
- `idx_order_date`

### 7. Order Items (Líneas de Orden)
```sql
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_code VARCHAR(50),
    product_name VARCHAR(100),
    unit_price NUMERIC(12, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    subtotal NUMERIC(14, 2),

    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES products(id)
);
```

**Índices:**
- `idx_order_items_order`

### 8. Goods Receipts (Recepción de Mercancía)
```sql
CREATE TABLE goods_receipts (
    id BIGSERIAL PRIMARY KEY,
    receipt_number VARCHAR(50) NOT NULL UNIQUE,
    order_id BIGINT NOT NULL,
    order_number VARCHAR(50),
    supplier_id BIGINT,
    supplier_name VARCHAR(100),
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    notes TEXT,
    expected_delivery_date DATE,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE,
    updated_at DATE,

    CONSTRAINT fk_receipt_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_receipt_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);
```

**Estados disponibles:** PENDING, RECEIVED, REJECTED

**Índices:**
- `idx_receipt_number` (UNIQUE)
- `idx_receipt_order`
- `idx_receipt_supplier`
- `idx_receipt_status`

### 9. Goods Receipt Items (Líneas de Recepción)
```sql
CREATE TABLE goods_receipt_items (
    id BIGSERIAL PRIMARY KEY,
    goods_receipt_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_code VARCHAR(50),
    product_name VARCHAR(255),
    ordered_quantity NUMERIC(10, 2) NOT NULL,
    received_quantity NUMERIC(10, 2) NOT NULL DEFAULT 0,

    CONSTRAINT fk_receipt_item_receipt FOREIGN KEY (goods_receipt_id) REFERENCES goods_receipts(id) ON DELETE CASCADE,
    CONSTRAINT fk_receipt_item_product FOREIGN KEY (product_id) REFERENCES products(id)
);
```

**Índices:**
- `idx_receipt_items_receipt`
- `idx_receipt_items_product`

## Relaciones entre Tablas

```
Users
├── 1:N → Password Reset Tokens (por email)

Customers
├── 1:N → Orders (cliente que realiza la orden)

Suppliers
├── 1:N → Orders (proveedor que surte la orden)

Products
├── 1:N → Order Items (productos en órdenes)
├── 1:N → Goods Receipt Items (productos en recepciones)

Orders
├── 1:N → Order Items
├── 1:N → Goods Receipts

Goods Receipts
├── 1:N → Goods Receipt Items
```

## Configuración de Base de Datos

### Desarrollo (H2)
- **URL:** `jdbc:h2:mem:testdb`
- **Usuario:** `sa`
- **Contraseña:** (vacía)
- **Consola:** http://localhost:8080/h2-console

### Producción (PostgreSQL)
- **Host:** Configurado en Render
- **Base de datos:** PostgreSQL 15
- **Credenciales:** Variables de entorno

## Migraciones

### Estrategia de Migración
- **DDL Auto:** `update` en desarrollo, `validate` en producción
- **Archivos SQL:** `schema.sql` y `data.sql` se ejecutan automáticamente
- **Versionado:** No se utiliza Flyway/ Liquibase (proyecto académico)

### Datos de Prueba
El archivo `data.sql` incluye:
- 3 clientes de ejemplo
- 2 proveedores
- 3 productos
- 1 orden de ejemplo con items

## Consultas Útiles para Desarrollo

### Ver estructura de tablas
```sql
SELECT table_name, table_type
FROM information_schema.tables
WHERE table_schema = 'public'
ORDER BY table_name;
```

### Ver índices
```sql
SELECT tablename, indexname, indexdef
FROM pg_indexes
WHERE schemaname = 'public'
ORDER BY tablename, indexname;
```

### Ver constraints
```sql
SELECT conname, conrelid::regclass, pg_get_constraintdef(c.oid)
FROM pg_constraint c
JOIN pg_namespace n ON n.oid = c.connamespace
WHERE n.nspname = 'public';
```

## Backup y Restore

### Backup (PostgreSQL)
```bash
pg_dump -h localhost -U username -d database_name > backup.sql
```

### Restore (PostgreSQL)
```bash
psql -h localhost -U username -d database_name < backup.sql
```

## Monitoreo

### Métricas importantes a monitorear:
- **Stock de productos:** Productos con stock < min_stock
- **Órdenes pendientes:** Órdenes en estado PENDING
- **Saldo de clientes:** Clientes con pending_balance > credit_limit
- **Recepciones pendientes:** Goods receipts en estado PENDING

### Queries de monitoreo:
```sql
-- Productos con stock bajo
SELECT code, name, stock, min_stock FROM products WHERE stock < min_stock AND active = true;

-- Órdenes pendientes
SELECT order_number, customer_name, total, order_date FROM orders WHERE status = 'PENDING';

-- Clientes con saldo alto
SELECT code, name, pending_balance, credit_limit FROM customers WHERE pending_balance > credit_limit * 0.8;
```

---

**Última actualización:** Marzo 2026
**Versión:** 1.0</content>
<parameter name="filePath">C:\Users\DANIEL-PC\Documents\software 3\softwareDrogueria\docs\DATABASE.md
