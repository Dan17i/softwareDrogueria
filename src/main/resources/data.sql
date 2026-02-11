-- Script SQL para datos de prueba
-- Ejecuta esto después de que Spring cree las tablas

-- Clientes de ejemplo
INSERT INTO customers (code, name, email, phone, address, city, postal_code, document_number, document_type, customer_type, credit_limit, pending_balance, active, created_at, updated_at) VALUES
('CLI001', 'Farmacia Central', 'farmacia.central@email.com', '031-123456', 'Calle 1 #100', 'Bogotá', '110000', '9001234567', 'NIT', 'MAYORISTA', 50000000.00, 0.00, true, NOW(), NOW()),
('CLI002', 'Farmacia del Barrio', 'farmacia.barrio@email.com', '031-654321', 'Carrera 5 #50', 'Bogotá', '110001', '8001234567', 'CC', 'MINORISTA', 5000000.00, 1000000.00, true, NOW(), NOW()),
('CLI003', 'Droguería Express', 'drogeria.express@email.com', '031-789456', 'Avenida Principal #200', 'Medellín', '050000', '9101234567', 'NIT', 'MAYORISTA', 75000000.00, 0.00, true, NOW(), NOW()),
('CLI004', 'Farmacia La Salud', 'salud@email.com', '031-321654', 'Calle 20 #30', 'Cali', '760000', '8101234567', 'CC', 'MINORISTA', 3000000.00, 500000.00, true, NOW(), NOW()),
('CLI005', 'Distribuidora Nacional', 'dist.nacional@email.com', '031-999888', 'Zona Industrial #45', 'Bucaramanga', '680000', '9201234567', 'NIT', 'MAYORISTA', 100000000.00, 5000000.00, true, NOW(), NOW()),
('CLI006', 'Tienda Don Carlos', 'don.carlos@email.com', '031-456789', 'Calle 10 #80', 'Barranquilla', '080000', '8201234567', 'CC', 'MINORISTA', 2000000.00, 0.00, true, NOW(), NOW()),

-- Cliente inactivo
('CLI007', 'Farmacia Antigua', 'farmacia.antigua@email.com', '031-111111', 'Calle Vieja #1', 'Bogotá', '110002', '8301234567', 'CC', 'MINORISTA', 1000000.00, 0.00, false, NOW(), NOW());

-- Proveedores de ejemplo
INSERT INTO suppliers (code, name, email, phone, address, city, postal_code, document_number, document_type, lead_time_days, average_payment_delay, active, created_at, updated_at) VALUES
('SUP001', 'Laboratorios Farmacéuticos SA', 'contacto@labfarm.com', '031-122000', 'Avenida 1 #500', 'Bogotá', '110000', '8601234567', 'NIT', 3, 1.50, true, NOW(), NOW()),
('SUP002', 'Distribuidora Médica Internacional', 'ventas@dismedint.com', '031-135000', 'Carrera 10 #150', 'Bogotá', '110001', '8602345678', 'NIT', 5, 2.00, true, NOW(), NOW()),
('SUP003', 'Proveedora de Medicamentos del Centro', 'info@provmedicamentos.com', '044-456789', 'Calle 50 #90', 'Medellín', '050000', '8103456789', 'NIT', 4, 1.00, true, NOW(), NOW()),
('SUP004', 'Suministros Médicos y Farmacéuticos', 'pedidos@suministrosmed.com', '031-987654', 'Avenida Principal #250', 'Bogotá', '110002', '8604567890', 'NIT', 2, 0.50, true, NOW(), NOW()),
('SUP005', 'Química y Farmacología Moderna', 'contacto@quimfarm.com', '032-111222', 'Zona Industrial #100', 'Cali', '760000', '8705678901', 'NIT', 6, 3.00, true, NOW(), NOW());

-- Productos de ejemplo
INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at) VALUES
('MED001', 'Acetaminofén 500mg', 'Analgésico y antipirético en tabletas', 5000.00, 150, 30, 'Medicamentos', true, NOW(), NOW()),
('MED002', 'Ibuprofeno 400mg', 'Antiinflamatorio no esteroideo', 8000.00, 80, 20, 'Medicamentos', true, NOW(), NOW()),
('MED003', 'Omeprazol 20mg', 'Inhibidor de la bomba de protones', 12000.00, 60, 15, 'Medicamentos', true, NOW(), NOW()),
('MED004', 'Loratadina 10mg', 'Antihistamínico para alergias', 6500.00, 100, 25, 'Medicamentos', true, NOW(), NOW()),
('MED005', 'Amoxicilina 500mg', 'Antibiótico de amplio espectro', 15000.00, 45, 20, 'Medicamentos', true, NOW(), NOW()),

('VIT001', 'Vitamina C 1000mg', 'Suplemento vitamínico', 18000.00, 120, 30, 'Vitaminas', true, NOW(), NOW()),
('VIT002', 'Vitamina D3 2000 UI', 'Suplemento de vitamina D', 22000.00, 70, 20, 'Vitaminas', true, NOW(), NOW()),
('VIT003', 'Complejo B', 'Vitaminas del complejo B', 16000.00, 90, 25, 'Vitaminas', true, NOW(), NOW()),

('COS001', 'Protector Solar FPS 50+', 'Protección UV de amplio espectro', 35000.00, 40, 15, 'Cosméticos', true, NOW(), NOW()),
('COS002', 'Crema Hidratante Facial', 'Hidratación profunda para todo tipo de piel', 28000.00, 55, 15, 'Cosméticos', true, NOW(), NOW()),
('COS003', 'Shampoo Anticaspa', 'Tratamiento contra la caspa', 18000.00, 65, 20, 'Cosméticos', true, NOW(), NOW()),

('HIG001', 'Alcohol Antiséptico 70%', 'Desinfectante de manos y superficies', 8000.00, 200, 50, 'Higiene', true, NOW(), NOW()),
('HIG002', 'Gel Antibacterial 500ml', 'Desinfectante de manos sin agua', 12000.00, 150, 40, 'Higiene', true, NOW(), NOW()),
('HIG003', 'Tapabocas Quirúrgico x50', 'Protección facial desechable', 25000.00, 80, 30, 'Higiene', true, NOW(), NOW()),
('HIG004', 'Guantes de Látex x100', 'Guantes desechables para uso médico', 30000.00, 60, 25, 'Higiene', true, NOW(), NOW()),

('BEB001', 'Suero Oral 500ml', 'Rehidratación oral', 6000.00, 100, 30, 'Bebidas', true, NOW(), NOW()),
('BEB002', 'Agua Destilada 1L', 'Agua purificada para uso médico', 4000.00, 120, 35, 'Bebidas', true, NOW(), NOW()),

('EQU001', 'Termómetro Digital', 'Medición de temperatura corporal', 25000.00, 30, 10, 'Equipos', true, NOW(), NOW()),
('EQU002', 'Tensiómetro Digital', 'Medición de presión arterial', 80000.00, 15, 5, 'Equipos', true, NOW(), NOW()),
('EQU003', 'Glucómetro con 50 Tiras', 'Medición de glucosa en sangre', 120000.00, 10, 3, 'Equipos', true, NOW(), NOW());

-- Productos con stock bajo (para probar la funcionalidad de reabastecimiento)
INSERT INTO products (code, name, description, price, stock, min_stock, category, active, created_at, updated_at) VALUES
('MED006', 'Aspirina 100mg', 'Antiagregante plaquetario', 7000.00, 15, 20, 'Medicamentos', true, NOW(), NOW()),
('VIT004', 'Calcio + Vitamina D', 'Suplemento para huesos', 20000.00, 8, 15, 'Vitaminas', true, NOW(), NOW()),
('COS004', 'Desodorante Roll-on', 'Protección 48 horas', 12000.00, 10, 20, 'Cosméticos', true, NOW(), NOW());

-- Órdenes de ejemplo
-- Orden 1: PENDING - Cliente mayorista con múltiples items
INSERT INTO orders (order_number, customer_id, customer_code, customer_name, supplier_id, supplier_code, supplier_name, status, total, notes, order_date, expected_delivery_date, actual_delivery_date, created_at, updated_at)
SELECT o.order_number, c.id, c.code, c.name, s.id, s.code, s.name, o.status, o.total, o.notes, o.order_date, o.expected_delivery_date, o.actual_delivery_date, o.created_at, o.updated_at
FROM (
  SELECT 'ORD-2024-001' as order_number, 'PENDING' as status, 940000.00 as total, 'Reabastecimiento mensual' as notes, NOW() as order_date, DATEADD('DAY', 5, CURRENT_TIMESTAMP()) as expected_delivery_date, NULL as actual_delivery_date, NOW() as created_at, NOW() as updated_at
) o, (SELECT id, code, name FROM customers WHERE code = 'CLI001') c, (SELECT id, code, name FROM suppliers WHERE code = 'SUP001') s;

-- Agregar items a la Orden 1
INSERT INTO order_items (order_id, product_id, product_code, product_name, unit_price, quantity, subtotal, created_at, updated_at)
SELECT o.id, p.id, p.code, p.name, p.price, oi.quantity, (p.price * oi.quantity) as subtotal, NOW(), NOW()
FROM orders o, products p, (
  SELECT p.code, COUNT(*) as quantity FROM (
    SELECT 'MED001' as code, 100 as qty
    UNION ALL SELECT 'VIT001', 50
    UNION ALL SELECT 'COS001', 20
  ) p GROUP BY p.code
) oi
WHERE o.order_number = 'ORD-2024-001'
  AND p.code = oi.code
ORDER BY oi.code;

-- Orden 2: PENDING - Cliente minorista con menos items
INSERT INTO orders (order_number, customer_id, customer_code, customer_name, supplier_id, supplier_code, supplier_name, status, total, notes, order_date, expected_delivery_date, actual_delivery_date, created_at, updated_at)
SELECT o.order_number, c.id, c.code, c.name, s.id, s.code, s.name, o.status, o.total, o.notes, o.order_date, o.expected_delivery_date, o.actual_delivery_date, o.created_at, o.updated_at
FROM (
  SELECT 'ORD-2024-002' as order_number, 'PENDING' as status, 228000.00 as total, 'Orden de farmacia del barrio' as notes, NOW() as order_date, DATEADD('DAY', 3, NOW()) as expected_delivery_date, NULL as actual_delivery_date, NOW() as created_at, NOW() as updated_at
) o, (SELECT id, code, name FROM customers WHERE code = 'CLI002') c, (SELECT id, code, name FROM suppliers WHERE code = 'SUP002') s;

-- Agregar items a la Orden 2
INSERT INTO order_items (order_id, product_id, product_code, product_name, unit_price, quantity, subtotal, created_at, updated_at)
SELECT o.id, p.id, p.code, p.name, p.price, oi.quantity, (p.price * oi.quantity) as subtotal, NOW(), NOW()
FROM orders o, products p, (
  SELECT p.code, COUNT(*) as quantity FROM (
    SELECT 'MED002' as code, 20 as qty
    UNION ALL SELECT 'MED003', 15
    UNION ALL SELECT 'HIG001', 30
  ) p GROUP BY p.code
) oi
WHERE o.order_number = 'ORD-2024-002'
  AND p.code = oi.code
ORDER BY oi.code;

-- Orden 3: COMPLETED - Una orden ya completada
INSERT INTO orders (order_number, customer_id, customer_code, customer_name, supplier_id, supplier_code, supplier_name, status, total, notes, order_date, expected_delivery_date, actual_delivery_date, created_at, updated_at)
SELECT o.order_number, c.id, c.code, c.name, s.id, s.code, s.name, o.status, o.total, o.notes, o.order_date, o.expected_delivery_date, o.actual_delivery_date, o.created_at, o.updated_at
FROM (
  SELECT 'ORD-2024-003' as order_number, 'COMPLETED' as status, 580000.00 as total, 'Orden completada - Distribuidora Nacional' as notes, DATEADD('DAY', -7, NOW()) as order_date, DATE_SUB(NOW(), INTERVAL 4 DAY) as expected_delivery_date, DATE_SUB(NOW(), INTERVAL 2 DAY) as actual_delivery_date, DATEADD('DAY', -7, NOW()) as created_at, DATE_SUB(NOW(), INTERVAL 2 DAY) as updated_at
) o, (SELECT id, code, name FROM customers WHERE code = 'CLI003') c, (SELECT id, code, name FROM suppliers WHERE code = 'SUP003') s;

-- Agregar items a la Orden 3
INSERT INTO order_items (order_id, product_id, product_code, product_name, unit_price, quantity, subtotal, created_at, updated_at)
SELECT o.id, p.id, p.code, p.name, p.price, oi.quantity, (p.price * oi.quantity) as subtotal, NOW(), NOW()
FROM orders o, products p, (
  SELECT p.code, COUNT(*) as quantity FROM (
    SELECT 'VIT001' as code, 15 as qty
    UNION ALL SELECT 'VIT002', 10
    UNION ALL SELECT 'COS002', 8
    UNION ALL SELECT 'HIG003', 25
  ) p GROUP BY p.code
) oi
WHERE o.order_number = 'ORD-2024-003'
  AND p.code = oi.code
ORDER BY oi.code;

-- Orden 4: CANCELLED - Una orden cancelada
INSERT INTO orders (order_number, customer_id, customer_code, customer_name, supplier_id, supplier_code, supplier_name, status, total, notes, order_date, expected_delivery_date, actual_delivery_date, created_at, updated_at)
SELECT o.order_number, c.id, c.code, c.name, s.id, s.code, s.name, o.status, o.total, o.notes, o.order_date, o.expected_delivery_date, o.actual_delivery_date, o.created_at, o.updated_at
FROM (
  SELECT 'ORD-2024-004' as order_number, 'CANCELLED' as status, 120000.00 as total, 'Orden cancelada - Cliente cambió de proveedor' as notes, DATE_SUB(NOW(), INTERVAL 10 DAY) as order_date, DATE_SUB(NOW(), INTERVAL 8 DAY) as expected_delivery_date, NULL as actual_delivery_date, DATE_SUB(NOW(), INTERVAL 10 DAY) as created_at, DATE_SUB(NOW(), INTERVAL 3 DAY) as updated_at
) o, (SELECT id, code, name FROM customers WHERE code = 'CLI004') c, (SELECT id, code, name FROM suppliers WHERE code = 'SUP004') s;

-- Agregar items a la Orden 4
INSERT INTO order_items (order_id, product_id, product_code, product_name, unit_price, quantity, subtotal, created_at, updated_at)
SELECT o.id, p.id, p.code, p.name, p.price, oi.quantity, (p.price * oi.quantity) as subtotal, NOW(), NOW()
FROM orders o, products p, (
  SELECT p.code, COUNT(*) as quantity FROM (
    SELECT 'MED004' as code, 40 as qty
    UNION ALL SELECT 'HIG002', 20
  ) p GROUP BY p.code
) oi
WHERE o.order_number = 'ORD-2024-004'
  AND p.code = oi.code
ORDER BY oi.code;

-- Orden 5: PENDING - Cliente con varios artículos de equipos
INSERT INTO orders (order_number, customer_id, customer_code, customer_name, supplier_id, supplier_code, supplier_name, status, total, notes, order_date, expected_delivery_date, actual_delivery_date, created_at, updated_at)
SELECT o.order_number, c.id, c.code, c.name, s.id, s.code, s.name, o.status, o.total, o.notes, o.order_date, o.expected_delivery_date, o.actual_delivery_date, o.created_at, o.updated_at
FROM (
  SELECT 'ORD-2024-005' as order_number, 'PENDING' as status, 305000.00 as total, 'Equipos médicos especiales' as notes, NOW() as order_date, DATE_ADD(NOW(), INTERVAL 7 DAY) as expected_delivery_date, NULL as actual_delivery_date, NOW() as created_at, NOW() as updated_at
) o, (SELECT id, code, name FROM customers WHERE code = 'CLI005') c, (SELECT id, code, name FROM suppliers WHERE code = 'SUP005') s;

-- Agregar items a la Orden 5
INSERT INTO order_items (order_id, product_id, product_code, product_name, unit_price, quantity, subtotal, created_at, updated_at)
SELECT o.id, p.id, p.code, p.name, p.price, oi.quantity, (p.price * oi.quantity) as subtotal, NOW(), NOW()
FROM orders o, products p, (
  SELECT p.code, COUNT(*) as quantity FROM (
    SELECT 'EQU001' as code, 5 as qty
    UNION ALL SELECT 'EQU002', 2
    UNION ALL SELECT 'BEB001', 25
  ) p GROUP BY p.code
) oi
WHERE o.order_number = 'ORD-2024-005'
  AND p.code = oi.code
ORDER BY oi.code;
-- Recepción de Mercancía (Goods Receipts) de ejemplo
-- GR1: RECEIVED - Recepción completa de la Orden 3
INSERT INTO goods_receipts (receipt_number, order_id, order_number, supplier_id, supplier_name, status, notes, expected_delivery_date, created_at, updated_at)
SELECT gr.receipt_number, o.id, o.order_number, o.supplier_id, o.supplier_name, gr.status, gr.notes, gr.expected_delivery_date, gr.created_at, gr.updated_at
FROM orders o, (
  SELECT 'GR-202400001' as receipt_number, 'RECEIVED' as status, 'Recepción completa de medicamentos y vitaminas' as notes, DATEADD('DAY', 5, CURRENT_TIMESTAMP()) as expected_delivery_date, DATE_SUB(NOW(), INTERVAL 2 DAY) as created_at, DATE_SUB(NOW(), INTERVAL 1 DAY) as updated_at
) gr
WHERE o.order_number = 'ORD-2024-003';

-- Agregar items a GR1
INSERT INTO goods_receipt_items (goods_receipt_id, product_id, product_code, product_name, ordered_quantity, received_quantity)
SELECT gr.id, oi.product_id, oi.product_code, oi.product_name, oi.quantity, oi.quantity
FROM goods_receipts gr, order_items oi, orders o
WHERE gr.receipt_number = 'GR-202400001'
  AND oi.order_id = o.id
  AND o.order_number = 'ORD-2024-003';

-- GR2: PARTIALLY_RECEIVED - Recepción parcial de la Orden 1
INSERT INTO goods_receipts (receipt_number, order_id, order_number, supplier_id, supplier_name, status, notes, expected_delivery_date, created_at, updated_at)
SELECT gr.receipt_number, o.id, o.order_number, o.supplier_id, o.supplier_name, gr.status, gr.notes, gr.expected_delivery_date, gr.created_at, gr.updated_at
FROM orders o, (
  SELECT 'GR-202400002' as receipt_number, 'PARTIALLY_RECEIVED' as status, 'Recepción parcial - Falta MED001' as notes, DATEADD('DAY', 5, CURRENT_TIMESTAMP()) as expected_delivery_date, NOW() as created_at, NOW() as updated_at
) gr
WHERE o.order_number = 'ORD-2024-001';

-- Agregar items a GR2 (parcialmente recibidos)
INSERT INTO goods_receipt_items (goods_receipt_id, product_id, product_code, product_name, ordered_quantity, received_quantity)
SELECT gr.id, oi.product_id, oi.product_code, oi.product_name, oi.quantity as ordered_qty, 
  CASE WHEN oi.product_code = 'MED001' THEN 0 ELSE oi.quantity END as received_qty
FROM goods_receipts gr, order_items oi, orders o
WHERE gr.receipt_number = 'GR-202400002'
  AND oi.order_id = o.id
  AND o.order_number = 'ORD-2024-001';

-- GR3: PENDING - Recepción pendiente de la Orden 2
INSERT INTO goods_receipts (receipt_number, order_id, order_number, supplier_id, supplier_name, status, notes, expected_delivery_date, created_at, updated_at)
SELECT gr.receipt_number, o.id, o.order_number, o.supplier_id, o.supplier_name, gr.status, gr.notes, gr.expected_delivery_date, gr.created_at, gr.updated_at
FROM orders o, (
  SELECT 'GR-202400003' as receipt_number, 'PENDING' as status, 'Recepción en espera de confirmación' as notes, DATEADD('DAY', 3, NOW()) as expected_delivery_date, NOW() as created_at, NOW() as updated_at
) gr
WHERE o.order_number = 'ORD-2024-002';

-- Agregar items a GR3 (sin recepción aún)
INSERT INTO goods_receipt_items (goods_receipt_id, product_id, product_code, product_name, ordered_quantity, received_quantity)
SELECT gr.id, oi.product_id, oi.product_code, oi.product_name, oi.quantity as ordered_qty, 0 as received_qty
FROM goods_receipts gr, order_items oi, orders o
WHERE gr.receipt_number = 'GR-202400003'
  AND oi.order_id = o.id
  AND o.order_number = 'ORD-2024-002';