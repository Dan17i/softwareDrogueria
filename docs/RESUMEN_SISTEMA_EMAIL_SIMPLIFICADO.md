# 📧 Sistema de Email - Versión Simplificada

## ✅ Implementación Completada

Se implementó un sistema de email con las siguientes características:

### 1. Email de Bienvenida Simple ✅
- Usuario se registra → Recibe email de bienvenida automáticamente
- **NO requiere verificación** - La cuenta está activa inmediatamente
- Email simple y amigable dando la bienvenida al sistema

### 2. Recuperación de Contraseña ✅
- Usuario solicita recuperación → Recibe email con link temporal
- Token válido por 1 hora
- Token de un solo uso (no reutilizable)
- Protección contra enumeración de emails

---

## 🔌 Endpoints Disponibles

### 1. Registro (modificado)
```http
POST /api/auth/register
```
**Cambio:** Ahora envía email de bienvenida automáticamente (sin verificación)

### 2. Solicitar Recuperación (nuevo)
```http
POST /api/auth/forgot-password
Body: { "email": "usuario@example.com" }
```

### 3. Restablecer Contraseña (nuevo)
```http
POST /api/auth/reset-password
Body: { "token": "abc123", "newPassword": "nueva123" }
```

---

## 📧 Emails Enviados

### 1. Bienvenida (al registrarse)
```
Asunto: ¡Bienvenido a Droguería Bellavista!

Hola usuario123,

¡Bienvenido a Droguería Bellavista!

Tu cuenta ha sido creada exitosamente y ya puedes comenzar a usar el sistema.

Si tienes alguna pregunta o necesitas ayuda, no dudes en contactarnos.

Saludos,
Equipo Droguería Bellavista
```

### 2. Recuperación de Contraseña
```
Asunto: Recuperación de Contraseña - Droguería Bellavista

Hola usuario123,

Recibimos una solicitud para restablecer la contraseña de tu cuenta.

Para crear una nueva contraseña, haz clic en el siguiente enlace:

http://localhost:5173/reset-password?token=xyz789-abc123-def456

Este enlace es válido por 1 hora.

Si no solicitaste restablecer tu contraseña, puedes ignorar este mensaje.

Saludos,
Equipo Droguería Bellavista
```

### 3. Contraseña Cambiada (confirmación)
```
Asunto: Contraseña Actualizada - Droguería Bellavista

Hola usuario123,

Tu contraseña ha sido actualizada exitosamente.

Si no realizaste este cambio, por favor contacta inmediatamente con soporte.

Saludos,
Equipo Droguería Bellavista
```

---

## ⚙️ Configuración Necesaria

### Variables de Entorno

```bash
# Email Server (Gmail)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=tu-email@gmail.com
MAIL_PASSWORD=tu-contraseña-de-aplicacion
MAIL_FROM=tu-email@gmail.com

# Frontend URL
FRONTEND_URL=http://localhost:5173
```

**Ver `CONFIGURAR_GMAIL_RAPIDO.md` para obtener la contraseña de aplicación de Gmail.**

---

## 🎨 Frontend Necesario

Solo necesitas crear **1 página**:

### `/reset-password` - Restablecer Contraseña

```typescript
// pages/ResetPassword.tsx
import { useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

const ResetPassword = () => {
    const [searchParams] = useSearchParams();
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const token = searchParams.get('token');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (newPassword !== confirmPassword) {
            setError('Las contraseñas no coinciden');
            return;
        }

        if (newPassword.length < 8) {
            setError('La contraseña debe tener al menos 8 caracteres');
            return;
        }

        setLoading(true);

        try {
            const response = await axios.post(
                'https://drogueria-bellavista-api.onrender.com/api/auth/reset-password',
                { token, newPassword }
            );
            
            setMessage(response.data.message);
            setTimeout(() => navigate('/login'), 3000);
            
        } catch (err: any) {
            setError(err.response?.data?.message || 'Error al restablecer contraseña');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="reset-password-container">
            <h2>Restablecer Contraseña</h2>
            
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Nueva Contraseña:</label>
                    <input
                        type="password"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        required
                        minLength={8}
                    />
                </div>

                <div className="form-group">
                    <label>Confirmar Contraseña:</label>
                    <input
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                    />
                </div>

                <button type="submit" disabled={loading}>
                    {loading ? 'Restableciendo...' : 'Restablecer Contraseña'}
                </button>
            </form>

            {message && <div className="success">{message}</div>}
            {error && <div className="error">{error}</div>}
        </div>
    );
};

export default ResetPassword;
```

### Modificar `/login` - Agregar Link

```typescript
<form onSubmit={handleLogin}>
    {/* ... campos de login ... */}
    
    <div className="forgot-password-link">
        <Link to="/forgot-password">¿Olvidaste tu contraseña?</Link>
    </div>
    
    <button type="submit">Iniciar Sesión</button>
</form>
```

### `/forgot-password` - Solicitar Recuperación

```typescript
// pages/ForgotPassword.tsx
import { useState } from 'react';
import axios from 'axios';

const ForgotPassword = () => {
    const [email, setEmail] = useState('');
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);

        try {
            const response = await axios.post(
                'https://drogueria-bellavista-api.onrender.com/api/auth/forgot-password',
                { email }
            );
            
            setMessage(response.data.message);
            setEmail('');
            
        } catch (err: any) {
            setMessage('Si el email existe, recibirás instrucciones.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="forgot-password-container">
            <h2>Recuperar Contraseña</h2>
            
            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    placeholder="tu-email@example.com"
                />
                <button type="submit" disabled={loading}>
                    {loading ? 'Enviando...' : 'Enviar Instrucciones'}
                </button>
            </form>

            {message && <div className="message">{message}</div>}
        </div>
    );
};

export default ForgotPassword;
```

---

## 🧪 Testing

### 1. Probar Registro con Email
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test",
    "email": "tu-email@gmail.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

**Resultado:** Deberías recibir un email de bienvenida simple.

### 2. Probar Recuperación
```bash
curl -X POST http://localhost:8080/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{"email": "tu-email@gmail.com"}'
```

**Resultado:** Deberías recibir un email con link de recuperación.

---

## 🔐 Seguridad

- ✅ Tokens UUID únicos
- ✅ Tokens expiran en 1 hora
- ✅ Tokens de un solo uso
- ✅ Contraseñas hasheadas (BCrypt)
- ✅ Emails asíncronos
- ✅ Anti-enumeración de emails

---

## 📊 Cambios en Base de Datos

### Tabla `users` (modificada)
- `email_verified` → Siempre TRUE (no se usa verificación)
- `email_verification_token` → NULL (no se usa)

### Tabla `password_reset_tokens` (nueva)
```sql
CREATE TABLE password_reset_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(100) UNIQUE,
    user_id BIGINT,
    expiry_date TIMESTAMP,
    used BOOLEAN,
    created_at TIMESTAMP
);
```

---

## 🎯 Resumen

**Lo que SÍ hace:**
- ✅ Envía email de bienvenida al registrarse (simple, sin verificación)
- ✅ Permite recuperar contraseña mediante email
- ✅ Envía confirmación cuando se cambia la contraseña

**Lo que NO hace:**
- ❌ NO requiere verificar el email para usar la cuenta
- ❌ NO bloquea la cuenta hasta verificar
- ❌ NO tiene endpoints de verificación de email

---

**Fecha:** Marzo 7, 2026  
**Estado:** ✅ COMPLETADO Y SIMPLIFICADO  
**Pendiente:** Configurar Email + Frontend (2 páginas)
