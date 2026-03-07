# 📧 Configuración de Email - Droguería Bellavista

## Resumen

Se implementó un sistema completo de autenticación por email que incluye:

1. **Email de bienvenida** con link de verificación al registrarse
2. **Recuperación de contraseña** mediante token temporal (válido por 1 hora)
3. **Notificaciones** de cambios importantes en la cuenta

---

## 🔧 Configuración del Servidor de Email

### Opción 1: Gmail (Recomendado para desarrollo)

1. **Crear una contraseña de aplicación en Gmail:**
   - Ve a tu cuenta de Google: https://myaccount.google.com/
   - Seguridad → Verificación en dos pasos (actívala si no está activa)
   - Seguridad → Contraseñas de aplicaciones
   - Genera una contraseña para "Correo" en "Otra (nombre personalizado)"
   - Copia la contraseña generada (16 caracteres)

2. **Configurar variables de entorno:**

```bash
# En desarrollo (local)
export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
export MAIL_USERNAME=tu-email@gmail.com
export MAIL_PASSWORD=tu-contraseña-de-aplicacion
export MAIL_FROM=tu-email@gmail.com
export FRONTEND_URL=http://localhost:5173
```

3. **En Render (producción):**
   - Ve a tu servicio en Render
   - Environment → Add Environment Variable
   - Agrega las siguientes variables:
     ```
     MAIL_HOST=smtp.gmail.com
     MAIL_PORT=587
     MAIL_USERNAME=tu-email@gmail.com
     MAIL_PASSWORD=tu-contraseña-de-aplicacion
     MAIL_FROM=tu-email@gmail.com
     FRONTEND_URL=https://tu-frontend.onrender.com
     ```

### Opción 2: SendGrid (Recomendado para producción)

1. **Crear cuenta en SendGrid:**
   - Regístrate en https://sendgrid.com/
   - Verifica tu email
   - Crea una API Key en Settings → API Keys

2. **Configurar variables de entorno:**

```bash
MAIL_HOST=smtp.sendgrid.net
MAIL_PORT=587
MAIL_USERNAME=apikey
MAIL_PASSWORD=tu-api-key-de-sendgrid
MAIL_FROM=noreply@tudominio.com
FRONTEND_URL=https://tu-frontend.onrender.com
```

### Opción 3: Mailgun

```bash
MAIL_HOST=smtp.mailgun.org
MAIL_PORT=587
MAIL_USERNAME=postmaster@tu-dominio.mailgun.org
MAIL_PASSWORD=tu-password-de-mailgun
MAIL_FROM=noreply@tudominio.com
FRONTEND_URL=https://tu-frontend.onrender.com
```

---

## 📋 Endpoints Implementados

### 1. Registro de Usuario
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "usuario123",
  "email": "usuario@example.com",
  "password": "password123",
  "firstName": "Juan",
  "lastName": "Pérez"
}
```

**Respuesta:**
```json
{
  "id": 1,
  "username": "usuario123",
  "email": "usuario@example.com",
  "firstName": "Juan",
  "lastName": "Pérez",
  "role": "USER"
}
```

**Email enviado:** Bienvenida con link de verificación

---

### 2. Verificar Email
```http
GET /api/auth/verify-email?token=abc123-def456-ghi789
```

**Respuesta:**
```json
{
  "message": "Email verificado exitosamente. Ya puedes iniciar sesión."
}
```

**Email enviado:** Confirmación de verificación

---

### 3. Reenviar Email de Verificación
```http
POST /api/auth/resend-verification
Content-Type: application/json

{
  "email": "usuario@example.com"
}
```

**Respuesta:**
```json
{
  "message": "Email de verificación enviado. Por favor revisa tu bandeja de entrada."
}
```

---

### 4. Solicitar Recuperación de Contraseña
```http
POST /api/auth/forgot-password
Content-Type: application/json

{
  "email": "usuario@example.com"
}
```

**Respuesta:**
```json
{
  "message": "Si el email existe, recibirás instrucciones para restablecer tu contraseña."
}
```

**Email enviado:** Link de recuperación (válido por 1 hora)

**Nota de seguridad:** El mensaje es genérico para no revelar si el email existe en el sistema.

---

### 5. Restablecer Contraseña
```http
POST /api/auth/reset-password
Content-Type: application/json

{
  "token": "abc123-def456-ghi789",
  "newPassword": "nuevaPassword123"
}
```

**Respuesta:**
```json
{
  "message": "Contraseña restablecida exitosamente. Ya puedes iniciar sesión con tu nueva contraseña."
}
```

**Email enviado:** Confirmación de cambio de contraseña

---

## 🔐 Seguridad Implementada

### Tokens de Verificación
- Generados con UUID (únicos y aleatorios)
- Almacenados en la base de datos
- Válidos hasta que se usen (verificación de email)

### Tokens de Recuperación
- Generados con UUID
- Expiran en 1 hora
- Solo se pueden usar una vez
- Se eliminan tokens anteriores al generar uno nuevo

### Protección contra Enumeración de Emails
- El endpoint `/forgot-password` siempre retorna el mismo mensaje
- No revela si el email existe o no en el sistema
- Previene ataques de descubrimiento de usuarios

### Validaciones
- Contraseña mínima: 8 caracteres
- Email debe ser válido
- Tokens validados antes de usarse

---

## 🎨 Implementación en Frontend

### 1. Página de Verificación de Email

```typescript
// pages/VerifyEmail.tsx
import { useEffect, useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

const VerifyEmail = () => {
    const [searchParams] = useSearchParams();
    const [status, setStatus] = useState<'loading' | 'success' | 'error'>('loading');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const token = searchParams.get('token');
        
        if (!token) {
            setStatus('error');
            setMessage('Token de verificación no encontrado');
            return;
        }

        verifyEmail(token);
    }, [searchParams]);

    const verifyEmail = async (token: string) => {
        try {
            const response = await axios.get(
                `https://drogueria-bellavista-api.onrender.com/api/auth/verify-email?token=${token}`
            );
            
            setStatus('success');
            setMessage(response.data.message);
            
            // Redirigir al login después de 3 segundos
            setTimeout(() => navigate('/login'), 3000);
            
        } catch (error: any) {
            setStatus('error');
            setMessage(error.response?.data?.message || 'Error al verificar email');
        }
    };

    return (
        <div className="verify-email-container">
            {status === 'loading' && <p>Verificando email...</p>}
            
            {status === 'success' && (
                <div className="success">
                    <h2>✅ Email Verificado</h2>
                    <p>{message}</p>
                    <p>Redirigiendo al login...</p>
                </div>
            )}
            
            {status === 'error' && (
                <div className="error">
                    <h2>❌ Error</h2>
                    <p>{message}</p>
                    <button onClick={() => navigate('/login')}>
                        Ir al Login
                    </button>
                </div>
            )}
        </div>
    );
};

export default VerifyEmail;
```

---

### 2. Página de Recuperación de Contraseña

```typescript
// pages/ForgotPassword.tsx
import { useState } from 'react';
import axios from 'axios';

const ForgotPassword = () => {
    const [email, setEmail] = useState('');
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setMessage('');
        setError('');

        try {
            const response = await axios.post(
                'https://drogueria-bellavista-api.onrender.com/api/auth/forgot-password',
                { email }
            );
            
            setMessage(response.data.message);
            setEmail('');
            
        } catch (err: any) {
            setError(err.response?.data?.message || 'Error al enviar email');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="forgot-password-container">
            <h2>Recuperar Contraseña</h2>
            
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Email:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        placeholder="tu-email@example.com"
                    />
                </div>

                <button type="submit" disabled={loading}>
                    {loading ? 'Enviando...' : 'Enviar Instrucciones'}
                </button>
            </form>

            {message && <div className="success-message">{message}</div>}
            {error && <div className="error-message">{error}</div>}
        </div>
    );
};

export default ForgotPassword;
```

---

### 3. Página de Restablecer Contraseña

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
        setMessage('');

        if (newPassword !== confirmPassword) {
            setError('Las contraseñas no coinciden');
            return;
        }

        if (newPassword.length < 8) {
            setError('La contraseña debe tener al menos 8 caracteres');
            return;
        }

        if (!token) {
            setError('Token no válido');
            return;
        }

        setLoading(true);

        try {
            const response = await axios.post(
                'https://drogueria-bellavista-api.onrender.com/api/auth/reset-password',
                { token, newPassword }
            );
            
            setMessage(response.data.message);
            
            // Redirigir al login después de 3 segundos
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
                        placeholder="Mínimo 8 caracteres"
                    />
                </div>

                <div className="form-group">
                    <label>Confirmar Contraseña:</label>
                    <input
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                        placeholder="Repite la contraseña"
                    />
                </div>

                <button type="submit" disabled={loading}>
                    {loading ? 'Restableciendo...' : 'Restablecer Contraseña'}
                </button>
            </form>

            {message && (
                <div className="success-message">
                    {message}
                    <p>Redirigiendo al login...</p>
                </div>
            )}
            {error && <div className="error-message">{error}</div>}
        </div>
    );
};

export default ResetPassword;
```

---

### 4. Agregar Link en Página de Login

```typescript
// pages/Login.tsx
<form onSubmit={handleLogin}>
    {/* ... campos de login ... */}
    
    <div className="forgot-password-link">
        <Link to="/forgot-password">¿Olvidaste tu contraseña?</Link>
    </div>
    
    <button type="submit">Iniciar Sesión</button>
</form>
```

---

### 5. Rutas en App.tsx

```typescript
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import VerifyEmail from './pages/VerifyEmail';
import ForgotPassword from './pages/ForgotPassword';
import ResetPassword from './pages/ResetPassword';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/verify-email" element={<VerifyEmail />} />
                <Route path="/forgot-password" element={<ForgotPassword />} />
                <Route path="/reset-password" element={<ResetPassword />} />
                {/* ... otras rutas ... */}
            </Routes>
        </BrowserRouter>
    );
}
```

---

## 📧 Ejemplos de Emails Enviados

### Email de Bienvenida
```
Asunto: ¡Bienvenido a Droguería Bellavista!

Hola usuario123,

¡Bienvenido a Droguería Bellavista!

Tu cuenta ha sido creada exitosamente. Para activar tu cuenta y verificar tu email, 
por favor haz clic en el siguiente enlace:

http://localhost:5173/verify-email?token=abc123-def456-ghi789

Este enlace es válido por 24 horas.

Si no creaste esta cuenta, puedes ignorar este mensaje.

Saludos,
Equipo Droguería Bellavista
```

### Email de Recuperación
```
Asunto: Recuperación de Contraseña - Droguería Bellavista

Hola usuario123,

Recibimos una solicitud para restablecer la contraseña de tu cuenta.

Para crear una nueva contraseña, haz clic en el siguiente enlace:

http://localhost:5173/reset-password?token=xyz789-abc123-def456

Este enlace es válido por 1 hora.

Si no solicitaste restablecer tu contraseña, puedes ignorar este mensaje. 
Tu contraseña actual seguirá siendo válida.

Por seguridad, nunca compartas este enlace con nadie.

Saludos,
Equipo Droguería Bellavista
```

---

## 🧪 Testing

### Probar en Desarrollo (sin email real)

Si no quieres configurar un servidor de email en desarrollo, puedes:

1. **Ver los logs:** Los emails se intentarán enviar y verás los errores en los logs, pero la funcionalidad seguirá funcionando.

2. **Usar MailHog (recomendado):**
   ```bash
   # Instalar MailHog
   docker run -d -p 1025:1025 -p 8025:8025 mailhog/mailhog
   
   # Configurar variables
   export MAIL_HOST=localhost
   export MAIL_PORT=1025
   export MAIL_USERNAME=
   export MAIL_PASSWORD=
   ```
   
   Luego abre http://localhost:8025 para ver los emails capturados.

---

## 🚀 Despliegue en Render

1. **Configurar variables de entorno en Render:**
   - Ve a tu servicio → Environment
   - Agrega todas las variables de email
   - Redeploy el servicio

2. **Verificar que funciona:**
   ```bash
   # Registrar usuario
   curl -X POST https://drogueria-bellavista-api.onrender.com/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{
       "username": "test",
       "email": "tu-email@gmail.com",
       "password": "password123",
       "firstName": "Test",
       "lastName": "User"
     }'
   
   # Deberías recibir un email de bienvenida
   ```

---

## ⚠️ Troubleshooting

### Error: "Authentication failed"
- Verifica que MAIL_USERNAME y MAIL_PASSWORD sean correctos
- Si usas Gmail, asegúrate de usar una contraseña de aplicación, no tu contraseña normal

### Error: "Connection timeout"
- Verifica que MAIL_HOST y MAIL_PORT sean correctos
- Verifica que tu firewall permita conexiones SMTP

### No recibo emails
- Revisa la carpeta de spam
- Verifica los logs del backend para ver errores
- Verifica que MAIL_FROM sea un email válido

### Emails se envían pero no llegan
- Algunos proveedores requieren verificar el dominio del remitente
- Usa un servicio profesional como SendGrid para producción

---

**Última actualización:** Marzo 2026  
**Versión:** 1.0  
**Autor:** Equipo Droguería Bellavista
