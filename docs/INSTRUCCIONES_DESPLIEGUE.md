# 🚀 Instrucciones de Despliegue a Producción

## Objetivo
Desplegar los cambios realizados para las métricas de calidad al servidor de producción en Render.

---

## ✅ Cambios Realizados en el Código

### Archivos Modificados:

1. **src/main/java/com/drogueria/bellavista/domain/model/Order.java**
   - ✅ Agregado campo `createdBy` para auditoría completa

2. **src/main/java/com/drogueria/bellavista/domain/service/OrderService.java**
   - ✅ Mejorados mensajes de error con información detallada
   - ✅ Agregada documentación de métricas en JavaDoc

### Archivos Nuevos Creados:

1. **docs/Queries_SQL_Evidencias.sql** - Queries para pgAdmin
2. **docs/Guia_Postman_Metricas.md** - Guía de pruebas en Postman
3. **docs/Configuracion_Uptime_Monitoring.md** - Configuración de monitoreo
4. **docs/Configuracion_SonarCloud.md** - Configuración de análisis de código
5. **docs/RESUMEN_METRICAS_IMPLEMENTACION.md** - Resumen general
6. **docs/INSTRUCCIONES_DESPLIEGUE.md** - Este archivo
7. **Postman_Collection_Metricas.json** - Collection de Postman

---

## 📋 Pasos para Desplegar

### Opción A: Despliegue Automático (Recomendado)

Si tu repositorio está conectado a Render con auto-deploy:

#### Paso 1: Commit y Push

```bash
# Verificar cambios
git status

# Agregar todos los archivos
git add .

# Commit con mensaje descriptivo
git commit -m "feat: implementar métricas de calidad - auditoría, mensajes de error mejorados y documentación"

# Push a la rama main
git push origin main
```

#### Paso 2: Verificar Despliegue en Render

1. Ir a [https://dashboard.render.com](https://dashboard.render.com)
2. Seleccionar el servicio "drogueria-bellavista-api"
3. Ir a la pestaña "Events"
4. Esperar a que aparezca "Deploy live" (puede tardar 3-5 minutos)

#### Paso 3: Verificar que el Servicio Está Funcionando

```bash
# Probar el health check
curl https://drogueria-bellavista-api.onrender.com/api/actuator/health

# Debe responder:
# {"status":"UP"}
```

---

### Opción B: Despliegue Manual

Si el auto-deploy no está configurado:

#### Paso 1: Hacer Commit Local

```bash
git add .
git commit -m "feat: implementar métricas de calidad"
git push origin main
```

#### Paso 2: Trigger Manual en Render

1. Ir a [https://dashboard.render.com](https://dashboard.render.com)
2. Seleccionar el servicio "drogueria-bellavista-api"
3. Click en "Manual Deploy" → "Deploy latest commit"
4. Esperar a que termine el despliegue

---

## 🔍 Verificación Post-Despliegue

### 1. Verificar Health Check

```bash
curl https://drogueria-bellavista-api.onrender.com/api/actuator/health
```

**Resultado esperado:**
```json
{
  "status": "UP"
}
```

### 2. Verificar Login

```bash
curl -X POST https://drogueria-bellavista-api.onrender.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"tu_password"}'
```

**Resultado esperado:** Token JWT

### 3. Verificar Mensajes de Error Mejorados

```bash
# Guardar el token de la respuesta anterior
TOKEN="tu_token_aqui"

# Intentar crear pedido con stock insuficiente
curl -X POST https://drogueria-bellavista-api.onrender.com/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "customerId": 1,
    "items": [
      {
        "productId": 1,
        "quantity": 99999
      }
    ]
  }'
```

**Resultado esperado:**
```json
{
  "message": "Stock insuficiente para el producto 'Nombre Producto' (código: XXX). Disponible: Y, solicitado: 99999",
  "status": 400
}
```

---

## 🗄️ Verificar Base de Datos

### Conectar a PostgreSQL de Render

1. En el dashboard de Render, ir a tu base de datos PostgreSQL
2. Copiar la "External Database URL"
3. Abrir pgAdmin y crear nueva conexión:
   - **Host:** (extraer de la URL)
   - **Port:** 5432
   - **Database:** (extraer de la URL)
   - **Username:** (extraer de la URL)
   - **Password:** (extraer de la URL)

### Verificar Campo `createdBy` en Orders

```sql
-- Ver estructura de la tabla orders
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_name = 'orders';

-- Debe aparecer la columna 'created_by'
```

**Nota:** Si la columna `createdBy` no existe, necesitas ejecutar una migración:

```sql
-- Agregar columna created_by si no existe
ALTER TABLE orders 
ADD COLUMN IF NOT EXISTS created_by VARCHAR(255);
```

---

## 🔧 Solución de Problemas

### Problema 1: El Despliegue Falla

**Síntomas:** Error en Render durante el build

**Solución:**
1. Revisar los logs en Render (pestaña "Logs")
2. Verificar que el `pom.xml` no tenga errores
3. Ejecutar localmente: `mvn clean package`
4. Si funciona local, hacer push de nuevo

### Problema 2: El Servicio No Responde

**Síntomas:** Timeout al hacer requests

**Solución:**
1. Esperar 30-60 segundos (plan gratuito de Render se apaga tras inactividad)
2. Hacer un request al health check para "despertar" el servicio
3. Verificar en Render que el servicio está "Running"

### Problema 3: Errores 500 en Producción

**Síntomas:** Requests que funcionaban ahora dan error 500

**Solución:**
1. Revisar logs en Render (pestaña "Logs")
2. Verificar que las variables de entorno estén configuradas:
   - `APP_JWT_SECRET`
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
3. Verificar que la BD esté accesible

### Problema 4: Campo `createdBy` Causa Errores

**Síntomas:** Error al crear órdenes después del despliegue

**Solución:**
1. Conectar a la BD y ejecutar:
```sql
ALTER TABLE orders 
ADD COLUMN IF NOT EXISTS created_by VARCHAR(255);
```

2. O modificar temporalmente el código para que `createdBy` sea opcional:
```java
// En Order.java, agregar @Column(nullable = true)
@Column(name = "created_by", nullable = true)
private String createdBy;
```

---

## 📊 Siguiente Paso: Recolectar Evidencias

Una vez que el despliegue esté completo y verificado:

1. **Configurar UptimeRobot** (necesita 7 días de monitoreo)
   - Ver: `docs/Configuracion_Uptime_Monitoring.md`

2. **Configurar SonarCloud** (análisis de código)
   - Ver: `docs/Configuracion_SonarCloud.md`

3. **Ejecutar Pruebas en Postman** (todas las métricas)
   - Importar: `Postman_Collection_Metricas.json`
   - Ver: `docs/Guia_Postman_Metricas.md`

4. **Ejecutar Queries SQL** (evidencias de BD)
   - Ver: `docs/Queries_SQL_Evidencias.sql`

---

## ✅ Checklist de Despliegue

- [ ] Código commiteado y pusheado a GitHub
- [ ] Despliegue completado en Render (sin errores)
- [ ] Health check responde correctamente
- [ ] Login funciona correctamente
- [ ] Mensajes de error mejorados funcionan
- [ ] Base de datos tiene columna `created_by`
- [ ] Todas las pruebas básicas pasan
- [ ] Logs de Render no muestran errores críticos

---

## 📞 Contacto de Soporte

Si tienes problemas con el despliegue:

1. Revisar los logs de Render
2. Verificar que el código compile localmente: `mvn clean package`
3. Consultar la documentación de Render: [https://render.com/docs](https://render.com/docs)

---

**¡Despliegue completado! Ahora puedes empezar a recolectar evidencias. 🎉**
