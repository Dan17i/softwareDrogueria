# ✅ Checklist Visual - Métricas de Calidad

## Droguería Bellavista - Guía de Seguimiento

---

## 🎯 FASE 1: CONFIGURACIÓN INICIAL (Esta Semana)

### Día 1: Despliegue y Monitoreo

```
┌─────────────────────────────────────────────────────────────┐
│ ⏰ URGENTE - Configurar Monitoreo (necesita 7 días)         │
└─────────────────────────────────────────────────────────────┘

□ Revisar cambios en el código
  └─ Order.java: campo createdBy agregado
  └─ OrderService.java: mensajes de error mejorados

□ Desplegar a producción
  └─ git add .
  └─ git commit -m "feat: implementar métricas de calidad"
  └─ git push origin main
  └─ Verificar en Render que el deploy fue exitoso

□ Configurar UptimeRobot (15 minutos)
  └─ Crear cuenta en uptimerobot.com
  └─ Agregar monitor HTTP(s)
  └─ URL: https://drogueria-bellavista-api.onrender.com/api/actuator/health
  └─ Intervalo: 5 minutos
  └─ ✅ DEJAR CORRIENDO 7 DÍAS MÍNIMO

□ Verificar que el servicio funciona
  └─ curl https://drogueria-bellavista-api.onrender.com/api/actuator/health
  └─ Debe responder: {"status":"UP"}
```

**Tiempo estimado:** 1 hora  
**Prioridad:** 🔥 CRÍTICA

---

### Día 2-3: Análisis de Código

```
┌─────────────────────────────────────────────────────────────┐
│ 🔍 Configurar SonarCloud                                     │
└─────────────────────────────────────────────────────────────┘

□ Crear cuenta en SonarCloud
  └─ Ir a sonarcloud.io
  └─ Login con GitHub

□ Importar repositorio
  └─ Seleccionar Dan17i/softwareDrogueria
  └─ Configurar análisis automático

□ Ejecutar primer análisis
  └─ Esperar 5-10 minutos
  └─ Revisar dashboard

□ Tomar captura de pantalla
  └─ Vulnerabilities: 0
  └─ Lines of Code: ~5,000
  └─ Quality Gate: Passed
```

**Tiempo estimado:** 30 minutos  
**Prioridad:** ⭐ ALTA

---

### Día 4-5: Preparación de Herramientas

```
┌─────────────────────────────────────────────────────────────┐
│ 🛠️ Configurar Herramientas de Prueba                        │
└─────────────────────────────────────────────────────────────┘

□ Postman
  └─ Importar Postman_Collection_Metricas.json
  └─ Crear Environment "Drogueria Bellavista - Producción"
  └─ Variable base_url: https://drogueria-bellavista-api.onrender.com/api
  └─ Probar login y guardar token

□ pgAdmin
  └─ Instalar pgAdmin (si no lo tienes)
  └─ Conectar a BD de Render
  └─ Copiar External Database URL de Render
  └─ Crear nueva conexión en pgAdmin
  └─ Probar conexión con query simple: SELECT 1;

□ Excel / Google Sheets
  └─ Crear archivo "Evidencias_Metricas.xlsx"
  └─ Crear hojas: Login, Pedidos, Inventario, Ventas
```

**Tiempo estimado:** 1 hora  
**Prioridad:** ⭐ ALTA

---

## 🧪 FASE 2: RECOLECCIÓN DE EVIDENCIAS (Semana 2)

### PROCESO 1: LOGIN (4 métricas)

```
┌─────────────────────────────────────────────────────────────┐
│ 🔐 Métrica 1.1: Tasa de Fallo del Sistema                   │
│ Meta: < 0.5% (uptime > 99.5%)                               │
└─────────────────────────────────────────────────────────────┘

□ Revisar UptimeRobot (después de 7 días)
  └─ Ir al dashboard
  └─ Seleccionar "Last 7 Days"
  └─ Anotar: Uptime %, Total Checks, Down Events

□ Tomar captura de pantalla
  └─ Mostrar uptime > 99.5%

□ Calcular tasa de fallo
  └─ (Tiempo caída / Tiempo total) × 100
  └─ Debe ser < 0.5%

□ Agregar a Excel
  └─ Hoja "Login" → Métrica 1.1
```

---

```
┌─────────────────────────────────────────────────────────────┐
│ 🔒 Métrica 1.2: Densidad de Vulnerabilidades                │
│ Meta: < 1 por KLOC                                          │
└─────────────────────────────────────────────────────────────┘

□ Revisar SonarCloud
  └─ Ir al dashboard del proyecto
  └─ Anotar: Vulnerabilities, Lines of Code

□ Calcular densidad
  └─ Vulnerabilidades / (Lines of Code / 1000)
  └─ Ejemplo: 0 / 5.234 = 0 por KLOC ✅

□ Tomar captura de pantalla
  └─ Mostrar dashboard completo

□ Agregar a Excel
  └─ Hoja "Login" → Métrica 1.2
```

---

```
┌─────────────────────────────────────────────────────────────┐
│ ✅ Métrica 1.3: Tasa de Éxito de Login                      │
│ Meta: > 98%                                                  │
└─────────────────────────────────────────────────────────────┘

□ Abrir Postman
  └─ Carpeta "PROCESO 1: LOGIN"

□ Ejecutar "Login Exitoso" 10 veces
  └─ Anotar tiempo de cada una
  └─ Verificar que todas dan 200 OK

□ Calcular tasa de éxito
  └─ (Exitosos / Total) × 100
  └─ Ejemplo: 10/10 = 100% ✅

□ Tomar capturas de pantalla
  └─ Mostrar las 10 respuestas

□ Agregar a Excel
  └─ Hoja "Login" → Métrica 1.3
  └─ Tabla con tiempos y resultados
```

---

```
┌─────────────────────────────────────────────────────────────┐
│ 🔐 Métrica 1.4: Datos Cifrados                              │
│ Meta: 100% de passwords cifrados                            │
└─────────────────────────────────────────────────────────────┘

□ Abrir pgAdmin
  └─ Conectar a BD de producción

□ Ejecutar queries (Queries_SQL_Evidencias.sql)
  └─ Total de usuarios
  └─ Usuarios con password cifrado (BCrypt)
  └─ Usuarios sin cifrar (debe ser 0)

□ Calcular porcentaje
  └─ (Cifrados / Total) × 100
  └─ Debe ser 100%

□ Tomar captura de pantalla
  └─ Mostrar resultados de queries

□ Agregar a Excel
  └─ Hoja "Login" → Métrica 1.4
```

---

### PROCESO 2: GESTIÓN DE PEDIDOS (3 métricas)

```
┌─────────────────────────────────────────────────────────────┐
│ ⚡ Métrica 2.1: Tiempo de Registro de Pedido                │
│ Meta: ≤ 2 segundos                                          │
└─────────────────────────────────────────────────────────────┘

□ Abrir Postman
  └─ Carpeta "PROCESO 2: GESTIÓN DE PEDIDOS"

□ Ejecutar "Crear Pedido" 10 veces
  └─ Anotar tiempo de cada una (ms)
  └─ Verificar que todas dan 201 Created

□ Calcular promedio
  └─ Suma de tiempos / 10
  └─ Debe ser < 2000 ms

□ Tomar capturas de pantalla
  └─ Mostrar las 10 respuestas con tiempos

□ Agregar a Excel
  └─ Hoja "Pedidos" → Métrica 2.1
  └─ Tabla con tiempos y promedio
```

---

```
┌─────────────────────────────────────────────────────────────┐
│ ❌ Métrica 2.2: Tasa de Pedidos Rechazados                  │
│ Meta: ≤ 5%                                                   │
└─────────────────────────────────────────────────────────────┘

□ Probar 3 casos de error (Postman)
  └─ Stock insuficiente → Verificar mensaje claro
  └─ Producto inexistente → Verificar mensaje claro
  └─ Items vacío → Verificar mensaje claro

□ Ejecutar 20 pedidos válidos
  └─ Contar cuántos dan 201 vs 400
  └─ Calcular: (Rechazados / Total) × 100

□ Tomar capturas de pantalla
  └─ Los 3 casos de error con mensajes
  └─ Tabla resumen de 20 intentos

□ Agregar a Excel
  └─ Hoja "Pedidos" → Métrica 2.2
  └─ Tabla con resultados y tasa
```

---

```
┌─────────────────────────────────────────────────────────────┐
│ 📝 Métrica 2.3: Completitud de Auditoría                    │
│ Meta: 100% con created_at NOT NULL                          │
└─────────────────────────────────────────────────────────────┘

□ Abrir pgAdmin

□ Ejecutar queries (Queries_SQL_Evidencias.sql)
  └─ Total de pedidos
  └─ Pedidos con created_at NOT NULL
  └─ Pedidos sin auditoría (debe ser 0)

□ Calcular porcentaje
  └─ (Con auditoría / Total) × 100
  └─ Debe ser 100%

□ Tomar captura de pantalla
  └─ Mostrar resultados de queries

□ Agregar a Excel
  └─ Hoja "Pedidos" → Métrica 2.3
```

---

### PROCESO 3: CONTROL DE INVENTARIO (3 métricas)

```
┌─────────────────────────────────────────────────────────────┐
│ 📊 Métrica 3.1: Precisión del Inventario                    │
│ Meta: ≥ 98% de coincidencia                                 │
└─────────────────────────────────────────────────────────────┘

□ Abrir pgAdmin

□ Ejecutar query de productos con stock

□ Crear tabla comparativa
  └─ Stock BD vs Stock "físico" (simulado)
  └─ Marcar coincidencias

□ Calcular porcentaje
  └─ (Coinciden / Total) × 100
  └─ Debe ser ≥ 98%

□ Tomar captura de pantalla

□ Agregar a Excel
  └─ Hoja "Inventario" → Métrica 3.1
```

---

```
┌─────────────────────────────────────────────────────────────┐
│ 🚨 Métrica 3.2: Efectividad de Alertas                      │
│ Meta: 100% de productos bajo mínimo detectados              │
└─────────────────────────────────────────────────────────────┘

□ Abrir pgAdmin

□ Ejecutar query de productos bajo mínimo
  └─ WHERE stock < min_stock

□ Contar productos detectados

□ Calcular efectividad
  └─ (Detectados / Bajo mínimo) × 100
  └─ Debe ser 100%

□ Tomar captura de pantalla

□ Agregar a Excel
  └─ Hoja "Inventario" → Métrica 3.2
```

---

```
┌─────────────────────────────────────────────────────────────┐
│ ⚡ Métrica 3.3: Tiempo de Consulta de Inventario            │
│ Meta: ≤ 500 ms                                              │
└─────────────────────────────────────────────────────────────┘

□ Abrir Postman
  └─ Carpeta "PROCESO 3: CONTROL DE INVENTARIO"

□ Ejecutar consultas 10 veces
  └─ GET /products
  └─ GET /products?name=...
  └─ GET /products/code/...

□ Anotar tiempos de cada una

□ Calcular promedio
  └─ Debe ser < 500 ms

□ Tomar capturas de pantalla

□ Agregar a Excel
  └─ Hoja "Inventario" → Métrica 3.3
```

---

### PROCESO 4: GESTIÓN DE VENTAS (4 métricas)

```
┌─────────────────────────────────────────────────────────────┐
│ 🔄 Métrica 4.1: Integridad Transaccional                    │
│ Meta: ≥ 99.9% sin inconsistencias                           │
└─────────────────────────────────────────────────────────────┘

□ Crear 10 órdenes en Postman

□ Confirmar las 10 órdenes
  └─ PUT /orders/{id}/confirm

□ Para cada confirmación, verificar en pgAdmin:
  └─ Estado cambió a COMPLETED
  └─ Stock se descontó
  └─ Total es consistente

□ Calcular integridad
  └─ (Consistentes / Total) × 100
  └─ Debe ser ≥ 99.9%

□ Tomar capturas de pantalla

□ Agregar a Excel
  └─ Hoja "Ventas" → Métrica 4.1
```

---

```
┌─────────────────────────────────────────────────────────────┐
│ ⚡ Métrica 4.2: Tiempo de Confirmación                       │
│ Meta: ≤ 2 segundos                                          │
└─────────────────────────────────────────────────────────────┘

□ Abrir Postman

□ Ejecutar "Confirmar Orden" 10 veces
  └─ PUT /orders/{id}/confirm
  └─ Anotar tiempo de cada una

□ Calcular promedio
  └─ Debe ser < 2000 ms

□ Tomar capturas de pantalla

□ Agregar a Excel
  └─ Hoja "Ventas" → Métrica 4.2
```

---

```
┌─────────────────────────────────────────────────────────────┐
│ 🔒 Métrica 4.3: Control de Acceso                           │
│ Meta: 100% de rechazos correctos                            │
└─────────────────────────────────────────────────────────────┘

□ Crear usuarios con diferentes roles

□ Probar confirmación con cada rol:
  └─ USER → debe dar 403 ❌
  └─ WAREHOUSE → debe dar 403 ❌
  └─ SALES → debe dar 200 ✅
  └─ MANAGER → debe dar 200 ✅

□ Verificar 100% de control correcto

□ Tomar capturas de pantalla

□ Agregar a Excel
  └─ Hoja "Ventas" → Métrica 4.3
```

---

```
┌─────────────────────────────────────────────────────────────┐
│ 💬 Métrica 4.4: Claridad de Mensajes                        │
│ Meta: ≥ 95% exitosas al primer intento                      │
└─────────────────────────────────────────────────────────────┘

□ De las 10 confirmaciones de Métrica 4.1
  └─ Contar cuántas fueron exitosas al primer intento

□ Calcular porcentaje
  └─ (Exitosas / Total) × 100
  └─ Debe ser ≥ 95%

□ Agregar a Excel
  └─ Hoja "Ventas" → Métrica 4.4
```

---

## 📝 FASE 3: DOCUMENTACIÓN FINAL (Semana 3)

```
┌─────────────────────────────────────────────────────────────┐
│ 📄 Compilar Documento Final                                 │
└─────────────────────────────────────────────────────────────┘

□ Organizar capturas de pantalla
  └─ Crear carpetas por proceso
  └─ Nombrar archivos claramente

□ Completar Excel con todas las métricas
  └─ Verificar cálculos
  └─ Agregar gráficos

□ Crear documento Word/PDF
  └─ Portada
  └─ Índice
  └─ Introducción
  └─ Sección por cada proceso (4)
  └─ Conclusiones
  └─ Anexos

□ Crear presentación PowerPoint
  └─ 15-20 diapositivas
  └─ Resumen de cada métrica
  └─ Gráficos y tablas
  └─ Conclusiones

□ Preparar demostración en vivo (opcional)
  └─ Practicar flujo
  └─ Tener Postman y pgAdmin listos
```

---

## ✅ CHECKLIST FINAL DE ENTREGA

```
┌─────────────────────────────────────────────────────────────┐
│ 📦 Verificación Final                                        │
└─────────────────────────────────────────────────────────────┘

□ Código
  └─ Cambios desplegados en producción
  └─ Servicio funcionando correctamente

□ Evidencias
  └─ 14 métricas documentadas
  └─ Capturas de pantalla de todas las pruebas
  └─ Tablas resumen con cálculos
  └─ Todas las métricas cumplen las metas

□ Documentos
  └─ Documento final en PDF
  └─ Presentación PowerPoint
  └─ Excel con datos y cálculos
  └─ Postman Collection exportado

□ Extras
  └─ Queries SQL documentados
  └─ README actualizado
  └─ Código comentado

□ Revisión
  └─ Ortografía y gramática
  └─ Formato consistente
  └─ Referencias completas
  └─ Conclusiones claras
```

---

## 📊 PROGRESO GENERAL

```
PROCESO 1: LOGIN
[□] 1.1 Tasa de Fallo
[□] 1.2 Vulnerabilidades
[□] 1.3 Éxito Login
[□] 1.4 Datos Cifrados
Progreso: 0/4 (0%)

PROCESO 2: PEDIDOS
[□] 2.1 Tiempo Registro
[□] 2.2 Tasa Rechazo
[□] 2.3 Auditoría
Progreso: 0/3 (0%)

PROCESO 3: INVENTARIO
[□] 3.1 Precisión
[□] 3.2 Alertas
[□] 3.3 Tiempo Consulta
Progreso: 0/3 (0%)

PROCESO 4: VENTAS
[□] 4.1 Integridad
[□] 4.2 Tiempo Confirmación
[□] 4.3 Control Acceso
[□] 4.4 Claridad Mensajes
Progreso: 0/4 (0%)

═══════════════════════════════════════
PROGRESO TOTAL: 0/14 (0%)
═══════════════════════════════════════
```

---

**Imprime este checklist y márcalo a medida que avanzas! ✅**
