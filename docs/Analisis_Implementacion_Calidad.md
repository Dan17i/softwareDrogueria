# ANÁLISIS DE IMPLEMENTACIÓN DEL PLAN DE GESTIÓN DE LA CALIDAD

## Resumen Ejecutivo

Este documento analiza qué componentes del Plan de Gestión de la Calidad requieren implementación directa en el código, cuáles ya están implementados, y proporciona guías específicas para implementar y evaluar cada uno.

---

## PARTE 1: ESTADO ACTUAL DE IMPLEMENTACIÓN

### 1.1 Elementos Ya Implementados ✅

| Componente | Descripción | Estado |
|------------|-------------|--------|
| **Arquitectura Hexagonal** | Separación en capas domain/application/infrastructure | ✅ Completo |
| **85 Pruebas Unitarias e Integración** | Unit tests + Integration tests con Testcontainers | ✅ Completo |
| **Autenticación JWT** | Sistema de autenticación con tokens | ✅ Completo |
| **Control de Acceso RBAC** | Roles: ADMIN, MANAGER, SALES, WAREHOUSE, USER | ✅ Completo |
| **Manejo de Excepciones** | GlobalExceptionHandler | ✅ Completo |
| **Documentación API** | SpringDoc OpenAPI/Swagger | ✅ Completo |
| **Dockerización** | Dockerfile multistage | ✅ Completo |
| **Perfiles de Configuración** | application-dev.yml, application-prod.yml | ✅ Completo |
| **Health Checks** | Spring Boot Actuator | ✅ Completo |
| **Contenedores** | Docker + docker-compose | ✅ Completo |

### 1.2 Elementos Parcialmente Implementados 🔶

| Componente | Estado Actual | Gap |
|------------|---------------|-----|
| **Cobertura de Pruebas** | ~70% estimada | Falta medir con JaCoCo |
| **Análisis de Código Estático** | No configurado | Falta SonarQube |
| **Pruebas de Rendimiento** | No implementado | Falta JMeter/k6 |
| **Pruebas de Seguridad** | Solo tests funcionales | Falta OWASP ZAP |
| **Monitorización en Producción** | Solo health check básico | Falta APM |

---

## PARTE 2: IMPLEMENTACIONES REQUERIDAS

### 2.1 MÉTRICAS DE CALIDAD - IMPLEMENTACIÓN PRIORITARIA

#### 2.1.1 JaCoCo (Cobertura de Pruebas)

**¿Qué es?**: Plugin de Maven que genera reportes de cobertura de código.

**¿Cómo implementarlo?**

1. **Agregar dependencia en `pom.xml`**:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

2. **Ejecutar reporte**:

```bash
mvn clean test jacoco:report
```

3. **Ver reporte**: Abrir `target/site/jacoco/index.html`

4. **Configurar umbrales** (agregar en `<configuration>`):

```xml
<rules>
    <rule element="PACKAGE">
        <limits>
            <limit>
                <counter>LINE</counter>
                <value>COVEREDRATIO</value>
                <minimum>0.80</minimum>
            </limit>
        </limits>
    </rule>
</rules>
```

**¿Cómo evaluarlo?**
- Objetivo: ≥ 80% de cobertura de líneas
- El build fallará si no se cumple el umbral

---

#### 2.1.2 SonarQube (Análisis de Código Estático)

**¿Qué es?**: Plataforma para análisis de calidad de código.

**¿Cómo implementarlo?**

Opción A: **SonarQube Local con Docker**

```bash
# 1. Levantar SonarQube
docker run -d --name sonarqube -p 9000:9000 sonarqube:latest

# 2. Acceder a http://localhost:9000 (admin/admin)

# 3. Crear proyecto "InventoryRX" y obtener token

# 4. Ejecutar análisis
mvn sonar:sonar \
  -Dsonar.projectKey=InventoryRX \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=TU_TOKEN
```

Opción B: **SonarCloud (Gratuito para proyectos públicos)**

```bash
# Agregar en pom.xml
<properties>
    <sonar.organization>tu-organizacion</sonar.organization>
    <sonar.host.url>https://sonarcloud.io</sonar.host.url>
</properties>

# Ejecutar
mvn sonar:sonar -Dsonar.token=TU_TOKEN
```

**¿Cómo evaluarlo?**

| Métrica | Objetivo | Acción si falla |
|---------|----------|-----------------|
| Code Smells | < 10 | Refactorizar |
| Bugs | 0 | Corregir inmediatamente |
| Vulnerabilities | 0 | Corregir inmediatamente |
| Duplication | < 3% | Eliminar duplicados |
| Coverage | ≥ 80% | Agregar tests |
| Maintainability | A | Mejorar código |

---

### 2.2 PRUEBAS DE RENDIMIENTO

#### 2.2.1 JMeter - Pruebas de Carga

**¿Qué es?**: Herramienta Apache para pruebas de rendimiento.

**¿Cómo implementarlo?**

1. **Descargar**: https://jmeter.apache.org/

2. **Crear plan de pruebas** (`test-plan.jmx`):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan>
  <hashTree>
    <TestPlan guiclass="TestPlanGui" testclass="TestPlan">
      <stringProp name="TestPlan.name">InventoryRX Performance Test</stringProp>
    </TestPlan>
    <hashTree>
      <ThreadGroup guiclass="ThreadGroupGui" testclass="ThreadGroup">
        <stringProp name="ThreadGroup.num_threads">100</stringProp>
        <stringProp name="ThreadGroup.ramp_time">10</stringProp>
        <stringProp name="ThreadGroup.duration">60</stringProp>
      </ThreadGroup>
      <hashTree>
        <HTTPSamplerProxy guiclass="HttpTestSampleGui" testclass="HTTPSamplerProxy">
          <stringProp name="HTTPSampler.domain">drogueria-bellavista-api.onrender.com</stringProp>
          <stringProp name="HTTPSampler.path">/api/products</stringProp>
          <stringProp name="HTTPSampler.method">GET</stringProp>
        </HTTPSamplerProxy>
      </hashTree>
    </hashTree>
  </hashTree>
</jmeterTestPlan>
```

3. **Ejecutar**:

```bash
jmeter -n -t test-plan.jmx -l results.jtl -e -o report/
```

**¿Cómo evaluarlo?**

| Métrica | Objetivo | Umbral de Alerta |
|---------|----------|------------------|
| Response Time (P95) | < 500ms | > 1000ms |
| Response Time (P99) | < 1000ms | > 2000ms |
| Throughput | ≥ 100 req/s | < 50 req/s |
| Error Rate | < 1% | > 5% |

---

#### 2.2.2 k6 - Alternativa Moderna (Recomendada)

**¿Qué es?**: Herramienta de load testing moderna con scripting en JavaScript.

**¿Cómo implementarlo?**

1. **Instalar**: https://k6.io/docs/getting-started/installation/

2. **Crear script** (`performance-test.js`):

```javascript
import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '30s', target: 20 },  // Ramp-up
    { duration: '1m', target: 50 },   // Steady
    { duration: '30s', target: 0 },  // Ramp-down
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'],  // P95 < 500ms
    http_req_failed: ['rate<0.01'],   // < 1% errors
  },
};

const BASE_URL = 'https://drogueria-bellavista-api.onrender.com/api';
const TOKEN = '__TOKEN_JWT__';  // Obtener previamente

export default function () {
  // Test 1: Listar productos
  const productsRes = http.get(`${BASE_URL}/products`, {
    headers: { 'Authorization': `Bearer ${TOKEN}` },
  });
  check(productsRes, { 'products status 200': (r) => r.status === 200 });
  
  sleep(1);

  // Test 2: Obtener producto por ID
  const productRes = http.get(`${BASE_URL}/products/1`, {
    headers: { 'Authorization': `Bearer ${TOKEN}` },
  });
  check(productRes, { 'product status 200': (r) => r.status === 200 });
  
  sleep(1);
}
```

3. **Ejecutar**:

```bash
k6 run performance-test.js
```

---

### 2.3 PRUEBAS DE SEGURIDAD

#### 2.3.1 OWASP Dependency-Check

**¿Qué es?**: Escáner de vulnerabilidades en dependencias.

**¿Cómo implementarlo?**

1. **Agregar en `pom.xml`**:

```xml
<plugin>
    <groupId>org.owasp</groupId>
    <artifactId>dependency-check-maven</artifactId>
    <version>8.4.0</version>
    <executions>
        <execution>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

2. **Ejecutar**:

```bash
mvn dependency-check:check
```

3. **Ver reporte**: `target/dependency-check-report.html`

**¿Cómo evaluarlo?**

| Severidad | Acción Requerida |
|-----------|------------------|
| Critical | Corregir inmediatamente (< 24h) |
| High | Corregir en sprint actual |
| Medium | Planificar corrección |
| Low | Revisar en siguiente release |

---

#### 2.3.2 OWASP ZAP (Pruebas Dinámicas)

**¿Qué es?**: Proxy de seguridad para pruebas DAST.

**¿Cómo implementarlo?**

1. **Instalar**: https://www.zaproxy.org/download/

2. **Ejecutar escaneo básico**:

```bash
# Iniciar ZAP en modo daemon
zap.sh -daemon -port 8080

# Ejecutar spider
curl -X POST "http://localhost:8080/JSON/spider/action/scan/?url=https://drogueria-bellavista-api.onrender.com/api"

# Ejecutir escaneo activo
curl -X POST "http://localhost:8080/JSON/ascan/action/scan/?url=https://drogueria-bellavista-api.onrender.com/api"
```

3. **Ver reporte**: UI de ZAP en http://localhost:8080

---

### 2.4 MONITORIZACIÓN EN PRODUCCIÓN

#### 2.4.1 Prometheus + Grafana

**¿Qué es?**: Sistema de monitoreo y visualización de métricas.

**¿Cómo implementarlo?**

1. **Agregar dependencia en `pom.xml`**:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

2. **Configurar en `application.yml`**:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus,info
  endpoint:
    health:
      show-details: always
  metrics:
    export:
      prometheus:
        enabled: true
```

3. **Crear docker-compose.yml para monitoreo**:

```yaml
version: '3'
services:
  prometheus:
    image: prom/prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml

  grafana:
    image: grafana/grafana
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin
```

4. **Crear `prometheus.yml`**:

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'inventoryrx'
    metrics_path: '/api/actuator/prometheus'
    static_configs:
      - targets: ['host.docker.internal:8080']
```

**¿Cómo evaluarlo?**

| Métrica | Objetivo |
|---------|----------|
| Uptime | ≥ 99.5% |
| CPU Usage | < 70% |
| Memory Usage | < 512MB heap |
| Response Time P95 | < 500ms |
| Error Rate | < 1% |

---

#### 2.4.2 Spring Boot Admin (Alternativa Ligera)

**¿Qué es?**: Herramienta de monitoreo específica para Spring Boot.

**¿Cómo implementarlo?**

1. **Agregar en el proyecto**:

```xml
<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-server</artifactId>
    <version>3.1.0</version>
</dependency>
```

2. **Agregar en Application class**:

```java
@SpringBootApplication
@EnableAdminServer
public class BellavistaApplication {
    public static void main(String[] args) {
        SpringApplication.run(BellavistaApplication.class, args);
    }
}
```

---

### 2.5 PIPELINE CI/CD CON CALIDAD

#### 2.5.1 GitHub Actions con Quality Gates

**¿Cómo implementarlo?**

Crear `.github/workflows/quality.yml`:

```yaml
name: Quality Gates

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  quality-gate:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
          
      - name: Cache Maven
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
          
      - name: Run Tests
        run: mvn clean test
        
      - name: JaCoCo Coverage
        run: mvn jacoco:report
        continue-on-error: true
        
      - name: Check Coverage
        run: |
          COVERAGE=$(cat target/site/jacoco/jacoco.xml | grep -o 'INSTRUCTION.*missed="[0-9]*" covered="[0-9]*"' | grep -o 'covered="[0-9]*"' | grep -o '[0-9]*')
          echo "Coverage: $COVERAGE"
          
      - name: Dependency Check
        run: mvn dependency-check:check
        continue-on-error: true
        
      - name: Build
        run: mvn clean package -DskipTests
```

---

## PARTE 3: ROADMAP DE IMPLEMENTACIÓN

### Fase 1: Métricas Básicas (Semana 1-2)

| Tarea | Herramienta | Esfuerzo |
|-------|-------------|----------|
| Configurar JaCoCo | Maven | 1 hora |
| Generar primer reporte de cobertura | JaCoCo | 30 min |
| Analizar cobertura actual | Reporte | 2 horas |
| Identificar gaps de cobertura | Análisis | 4 horas |

**Entregable**: Reporte de cobertura baseline con plan de mejora.

### Fase 2: Análisis de Código (Semana 3-4)

| Tarea | Herramienta | Esfuerzo |
|-------|-------------|----------|
| Levantar SonarQube local | Docker | 1 hora |
| Ejecutar primer análisis | SonarQube | 30 min |
| Corregir issues críticos | Código | 8 horas |
| Configurar Quality Gate | SonarQube | 2 horas |

**Entregable**: Código limpio con rating A en SonarQube.

### Fase 3: Pruebas de Rendimiento (Semana 5-6)

| Tarea | Herramienta | Esfuerzo |
|-------|-------------|----------|
| Crear script k6 básico | k6 | 4 horas |
| Ejecutar pruebas de carga | k6 | 2 horas |
| Analizar resultados | Reporte | 2 horas |
| Optimizar endpoints críticos | Código | 8 horas |

**Entregable**: Reporte de rendimiento con baseline de tiempos.

### Fase 4: Seguridad (Semana 7-8)

| Tarea | Herramienta | Esfuerzo |
|-------|-------------|----------|
| Configurar Dependency-Check | Maven | 1 hora |
| Ejecutar escaneo | OWASP | 1 hora |
| Corregir vulnerabilidades | Código | 8 horas |
| Configurar ZAP en CI | GitHub Actions | 4 horas |

**Entregable**: cero vulnerabilidades críticas.

### Fase 5: Monitoreo (Semana 9-10)

| Tarea | Herramienta | Esfuerzo |
|-------|-------------|----------|
| Configurar Prometheus | Docker | 2 horas |
| Configurar Grafana | Docker | 2 horas |
| Crear dashboards | Grafana | 4 horas |
| Configurar alertas | Prometheus | 2 horas |

**Entregable**: Dashboard de monitoreo en producción.

---

## PARTE 4: MATRIZ DE EVALUACIÓN

### 4.1 Checklist de Cumplimiento

| # | Componente | Estado | % Cumplimiento | Responsable |
|---|------------|--------|----------------|-------------|
| 1 | JaCoCo configurado | ⬜ Pendiente | 0% | - |
| 2 | Cobertura ≥ 80% | ⬜ Pendiente | 0% | - |
| 3 | SonarQube integrado | ⬜ Pendiente | 0% | - |
| 4 | Quality Gate configurado | ⬜ Pendiente | 0% | - |
| 5 | Pruebas de carga implementadas | ⬜ Pendiente | 0% | - |
| 6 | Tiempos de respuesta medidos | ⬜ Pendiente | 0% | - |
| 7 | OWASP Dependency-Check | ⬜ Pendiente | 0% | - |
| 8 | OWASP ZAP integrado | ⬜ Pendiente | 0% | - |
| 9 | Prometheus/Grafana | ⬜ Pendiente | 0% | - |
| 10 | Pipeline CI/CD con Quality Gates | ⬜ Pendiente | 0% | - |

### 4.2 Métricas Objetivo

| Métrica | Actual | Objetivo | Plazo |
|---------|--------|----------|-------|
| Cobertura de pruebas | ~70% (estimado) | ≥ 80% | Semana 2 |
| Code coverage branches | ? | ≥ 75% | Semana 2 |
| Code smells | ? | < 10 | Semana 4 |
| Vulnerabilidades | ? | 0 críticas | Semana 8 |
| Response Time P95 | ? | < 500ms | Semana 6 |
| Throughput | ? | ≥ 100 req/s | Semana 6 |
| Uptime | ? | ≥ 99.5% | Semana 10 |

---

## RESUMEN: QUÉ IMPLEMENTAR Y CÓMO EVALUAR

### ✅ Implementaciones Inmediatas (Alta Prioridad)

1. **JaCoCo** - Medir cobertura actual
   - Cómo: Agregar plugin en pom.xml
   - Evaluar: Reporte HTML, objetivo ≥ 80%

2. **SonarQube** - Análisis de código estático
   - Cómo: Contenedor Docker + análisis Maven
   - Evaluar: Rating A, 0 vulnerabilidades

3. **OWASP Dependency-Check** - Vulnerabilidades en dependencias
   - Cómo: Plugin Maven
   - Evaluar: 0 vulnerabilidades críticas

### 🔶 Implementaciones Medianas (Media Prioridad)

4. **Pruebas de Rendimiento (k6/JMeter)** 
   - Cómo: Script de load testing
   - Evaluar: P95 < 500ms, 0% errores

5. **Pipeline CI/CD con Quality Gates**
   - Cómo: GitHub Actions
   - Evaluar: Build falla si no cumple estándares

### ⏳ Implementaciones Avanzadas (Baja Prioridad)

6. **Monitorización (Prometheus + Grafana)**
   - Cómo: Contenedores Docker
   - Evaluar: Dashboard operativo

7. **Pruebas de Seguridad (OWASP ZAP)**
   - Cómo: Escaneo automatizado
   - Evaluar: Reporte de vulnerabilidades

---

*Documento elaborado para el Proyecto Droguería Bellavista - InventoryRX*
*Análisis de Implementación v1.0 - 2026*
