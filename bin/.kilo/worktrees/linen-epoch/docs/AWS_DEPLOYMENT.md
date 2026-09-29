# Despliegue en AWS EC2 — Droguería Bellavista

## Arquitectura

```
Internet
    │
    ▼
┌─────────────────────────────────────┐
│         AWS EC2 t2.micro            │
│         Ubuntu 24.04 LTS            │
│                                     │
│  ┌──────────────────────────────┐   │
│  │      Docker Engine           │   │
│  │                              │   │
│  │  ┌────────┐  drogueria_net   │   │
│  │  │  app   │◄───────────────► │   │
│  │  │ :8080  │   ┌──────────┐  │   │
│  │  └────────┘   │ postgres │  │   │
│  │               │  :5432   │  │   │
│  │               └──────────┘  │   │
│  │               ┌──────────┐  │   │
│  │               │  redis   │  │   │
│  │               │  :6379   │  │   │
│  │               └──────────┘  │   │
│  └──────────────────────────────┘   │
└─────────────────────────────────────┘
         Puerto 8080 expuesto
```

**IP pública:** `3.83.244.10`  
**URL del backend:** `http://3.83.244.10:8080/api`

---

## Por qué se migró de Render a AWS

| Motivo | Render (antes) | AWS EC2 (ahora) |
|--------|----------------|-----------------|
| Disponibilidad | App duerme tras 15 min de inactividad en plan gratuito | Siempre activa |
| Base de datos | PostgreSQL gestionado externo (plan gratuito expira) | PostgreSQL en el mismo servidor, sin costo adicional |
| Control | Limitado por la plataforma | Control total del servidor |
| Costo | Gratis con limitaciones severas | ~$0 con créditos AWS Academy |
| Redis | No soportado en plan gratuito | Contenedor local sin costo |

---

## Componentes del docker-compose

### `postgres` — Base de datos
- **Imagen:** `postgres:15-alpine`
- **Contenedor:** `drogueria_db`
- **Límite de RAM:** 256 MB
- **Persistencia:** volumen Docker `postgres_data` en `/var/lib/postgresql/data`
- **Healthcheck:** `pg_isready` antes de arrancar la app
- **Acceso:** solo interno (no expone puerto al host)

### `redis` — Blacklist de tokens JWT
- **Imagen:** `redis:7-alpine`
- **Contenedor:** `drogueria_redis`
- **Límite de RAM:** 64 MB
- **Uso:** revocación de tokens JWT en logout (HC-02)
- **Acceso:** solo interno

### `app` — Spring Boot backend
- **Build:** multi-stage Maven + JRE 21 Alpine
- **Contenedor:** `drogueria_app`
- **Límite de RAM:** 512 MB (JVM usa máx. ~384 MB con `MaxRAMPercentage=75`)
- **Puerto expuesto:** `8080`
- **Espera:** arranca solo después de que `postgres` pase el healthcheck y `redis` esté iniciado

---

## Red Docker (`drogueria_net`)

Todos los servicios se comunican a través de la red bridge interna `drogueria_net`. Los nombres de contenedor funcionan como hostnames:

| Desde `app`, conectarse a | Hostname | Puerto |
|---------------------------|----------|--------|
| PostgreSQL | `postgres` | `5432` |
| Redis | `redis` | `6379` |

PostgreSQL y Redis **no exponen puertos al host**, reduciendo la superficie de ataque.

---

## Variables de entorno (`.env`)

El archivo `.env` debe existir en la raíz del proyecto en el servidor. **Nunca se sube al repositorio** (está en `.gitignore`).

| Variable | Descripción | Obligatoria |
|----------|-------------|-------------|
| `DB_USER` | Usuario de PostgreSQL | Sí |
| `DB_PASSWORD` | Contraseña de PostgreSQL | Sí |
| `APP_JWT_SECRET` | Clave JWT (mín. 32 caracteres) | Sí |
| `MAIL_USERNAME` | Usuario SMTP (Gmail) | No |
| `MAIL_PASSWORD` | Contraseña de aplicación Gmail | No |
| `MAIL_FROM` | Email remitente | No |
| `FRONTEND_URL` | URL del frontend en Netlify | No |

Generar `APP_JWT_SECRET`:
```bash
openssl rand -hex 32
```

---

## Guía de despliegue paso a paso

### 1. Lanzar la instancia EC2

1. En la consola AWS → EC2 → **Launch Instance**
2. Configuración:
   - **AMI:** Ubuntu Server 24.04 LTS
   - **Tipo:** `t2.micro` (capa gratuita)
   - **Storage:** 20 GB gp3
   - **Security Group:** abrir puertos `22` (SSH) y `8080` (HTTP API)
3. Crear o seleccionar un key pair `.pem`

### 2. Conectarse por SSH

```bash
chmod 400 tu-clave.pem
ssh -i tu-clave.pem ubuntu@3.83.244.10
```

### 3. Subir y ejecutar el script de despliegue

Desde tu máquina local:
```bash
scp -i tu-clave.pem scripts/deploy-aws.sh ubuntu@3.83.244.10:~
```

En la instancia:
```bash
bash deploy-aws.sh
```

El script realiza automáticamente:
- Configura 1 GB de swap (necesario para el build Maven en t2.micro)
- Instala Docker CE y Docker Compose plugin
- Clona el repositorio desde GitHub
- Crea `application-prod.yml` (está en `.gitignore`)
- Solicita interactivamente los valores del `.env`
- Ejecuta `docker compose up -d --build`

### 4. Verificar el despliegue

```bash
# Health check
curl http://3.83.244.10:8080/api/actuator/health

# Respuesta esperada
{"status":"UP"}
```

---

## Guía para el frontend — Actualizar URL en Netlify

El frontend debe apuntar a la nueva URL del backend en AWS en lugar de la anterior URL de Render.

### Pasos en Netlify

1. Ir a [netlify.com](https://netlify.com) → tu sitio → **Site configuration** → **Environment variables**
2. Buscar la variable que apunta al backend (normalmente `VITE_API_URL`, `REACT_APP_API_URL` o similar)
3. Cambiar el valor:

| Antes (Render) | Después (AWS EC2) |
|----------------|-------------------|
| `https://drogueria-bellavista-api.onrender.com/api` | `http://3.83.244.10:8080/api` |

4. Guardar y hacer **Trigger deploy** → **Deploy site** para que el cambio tome efecto

### Verificar desde el frontend

Abrir las DevTools del navegador → pestaña **Network** y confirmar que las peticiones van a `3.83.244.10:8080`.

---

## Comandos de mantenimiento

### Ver logs

```bash
# Logs en tiempo real de la app
docker compose -f ~/softwareDrogueria/docker-compose.yml logs -f app

# Últimas 100 líneas de todos los servicios
docker compose -f ~/softwareDrogueria/docker-compose.yml logs --tail=100

# Solo errores
docker compose -f ~/softwareDrogueria/docker-compose.yml logs app | grep -i error
```

### Reiniciar contenedores

```bash
cd ~/softwareDrogueria

# Reiniciar solo la app (sin reconstruir)
docker compose restart app

# Reconstruir y reiniciar la app (tras un git pull)
docker compose up -d --build app

# Reiniciar todo
docker compose restart
```

### Health check

```bash
# Estado de los contenedores
docker compose -f ~/softwareDrogueria/docker-compose.yml ps

# Health check de la API
curl http://localhost:8080/api/actuator/health

# Uso de recursos
docker stats --no-stream
```

### Actualizar a una nueva versión

```bash
cd ~/softwareDrogueria
git pull
docker compose up -d --build app
```

### Detener y limpiar

```bash
cd ~/softwareDrogueria

# Detener sin borrar datos
docker compose down

# Detener y borrar volúmenes (¡borra la base de datos!)
docker compose down -v
```

### Acceder a PostgreSQL

```bash
docker exec -it drogueria_db psql -U $DB_USER -d drogueria_bellavista
```
