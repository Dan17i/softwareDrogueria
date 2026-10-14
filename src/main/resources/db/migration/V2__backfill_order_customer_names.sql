-- Las órdenes creadas antes de guardar la foto del cliente tienen customer_code / customer_name en NULL.
-- A partir de ahora OrderService.createOrder los llena; aquí se rellenan las existentes.
UPDATE orders o
SET customer_code = c.code,
    customer_name = c.name
FROM customers c
WHERE o.customer_id = c.id
  AND (o.customer_code IS NULL OR o.customer_name IS NULL);
