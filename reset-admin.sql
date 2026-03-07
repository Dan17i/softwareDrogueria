-- Script para resetear el usuario admin en producción
-- Ejecuta esto en la consola SQL de Render

-- 1. Eliminar el admin existente
DELETE FROM users WHERE username = 'admin';

-- Ahora puedes llamar al endpoint POST /api/auth/dev-create-admin
-- para crear un nuevo admin con credenciales conocidas:
-- username: admin
-- password: admin123
