# Proyecto: Droguería Bellavista (Invetoryrx)

## Contexto Técnico Crítico

- **Stack:** Java 21, Spring Boot 3.2.2, Maven 3.8+, Docker Compose
- **Arquitectura:** Hexagonal (Ports & Adapters)
    - Capas: `domain`, `application`, `infrastructure`, `controller`
- **Persistencia:**
    - PostgreSQL 15 (Producción/Railway)
    - H2 (Dev)
    - Redis 7 (Cache/Blacklist JWT)
- **Integraciones:**
    - Stripe 24.0.0
    - SpringDoc OpenAPI 2.2.0
    - JJWT 0.12.3
- **Testing:** JUnit 5, Testcontainers 1.19.0 (PostgreSQL/Redis)
- **Infraestructura:**
    - AWS EC2 (Ubuntu 24.04)
    - Contenedores: `drogueria_app`, `drogueria_db`, `drogueria_redis`
- **Email:** AWS SES
    - Host: `email-smtp.${REGION}.amazonaws.com`

---

## Bug Actual

- **Problema:** Correos devueltos (bounce) en EC2
- **Acción Prioritaria:** Revisar IAM Role y SES Sandbox

---

## Reglas de Comportamiento (Token Efficiency)

- **Estilo:** `CAVEMAN` → extremadamente breve, sin saludos ni rellenos
- **Estructura PAC2026:**
    - 30% Inicial → Reglas críticas y formato de salida
    - 55% Medio → Lógica, diagnósticos y contexto dinámico
    - 15% Final → Criterios de éxito y validación
- **Formato XML:**
    - Usa siempre `<thought>`, `<code>` (solo diffs) y `<summary>`
- **Cero Redundancia:** No repetir código existente, solo cambios necesarios
- **Convención Java:**
    - Sin Lombok
    - Inyección por constructor
    - Java 21 `record` para DTOs

---

## Comandos Frecuentes

- **Build:**
  ```bash
  mvn clean package -DskipTests


- **Docker:**
```bash
docker compose down && docker compose up -d
Logs:
```
```bash
docker compose logs -f drogueria_app
```
- **Tests:**
```bash
mvn verify
```


<thought>
Reglas críticas: estilo CAVEMAN, cero redundancia, formato XML con <thought>, <code>, <summary>.  
Convención Java: sin Lombok, inyección por constructor, records para DTOs.  
Bug actual: SES bounce en EC2 → revisar IAM Role y Sandbox.
</thought>

<code>
<!-- Ajuste IAM Role para SES -->
diff --git a/infrastructure/aws/IamPolicy.json b/infrastructure/aws/IamPolicy.json
{
  "Effect": "Allow",
  "Action": [
    "ses:SendEmail",
    "ses:SendRawEmail"
  ],
  "Resource": "*"
}
</code>

<thought>
Diagnóstico:  
- Bounce indica sandbox activo o permisos insuficientes.  
- Validar dominio y correos verificados en SES.  
- Revisar logs en `drogueria_app` para trazas SMTP.  
- Confirmar que EC2 tiene role adjunto con política SES.  
- Redis y PostgreSQL no relacionados al bug.  
</thought>

<summary>
Éxito = correos entregados sin bounce.  
Validación = IAM Role con permisos SES, dominio verificado, salida limpia en logs.  
</summary>
