# 🚀 Guía Rápida: Mejoras de Seguridad HC-02, HC-01, M-02

## ⚡ Quick Start en 5 Minutos

### Paso 1: Iniciar Redis (desarrollo local)
```bash
# Opción A: Docker (recomendado)
docker run -d -p 6379:6379 --name redis-drogueria redis:latest

# Opción B: Descargar y ejecutar localmente
# Descarga desde https://redis.io/download
redis-server
```

### Paso 2: Compilar el proyecto
```bash
cd "c:\Users\DANIEL-PC\Documents\software 3\softwareDrogueria"
mvn clean compile
# Resultado esperado: BUILD SUCCESS
```

### Paso 3: Ejecutar tests
```bash
mvn test
# Prueba las aserciones de timestamps (M-02)
```

### Paso 4: Iniciar servidor
```bash
mvn spring-boot:run
# O iniciar desde IDE: Run → AuthServiceApplication
```

---

## 🧪 Probar las Nuevas Funcionalidades

### Test 1: Token Blacklist (HC-02)

**Opción A: Usando Postman**

1. Abrir `Postman_Collection.json` en Postman
2. Crear nueva request:
   - **URL**: `http://localhost:8080/api/auth/login`
   - **Método**: POST
   - **Body**:
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
   - **Ejecutar** y copiar el `token` de la respuesta

3. Crear segunda request:
   - **URL**: `http://localhost:8080/api/auth/logout`
   - **Método**: POST
   - **Header**: `Authorization: Bearer <token_copiado>`
   - **Body**: ninguno (o `{}`)
   - **Ejecutar** → Debería retornar mensaje de éxito

4. Crear tercera request:
   - **URL**: `http://localhost:8080/api/products`
   - **Método**: GET
   - **Header**: `Authorization: Bearer <mismo_token>`
   - **Ejecutar** → Debería retornar 403 Unauthorized (token en blacklist)

**Opción B: Usando archivos .http en VS Code**

1. Instalar extensión "REST Client"
2. Abre `http/auth.http`
3. Ejecuta "Login usuario correcto" y copia el token
4. Abre `http/auth-logout-HC02.http`
5. Reemplaza `REEMPLAZAR_CON_TOKEN_REAL` con el token
6. Haz click en "Send Request"
7. Verifica respuesta exitosa

### Test 2: Cabeceras de Seguridad (HC-01)

```bash
# Verificar cabeceras enviadas por servidor
curl -i http://localhost:8080/api/products

# Debería ver en la respuesta:
# X-Frame-Options: SAMEORIGIN
# Content-Security-Policy: default-src 'self'; script-src 'self' 'unsafe-inline'...
# Strict-Transport-Security: max-age=31536000; includeSubDomains
# Referrer-Policy: strict-origin-when-cross-origin
```

### Test 3: Validación de Timestamps (M-02)

```bash
# Ejecutar tests de integración
mvn test -Dtest=ProductIntegrationTest

# Debería pasar las nuevas aserciones:
# - shouldCreateProduct: valida createdAt y updatedAt no null
# - shouldUpdateProduct: valida updatedAt actualizado
```

---

## 📋 Checklist de Verificación

- [ ] Redis está corriendo en puerto 6379
- [ ] Project compila sin errores: `mvn clean compile`
- [ ] Tests pasan: `mvn test`
- [ ] Servidor inicia sin errores: `mvn spring-boot:run`
- [ ] Login funciona y retorna token
- [ ] Logout invalida token correctamente
- [ ] Nuevo login después de logout genera token valido
- [ ] Token invalidado retorna 403 en peticiones posteriores
- [ ] Cabeceras de seguridad presentes en respuestas
- [ ] Tests de timestamps pasan

---

## 🔍 Troubleshooting

### ❌ "Connection refused" en Redis
```
Error: redis://localhost:6379
Solución: Asegúrate que Redis está corriendo
$ docker ps  # Verifica si el contenedor está activo
$ redis-cli ping  # Debería retornar PONG
```

### ❌ Error en compilación: "cannot find symbol - method includeSubDomains"
```
Solución: Ya fue corregido. Recompila con:
$ mvn clean compile
```

### ❌ Endpoint `/auth/logout` retorna 404
```
Solución: Reinicia el servidor después de cambios:
$ mvn spring-boot:run
```

### ❌ Test ProductIntegrationTest falla
```
Solución: Ejecuta con limpieza:
$ mvn clean test -Dtest=ProductIntegrationTest
```

---

## 📊 Cambios Implementados Resumen

| Característica | Archivo | Línea | Estado |
|---|---|---|---|
| Token Blacklist | AuthService.java | +45 | ✅ |
| Redis Config | RedisConfig.java | NEW | ✅ |
| Logout Endpoint | AuthController.java | +40 | ✅ |
| Blacklist Check | JwtAuthenticationFilter.java | +1 | ✅ |
| Security Headers | SecurityConfig.java | +40 | ✅ |
| Timestamp Tests | ProductIntegrationTest.java | +10 | ✅ |
| Dependencies | pom.xml | +2 | ✅ |

---

## 🛡️ Seguridad Mejorada

**Antes:**
- ❌ Tokens no podían ser revocados
- ❌ Sin cabeceras de seguridad
- ❌ Tests sin validación de timestamps

**Después:**
- ✅ Tokens pueden invalidarse con `/auth/logout`
- ✅ Cabeceras CSP, HSTS, X-Frame-Options activas
- ✅ Tests validan createdAt y updatedAt
- ✅ Redis almacena blacklist con TTL automático
- ✅ Implementación OWASP compliant

---

## 📞 Soporte

Para más detalles, consulta:
- `SECURITY_IMPROVEMENTS.md` - Documentación técnica completa
- `http/auth-logout-HC02.http` - Ejemplos de prueba
- `src/main/java/com/drogueria/bellavista/` - Código fuente comentado

---

**Implementación completada:** ✅ 8 de Abril, 2026  
**Estado de compilación:** BUILD SUCCESS
