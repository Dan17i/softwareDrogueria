# 🚀 Configuración Rápida de Gmail para Emails

## Pasos para Configurar Gmail

### 1. Activar Verificación en Dos Pasos

1. Ve a https://myaccount.google.com/
2. Click en "Seguridad" (menú izquierdo)
3. Busca "Verificación en dos pasos"
4. Click en "Empezar" y sigue los pasos
5. Verifica tu identidad con tu teléfono

### 2. Generar Contraseña de Aplicación

1. Una vez activada la verificación en dos pasos
2. Ve a https://myaccount.google.com/apppasswords
3. O busca "Contraseñas de aplicaciones" en Seguridad
4. Selecciona:
   - App: "Correo"
   - Dispositivo: "Otro (nombre personalizado)"
   - Escribe: "Drogueria Bellavista"
5. Click en "Generar"
6. **COPIA LA CONTRASEÑA** (16 caracteres sin espacios)
   - Ejemplo: `abcd efgh ijkl mnop`
   - Úsala sin espacios: `abcdefghijklmnop`

### 3. Configurar Variables de Entorno

#### En Windows (CMD):
```cmd
set MAIL_HOST=smtp.gmail.com
set MAIL_PORT=587
set MAIL_USERNAME=tu-email@gmail.com
set MAIL_PASSWORD=abcdefghijklmnop
set MAIL_FROM=tu-email@gmail.com
set FRONTEND_URL=http://localhost:5173
```

#### En Windows (PowerShell):
```powershell
$env:MAIL_HOST="smtp.gmail.com"
$env:MAIL_PORT="587"
$env:MAIL_USERNAME="tu-email@gmail.com"
$env:MAIL_PASSWORD="abcdefghijklmnop"
$env:MAIL_FROM="tu-email@gmail.com"
$env:FRONTEND_URL="http://localhost:5173"
```

#### En Linux/Mac:
```bash
export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
export MAIL_USERNAME=tu-email@gmail.com
export MAIL_PASSWORD=abcdefghijklmnop
export MAIL_FROM=tu-email@gmail.com
export FRONTEND_URL=http://localhost:5173
```

### 4. Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

### 5. Probar

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "tu-email@gmail.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

**Deberías recibir un email de bienvenida en tu Gmail!**

---

## 🔧 Configurar en Render (Producción)

1. Ve a tu servicio en Render
2. Click en "Environment"
3. Click en "Add Environment Variable"
4. Agrega cada variable:

```
MAIL_HOST = smtp.gmail.com
MAIL_PORT = 587
MAIL_USERNAME = tu-email@gmail.com
MAIL_PASSWORD = abcdefghijklmnop
MAIL_FROM = tu-email@gmail.com
FRONTEND_URL = https://tu-frontend.onrender.com
```

5. Click en "Save Changes"
6. Render redesplegará automáticamente

---

## ⚠️ Troubleshooting

### Error: "Authentication failed"
- ✅ Verifica que usaste la contraseña de aplicación, NO tu contraseña normal de Gmail
- ✅ Copia la contraseña sin espacios
- ✅ Verifica que el email sea correcto

### Error: "Connection timeout"
- ✅ Verifica que MAIL_HOST sea `smtp.gmail.com`
- ✅ Verifica que MAIL_PORT sea `587`
- ✅ Verifica tu conexión a internet

### No recibo emails
- ✅ Revisa la carpeta de spam
- ✅ Verifica los logs del backend para ver errores
- ✅ Verifica que MAIL_FROM sea tu email de Gmail

### "Less secure app access"
- ❌ NO uses esta opción (está deprecada)
- ✅ Usa contraseña de aplicación en su lugar

---

## 📝 Notas Importantes

1. **Contraseña de Aplicación ≠ Contraseña de Gmail**
   - La contraseña de aplicación es diferente
   - Es de 16 caracteres
   - Solo se muestra una vez

2. **Límites de Gmail**
   - 500 emails por día (cuenta gratuita)
   - 100 emails por hora
   - Para más, usa SendGrid o Mailgun

3. **Seguridad**
   - Nunca compartas tu contraseña de aplicación
   - Puedes revocarla en cualquier momento
   - Genera una nueva si la pierdes

---

## 🎯 Verificación Rápida

Después de configurar, verifica que funciona:

```bash
# 1. Registrar usuario
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test",
    "email": "tu-email@gmail.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'

# 2. Revisa tu email - deberías recibir bienvenida

# 3. Solicitar recuperación
curl -X POST http://localhost:8080/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{"email": "tu-email@gmail.com"}'

# 4. Revisa tu email - deberías recibir recuperación
```

---

**¡Listo! Tu sistema de emails está configurado y funcionando.**
