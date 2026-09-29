# 🔧 Guía de Operaciones - Droguería Bellavista

## Monitoreo y Mantenimiento

### Endpoints de Monitoreo

#### Health Check
```bash
GET /api/actuator/health
```
**Respuesta esperada:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 1073741824,
        "free": 536870912,
        "threshold": 10485760,
        "exists": true
      }
    }
  }
}
```

#### Información del Sistema
```bash
GET /api/actuator/info
```

#### Métricas
```bash
GET /api/actuator/metrics
GET /api/actuator/metrics/jvm.memory.used
GET /api/actuator/metrics/http.server.requests
```

### Monitoreo con UptimeRobot

1. **Crear cuenta gratuita** en [uptimerobot.com](https://uptimerobot.com)
2. **Agregar monitor:**
   - URL: `https://drogueria-bellavista-api.onrender.com/api/actuator/health`
   - Tipo: HTTP(s)
   - Intervalo: 5 minutos
   - Método: GET
3. **Configurar alertas** por email
4. **Monitorear por 7 días** para obtener estadísticas de uptime

### Logs en Render

1. **Acceder al dashboard** de Render
2. **Ir a la aplicación** Droguería Bellavista
3. **Ver logs en tiempo real** en la pestaña "Logs"
4. **Buscar errores** con términos como:
   - `ERROR`
   - `Exception`
   - `Failed`
   - `Timeout`

## Troubleshooting

### Problemas Comunes

#### 1. Error de Conexión a Base de Datos
**Síntomas:**
- Health check retorna `DOWN` para componente `db`
- Errores: `Connection timeout` o `Connection refused`

**Solución:**
1. Verificar credenciales en variables de entorno
2. Comprobar conectividad a PostgreSQL
3. Revisar logs de Render para errores específicos

#### 2. Error de Autenticación JWT
**Síntomas:**
- `401 Unauthorized` en endpoints protegidos
- Mensaje: "Invalid JWT token"

**Solución:**
1. Verificar que el token no haya expirado (24 horas)
2. Comprobar formato: `Authorization: Bearer <token>`
3. Validar clave secreta JWT en configuración

#### 3. Error de Stock Insuficiente
**Síntomas:**
- `400 Bad Request` al crear órdenes
- Mensaje: "Stock insuficiente para el producto..."

**Solución:**
1. Verificar stock actual del producto
2. Revisar órdenes pendientes que puedan afectar el stock
3. Considerar reabastecimiento si stock < min_stock

#### 4. Error de Email
**Síntomas:**
- Emails de bienvenida o recuperación no llegan
- Logs muestran errores de SMTP

**Solución:**
1. Verificar configuración SMTP (Gmail requiere app password)
2. Comprobar variables de entorno: `MAIL_*`
3. Revisar bandeja de spam

### Comandos Útiles para Debugging

#### Ver estado de la aplicación
```bash
curl https://drogueria-bellavista-api.onrender.com/api/actuator/health
```

#### Ver logs de la aplicación
```bash
# En Render dashboard → Logs
# O usando Render CLI si está instalado
render logs --app drogueria-bellavista-api
```

#### Probar conectividad a BD
```bash
# Desde una terminal con acceso a BD
psql -h [host] -U [user] -d [database] -c "SELECT 1;"
```

#### Ver procesos activos
```bash
# En servidor Linux (no aplica a Render)
ps aux | grep java
```

## Mantenimiento

### Limpieza de Tokens Expirados

Los tokens de recuperación de contraseña expiran automáticamente después de 1 hora. Para limpieza manual:

```sql
-- Ver tokens expirados
SELECT * FROM password_reset_tokens
WHERE expiry_date < NOW() AND used = false;

-- Eliminar tokens expirados
DELETE FROM password_reset_tokens
WHERE expiry_date < NOW();
```

### Backup de Base de Datos

#### Backup Automático (Render)
Render realiza backups automáticos diarios de PostgreSQL. Para acceder:
1. Ir al dashboard de Render
2. Seleccionar la base de datos
3. Ir a "Backups"
4. Descargar el backup más reciente

#### Backup Manual
```bash
# Conectarse a la BD de Render
psql "postgresql://[user]:[password]@[host]:[port]/[database]"

# Crear backup
pg_dump postgresql://[user]:[password]@[host]:[port]/[database] > backup_$(date +%Y%m%d_%H%M%S).sql
```

### Actualización de Dependencias

#### Verificar vulnerabilidades
```bash
# Usar Dependabot (GitHub) o Snyk
# O manualmente revisar versiones en pom.xml
mvn dependency:tree
mvn dependency:analyze
```

#### Actualizar Spring Boot
```xml
<!-- En pom.xml -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.2</version>  <!-- ← Actualizar aquí -->
</parent>
```

### Monitoreo de Rendimiento

#### Queries Lentas
```sql
-- Ver queries activas (en PostgreSQL)
SELECT pid, query, state, age(clock_timestamp(), query_start) AS duration
FROM pg_stat_activity
WHERE state = 'active' AND age(clock_timestamp(), query_start) > interval '1 second'
ORDER BY duration DESC;
```

#### Uso de Memoria
```bash
# Ver métricas JVM
GET /api/actuator/metrics/jvm.memory.used
GET /api/actuator/metrics/jvm.memory.max
```

#### Conexiones a BD
```sql
-- Ver conexiones activas
SELECT count(*) as active_connections
FROM pg_stat_activity
WHERE state = 'active';
```

## Alertas y Notificaciones

### Configurar Alertas

#### 1. UptimeRobot
- **URL down:** Notificación inmediata por email
- **Response time > 5s:** Alerta de rendimiento
- **SSL certificate expiry:** 30 días antes

#### 2. Render Alerts
- **Deploy failed:** Notificación por email
- **Service crashed:** Notificación inmediata
- **High CPU/Memory:** Alertas de recursos

#### 3. Email Alerts Personalizados
Configurar en `application.yml`:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  health:
    mail:
      enabled: false  # Deshabilitar health check de email
  metrics:
    export:
      simple:
        enabled: true
```

### Dashboard de Monitoreo

#### Crear Dashboard Simple
```bash
# Script para monitoreo básico
#!/bin/bash

echo "=== Droguería Bellavista - Status Check ==="
echo "Timestamp: $(date)"

# Health check
HEALTH=$(curl -s https://drogueria-bellavista-api.onrender.com/api/actuator/health | jq -r '.status')
echo "API Health: $HEALTH"

# Database connections
DB_STATUS=$(curl -s https://drogueria-bellavista-api.onrender.com/api/actuator/health | jq -r '.components.db.status')
echo "Database: $DB_STATUS"

# Recent orders
echo "Recent Orders:"
curl -s -H "Authorization: Bearer YOUR_TOKEN" \
  https://drogueria-bellavista-api.onrender.com/api/orders | jq '. | length'

echo "=== End Check ==="
```

## Recuperación de Desastres

### Escenario: Base de Datos Corrupta

1. **Detener la aplicación** en Render
2. **Restaurar backup** desde el dashboard de Render
3. **Verificar integridad** de datos
4. **Reiniciar aplicación**
5. **Probar funcionalidades** críticas

### Escenario: Credenciales Comprometidas

1. **Cambiar todas las contraseñas** de usuarios
2. **Regenerar JWT secret** en variables de entorno
3. **Invalidar tokens existentes** (requiere logout de todos)
4. **Revisar logs** por accesos sospechosos

### Escenario: Ataque de Fuerza Bruta

1. **Implementar rate limiting** (si no está)
2. **Bloquear IPs sospechosas** (en firewall)
3. **Monitorear logs** por patrones de ataque
4. **Considerar CAPTCHA** para login

## Contactos de Emergencia

- **Equipo de Desarrollo:** [emails de desarrolladores]
- **Soporte de Render:** support@render.com
- **Soporte PostgreSQL:** https://www.postgresql.org/support/

## Checklist de Mantenimiento Semanal

- [ ] Verificar uptime (debe ser > 99.9%)
- [ ] Revisar logs por errores
- [ ] Verificar espacio en disco
- [ ] Comprobar stock de productos críticos
- [ ] Revisar órdenes pendientes antiguas
- [ ] Limpiar tokens expirados
- [ ] Verificar backups automáticos

## Checklist de Mantenimiento Mensual

- [ ] Actualizar dependencias de seguridad
- [ ] Revisar configuración de seguridad
- [ ] Analizar rendimiento de queries
- [ ] Verificar integridad de datos
- [ ] Probar procedimientos de backup/restore
- [ ] Revisar alertas y notificaciones

---

**Última actualización:** Marzo 2026
**Versión:** 1.0
**Responsable:** Equipo de Operaciones</content>
<parameter name="filePath">C:\Users\DANIEL-PC\Documents\software 3\softwareDrogueria\docs\OPERATIONS.md
