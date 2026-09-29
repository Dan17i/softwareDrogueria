# 🆕 CAMBIO 4: Sistema de Email - Bienvenida y Recuperación de Contraseña

## Descripción

Se implementó un sistema de email que envía correos automáticos en dos situaciones:

1. **Email de Bienvenida**: Cuando un usuario se registra
2. **Recuperación de Contraseña**: Cuando un usuario olvida su contraseña

**IMPORTANTE**: El email de bienvenida es solo informativo. NO requiere verificación. La cuenta está activa inmediatamente al registrarse.

---

## Nuevos Endpoints Disponibles

### 1. Solicitar Recuperación de Contraseña
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

**Nota de Seguridad**: El mensaje es genérico para no revelar si el email existe en el sistema.

---

### 2. Restablecer Contraseña
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

---

## 📧 Emails que se Envían

### 1. Email de Bienvenida (al registrarse)

```
Asunto: ¡Bienvenido a Droguería Bellavista!

Hola usuario123,

¡Bienvenido a Droguería Bellavista!

Tu cuenta ha sido creada exitosamente y ya puedes comenzar a usar el sistema.

Si tienes alguna pregunta o necesitas ayuda, no dudes en contactarnos.

Saludos,
Equipo Droguería Bellavista
```

**Características:**
- Se envía automáticamente al registrarse
- NO requiere acción del usuario
- La cuenta está activa inmediatamente

---

### 2. Email de Recuperación de Contraseña

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

**Características:**
- Token válido por 1 hora
- Token de un solo uso
- Link apunta a tu frontend

---

### 3. Email de Confirmación de Cambio

```
Asunto: Contraseña Actualizada - Droguería Bellavista

Hola usuario123,

Tu contraseña ha sido actualizada exitosamente.

Si no realizaste este cambio, por favor contacta inmediatamente con soporte.

Saludos,
Equipo Droguería Bellavista
```

---

## 🎨 Implementación en Frontend

### Páginas Necesarias

Necesitas crear **2 páginas nuevas**:

1. `/forgot-password` - Solicitar recuperación
2. `/reset-password` - Restablecer contraseña

---

### 1. Página: Solicitar Recuperación (`/forgot-password`)

```typescript
// pages/ForgotPassword.tsx
import { useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import './ForgotPassword.css';

const API_BASE_URL = 'https://drogueria-bellavista-api.onrender.com/api';

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
                `${API_BASE_URL}/auth/forgot-password`,
                { email }
            );
            
            setMessage(response.data.message);
            setEmail(''); // Limpiar formulario
            
        } catch (err: any) {
            // Mostrar mensaje genérico por seguridad
            setMessage('Si el email existe, recibirás instrucciones.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="forgot-password-page">
            <div className="forgot-password-container">
                <h2>Recuperar Contraseña</h2>
                <p className="subtitle">
                    Ingresa tu email y te enviaremos instrucciones para restablecer tu contraseña.
                </p>
                
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="email">Email:</label>
                        <input
                            id="email"
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                            placeholder="tu-email@example.com"
                            disabled={loading}
                        />
                    </div>

                    <button type="submit" disabled={loading} className="btn-primary">
                        {loading ? 'Enviando...' : 'Enviar Instrucciones'}
                    </button>
                </form>

                {message && (
                    <div className="success-message">
                        <p>{message}</p>
                        <p className="small">Por favor revisa tu bandeja de entrada y spam.</p>
                    </div>
                )}

                {error && (
                    <div className="error-message">
                        {error}
                    </div>
                )}

                <div className="back-to-login">
                    <Link to="/login">← Volver al Login</Link>
                </div>
            </div>
        </div>
    );
};

export default ForgotPassword;
```

---

### 2. Página: Restablecer Contraseña (`/reset-password`)

```typescript
// pages/ResetPassword.tsx
import { useState, useEffect } from 'react';
import { useSearchParams, useNavigate, Link } from 'react-router-dom';
import axios from 'axios';
import './ResetPassword.css';

const API_BASE_URL = 'https://drogueria-bellavista-api.onrender.com/api';

const ResetPassword = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const [showPassword, setShowPassword] = useState(false);

    const token = searchParams.get('token');

    useEffect(() => {
        if (!token) {
            setError('Token no válido. Por favor solicita un nuevo enlace de recuperación.');
        }
    }, [token]);

    const validatePassword = (password: string): string | null => {
        if (password.length < 8) {
            return 'La contraseña debe tener al menos 8 caracteres';
        }
        // Puedes agregar más validaciones aquí
        return null;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setMessage('');

        // Validaciones
        if (newPassword !== confirmPassword) {
            setError('Las contraseñas no coinciden');
            return;
        }

        const passwordError = validatePassword(newPassword);
        if (passwordError) {
            setError(passwordError);
            return;
        }

        if (!token) {
            setError('Token no válido');
            return;
        }

        setLoading(true);

        try {
            const response = await axios.post(
                `${API_BASE_URL}/auth/reset-password`,
                { token, newPassword }
            );
            
            setMessage(response.data.message);
            
            // Redirigir al login después de 3 segundos
            setTimeout(() => {
                navigate('/login');
            }, 3000);
            
        } catch (err: any) {
            const errorMsg = err.response?.data?.message || 'Error al restablecer contraseña';
            setError(errorMsg);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="reset-password-page">
            <div className="reset-password-container">
                <h2>Restablecer Contraseña</h2>
                
                {!token ? (
                    <div className="error-message">
                        <p>Token no válido o expirado.</p>
                        <Link to="/forgot-password" className="btn-link">
                            Solicitar nuevo enlace
                        </Link>
                    </div>
                ) : (
                    <>
                        <p className="subtitle">
                            Ingresa tu nueva contraseña (mínimo 8 caracteres)
                        </p>
                        
                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label htmlFor="newPassword">Nueva Contraseña:</label>
                                <div className="password-input-wrapper">
                                    <input
                                        id="newPassword"
                                        type={showPassword ? 'text' : 'password'}
                                        value={newPassword}
                                        onChange={(e) => setNewPassword(e.target.value)}
                                        required
                                        minLength={8}
                                        placeholder="Mínimo 8 caracteres"
                                        disabled={loading}
                                    />
                                    <button
                                        type="button"
                                        className="toggle-password"
                                        onClick={() => setShowPassword(!showPassword)}
                                    >
                                        {showPassword ? '👁️' : '👁️‍🗨️'}
                                    </button>
                                </div>
                            </div>

                            <div className="form-group">
                                <label htmlFor="confirmPassword">Confirmar Contraseña:</label>
                                <input
                                    id="confirmPassword"
                                    type={showPassword ? 'text' : 'password'}
                                    value={confirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                    required
                                    placeholder="Repite la contraseña"
                                    disabled={loading}
                                />
                            </div>

                            <button type="submit" disabled={loading} className="btn-primary">
                                {loading ? 'Restableciendo...' : 'Restablecer Contraseña'}
                            </button>
                        </form>

                        {message && (
                            <div className="success-message">
                                <p>✅ {message}</p>
                                <p className="small">Redirigiendo al login...</p>
                            </div>
                        )}

                        {error && (
                            <div className="error-message">
                                ❌ {error}
                            </div>
                        )}
                    </>
                )}

                <div className="back-to-login">
                    <Link to="/login">← Volver al Login</Link>
                </div>
            </div>
        </div>
    );
};

export default ResetPassword;
```

---

### 3. Modificar Página de Login

Agrega un link para recuperar contraseña:

```typescript
// pages/Login.tsx
import { Link } from 'react-router-dom';

const Login = () => {
    // ... tu código existente ...

    return (
        <div className="login-page">
            <form onSubmit={handleLogin}>
                <div className="form-group">
                    <label>Usuario:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Contraseña:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>

                {/* ✅ AGREGAR ESTE LINK */}
                <div className="forgot-password-link">
                    <Link to="/forgot-password">¿Olvidaste tu contraseña?</Link>
                </div>

                <button type="submit">Iniciar Sesión</button>
            </form>
        </div>
    );
};
```

---

### 4. Configurar Rutas en App.tsx

```typescript
// App.tsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import ForgotPassword from './pages/ForgotPassword';
import ResetPassword from './pages/ResetPassword';
// ... otros imports

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                
                {/* ✅ AGREGAR ESTAS RUTAS */}
                <Route path="/forgot-password" element={<ForgotPassword />} />
                <Route path="/reset-password" element={<ResetPassword />} />
                
                {/* ... otras rutas ... */}
            </Routes>
        </BrowserRouter>
    );
}

export default App;
```

---

## 🎨 Estilos CSS Sugeridos

### ForgotPassword.css

```css
.forgot-password-page {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-color: #f5f5f5;
}

.forgot-password-container {
    background: white;
    padding: 40px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    max-width: 450px;
    width: 100%;
}

.forgot-password-container h2 {
    margin-bottom: 10px;
    color: #333;
}

.subtitle {
    color: #666;
    margin-bottom: 30px;
    font-size: 14px;
}

.form-group {
    margin-bottom: 20px;
}

.form-group label {
    display: block;
    margin-bottom: 8px;
    color: #333;
    font-weight: 500;
}

.form-group input {
    width: 100%;
    padding: 12px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
}

.form-group input:focus {
    outline: none;
    border-color: #007bff;
}

.form-group input:disabled {
    background-color: #f5f5f5;
    cursor: not-allowed;
}

.btn-primary {
    width: 100%;
    padding: 12px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 16px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.btn-primary:hover:not(:disabled) {
    background-color: #0056b3;
}

.btn-primary:disabled {
    background-color: #ccc;
    cursor: not-allowed;
}

.success-message {
    margin-top: 20px;
    padding: 15px;
    background-color: #d4edda;
    border: 1px solid #c3e6cb;
    border-radius: 4px;
    color: #155724;
}

.success-message .small {
    font-size: 12px;
    margin-top: 5px;
    color: #666;
}

.error-message {
    margin-top: 20px;
    padding: 15px;
    background-color: #f8d7da;
    border: 1px solid #f5c6cb;
    border-radius: 4px;
    color: #721c24;
}

.back-to-login {
    margin-top: 20px;
    text-align: center;
}

.back-to-login a {
    color: #007bff;
    text-decoration: none;
}

.back-to-login a:hover {
    text-decoration: underline;
}
```

### ResetPassword.css

```css
.reset-password-page {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-color: #f5f5f5;
}

.reset-password-container {
    background: white;
    padding: 40px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    max-width: 450px;
    width: 100%;
}

.password-input-wrapper {
    position: relative;
}

.password-input-wrapper input {
    padding-right: 45px;
}

.toggle-password {
    position: absolute;
    right: 10px;
    top: 50%;
    transform: translateY(-50%);
    background: none;
    border: none;
    cursor: pointer;
    font-size: 20px;
    padding: 5px;
}

.toggle-password:hover {
    opacity: 0.7;
}

.btn-link {
    display: inline-block;
    margin-top: 15px;
    padding: 10px 20px;
    background-color: #007bff;
    color: white;
    text-decoration: none;
    border-radius: 4px;
}

.btn-link:hover {
    background-color: #0056b3;
}
```

### Modificar Login.css

```css
/* Agregar al final de Login.css */
.forgot-password-link {
    text-align: right;
    margin-bottom: 20px;
}

.forgot-password-link a {
    color: #007bff;
    text-decoration: none;
    font-size: 14px;
}

.forgot-password-link a:hover {
    text-decoration: underline;
}
```

---

## 🔧 Servicio de Autenticación (Opcional)

Si quieres centralizar las llamadas a la API:

```typescript
// services/authService.ts
import axios from 'axios';

const API_BASE_URL = 'https://drogueria-bellavista-api.onrender.com/api';

class AuthService {
    async forgotPassword(email: string): Promise<string> {
        try {
            const response = await axios.post(
                `${API_BASE_URL}/auth/forgot-password`,
                { email }
            );
            return response.data.message;
        } catch (error: any) {
            throw new Error(
                error.response?.data?.message || 
                'Error al solicitar recuperación de contraseña'
            );
        }
    }

    async resetPassword(token: string, newPassword: string): Promise<string> {
        try {
            const response = await axios.post(
                `${API_BASE_URL}/auth/reset-password`,
                { token, newPassword }
            );
            return response.data.message;
        } catch (error: any) {
            throw new Error(
                error.response?.data?.message || 
                'Error al restablecer contraseña'
            );
        }
    }
}

export default new AuthService();
```

---

## 📱 Ejemplo con Vue.js

### ForgotPassword.vue

```vue
<template>
    <div class="forgot-password-page">
        <div class="forgot-password-container">
            <h2>Recuperar Contraseña</h2>
            <p class="subtitle">
                Ingresa tu email y te enviaremos instrucciones.
            </p>
            
            <form @submit.prevent="handleSubmit">
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input
                        id="email"
                        v-model="email"
                        type="email"
                        required
                        placeholder="tu-email@example.com"
                        :disabled="loading"
                    />
                </div>

                <button type="submit" :disabled="loading" class="btn-primary">
                    {{ loading ? 'Enviando...' : 'Enviar Instrucciones' }}
                </button>
            </form>

            <div v-if="message" class="success-message">
                <p>{{ message }}</p>
                <p class="small">Por favor revisa tu bandeja de entrada.</p>
            </div>

            <div class="back-to-login">
                <router-link to="/login">← Volver al Login</router-link>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            email: '',
            loading: false,
            message: ''
        };
    },
    
    methods: {
        async handleSubmit() {
            this.loading = true;
            this.message = '';

            try {
                const response = await this.$http.post('/auth/forgot-password', {
                    email: this.email
                });
                
                this.message = response.data.message;
                this.email = '';
                
            } catch (error) {
                this.message = 'Si el email existe, recibirás instrucciones.';
            } finally {
                this.loading = false;
            }
        }
    }
};
</script>
```

---

## 🧪 Testing

### Probar Flujo Completo

1. **Registrar usuario:**
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
**Resultado:** Deberías recibir email de bienvenida

2. **Solicitar recuperación:**
```bash
curl -X POST http://localhost:8080/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{"email": "tu-email@gmail.com"}'
```
**Resultado:** Deberías recibir email con link de recuperación

3. **Usar el link del email:**
   - Abre el link en tu navegador
   - Ingresa nueva contraseña
   - Confirma el cambio

4. **Iniciar sesión con nueva contraseña**

---

## ⚠️ Consideraciones Importantes

### Seguridad

1. **No revelar si el email existe:**
   - El endpoint `/forgot-password` siempre retorna el mismo mensaje
   - Esto previene ataques de enumeración de usuarios

2. **Token temporal:**
   - Válido solo por 1 hora
   - Un solo uso (no reutilizable)
   - Se elimina después de usarse

3. **Validación de contraseña:**
   - Mínimo 8 caracteres
   - Puedes agregar más validaciones (mayúsculas, números, etc.)

### UX/UI

1. **Feedback claro:**
   - Mostrar mensajes de éxito/error
   - Loading states durante peticiones
   - Redirigir automáticamente después del éxito

2. **Accesibilidad:**
   - Labels en todos los inputs
   - Mensajes de error descriptivos
   - Botón para mostrar/ocultar contraseña

3. **Responsive:**
   - Diseño adaptable a móviles
   - Formularios fáciles de usar en pantallas pequeñas

---

## 📋 Checklist de Implementación

### Backend (Ya Completado ✅)
- [x] Endpoint `/auth/forgot-password`
- [x] Endpoint `/auth/reset-password`
- [x] Servicio de email configurado
- [x] Tokens de recuperación con expiración
- [x] Email de bienvenida automático

### Frontend (Por Hacer)
- [ ] Crear página `/forgot-password`
- [ ] Crear página `/reset-password`
- [ ] Agregar link en página de login
- [ ] Configurar rutas en App.tsx/router
- [ ] Agregar estilos CSS
- [ ] Probar flujo completo
- [ ] Agregar validaciones de contraseña
- [ ] Implementar loading states
- [ ] Agregar manejo de errores

---

## 🚀 Próximos Pasos

1. **Implementar las 2 páginas en tu frontend**
2. **Configurar el email en el backend** (ver `CONFIGURAR_GMAIL_RAPIDO.md`)
3. **Probar el flujo completo**
4. **Ajustar estilos según tu diseño**
5. **Agregar validaciones adicionales si es necesario**

---

**Documentación Relacionada:**
- `CONFIGURAR_GMAIL_RAPIDO.md` - Configuración de email
- `RESUMEN_SISTEMA_EMAIL_SIMPLIFICADO.md` - Resumen técnico
- `docs/CONFIGURACION_EMAIL.md` - Guía completa

---

**Última actualización:** Marzo 2026  
**Versión:** 1.0  
**Autor:** Equipo Droguería Bellavista
