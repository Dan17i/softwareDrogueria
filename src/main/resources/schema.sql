-- Schema SQL para la base de datos
-- Este archivo se ejecuta automáticamente antes que data.sql

-- Tabla: Clientes
CREATE TABLE IF NOT EXISTS customers (
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
    updated_at TIMESTAMP,
    
    CONSTRAINT idx_customers_code UNIQUE (code),
    CONSTRAINT idx_customers_email UNIQUE (email),
    CONSTRAINT idx_customers_document UNIQUE (document_number)
);

CREATE INDEX IF NOT EXISTS idx_customers_active ON customers(active);
CREATE INDEX IF NOT EXISTS idx_customers_type ON customers(customer_type);
CREATE INDEX IF NOT EXISTS idx_customers_pending_balance ON customers(pending_balance);

-- Tabla: Proveedores
CREATE TABLE IF NOT EXISTS suppliers (
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
    updated_at TIMESTAMP,
    
    CONSTRAINT idx_suppliers_code UNIQUE (code),
    CONSTRAINT idx_suppliers_email UNIQUE (email)
);

CREATE INDEX IF NOT EXISTS idx_suppliers_active ON suppliers(active);

-- Tabla: Órdenes de Compra
CREATE TABLE IF NOT EXISTS orders (
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

CREATE INDEX IF NOT EXISTS idx_order_number ON orders(order_number);
CREATE INDEX IF NOT EXISTS idx_order_customer ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_order_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_order_date ON orders(order_date);

-- Tabla: Líneas de Orden
CREATE TABLE IF NOT EXISTS order_items (
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

CREATE INDEX IF NOT EXISTS idx_order_items_order ON order_items(order_id);

-- Tabla: Productos
CREATE TABLE IF NOT EXISTS products (
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
    updated_at TIMESTAMP,
    
    CONSTRAINT idx_products_code UNIQUE (code)
);

CREATE INDEX IF NOT EXISTS idx_products_active ON products(active);
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);
CREATE INDEX IF NOT EXISTS idx_products_stock ON products(stock);

-- Tabla: Recepción de Mercancía (Goods Receipt)
CREATE TABLE IF NOT EXISTS goods_receipts (
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

CREATE INDEX IF NOT EXISTS idx_receipt_number ON goods_receipts(receipt_number);
CREATE INDEX IF NOT EXISTS idx_receipt_order ON goods_receipts(order_id);
CREATE INDEX IF NOT EXISTS idx_receipt_supplier ON goods_receipts(supplier_id);
CREATE INDEX IF NOT EXISTS idx_receipt_status ON goods_receipts(status);

-- Tabla: Líneas de Recepción de Mercancía
CREATE TABLE IF NOT EXISTS goods_receipt_items (
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

CREATE INDEX IF NOT EXISTS idx_receipt_items_receipt ON goods_receipt_items(goods_receipt_id);
CREATE INDEX IF NOT EXISTS idx_receipt_items_product ON goods_receipt_items(product_id);
