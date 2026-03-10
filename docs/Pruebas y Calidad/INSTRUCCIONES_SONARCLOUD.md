# 🔍 Instrucciones para Configurar SonarCloud

## Paso a Paso para Activar el Análisis Automático

---

## 📋 Requisitos Previos

- ✅ Cuenta de GitHub (ya la tienes)
- ✅ Repositorio `Dan17i/softwareDrogueria` (ya existe)
- ✅ Archivo `sonarcloud.yml` creado en `.github/workflows/` (✅ LISTO)
- ✅ Propiedades de SonarCloud en `pom.xml` (✅ LISTO)

---

## 🚀 PASO 1: Crear Cuenta en SonarCloud

1. Ve a [https://sonarcloud.io](https://sonarcloud.io)
2. Click en **"Log in"**
3. Selecciona **"With GitHub"**
4. Autoriza a SonarCloud a acceder a tu cuenta de GitHub
5. Acepta los términos de servicio

---

## 🔗 PASO 2: Importar tu Repositorio

1. Una vez dentro de SonarCloud, click en el **"+"** (arriba a la derecha)
2. Selecciona **"Analyze new project"**
3. Busca y selecciona el repositorio **`Dan17i/softwareDrogueria`**
4. Click en **"Set Up"**

---

## 🔑 PASO 3: Configurar el Token en GitHub

### 3.1 Generar Token en SonarCloud

1. En SonarCloud, ve a tu proyecto
2. Click en **"Administration"** → **"Analysis Method"**
3. Selecciona **"With GitHub Actions"**
4. Copia el **SONAR_TOKEN** que aparece (algo como `sqp_xxxxxxxxxxxxx`)

### 3.2 Agregar Token a GitHub Secrets

1. Ve a tu repositorio en GitHub: [https://github.com/Dan17i/softwareDrogueria](https://github.com/Dan17i/softwareDrogueria)
2. Click en **"Settings"** (pestaña del repositorio)
3. En el menú izquierdo, click en **"Secrets and variables"** → **"Actions"**
4. Click en **"New repository secret"**
5. Configura:
   - **Name:** `SONAR_TOKEN`
   - **Value:** Pega el token que copiaste de SonarCloud
6. Click en **"Add secret"**

---

## ✅ PASO 4: Verificar la Configuración

### 4.1 Hacer un Commit para Activar el Workflow

El workflow ya está configurado para ejecutarse automáticamente en cada push a `main` o `develop`.

Para probarlo:

```bash
# Ya hicimos los cambios, solo necesitas hacer push
git add .
git commit -m "ci: configurar SonarCloud con GitHub Actions"
git push origin main
```

### 4.2 Ver el Análisis en Ejecución

1. Ve a tu repositorio en GitHub
2. Click en la pestaña **"Actions"**
3. Deberías ver un workflow llamado **"SonarCloud Analysis"** ejecutándose
4. Click en él para ver el progreso

### 4.3 Ver Resultados en SonarCloud

1. Ve a [https://sonarcloud.io](https://sonarcloud.io)
2. Click en tu proyecto **"softwareDrogueria"**
3. Verás el dashboard con:
   - **Bugs**
   - **Vulnerabilities** ⭐
   - **Code Smells**
   - **Coverage**
   - **Duplications**
   - **Lines of Code**

---

## 📊 Qué Esperar del Análisis

### Métricas que SonarCloud Analizará:

| Métrica | Descripción | Meta |
|---------|-------------|------|
| **Bugs** | Errores de código que pueden causar fallos | 0 |
| **Vulnerabilities** | Problemas de seguridad | 0 |
| **Code Smells** | Problemas de mantenibilidad | < 10 |
| **Coverage** | Cobertura de pruebas | > 80% |
| **Duplications** | Código duplicado | < 3% |
| **Security Hotspots** | Puntos sensibles de seguridad | 0 |

### Quality Gate

SonarCloud tiene un "Quality Gate" por defecto que verifica:
- ✅ 0 vulnerabilidades en código nuevo
- ✅ 0 bugs en código nuevo
- ✅ Cobertura > 80% en código nuevo
- ✅ Duplicación < 3% en código nuevo

---

## 🔧 Configuración del Workflow

El archivo `.github/workflows/sonarcloud.yml` que creamos hace lo siguiente:

```yaml
# Se ejecuta en:
- Push a main o develop
- Pull requests

# Pasos:
1. Checkout del código
2. Configurar Java 21
3. Cachear dependencias de Maven y SonarCloud
4. Compilar el proyecto
5. Ejecutar análisis de SonarCloud
6. Enviar resultados a SonarCloud
```

---

## 📝 Propiedades en pom.xml

Agregamos estas propiedades al `pom.xml`:

```xml
<properties>
    <sonar.organization>dan17i</sonar.organization>
    <sonar.host.url>https://sonarcloud.io</sonar.host.url>
    <sonar.projectKey>Dan17i_softwareDrogueria</sonar.projectKey>
</properties>
```

Estas propiedades le dicen a Maven:
- Dónde está tu organización en SonarCloud
- Cuál es la URL de SonarCloud
- Cuál es la clave de tu proyecto

---

## 🎯 Cómo Usar los Resultados

### Para la Métrica 1.2: Densidad de Vulnerabilidades

1. Ve al dashboard de SonarCloud
2. Busca la sección **"Security"**
3. Anota el número de **Vulnerabilities**
4. Anota las **Lines of Code** (KLOC)
5. Calcula: `Vulnerabilidades / KLOC`

**Ejemplo:**
```
Vulnerabilities: 0
Lines of Code: 5,234
KLOC: 5.234
Densidad: 0 / 5.234 = 0 vulnerabilidades por KLOC ✅
Meta: < 1 por KLOC ✅ CUMPLE
```

### Tomar Capturas de Pantalla

Para tu documentación, toma capturas de:
1. **Dashboard principal** mostrando todas las métricas
2. **Security tab** mostrando 0 vulnerabilidades
3. **Quality Gate** mostrando "Passed"
4. **Measures** mostrando líneas de código

---

## 🔄 Análisis Automático

Una vez configurado, el análisis se ejecutará automáticamente:

- ✅ Cada vez que hagas `git push` a `main`
- ✅ Cada vez que hagas `git push` a `develop`
- ✅ Cada vez que abras o actualices un Pull Request

No necesitas hacer nada más, GitHub Actions se encarga de todo.

---

## 🐛 Troubleshooting

### Error: "SONAR_TOKEN not found"

**Solución:** Verifica que agregaste el secret en GitHub:
1. Settings → Secrets and variables → Actions
2. Debe existir un secret llamado `SONAR_TOKEN`

### Error: "Project not found"

**Solución:** Verifica que:
1. El proyecto existe en SonarCloud
2. La organización es correcta: `dan17i`
3. La clave del proyecto es correcta: `Dan17i_softwareDrogueria`

### Error: "Build failed"

**Solución:** 
1. Ve a la pestaña Actions en GitHub
2. Click en el workflow fallido
3. Revisa los logs para ver el error específico
4. Usualmente es un problema de compilación de Maven

### El análisis no se ejecuta

**Solución:**
1. Verifica que el archivo `.github/workflows/sonarcloud.yml` existe
2. Verifica que hiciste push del archivo
3. Ve a Actions → Workflows → Debería aparecer "SonarCloud Analysis"

---

## 📊 Ejemplo de Resultado Esperado

```
Project: softwareDrogueria
Quality Gate: Passed ✅

Reliability: A
Security: A
Maintainability: A

Bugs: 0 ✅
Vulnerabilities: 0 ✅
Code Smells: 12
Coverage: 70.5%
Duplications: 1.2%
Lines of Code: 5,234

Densidad de Vulnerabilidades: 0 / 5.234 KLOC = 0 ✅ CUMPLE (< 1)
```

---

## 🎓 Beneficios de SonarCloud

1. **Análisis automático** en cada commit
2. **Detección temprana** de bugs y vulnerabilidades
3. **Métricas de calidad** en tiempo real
4. **Integración con GitHub** (comentarios en PRs)
5. **Historial de calidad** del proyecto
6. **Gratis para proyectos públicos**

---

## 📚 Recursos Adicionales

- [Documentación de SonarCloud](https://docs.sonarcloud.io/)
- [SonarCloud con GitHub Actions](https://docs.sonarcloud.io/advanced-setup/ci-based-analysis/github-actions/)
- [Quality Gates](https://docs.sonarcloud.io/improving/quality-gates/)
- [Métricas de SonarCloud](https://docs.sonarcloud.io/digging-deeper/metric-definitions/)

---

## ✅ Checklist de Configuración

- [ ] Cuenta de SonarCloud creada
- [ ] Repositorio importado en SonarCloud
- [ ] Token generado en SonarCloud
- [ ] Secret `SONAR_TOKEN` agregado en GitHub
- [ ] Archivo `sonarcloud.yml` en `.github/workflows/`
- [ ] Propiedades agregadas en `pom.xml`
- [ ] Push realizado para activar el workflow
- [ ] Workflow ejecutado exitosamente en GitHub Actions
- [ ] Resultados visibles en SonarCloud
- [ ] Capturas de pantalla tomadas para documentación

---

**¡Listo! Tu proyecto ahora tiene análisis automático de calidad con SonarCloud! 🎉**

---

**Fecha:** Marzo 2026  
**Versión:** 1.0  
**Estado:** ✅ CONFIGURACIÓN LISTA PARA USAR

