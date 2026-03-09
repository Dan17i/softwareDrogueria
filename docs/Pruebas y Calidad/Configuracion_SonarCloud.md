# Configuración de SonarCloud (Métrica 1.2)

## 🎯 Objetivo
Demostrar que el proyecto tiene una **Densidad de Vulnerabilidades < 1 por KLOC** (mil líneas de código)

---

## 📊 Métrica 1.2: Densidad de Vulnerabilidades (Seguridad)

**Fórmula:** Vulnerabilidades / KLOC  
**Meta:** < 1 por KLOC (mil líneas de código)

---

## 🔧 Opción A: SonarCloud (Recomendado - Gratis para Proyectos Públicos)

### Paso 1: Crear Cuenta en SonarCloud

1. Ir a [https://sonarcloud.io](https://sonarcloud.io)
2. Click en "Log in" → "With GitHub"
3. Autorizar SonarCloud a acceder a tu cuenta de GitHub
4. Aceptar los términos de servicio

### Paso 2: Importar el Repositorio

1. En el dashboard, click en "+" → "Analyze new project"
2. Seleccionar la organización de GitHub (tu usuario)
3. Buscar y seleccionar el repositorio `Dan17i/softwareDrogueria`
4. Click en "Set Up"

### Paso 3: Configurar el Análisis

#### Opción 3.1: Análisis Automático (Más Fácil)

1. Seleccionar "With GitHub Actions"
2. SonarCloud generará un workflow de GitHub Actions
3. Copiar el archivo `.github/workflows/sonarcloud.yml` sugerido
4. Agregar el secret `SONAR_TOKEN` en GitHub:
   - Ir a Settings → Secrets and variables → Actions
   - Click "New repository secret"
   - Name: `SONAR_TOKEN`
   - Value: (copiar el token de SonarCloud)

#### Opción 3.2: Análisis Manual con Maven

1. Agregar el plugin de SonarCloud al `pom.xml`:

```xml
<properties>
    <!-- Otras propiedades -->
    <sonar.organization>dan17i</sonar.organization>
    <sonar.host.url>https://sonarcloud.io</sonar.host.url>
</properties>
```

2. Ejecutar el análisis localmente:

```bash
# Configurar el token como variable de entorno
export SONAR_TOKEN=tu_token_aqui

# Ejecutar el análisis
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=Dan17i_softwareDrogueria \
  -Dsonar.organization=dan17i \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.token=$SONAR_TOKEN
```

### Paso 4: Esperar el Análisis

El análisis puede tardar 5-10 minutos. Una vez completado, verás el dashboard con:

- **Bugs**
- **Vulnerabilities** ⭐ (esto es lo que necesitas)
- **Code Smells**
- **Coverage**
- **Duplications**
- **Lines of Code** (KLOC)

### Paso 5: Obtener Evidencias

1. En el dashboard de SonarCloud, ir a tu proyecto
2. Tomar captura mostrando:
   - **Vulnerabilities:** 0 (idealmente)
   - **Lines of Code:** ~5,000 (ejemplo)
   - **Densidad:** 0 / 5 KLOC = 0 vulnerabilidades por KLOC ✅

### Ejemplo de Captura

```
Project: softwareDrogueria
Quality Gate: Passed ✅

Reliability: A
Security: A
Maintainability: A

Bugs: 0
Vulnerabilities: 0 ⭐
Code Smells: 12
Coverage: 85.2%
Duplications: 1.2%
Lines of Code: 5,234

Densidad de Vulnerabilidades: 0 / 5.234 KLOC = 0 ✅ CUMPLE (< 1)
```

---

## 🔧 Opción B: Análisis Manual (Sin SonarCloud)

Si no puedes usar SonarCloud, puedes hacer un análisis manual:

### Paso 1: Contar Líneas de Código

En Git Bash o terminal:

```bash
# Contar líneas de código Java (excluyendo comentarios y líneas vacías)
find src/main/java -name "*.java" -exec wc -l {} + | tail -1

# Resultado ejemplo: 5234 líneas
```

O usar una herramienta online como [cloc](https://github.com/AlDanial/cloc):

```bash
# Instalar cloc
# Windows: choco install cloc
# Mac: brew install cloc
# Linux: sudo apt install cloc

# Ejecutar
cloc src/main/java

# Resultado:
# Language    files  blank  comment  code
# Java          45    890    1200     5234
```

### Paso 2: Identificar Vulnerabilidades Conocidas

Revisar manualmente el código buscando:

1. **Inyección SQL:** ¿Usas queries concatenadas? ❌
   - ✅ Tu proyecto usa JPA/Hibernate → Seguro

2. **Contraseñas en texto plano:** ¿Guardas passwords sin cifrar? ❌
   - ✅ Tu proyecto usa BCrypt → Seguro

3. **Falta de autenticación:** ¿Endpoints sin protección? ❌
   - ✅ Tu proyecto usa Spring Security + JWT → Seguro

4. **Exposición de datos sensibles:** ¿Logs con passwords? ❌
   - ✅ Revisar que no haya `log.info(password)` en el código

5. **Dependencias vulnerables:** ¿Librerías desactualizadas? ❌
   - Ejecutar: `mvn dependency:tree` y revisar versiones

### Paso 3: Calcular Densidad

```
Líneas de código: 5,234
KLOC: 5.234
Vulnerabilidades encontradas: 0
Densidad: 0 / 5.234 = 0 vulnerabilidades por KLOC ✅
```

### Paso 4: Documentar

Crear una tabla como esta:

| Categoría de Vulnerabilidad | Encontradas | Mitigación |
|------------------------------|-------------|------------|
| Inyección SQL | 0 | JPA/Hibernate con prepared statements |
| Contraseñas en texto plano | 0 | BCrypt para hashing |
| Falta de autenticación | 0 | Spring Security + JWT |
| Exposición de datos sensibles | 0 | No hay logs con passwords |
| Dependencias vulnerables | 0 | Dependencias actualizadas |
| **Total** | **0** | **✅ CUMPLE** |

**Cálculo:**
- KLOC: 5.234
- Vulnerabilidades: 0
- Densidad: 0 / 5.234 = 0 por KLOC ✅ (meta < 1)

---

## 🔧 Opción C: OWASP Dependency Check

Otra herramienta gratuita para detectar vulnerabilidades en dependencias:

### Paso 1: Agregar Plugin al pom.xml

```xml
<build>
    <plugins>
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
    </plugins>
</build>
```

### Paso 2: Ejecutar el Análisis

```bash
mvn dependency-check:check
```

### Paso 3: Ver Reporte

El reporte se genera en `target/dependency-check-report.html`

Abrir en navegador y revisar:
- **Total Dependencies:** 45 (ejemplo)
- **Vulnerabilities Found:** 0 ✅
- **High Severity:** 0
- **Medium Severity:** 0
- **Low Severity:** 0

---

## 📸 Evidencias Requeridas

Para el documento final, incluir:

1. **Captura del dashboard de SonarCloud** mostrando:
   - Vulnerabilities: 0
   - Lines of Code: X
   - Quality Gate: Passed

2. **Tabla resumen:**

| Métrica | Valor | Meta | Cumple |
|---------|-------|------|--------|
| Vulnerabilidades | 0 | - | - |
| Líneas de Código | 5,234 | - | - |
| KLOC | 5.234 | - | - |
| Densidad | 0 / 5.234 = 0 | < 1 | ✅ |

3. **Argumentación de seguridad:**

> El proyecto implementa las siguientes medidas de seguridad:
> 
> - **Spring Security:** Autenticación y autorización basada en roles
> - **JWT:** Tokens seguros para sesiones stateless
> - **BCrypt:** Hashing de contraseñas con salt automático
> - **JPA/Hibernate:** Prevención de inyección SQL con prepared statements
> - **Validaciones:** Input validation en DTOs con Bean Validation
> - **CORS:** Configuración restrictiva de orígenes permitidos
> - **HTTPS:** Comunicación cifrada en producción (Render)
> 
> Estas medidas, combinadas con el análisis de SonarCloud, garantizan una densidad de vulnerabilidades de 0 por KLOC, cumpliendo ampliamente la meta de < 1 por KLOC.

---

## 🎓 Consejos

1. **SonarCloud es la opción más profesional** y fácil de demostrar
2. **Configura el análisis automático** con GitHub Actions para que se ejecute en cada push
3. **Revisa y corrige los Code Smells** para mejorar la calidad general
4. **Documenta las medidas de seguridad** implementadas en el proyecto
5. **Si encuentras vulnerabilidades**, corrígelas antes de la entrega

---

## 🔗 Recursos Adicionales

- [SonarCloud Documentation](https://docs.sonarcloud.io/)
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [Spring Security Best Practices](https://docs.spring.io/spring-security/reference/features/index.html)
- [Maven Dependency Check](https://jeremylong.github.io/DependencyCheck/dependency-check-maven/)

---

## ❓ Preguntas Frecuentes

**P: ¿Qué pasa si SonarCloud encuentra vulnerabilidades?**  
R: Revisa cada una, corrígela si es real, o márcala como "falso positivo" si no aplica.

**P: ¿Cuánto tiempo tarda el análisis?**  
R: Entre 5-10 minutos para un proyecto de este tamaño.

**P: ¿Puedo usar SonarCloud en repositorio privado?**  
R: Sí, pero necesitas una licencia de pago. Para académico, haz el repo público temporalmente.

**P: ¿Qué diferencia hay entre Bug y Vulnerability?**  
R: Bug = error de lógica. Vulnerability = fallo de seguridad explotable.

---

**¡Analiza tu código hoy mismo! 🔍**
