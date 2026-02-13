-- Script SQL para datos de prueba - PostgreSQL
-- Ejecuta esto después de que Spring cree las tablas

-- ============================================
-- CLIENTES
-- ============================================
INSERT INTO customers (code, name, email, phone, address, city, postal_code, document_number, document_type, customer_type, credit_limit, pending_balance, active, created_at, updated_at)
VALUES ('CLI001', 'Farmacia Central', 'farmacia.central@email.com', '031-123456', 'Calle 1 #100', 'Bogotá', '110000', '9001234567', 'NIT', 'MAYORISTA', 50000000.00, 0.00, true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO customers (code, name, email, phone, address, city, postal_code, document_number, document_type, customer_type, credit_limit, pending_balance, active, created_at, updated_at)
VALUES ('CLI002', 'Farmacia del Barrio', 'farmacia.barrio@email.com', '031-654321', 'Carrera 5 #50', 'Bogotá', '110001', '8001234567', 'CC', 'MINORISTA', 5000000.00, 1000000.00, true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO customers (code, name, email, phone, address, city, postal_code, document_number, document_type, customer_type, credit_limit, pending_balance, active, created_at, updated_at)
VALUES ('CLI003', 'Droguería Express', 'drogeria.express@email.com', '031-789456', 'Avenida Principal #200', 'Medellín', '050000', '9101234567', 'NIT', 'MAYORISTA', 75000000.00, 0.00, true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO customers (code, name, email, phone, address, city, postal_code, document_number, document_type, customer_type, credit_limit, pending_balance, active, created_at, updated_at)
VALUES ('CLI004', 'Farmacia La Salud', 'salud@email.com', '031-321654', 'Calle 20 #30', 'Cali', '760000', '8101234567', 'CC', 'MINORISTA', 3000000.00, 500000.00, true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO customers (code, name, email, phone, address, city, postal_code, document_number, document_type, customer_type, credit_limit, pending_balance, active, created_at, updated_at)
VALUES ('CLI005', 'Distribuidora Nacional', 'dist.nacional@email.com', '031-999888', 'Zona Industrial #45', 'Bucaramanga', '680000', '9201234567', 'NIT', 'MAYORISTA', 100000000.00, 5000000.00, true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO customers (code, name, email, phone, address, city, postal_code, document_number, document_type, customer_type, credit_limit, pending_balance, active, created_at, updated_at)
VALUES ('CLI006', 'Tienda Don Carlos', 'don.carlos@email.com', '031-456789', 'Calle 10 #80', 'Barranquilla', '080000', '8201234567', 'CC', 'MINORISTA', 2000000.00, 0.00, true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO customers (code, name, email, phone, address, city, postal_code, document_number, document_type, customer_type, credit_limit, pending_balance, active, created_at, updated_at)
VALUES ('CLI007', 'Farmacia Antigua', 'farmacia.antigua@email.com', '031-111111', 'Calle Vieja #1', 'Bogotá', '110002', '8301234567', 'CC', 'MINORISTA', 1000000.00, 0.00, false, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

-- ============================================
-- PROVEEDORES
-- ============================================
INSERT INTO suppliers (code, name, email, phone, address, city, postal_code, document_number, document_type, lead_time_days, average_payment_delay, active, created_at, updated_at)
VALUES ('SUP001', 'Laboratorios Farmacéuticos SA', 'contacto@labfarm.com', '031-122000', 'Avenida 1 #500', 'Bogotá', '110000', '8601234567', 'NIT', 3, 1.50, true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO suppliers (code, name, email, phone, address, city, postal_code, document_number, document_type, lead_time_days, average_payment_delay, active, created_at, updated_at)
VALUES ('SUP002', 'Distribuidora Médica Internacional', 'ventas@dismedint.com', '031-135000', 'Carrera 10 #150', 'Bogotá', '110001', '8602345678', 'NIT', 5, 2.00, true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO suppliers (code, name, email, phone, address, city, postal_code, document_number, document_type, lead_time_days, average_payment_delay, active, created_at, updated_at)
VALUES ('SUP003', 'Proveedora de Medicamentos del Centro', 'info@provmedicamentos.com', '044-456789', 'Calle 50 #90', 'Medellín', '050000', '8103456789', 'NIT', 4, 1.00, true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO suppliers (code, name, email, phone, address, city, postal_code, document_number, document_type, lead_time_days, average_payment_delay, active, created_at, updated_at)
VALUES ('SUP004', 'Suministros Médicos y Farmacéuticos', 'pedidos@suministrosmed.com', '031-987654', 'Avenida Principal #250', 'Bogotá', '110002', '8604567890', 'NIT', 2, 0.50, true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO suppliers (code, name, email, phone, address, city, postal_code, document_number, document_type, lead_time_days, average_payment_delay, active, created_at, updated_at)
VALUES ('SUP005', 'Química y Farmacología Moderna', 'contacto@quimfarm.com', '032-111222', 'Zona Industrial #100', 'Cali', '760000', '8705678901', 'NIT', 6, 3.00, true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

-- ============================================
-- PRODUCTOS
-- ============================================
INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('MED001', 'Acetaminofén 500mg', 'Analgésico y antipirético en tabletas', 5000.00, 150, 30, 'Medicamentos', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('MED002', 'Ibuprofeno 400mg', 'Antiinflamatorio no esteroideo', 8000.00, 80, 20, 'Medicamentos', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('MED003', 'Omeprazol 20mg', 'Inhibidor de la bomba de protones', 12000.00, 60, 15, 'Medicamentos', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('MED004', 'Loratadina 10mg', 'Antihistamínico para alergias', 6500.00, 100, 25, 'Medicamentos', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('MED005', 'Amoxicilina 500mg', 'Antibiótico de amplio espectro', 15000.00, 45, 20, 'Medicamentos', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('VIT001', 'Vitamina C 1000mg', 'Suplemento vitamínico', 18000.00, 120, 30, 'Vitaminas', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('VIT002', 'Vitamina D3 2000 UI', 'Suplemento de vitamina D', 22000.00, 70, 20, 'Vitaminas', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('VIT003', 'Complejo B', 'Vitaminas del complejo B', 16000.00, 90, 25, 'Vitaminas', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('COS001', 'Protector Solar FPS 50+', 'Protección UV de amplio espectro', 35000.00, 40, 15, 'Cosméticos', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('COS002', 'Crema Hidratante Facial', 'Hidratación profunda para todo tipo de piel', 28000.00, 55, 15, 'Cosméticos', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('COS003', 'Shampoo Anticaspa', 'Tratamiento contra la caspa', 18000.00, 65, 20, 'Cosméticos', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('HIG001', 'Alcohol Antiséptico 70%', 'Desinfectante de manos y superficies', 8000.00, 200, 50, 'Higiene', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('HIG002', 'Gel Antibacterial 500ml', 'Desinfectante de manos sin agua', 12000.00, 150, 40, 'Higiene', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('HIG003', 'Tapabocas Quirúrgico x50', 'Protección facial desechable', 25000.00, 80, 30, 'Higiene', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('HIG004', 'Guantes de Látex x100', 'Guantes desechables para uso médico', 30000.00, 60, 25, 'Higiene', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('BEB001', 'Suero Oral 500ml', 'Rehidratación oral', 6000.00, 100, 30, 'Bebidas', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('BEB002', 'Agua Destilada 1L', 'Agua purificada para uso médico', 4000.00, 120, 35, 'Bebidas', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('EQU001', 'Termómetro Digital', 'Medición de temperatura corporal', 25000.00, 30, 10, 'Equipos', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('EQU002', 'Tensiómetro Digital', 'Medición de presión arterial', 80000.00, 15, 5, 'Equipos', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('EQU003', 'Glucómetro con 50 Tiras', 'Medición de glucosa en sangre', 120000.00, 10, 3, 'Equipos', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('MED006', 'Aspirina 100mg', 'Antiagregante plaquetario', 7000.00, 15, 20, 'Medicamentos', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('VIT004', 'Calcio + Vitamina D', 'Suplemento para huesos', 20000.00, 8, 15, 'Vitaminas', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at)
VALUES ('COS004', 'Desodorante Roll-on', 'Protección 48 horas', 12000.00, 10, 20, 'Cosméticos', true, NOW(), NOW())
    ON CONFLICT (code) DO NOTHING;

-- ============================================
-- ÓRDENES
-- ============================================
INSERT INTO orders (order_number, customer_id, customer_code, customer_name, supplier_id, supplier_code, supplier_name, status, total, notes, order_date, expected_delivery_date, created_at, updated_at)
SELECT 'ORD-2024-001', c.id, c.code, c.name, s.id, s.code, s.name, 'PENDING', 940000.00, 'Reabastecimiento mensual', NOW(), NOW() + INTERVAL '5 days', NOW(), NOW()
FROM customers c, suppliers s
WHERE c.code = 'CLI001' AND s.code = 'SUP001'
ON CONFLICT (order_number) DO NOTHING;

INSERT INTO orders (order_number, customer_id, customer_code, customer_name, supplier_id, supplier_code, supplier_name, status, total, notes, order_date, expected_delivery_date, created_at, updated_at)
SELECT 'ORD-2024-002', c.id, c.code, c.name, s.id, s.code, s.name, 'PENDING', 228000.00, 'Orden de farmacia del barrio', NOW(), NOW() + INTERVAL '3 days', NOW(), NOW()
FROM customers c, suppliers s
WHERE c.code = 'CLI002' AND s.code = 'SUP002'
ON CONFLICT (order_number) DO NOTHING;

INSERT INTO orders (order_number, customer_id, customer_code, customer_name, supplier_id, supplier_code, supplier_name, status, total, notes, order_date, expected_delivery_date, actual_delivery_date, created_at, updated_at)
SELECT 'ORD-2024-003', c.id, c.code, c.name, s.id, s.code, s.name, 'COMPLETED', 580000.00, 'Orden completada', NOW() - INTERVAL '7 days', NOW() - INTERVAL '4 days', NOW() - INTERVAL '2 days', NOW() - INTERVAL '7 days', NOW() - INTERVAL '2 days'
FROM customers c, suppliers s
WHERE c.code = 'CLI003' AND s.code = 'SUP003'
ON CONFLICT (order_number) DO NOTHING;

-- ============================================
-- ITEMS DE ÓRDENES
-- ============================================
-- Para order_items usamos INSERT con verificación
INSERT INTO order_items (order_id, product_id, product_code, product_name, unit_price, quantity, subtotal)
SELECT o.id, p.id, p.code, p.name, p.price, 100, p.price * 100
FROM orders o, products p
WHERE o.order_number = 'ORD-2024-001' AND p.code = 'MED001'
  AND NOT EXISTS (
    SELECT 1 FROM order_items oi
    WHERE oi.order_id = o.id AND oi.product_id = p.id
);

INSERT INTO order_items (order_id, product_id, product_code, product_name, unit_price, quantity, subtotal)
SELECT o.id, p.id, p.code, p.name, p.price, 50, p.price * 50
FROM orders o, products p
WHERE o.order_number = 'ORD-2024-001' AND p.code = 'VIT001'
  AND NOT EXISTS (
    SELECT 1 FROM order_items oi
    WHERE oi.order_id = o.id AND oi.product_id = p.id
);

INSERT INTO order_items (order_id, product_id, product_code, product_name, unit_price, quantity, subtotal)
SELECT o.id, p.id, p.code, p.name, p.price, 20, p.price * 20
FROM orders o, products p
WHERE o.order_number = 'ORD-2024-001' AND p.code = 'COS001'
  AND NOT EXISTS (
    SELECT 1 FROM order_items oi
    WHERE oi.order_id = o.id AND oi.product_id = p.id
);

-- ============================================
-- USUARIOS INICIALES
-- ============================================

-- Usuario Administrador
-- Password: Admin123456
INSERT INTO users (username, email, password, first_name, last_name, role, active, created_at)
VALUES (
           'admin',
           'admin@drogueria.com',
           '$2a$10$N9qo8uLOickgx2ZMRZoMye1lLjyH9K5c0Kz8ZXcGPNQ4XH9qNPJWu',
           'Administrador',
           'Sistema',
           'ADMIN',
           true,
           NOW()
       ) ON CONFLICT (username) DO NOTHING;

-- Usuario Vendedor de prueba
-- Password: Vendedor123
INSERT INTO users (username, email, password, first_name, last_name, role, active, created_at)
VALUES (
           'vendedor1',
           'vendedor1@drogueria.com',
           '$2a$10$rI8.Nc/5153JYjGOLlOSH.JwHLvqT5z8QwJvLpGJKvH0v8P8sVIXq',
           'Carlos',
           'Vendedor',
           'USER',
           true,
           NOW()
       ) ON CONFLICT (username) DO NOTHING;

-- Usuario de prueba general
-- Password: Test12345678
INSERT INTO users (username, email, password, first_name, last_name, role, active, created_at)
VALUES (
           'test123',
           'test123@test.com',
           '$2a$10$8h6qX0YvH8KkR5ZCvJHOCeMxJLNvJ.nXqnKb6xGqS9y4J6vZ7Kn5S',
           'Usuario',
           'Prueba',
           'USER',
           true,
           NOW()
       ) ON CONFLICT (username) DO NOTHING;