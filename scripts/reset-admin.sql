-- Script para resetear el usuario admin en producción
-- Ejecuta esto en la consola SQL de Render o DBeaver

-- 1. Verificar usuarios existentes
SELECT id, username, email, role FROM users;

-- 2. Eliminar el admin existente (si existe)
DELETE FROM users WHERE username = 'admin';

-- 3. Eliminar cualquier usuario con el email admin@bellavista.com
DELETE FROM users WHERE email = 'admin@bellavista.com';

-- Ahora puedes llamar al endpoint POST /api/auth/dev-create-admin
-- para crear un nuevo admin con credenciales conocidas:
-- username: admin
-- password: admin123
