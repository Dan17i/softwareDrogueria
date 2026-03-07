# INVENTORYRX - PLAN DE GESTIÓN DE LA CALIDAD DEL PROYECTO

---

## 1. INTRODUCCIÓN

### 1.1 Alcance

Este Plan de Gestión de la Calidad define el enfoque sistemático para garantizar que el sistema de gestión de droguería **InventoryRX** cumpla con los estándares de calidad establecidos, los requisitos del cliente y las normas internacionales aplicables. El alcance incluye:

- **Cobertura funcional**: Módulos de gestión de productos, clientes, proveedores, órdenes y recepciones de mercancía.
- **Infraestructura técnica**: API REST desarrollada con Spring Boot 3.2.2 y Java 21, con persistencia en PostgreSQL 15.
- **Criterios de calidad**: Atributos de calidad definidos según ISO/IEC 25010, incluyendo funcionalidad, rendimiento, seguridad, usabilidad y mantenibilidad.
- **Procesos de verificación**: Suite de pruebas automatizadas y métricas de calidad de código.

### 1.2 Objetivos

Los objetivos del Plan de Gestión de la Calidad son:

1. **Asegurar la conformidad**: Verificar que el software cumple con todos los requisitos funcionales y no funcionales especificados.
2. **Establecer estándares**: Definir métricas y umbrales de calidad alineados con las mejores prácticas de la industria.
3. **Minimizar defectos**: Reducir la densidad de defectos en producción mediante pruebas exhaustivas y análisis estático.
4. **Mejorar continuamente**: Implementar procesos de retroalimentación y mejora iterativa del código y las pruebas.
5. **Garantizar la seguridad**: Proteger los datos de la droguería mediante controles de seguridad robustos.

### 1.3 Glosario de términos

| Término | Definición |
|---------|------------|
| **Atributo de calidad** | Propiedad medible del software que contribuye a su calidad global |
| **Caso de prueba** | Especificación de las condiciones de ejecución y resultados esperados |
| **Cobertura de pruebas** | Porcentaje de código ejecutado durante las pruebas |
| **Defecto** | Fallo o imperfección en el software que causa comportamiento incorrecto |
| **ISO/IEC 25010** | Estándar internacional para la evaluación de calidad de productos de software |
| **Métrica de calidad** | Medida cuantitativa de un atributo de calidad |
| **Prueba de integración** | Prueba que verifica la interacción entre componentes |
| **Prueba unitaria** | Prueba que verifica el funcionamiento de unidades individuales de código |
| **Quality Gate** | Conjunto de condiciones que deben cumplirse para aprobar un build |
| **Technical Debt** | Costo futuro de retrabajo causado por decisiones de diseño subóptimas |

---

## 2. INFORMACIÓN DEL CASO DE ESTUDIO

**Nombre del Proyecto**: InventoryRX - Sistema de Gestión para Droguería Bellavista

**Tipo de sistema**: Backend API REST para gestión de inventario y ventas

**Descripción general**: Sistema de información desarrollado en Java 21 con Spring Boot 3.2.2 que permite la gestión integral de una droguería, incluyendo control de inventario, gestión de clientes, proveedores, órdenes de venta y recepciones de mercancía.

### Características técnicas del proyecto

| Componente | Tecnología |
|------------|------------|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.2.2 |
| Arquitectura | Hexagonal (Ports & Adapters) |
| Seguridad | Spring Security + JWT |
| Base de datos | PostgreSQL 15 (prod) / H2 (dev) |
| Build | Maven |
| Testing | JUnit 5, Testcontainers |
| Despliegue | Docker + Render |

### Funcionalidades principales

1. **Gestión de Productos**: CRUD, control de stock, búsqueda por código
2. **Gestión de Clientes**: Registro, actualización, consulta de historial
3. **Gestión de Proveedores**: Administración de datos de proveedores
4. **Gestión de Órdenes**: Creación, seguimiento y gestión de pedidos
5. **Recepciones de Mercancía**: Control de entradas de inventario
6. **Seguridad**: Autenticación JWT con control de acceso basado en roles (RBAC)

### Estado actual de pruebas

El proyecto cuenta actualmente con **85 pruebas implementadas**:

| Tipo de Prueba | Cantidad |
|----------------|----------|
| Unit Tests (Servicios) | 58 |
| SecurityIntegrationTest | 11 |
| ProductIntegrationTest | 15 |
| AuthOrderIntegrationTest | 1 |
| **Total** | **85** |

---

## 3. PLAN DE GESTIÓN DE LA CALIDAD

### 3.1 Calidad del Software

La calidad del software se define como el grado en que el sistema cumple con los requisitos funcionales explícitos e implícitos, así como las características de calidad inherentes al producto. Para el proyecto InventoryRX, la calidad del software se evalúa bajo dos perspectivas complementarias:

**Perspectiva interna**: Se refiere a las características de calidad visibles para los desarrolladores, tales como la estructura del código, la mantenibilidad, la complejidad ciclomática y la deuda técnica. Esta perspectiva garantiza que el código sea legible, testeable y extensible.

**Perspectiva externa**: Se refiere a las características de calidad visibles para los usuarios finales, tales como la funcionalidad, el rendimiento, la usabilidad y la confiabilidad. Esta perspectiva garantiza que el sistema satisfaga las necesidades del negocio de la droguería.

### 3.2 Factores de Calidad del Software

Los factores de calidad del software para InventoryRX se alinean con el modelo de McCall y la norma ISO/IEC 25010:

| Factor | Descripción | Aplicación en el Proyecto |
|--------|-------------|---------------------------|
| **Corrección** | Grado en que el software cumple las especificaciones | Validado mediante 85 pruebas unitarias e integradas |
| **Fiabilidad** | Capacidad de mantener su nivel de rendimiento | Endpoints con manejo robusto de excepciones |
| **Eficiencia** | Uso óptimo de recursos del sistema | Consultas optimizadas con Spring Data JPA |
| **Integridad** | Protección contra accesos no autorizados | Autenticación JWT y control de roles |
| **Usabilidad** | Facilidad de uso e interacción | API REST bien documentada con Swagger |
| **Mantenibilidad** | Facilidad de corrección y evolución | Arquitectura hexagonal con separación de responsabilidades |
| **Portabilidad** | Facilidad de adaptación a diferentes entornos | Contenedores Docker, perfiles configurables |

### 3.3 Métricas para la Calidad de un Proceso

Las métricas de proceso permiten evaluar la efectividad del proceso de desarrollo y las prácticas de calidad implementadas. A continuación se presentan las métricas específicas para los procesos principales del proyecto.

#### 3.3.1 Proceso 1: Autenticación y Control de Acceso

El proceso de autenticación y control de acceso garantiza que únicamente usuarios registrados y autorizados accedan al sistema, validando su identidad y asignando permisos según el rol definido.

| Nombre de la Métrica | Descripción | Fórmula / Unidad | Fuente de Datos | Frecuencia de Medición |
|---------------------|-------------|------------------|-----------------|----------------------|
| Tasa de Defectos | Porcentaje de defectos encontrados en el módulo de seguridad | (Defectos encontrados / Total casos de prueba) × 100 | Reportes de pruebas JUnit | Por sprint |
| Cobertura de Código | Porcentaje de líneas de código ejecutadas por las pruebas | (Líneas ejecutadas / Líneas totales) × 100 | JaCoCo Report | Por build |
| Tests de Seguridad | Número de pruebas de autenticación y autorización | Count de SecurityIntegrationTests | Código de tests | Por build |
| Tiempo de Respuesta Login | Latencia promedio del proceso de autenticación | Promedio en milisegundos (ms) | APM / JMeter | Semanal |
| Tasa de Fallos de Autenticación | Porcentaje de intentos de login que fallan | (Logins fallidos / Total intentos) × 100 | Logs de seguridad | Diario |

#### 3.3.2 Proceso 2: Gestión de Pedidos

El proceso de gestión de pedidos comprende la creación, actualización, consulta y seguimiento de pedidos de clientes, incluyendo la gestión de ítems, estados de orden y verificación de fórmula médica cuando aplique.

| Nombre de la Métrica | Descripción | Fórmula / Unidad | Fuente de Datos | Frecuencia de Medición |
|---------------------|-------------|------------------|-----------------|----------------------|
| Tasa de Defectos | Porcentaje de defectos encontrados en el módulo de pedidos | (Defectos encontrados / Total casos de prueba) × 100 | Reportes de pruebas JUnit | Por sprint |
| Cobertura de Código | Porcentaje de líneas de código ejecutadas por las pruebas | (Líneas ejecutadas / Líneas totales) × 100 | JaCoCo Report | Por build |
| Cobertura de Flujos E2E | Porcentaje de escenarios de negocio cubiertos | (Flujos probados / Flujos identificados) × 100 | Código de tests | Por sprint |
| Tests de Integración | Número de pruebas de integración con base de datos real | Count de tests con Testcontainers | Código de tests | Por build |
| Tiempo de Procesamiento | Tiempo promedio de creación de pedidos | Promedio en milisegundos (ms) | APM / JMeter | Semanal |

#### 3.3.3 Proceso 3: Control de Inventario

El proceso de control de inventario garantiza la disponibilidad y correcta administración de los productos farmacéuticos, manteniendo consistencia entre inventario físico y digital, gestionando lotes y fechas de vencimiento.

| Nombre de la Métrica | Descripción | Fórmula / Unidad | Fuente de Datos | Frecuencia de Medición |
|---------------------|-------------|------------------|-----------------|----------------------|
| Tasa de Defectos | Porcentaje de defectos encontrados en el módulo de inventario | (Defectos encontrados / Total casos de prueba) × 100 | Reportes de pruebas JUnit | Por sprint |
| Cobertura de Código | Porcentaje de líneas de código ejecutadas por las pruebas | (Líneas ejecutadas / Líneas totales) × 100 | JaCoCo Report | Por build |
| Complejidad Ciclomática | Complejidad lógica de los métodos del servicio | Número de rutas independientes | SonarQube | Por release |
| Tiempo de Actualización | Latencia promedio de actualizaciones de stock | Promedio en milisegundos (ms) | APM / JMeter | Semanal |
| Disponibilidad del Endpoint | Porcentaje de disponibilidad del endpoint de inventario | (Tiempo uptime / Tiempo total) × 100 | Health checks | Continuo |

#### 3.3.4 Proceso 4: Gestión de Ventas y Confirmación

El proceso de gestión de ventas y confirmación formaliza la venta, confirma el pedido y garantiza la actualización definitiva del inventario y el estado de la transacción.

| Nombre de la Métrica | Descripción | Fórmula / Unidad | Fuente de Datos | Frecuencia de Medición |
|---------------------|-------------|------------------|-----------------|----------------------|
| Tasa de Defectos | Porcentaje de defectos encontrados en el módulo de ventas | (Defectos encontrados / Total casos de prueba) × 100 | Reportes de pruebas JUnit | Por sprint |
| Cobertura de Código | Porcentaje de líneas de código ejecutadas por las pruebas | (Líneas ejecutadas / Líneas totales) × 100 | JaCoCo Report | Por build |
| Tasa de Ventas Exitosas | Porcentaje de ventas que se completan exitosamente | (Ventas exitosas / Total ventas) × 100 | Logs de negocio | Diario |
| Tiempo de Confirmación | Tiempo promedio de confirmación de pedidos | Promedio en milisegundos (ms) | APM / JMeter | Semanal |
| Consistencia de Inventario | Porcentaje de transacciones que mantienen stock consistente | (Transacciones consistentes / Total transacciones) × 100 | Código de tests | Por build |

### 3.4 Creación de Métricas para la Calidad

La creación de métricas para la calidad sigue un proceso sistemático que garantiza su relevancia, medibilidad y utilidad para la toma de decisiones. A continuación se presentan las métricas específicas creadas para los procesos del proyecto.

#### 3.4.1 Proceso 1: Autenticación y Control de Acceso

| Nombre de la Métrica | Proceso de Negocio Asociado | Descripción del Indicador | Fórmula / Criterio de Medición | Interpretación Esperada |
|---------------------|---------------------------|-------------------------|-------------------------------|----------------------|
| Tiempo de Login | Autenticación y Control de Acceso | Medir el tiempo desde que el usuario envía credenciales hasta que recibe respuesta | Tiempo total / Número de logins | Debe ser < 2 segundos |
| Tasa de Login Exitoso | Autenticación y Control de Acceso | Porcentaje de intentos de login que resultan en autenticación exitosa | (Logins exitosos / Total intentos) × 100 | Superior al 95% |
| Tasa de Token Válido | Autenticación y Control de Acceso | Porcentaje de tokens JWT que pasan validación | (Tokens válidos / Total tokens verificados) × 100 | Superior al 99% |
| Tiempo de Respuesta de Seguridad | Autenticación y Control de Acceso | Tiempo promedio de verificación de permisos y roles | Tiempo medio en milisegundos | Menor a 200 ms |
| Disponibilidad del Servicio de Auth | Autenticación y Control de Acceso | Porcentaje de tiempo que el sistema de autenticación está operativo | (Tiempo uptime / Tiempo total) × 100 | Superior al 99.9% |

#### 3.4.2 Proceso 2: Gestión de Pedidos

| Nombre de la Métrica | Proceso de Negocio Asociado | Descripción del Indicador | Fórmula / Criterio de Medición | Interpretación Esperada |
|---------------------|---------------------------|-------------------------|-------------------------------|----------------------|
| Tiempo de Procesamiento de Órdenes | Gestión de Pedidos | Medir el tiempo desde que se crea una orden hasta que se procesa completamente | Tiempo total / Número de órdenes | Debe ser < 5 segundos |
| Exactitud de Órdenes | Gestión de Pedidos | Mide el % de pedidos entregados sin error | (Órdenes correctas / Total órdenes) % | Superior al 95% |
| Tasa de Respuesta del Sistema | Gestión de Pedidos | Tiempo promedio que tarda el sistema en responder al usuario | Tiempo medio en milisegundos | Menor a 1000 ms bajo carga esperada |
| Tasa de Órdenes Exitosas | Gestión de Pedidos | Porcentaje de órdenes que se completan exitosamente | (Órdenes exitosas / Total órdenes) × 100 | Superior al 98% |
| Tiempo de Confirmación | Gestión de Pedidos | Tiempo que tarda el sistema en confirmar una orden al cliente | Tiempo medio en segundos | Menor a 2 segundos |

#### 3.4.3 Proceso 3: Control de Inventario

| Nombre de la Métrica | Proceso de Negocio Asociado | Descripción del Indicador | Fórmula / Criterio de Medición | Interpretación Esperada |
|---------------------|---------------------------|-------------------------|-------------------------------|----------------------|
| Tiempo de Actualización de Inventario | Control de Inventario | Medir el tiempo desde que llega una solicitud de actualización de stock hasta que se procesa completamente | Tiempo total de operación / Número de actualizaciones | Debe ser < 2 segundos |
| Exactitud del Stock | Control de Inventario | Mide el % de registros de stock que reflejan la realidad | (Registros correctos / Total registros) % | Superior al 98% |
| Tasa de Disponibilidad de Productos | Control de Inventario | Porcentaje de productos disponibles para venta respecto al total | (Productos disponibles / Total productos) % | Superior al 95% |
| Tiempo de Búsqueda de Productos | Control de Inventario | Tiempo promedio de búsqueda de productos por código o nombre | Tiempo medio en milisegundos | Menor a 500 ms |
| Tasa de Errores en Transacciones | Control de Inventario | Porcentaje de transacciones de inventario que fallan | (Transacciones fallidas / Total transacciones) % | Menor al 1% |

#### 3.4.4 Proceso 4: Gestión de Ventas y Confirmación

| Nombre de la Métrica | Proceso de Negocio Asociado | Descripción del Indicador | Fórmula / Criterio de Medición | Interpretación Esperada |
|---------------------|---------------------------|-------------------------|-------------------------------|----------------------|
| Tiempo de Confirmación de Venta | Gestión de Ventas | Medir el tiempo desde que se confirma un pedido hasta que se registra la venta | Tiempo total / Número de ventas | Debe ser < 3 segundos |
| Tasa de Ventas Exitosas | Gestión de Ventas | Porcentaje de ventas que se completan sin errores | (Ventas exitosas / Total ventas) × 100 | Superior al 98% |
| Exactitud de Actualización de Inventario | Gestión de Ventas | Porcentaje de ventas que actualizan correctamente el inventario | (Actualizaciones correctas / Total ventas) × 100 | Superior al 99% |
| Tiempo de Generación de Comprobante | Gestión de Ventas | Tiempo promedio de generación de comprobante de venta | Tiempo medio en milisegundos | Menor a 1000 ms |
| Disponibilidad del Proceso | Gestión de Ventas | Porcentaje de tiempo que el proceso de ventas está operativo | (Tiempo uptime / Tiempo total) × 100 | Superior al 99.5% |

### 3.5 Modelos y Estándares Aplicables

El proyecto InventoryRX se fundamenta en los siguientes modelos y estándares internacionales de calidad:

**ISO/IEC 25010:2011** - Sistemas y software Calidad y Requisitos de Calidad (SQuaRE)

Este estándar define el modelo de calidad para productos de software, dividiendo las características en dos grupos:

- **Características internas y externas**: Funcionalidad, Fiabilidad, Eficiencia, Mantenibilidad, Portabilidad, Usabilidad, Seguridad, Compatibilidad
- **Características en uso**: Efectividad, Eficiencia, Satisfacción, Libertad de riesgo, Contexto de cobertura

**IEEE 829** - Estándar para Documentación de Pruebas de Software

Define la estructura y contenido de la documentación de pruebas, incluyendo:

- Plan de pruebas
- Especificación de diseño de pruebas
- Especificación de casos de prueba
- Informes de incidentes

**ISO/IEC/IEEE 29119** - Software Testing

Estándar internacional que define conceptos, procesos, técnicas y organización para pruebas de software.

### 3.6 Administración de la Calidad

La administración de la calidad del proyecto InventoryRX se estructura en tres niveles:

**Nivel estratégico**: Definición de políticas de calidad, objetivos globales y asignación de recursos. Este nivel es responsabilidad del líder del proyecto y establece las directrices generales.

**Nivel táctico**: Implementación de procesos de calidad, definición de métricas y umbrales, y supervisión del cumplimiento. Este nivel es responsabilidad del equipo de QA.

**Nivel operativo**: Ejecución de pruebas, análisis de resultados y generación de informes. Este nivel es responsabilidad de todos los desarrolladores.

### 3.7 Características de la Administración de la Calidad

| Característica | Descripción | Implementación en el Proyecto |
|----------------|-------------|-------------------------------|
| **Prevención** | Evitar defectos antes de que ocurran | Code reviews, análisis estático con SonarQube, pair programming |
| **Detección** | Identificar defectos durante el desarrollo | Suite de 85 pruebas, pruebas de integración con Testcontainers |
| **Medición** | Cuantificar la calidad del software | Métricas de cobertura, complejidad, defectos |
| **Mejora continua** | Iterar y optimizar los procesos | Retrospectivas sprint, refactoring sistemático |
| **Trazabilidad** | Relacionar requisitos con pruebas | Mapping en documentación y herramientas de gestión |

### 3.8 Atributos de Calidad

Los atributos de calidad para InventoryRX se han definido considerando las necesidades del negocio de droguería y los estándares internacionales:

| Atributo | Descripción | Prioridad |
|----------|-------------|-----------|
| **Funcionalidad** | El sistema debe realizar todas las operaciones de gestión de inventario, clientes, órdenes y proveedores | Crítica |
| **Rendimiento** | El sistema debe responder en menos de 500ms para el 95% de las solicitudes | Alta |
| **Seguridad** | El sistema debe proteger los datos mediante autenticación JWT y control de acceso | Crítica |
| **Fiabilidad** | El sistema debe mantener disponibilidad del 99.5% | Alta |
| **Usabilidad** | La API debe ser intuitiva y bien documentada | Media |
| **Mantenibilidad** | El código debe ser claro y extensible para futuras funcionalidades | Alta |
| **Portabilidad** | El sistema debe desplegarse fácilmente en diferentes entornos | Media |

### 3.9 Atributos Referenciados en la ISO/IEC 25010

La norma ISO/IEC 25010 define ocho características de calidad que se mapean a los requisitos del proyecto:

| Característica ISO 25010 | Subcaracterísticas | Aplicación en InventoryRX |
|-------------------------|---------------------|---------------------------|
| **Functional Suitability** | Functional completeness, Functional correctness, Functional appropriateness | Todas las operaciones CRUD, gestión de inventario, órdenes |
| **Performance Efficiency** | Time behavior, Resource utilization, Capacity | Tiempos de respuesta < 500ms, uso eficiente de memoria |
| **Compatibility** | Coexistence, Interoperabilidad | Integración con múltiples clientes API |
| **Usability** | Appropriateness recognizability, Learnability, Operability, User error protection, User interface aesthetics, Accessibility | API REST bien diseñada, mensajes de error claros |
| **Reliability** | Maturity, Availability, Fault tolerance, Recoverability | Manejo robusto de excepciones, recuperación ante errores |
| **Security** | Confidentiality, Integrity, Non-repudiation, Accountability, Authenticity | Autenticación JWT, control de acceso por roles, auditoría |
| **Maintainability** | Modularity, Reusability, Analysability, Modifiability, Testability | Arquitectura hexagonal, código modular, pruebas automatizadas |
| **Portability** | Adaptability, Installability, Replaceability | Despliegue Docker, perfiles configurables |

---

## 4. REQUISITOS NO FUNCIONALES DEL SISTEMA (RNF)

### 4.1 Característica de Calidad (ISO/IEC 25010): SEGURIDAD

La seguridad es una característica crítica para el sistema de gestión de droguería, considerando la sensibilidad de los datos de inventario, clientes y transacciones comerciales.

#### 4.1.1 RNF: 01 - Autenticación JWT - Proceso: Autenticación

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | El sistema debe implementar autenticación basada en tokens JWT con expiración configurable |
| **Criterio de aceptación** | Los usuarios deben poder autenticarse mediante username/password y recibir un token JWT válido |
| **Validación existente** | 11 SecurityIntegrationTest cubriendo flujos de autenticación |

#### 4.1.2 RNF: 02 - Control de Acceso Basado en Roles - Proceso: Autorización

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | El sistema debe implementar RBAC con roles diferenciados: ADMIN, MANAGER, SALES, WAREHOUSE, USER |
| **Criterio de aceptación** | Cada rol debe tener acceso únicamente a los endpoints autorizados |
| **Validación existente** | SecurityIntegrationTest verificando restricciones por rol |

#### 4.1.3 RNF: 03 - Protección de Datos en Tránsito - Proceso: Seguridad

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | Todas las comunicaciones deben utilizar HTTPS en producción |
| **Criterio de aceptación** | Certificados SSL válidos y configuración de headers de seguridad |
| **Validación existente** | Configuración en render.yaml y Dockerfile |

#### 4.1.4 RNF: 04 - Validación de Entrada - Proceso: Seguridad

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | El sistema debe validar todos los datos de entrada para prevenir inyección y ataques malformed |
| **Criterio de aceptación** | Spring Validation con anotaciones en DTOs |
| **Validación existente** | Unit tests de servicios con casos de validación |

#### 4.1.5 RNF: 05 - Auditoría de Accesos - Proceso: Seguridad

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | El sistema debe registrar intentos de acceso y operaciones sensibles |
| **Criterio de aceptación** | Logs de autenticación y operaciones críticas |
| **Validación existente** | JwtAuthenticationFilter con logging |

### 4.2 Característica de Calidad (ISO/IEC 25010): RENDIMIENTO

El rendimiento es esencial para garantizar una experiencia de usuario fluida, especialmente durante picos de operación en la droguería.

#### 4.2.1 RNF: 01 - Tiempo de Respuesta - Proceso: Gestión de Productos

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | El tiempo de respuesta para consultas de productos debe ser menor a 500ms (P95) |
| **Criterio de aceptación** | Benchmarks de rendimiento con JMeter o k6 |
| **Validación existente** | Tests de integración con PostgreSQL real |

#### 4.2.2 RNF: 02 - Throughput - Proceso: Gestión de Órdenes

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | El sistema debe soportar al menos 100 solicitudes por segundo |
| **Criterio de aceptación** | Pruebas de carga con múltiples usuarios concurrentes |
| **Validación recomendada** | JMeter load tests |

#### 4.2.3 RNF: 03 - Uso de Memoria - Proceso: Gestión de Productos

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | El consumo de memoria heap no debe exceder 512MB en condiciones normales |
| **Criterio de aceptación** | Monitoreo con Spring Boot Actuator y APM |
| **Validación recomendada** | Integración con Prometheus/Grafana |

#### 4.2.4 RNF: 04 - Conexiones de Base de Datos - Proceso: Persistencia

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | Pool de conexiones configurado con máximo 20 conexiones simultáneas |
| **Criterio de aceptación** | Configuración en application.yml con HikariCP |
| **Validación existente** | Revisión de configuración |

#### 4.2.5 RNF: 05 - Tiempo de Inicio - Proceso: Despliegue

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | El tiempo de inicio de la aplicación no debe exceder 30 segundos |
| **Criterio de aceptación** | Medido en entorno de producción |
| **Validación existente** | Health check en /api/actuator/health |

### 4.3 Característica de Calidad (ISO/IEC 25010): FIABILIDAD

La fiabilidad garantiza que el sistema funcione correctamente bajo condiciones normales y de estrés.

#### 4.3.1 RNF: 01 - Manejo de Errores - Proceso: Gestión de Productos

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | El sistema debe manejar errores gracefully sin revelar información sensible |
| **Criterio de aceptación** | GlobalExceptionHandler con respuestas consistentes |
| **Validación existente** | Unit tests verificando excepciones |

#### 4.3.2 RNF: 02 - Recuperación ante Fallos - Proceso: Persistencia

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | El sistema debe mantener la integridad de datos ante fallos de conexión |
| **Criterio de aceptación** | Transacciones con rollback automático |
| **Validación existente** | Spring Data JPA con gestión de transacciones |

#### 4.3.3 RNF: 03 - Disponibilidad - Proceso: Despliegue

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | El sistema debe mantener disponibilidad del 99.5% |
| **Criterio de aceptación** | SLA medido mensualmente |
| **Validación recomendada** | Monitorización con APM |

#### 4.3.4 RNF: 04 - Consistencia de Datos - Proceso: Gestión de Órdenes

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | Las operaciones de inventario deben mantener consistencia (stock no negativo) |
| **Criterio de aceptación** | Validaciones de negocio en servicios |
| **Validación existente** | Unit tests de ProductService |

#### 4.3.5 RNF: 05 - Robustez - Proceso: Gestión de Clientes

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | El sistema debe validar todos los datos de entrada antes de procesarlos |
| **Criterio de aceptación** | Validaciones en DTOs y servicios |
| **Validación existente** | Pruebas con datos inválidos |

### 4.4 Característica de Calidad (ISO/IEC 25010): USABILIDAD

La usabilidad garantiza que la API sea fácil de usar tanto para desarrolladores como para operadores del sistema.

#### 4.4.1 RNF: 01 - Documentación de API - Proceso: Desarrollo

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | La API debe estar documentada con OpenAPI/Swagger |
| **Criterio de aceptación** | Endpoint /swagger-ui.html accesible |
| **Validación existente** | SpringDoc OpenAPI configurado |

#### 4.4.2 RNF: 02 - Mensajes de Error Claros - Proceso: Seguridad

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | Los mensajes de error deben ser informativos pero no revelar detalles sensibles |
| **Criterio de aceptación** | GlobalExceptionHandler con respuestas estandarizadas |
| **Validación existente** | Tests de integración verificando respuestas |

#### 4.4.3 RNF: 03 - Consistencia de Respuestas - Proceso: Desarrollo

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | Todas las respuestas de la API deben seguir un formato consistente |
| **Criterio de aceptación** | DTOs bien definidos y mapeados |
| **Validación existente** | Unit tests de mapeo |

#### 4.4.4 RNF: 04 - Versionamiento de API - Proceso: Desarrollo

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | La API debe soportar versionamiento para compatibilidad hacia atrás |
| **Criterio de aceptación** | URL versioning (/api/v1/...) |
| **Validación recomendada** | Implementación de estrategia de versionamiento |

#### 4.4.5 RNF: 05 - Facilidad de Integración - Proceso: Desarrollo

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | La API debe ser fácilmente integrable con sistemas externos |
| **Criterio de aceptación** | Estándar REST, formatos JSON |
| **Validación existente** | Estructura de endpoints RESTful |

### 4.5 Característica de Calidad (ISO/IEC 25010): MANTENIBILIDAD

La mantenibilidad asegura que el código pueda ser modificado de manera eficiente y segura.

#### 4.5.1 RNF: 01 - Complejidad del Código - Proceso: Desarrollo

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | La complejidad ciclomática no debe exceder 10 por método |
| **Criterio de aceptación** | Análisis con SonarQube |
| **Validación recomendada** | Integración con CI/CD |

#### 4.5.2 RNF: 02 - Cobertura de Pruebas - Proceso: Testing

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | La cobertura de pruebas debe ser al menos 80% |
| **Criterio de aceptación** | Reportes JaCoCo |
| **Validación actual** | 85 tests implementados |

#### 4.5.3 RNF: 03 - Calidad del Código - Proceso: Desarrollo

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | El código debe pasar todas las reglas de SonarQube con rating A |
| **Criterio de aceptación** | Quality Gate en CI/CD |
| **Validación recomendada** | SonarQube configurado en pipeline |

#### 4.5.4 RNF: 04 - Modularidad - Proceso: Arquitectura

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | El código debe seguir arquitectura hexagonal con separación de capas |
| **Criterio de aceptación** | domain/application/infrastructure separados |
| **Validación existente** | Estructura del proyecto validada |

#### 4.5.5 RNF: 05 - Deuda Técnica - Proceso: Desarrollo

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | La deuda técnica no debe exceder el 5% del tiempo de desarrollo |
| **Criterio de aceptación** | Revisión de code smells en SonarQube |
| **Validación recomendada** | Revisión periódica de métricas |

### 4.6 Característica de Calidad (ISO/IEC 25010): PORTABILIDAD

La portabilidad facilita el despliegue en diferentes entornos.

#### 4.6.1 RNF: 01 - Contenedorización - Proceso: Despliegue

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | La aplicación debe ejecutarse en contenedores Docker |
| **Criterio de aceptación** | Dockerfile multistage funcional |
| **Validación existente** | Dockerfile en proyecto |

#### 4.6.2 RNF: 02 - Perfiles de Configuración - Proceso: Configuración

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | La aplicación debe soportar perfiles dev, prod |
| **Criterio de aceptación** | application-dev.yml y application-prod.yml |
| **Validación existente** | Configuración en resources/ |

#### 4.6.3 RNF: 03 - Base de Datos Portátil - Proceso: Persistencia

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | La aplicación debe funcionar con H2 en desarrollo y PostgreSQL en producción |
| **Criterio de aceptación** | Profiles configurados correctamente |
| **Validación existente** | docker-compose para desarrollo local |

### 4.7 Característica de Calidad (ISO/IEC 25010): COMPATIBILIDAD

La compatibilidad asegura que el sistema pueda coexistir e interoperar con otros sistemas.

#### 4.7.1 RNF: 01 - Formato de Datos - Proceso: Desarrollo

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | La API debe utilizar formatos estándar (JSON) |
| **Criterio de aceptación** | Todos los endpoints responden en JSON |
| **Validación existente** | Spring MVC con Jackson |

#### 4.7.2 RNF: 02 - Interoperabilidad - Proceso: Desarrollo

| Aspecto | Descripción |
|---------|-------------|
| **Requisito** | La API debe ser accesible desde cualquier cliente HTTP |
| **Criterio de aceptación** | Estándar RESTful |
| **Validación existente** | Arquitectura REST |

---

## 5. EVIDENCIAS MÉTRICAS

### 5.1 Estado Actual de Métricas

El proyecto actualmente cuenta con las siguientes métricas registradas:

| Categoría | Métrica | Valor Actual | Objetivo | Estado |
|-----------|---------|--------------|----------|--------|
| **Pruebas** | Total de tests | 85 | - | ✅ |
| **Pruebas** | Unit tests | 58 | - | ✅ |
| **Pruebas** | Integration tests | 27 | - | ✅ |
| **Pruebas** | Tests de seguridad | 11 | - | ✅ |
| **Pruebas** | Cobertura estimada | ~70% | ≥ 80% | 🔶 |
| **Seguridad** | Autenticación JWT | Implementado | - | ✅ |
| **Seguridad** | Roles definidos | 5 roles | - | ✅ |
| **Rendimiento** | Tiempo de respuesta | Por medir | < 500ms | ⬜ |
| **Rendimiento** | Throughput | Por medir | ≥ 100 req/s | ⬜ |
| **Calidad** | Code smells | Por medir | < 10 | ⬜ |
| **Calidad** | Vulnerabilidades | Por medir | 0 | ⬜ |

### 5.2 Plan de Recolección de Métricas

Para completar las métricas pendientes, se implementará:

1. **JaCoCo**: Configuración de plugin Maven para medición de cobertura
2. **SonarQube**: Integración para análisis de código estático
3. **JMeter/k6**: Pruebas de rendimiento para tiempos de respuesta
4. **OWASP**: Escaneo de vulnerabilidades

### 5.3 Tablero de Métricas (Dashboard)

| Métrica | Frecuencia de Medición | Responsable | Herramienta |
|---------|----------------------|-------------|-------------|
| Cobertura de pruebas | Por build | Desarrollador | JaCoCo |
| Code smells | Por build | Desarrollador | SonarQube |
| Tiempo de respuesta | Semanal | QA | JMeter |
| Vulnerabilidades | Por release | DevOps | OWASP |
| Disponibilidad | Continuo | DevOps | APM |

---

*Documento elaborado para el Proyecto Droguería Bellavista - InventoryRX*
*Versión 1.0 - 2026*
