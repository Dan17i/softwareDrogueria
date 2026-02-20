-- Script SQL para datos de prueba - Versión H2 (Droguería Bellavista)

-- ============================================
-- CLIENTES
-- ============================================
INSERT INTO customers (code, name, email, phone, address, city, postal_code, document_number, document_type, customer_type, credit_limit, pending_balance, active, created_at, updated_at)
VALUES ('CLI001', 'Farmacia Central', 'farmacia.central@email.com', '031-123456', 'Calle 1 #100', 'Bogotá', '110000', '9001234567', 'NIT', 'MAYORISTA', 50000000.00, 0.00, true, NOW(), NOW());

INSERT INTO customers (code, name, email, phone, address, city, postal_code, document_number, document_type, customer_type, credit_limit, pending_balance, active, created_at, updated_at)
VALUES ('CLI002', 'Farmacia del Barrio', 'farmacia.barrio@email.com', '031-654321', 'Carrera 5 #50', 'Bogotá', '110001', '8001234567', 'CC', 'MINORISTA', 5000000.00, 1000000.00, true, NOW(), NOW());

INSERT INTO customers (code, name, email, phone, address, city, postal_code, document_number, document_type, customer_type, credit_limit, pending_balance, active, created_at, updated_at)
VALUES ('CLI003', 'Droguería Express', 'drogeria.express@email.com', '031-789456', 'Avenida Principal #200', 'Medellín', '050000', '9101234567', 'NIT', 'MAYORISTA', 75000000.00, 0.00, true, NOW(), NOW());

-- ============================================
-- PROVEEDORES
-- ============================================
INSERT INTO suppliers (code, name, email, phone, address, city, postal_code, document_number, document_type, lead_time_days, average_payment_delay, active, created_at, updated_at)
VALUES ('SUP001', 'Laboratorios Farmacéuticos SA', 'contacto@labfarm.com', '031-122000', 'Avenida 1 #500', 'Bogotá', '110000', '8601234567', 'NIT', 3, 1.50, true, NOW(), NOW());

INSERT INTO suppliers (code, name, email, phone, address, city, postal_code, document_number, document_type, lead_time_days, average_payment_delay, active, created_at, updated_at)
VALUES ('SUP002', 'Distribuidora Médica Internacional', 'ventas@dismedint.com', '031-135000', 'Carrera 10 #150', 'Bogotá', '110001', '8602345678', 'NIT', 5, 2.00, true, NOW(), NOW());

-- ============================================
-- PRODUCTOS
-- ============================================
INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('MED001', 'Acetaminofén 500mg', 'Analgésico y antipirético en tabletas', 5000.00, 150, 30, 'Medicamentos', true, NOW(), NOW());

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('MED002', 'Ibuprofeno 400mg', 'Antiinflamatorio no esteroideo', 8000.00, 80, 20, 'Medicamentos', true, NOW(), NOW());

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('VIT001', 'Vitamina C 1000mg', 'Suplemento vitamínico', 18000.00, 120, 30, 'Vitaminas', true, NOW(), NOW());

-- ============================================
-- ÓRDENES
-- ============================================
INSERT INTO orders (order_number, customer_id, customer_code, customer_name, supplier_id, supplier_code, supplier_name, status, total, notes, order_date, expected_delivery_date, created_at, updated_at)
SELECT 'ORD-2024-001', c.id, c.code, c.name, s.id, s.code, s.name, 'PENDING', 940000.00, 'Reabastecimiento mensual', NOW(), DATEADD('DAY', 5, NOW()), NOW(), NOW()
FROM customers c, suppliers s
WHERE c.code = 'CLI001' AND s.code = 'SUP001' LIMIT 1;

-- ============================================
-- ITEMS DE ÓRDENES
-- ============================================
INSERT INTO order_items (order_id, product_id, product_code, product_name, unit_price, quantity, subtotal)
SELECT o.id, p.id, p.code, p.name, p.price, 100, p.price * 100
FROM orders o, products p
WHERE o.order_number = 'ORD-2024-001' AND p.code = 'MED001' LIMIT 1;
