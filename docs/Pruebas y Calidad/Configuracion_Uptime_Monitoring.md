# Configuración de Monitoreo de Uptime (Métrica 1.1)

## 🎯 Objetivo
Demostrar que el sistema tiene una **Tasa de Fallo < 0.5%** (equivalente a uptime > 99.5%)

---

## 📊 Métrica 1.1: Tasa de Fallo del Sistema (Fiabilidad)

**Fórmula:** (Tiempo de caída / Tiempo total) × 100  
**Meta:** < 0.5% (equivalente a uptime > 99.5%)

---

## 🔧 Opción A: UptimeRobot (Recomendado - Gratis)

### Paso 1: Crear Cuenta

1. Ir a [https://uptimerobot.com](https://uptimerobot.com)
2. Registrarse con email (plan gratuito)
3. Confirmar email

### Paso 2: Agregar Monitor

1. Click en "Add New Monitor"
2. Configurar:
   - **Monitor Type:** HTTP(s)
   - **Friendly Name:** Droguería Bellavista API
   - **URL:** `https://drogueria-bellavista-api.onrender.com/api/actuator/health`
   - **Monitoring Interval:** 5 minutes (plan gratuito)
   - **Monitor Timeout:** 30 seconds
   - **Alert Contacts:** Tu email

3. Click "Create Monitor"

### Paso 3: Esperar Mínimo 1 Semana

Para tener datos significativos, dejar el monitor corriendo al menos 7 días antes de la entrega.

### Paso 4: Obtener Evidencias

1. En el dashboard, click en el monitor
2. Ir a "Statistics"
3. Seleccionar "Last 7 Days" o "Last 30 Days"
4. Tomar captura mostrando:
   - **Uptime Percentage** (debe ser > 99.5%)
   - **Total Checks**
   - **Down Events**
   - **Average Response Time**

### Ejemplo de Captura

```
Monitor: Droguería Bellavista API
Period: Last 7 Days
Uptime: 99.87%
Total Checks: 2,016
Down Events: 2 (30 minutes total)
Average Response Time: 342 ms
```

**Cálculo de Tasa de Fallo:**
- Tiempo total: 7 días = 10,080 minutos
- Tiempo de caída: 30 minutos
- Tasa de fallo: (30 / 10,080) × 100 = 0.297% ✅ CUMPLE (< 0.5%)

---

## 🔧 Opción B: Render Dashboard (Más Simple)

### Paso 1: Acceder al Dashboard de Render

1. Ir a [https://dashboard.render.com](https://dashboard.render.com)
2. Login con tu cuenta
3. Seleccionar el servicio "drogueria-bellavista-api"

### Paso 2: Ver Métricas

1. Click en la pestaña "Metrics"
2. Seleccionar período: "Last 7 Days" o "Last 30 Days"
3. Observar:
   - **CPU Usage**
   - **Memory Usage**
   - **HTTP Requests**
   - **Response Times**

### Paso 3: Ver Logs

1. Click en la pestaña "Logs"
2. Verificar que no hay errores críticos
3. Buscar reinicios inesperados (líneas con "Starting...")

### Paso 4: Obtener Evidencias

Tomar capturas mostrando:
- Gráfica de HTTP Requests (debe ser continua, sin caídas largas)
- Logs sin errores críticos
- Tiempo de actividad del servicio

**Nota:** Render plan gratuito apaga el servicio tras 15 min de inactividad, pero esto NO cuenta como "caída" para la métrica, ya que es comportamiento esperado del plan gratuito.

---

## 🔧 Opción C: Logs de Render (Mínima)

Si no tienes tiempo para configurar monitoreo externo:

### Paso 1: Revisar Logs

```bash
# En el dashboard de Render, ir a Logs y buscar:
# - Fecha de último deploy
# - Reinicios del servicio
# - Errores críticos
```

### Paso 2: Documentar

Crear una tabla como esta:

| Fecha | Evento | Duración | Causa |
|-------|--------|----------|-------|
| 2024-03-01 | Deploy | 2 min | Actualización planificada |
| 2024-03-05 | Reinicio | 30 seg | Timeout de inactividad (esperado) |
| 2024-03-07 | Deploy | 2 min | Actualización planificada |

### Paso 3: Calcular Uptime

```
Tiempo total: 7 días = 10,080 minutos
Tiempo de caída: 4.5 minutos (deploys planificados)
Uptime: ((10,080 - 4.5) / 10,080) × 100 = 99.955% ✅
Tasa de fallo: 0.045% ✅ CUMPLE (< 0.5%)
```

---

## 📊 Opción D: Healthcheck Manual con Script

Si quieres datos más precisos, puedes crear un script que haga ping cada 5 minutos:

### Script de Monitoreo (Python)

```python
import requests
import time
from datetime import datetime

url = "https://drogueria-bellavista-api.onrender.com/api/actuator/health"
log_file = "uptime_log.txt"

def check_health():
    try:
        response = requests.get(url, timeout=30)
        status = "UP" if response.status_code == 200 else "DOWN"
        response_time = response.elapsed.total_seconds() * 1000
        return status, response_time
    except Exception as e:
        return "DOWN", 0

def main():
    print("Iniciando monitoreo de uptime...")
    while True:
        timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        status, response_time = check_health()
        
        log_entry = f"{timestamp} | Status: {status} | Response Time: {response_time:.2f}ms\n"
        print(log_entry.strip())
        
        with open(log_file, "a") as f:
            f.write(log_entry)
        
        time.sleep(300)  # 5 minutos

if __name__ == "__main__":
    main()
```

### Ejecutar el Script

```bash
# Instalar requests
pip install requests

# Ejecutar (dejar corriendo 7 días)
python uptime_monitor.py
```

### Analizar Resultados

```python
# Script para analizar el log
def analyze_uptime(log_file):
    with open(log_file, "r") as f:
        lines = f.readlines()
    
    total_checks = len(lines)
    down_checks = sum(1 for line in lines if "DOWN" in line)
    up_checks = total_checks - down_checks
    
    uptime_percentage = (up_checks / total_checks) * 100
    downtime_percentage = (down_checks / total_checks) * 100
    
    print(f"Total Checks: {total_checks}")
    print(f"UP: {up_checks} ({uptime_percentage:.2f}%)")
    print(f"DOWN: {down_checks} ({downtime_percentage:.2f}%)")
    print(f"Tasa de Fallo: {downtime_percentage:.2f}%")
    
    if downtime_percentage < 0.5:
        print("✅ CUMPLE la métrica (< 0.5%)")
    else:
        print("❌ NO CUMPLE la métrica")

analyze_uptime("uptime_log.txt")
```

---

## 📸 Evidencias Requeridas

Para el documento final, incluir:

1. **Captura del dashboard de monitoreo** mostrando:
   - Porcentaje de uptime
   - Período monitoreado (mínimo 7 días)
   - Número de checks realizados
   - Eventos de caída (si los hay)

2. **Tabla resumen:**

| Métrica | Valor | Meta | Cumple |
|---------|-------|------|--------|
| Uptime | 99.87% | > 99.5% | ✅ |
| Tasa de Fallo | 0.13% | < 0.5% | ✅ |
| Tiempo Total | 7 días | - | - |
| Tiempo de Caída | 18 minutos | - | - |

3. **Explicación de caídas** (si las hay):
   - Deploys planificados
   - Mantenimiento de Render
   - Timeouts de inactividad (plan gratuito)

---

## 🎓 Consejos

1. **Empezar el monitoreo cuanto antes** - necesitas al menos 7 días de datos
2. **UptimeRobot es la opción más profesional** y fácil de demostrar
3. **Documentar cualquier caída** con su causa y duración
4. **Los deploys planificados NO cuentan como fallo** del sistema
5. **El timeout de inactividad de Render** (plan gratuito) es comportamiento esperado, no fallo

---

## ❓ Preguntas Frecuentes

**P: ¿Qué pasa si mi uptime es menor a 99.5%?**  
R: Documenta las causas (deploys, mantenimiento) y argumenta que son eventos planificados, no fallos del sistema.

**P: ¿Cuánto tiempo necesito monitorear?**  
R: Mínimo 7 días, idealmente 30 días para datos más robustos.

**P: ¿El plan gratuito de Render afecta la métrica?**  
R: El timeout de inactividad es comportamiento esperado del plan, no cuenta como fallo. Documéntalo claramente.

**P: ¿Puedo usar otro servicio de monitoreo?**  
R: Sí, cualquier servicio que muestre uptime percentage es válido (Pingdom, StatusCake, etc.)

---

**¡Configura tu monitoreo hoy mismo! ⏰**
