# Guía de Despliegue en Render

## Opción 1: Despliegue Manual (Recomendado primera vez)

### Paso 1: Crear Base de Datos PostgreSQL

1. Ir a [Render Dashboard](https://dashboard.render.com)
2. Click **New** → **PostgreSQL**
3. Configurar:
    - **Name**: `drogueria-db`
    - **Region**: Oregon (US West)
    - **Plan**: Free
4. Click **Create Database**
5. Esperar que se cree y copiar las credenciales

### Paso 2: Crear Web Service

1. Click **New** → **Web Service**
2. Conectar tu repositorio de GitHub
3. Configurar:
    - **Name**: `drogueria-bellavista-api`
    - **Region**: Oregon (US West)
    - **Runtime**: Docker
    - **Plan**: Free
4. En **Environment Variables**, agregar:

| Variable | Valor |
|----------|-------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://HOST:5432/DATABASE` (de tu BD) |
| `SPRING_DATASOURCE_USERNAME` | (de tu BD) |
| `SPRING_DATASOURCE_PASSWORD` | (de tu BD) |
| `APP_JWT_SECRET` | (generar uno seguro - ver abajo) |

5. Click **Create Web Service**

### Generar JWT Secret Seguro

**Linux/Mac:**
```bash
openssl rand -base64 32
```

**PowerShell:**
```powershell
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 }) -as [byte[]])
```

---

## Opción 2: Blueprint (Automático)

1. Ir a [Render Dashboard](https://dashboard.render.com)
2. Click **New** → **Blueprint**
3. Conectar repositorio
4. Render detectará `render.yaml` y creará todo automáticamente

---

## Verificar Despliegue

Una vez desplegado, probar:
```bash
# Health check
curl https://TU-APP.onrender.com/api/actuator/health

# Registrar usuario
curl -X POST https://TU-APP.onrender.com/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","email":"admin@test.com","password":"password123","firstName":"Admin","lastName":"User"}'
```

---

## Consideraciones Plan Gratuito

- **Spin-down**: La app se apaga tras 15 min de inactividad
- **Cold start**: Primera petición tarda ~30-60 segundos
- **Base de datos**: Se elimina tras 90 días de inactividad

### Mantener App Activa (Opcional)

Usar [UptimeRobot](https://uptimerobot.com):
1. Crear cuenta gratuita
2. Agregar monitor HTTP(s)
3. URL: `https://TU-APP.onrender.com/api/actuator/health`
4. Intervalo: 5 minutos

---

## Troubleshooting

| Problema | Solución |
|----------|----------|
| Build falla | Verificar que `pom.xml` tenga Java 21 |
| App no inicia | Revisar logs en Render Dashboard |
| Error conexión BD | Verificar `SPRING_DATASOURCE_URL` formato JDBC |
| 401 en endpoints | Verificar `APP_JWT_SECRET` está configurado |
| Health check falla | Verificar `/api/actuator/health` responde |