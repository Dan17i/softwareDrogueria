# PROGRESS — Droguería Bellavista

> Leer ESTE archivo primero. No re-explorar el proyecto salvo que falte info.
> Actualizar al terminar cada tarea: mover ítem a "Hecho", añadir línea en "Bitácora".

## Mapa rápido
- Código: `src/main/java/com/drogueria/bellavista/{domain,application,infrastructure,controller,config,exception}`
- Config: `src/main/resources/application{,-dev,-prod}.yml`, `schema.sql`, `data.sql`
- CI/CD: `.github/workflows/{deploy,sonarcloud}.yml` (push main → GHCR → SSH EC2)
- Docs: `docs/`, requests: `http/`
- Tests: 40 archivos / 156 clases main (snapshot 2026-10-06)

## Hecho
- Seguridad: cierre accesos anónimos orders/customers/notifications, `dev-create-admin` solo `@Profile("dev")`
- Bugs negocio: órdenes, recepciones, clientes, stock concurrente
- Perf: N+1 eliminado (alertas inventario, órdenes, recepciones)
- Lombok fuera de services/controllers/config/adapters; DTOs → `record`
- Grafana + Prometheus + actuator; CI/CD GHCR → EC2

## Pendiente (por prioridad)
### P0 — Bug SES bounce en EC2
- [x] (config) prod/compose sin defaults de mail (fail-fast); `.env.example` con SES. FALTA operativo ↓
- [ ] `MAIL_FROM` → verificar identidad/dominio en SES (DKIM/SPF) o usar remitente verificado
- [ ] `MAIL_HOST` default `smtp.gmail.com` → `email-smtp.<REGION>.amazonaws.com:587` con credenciales SMTP de SES (IAM Role NO sirve para SMTP; alternativa: AWS SDK v2 SES)
- [ ] Salir de SES Sandbox (production access)
- [ ] Manejo bounces/complaints vía SNS
- [ ] Poner MAIL_HOST/PORT/USERNAME/PASSWORD/FROM de SES en `.env` de EC2 ANTES del próximo deploy (compose ahora exige las 4, si no, no levanta)
- [x] `@Async` ya estaba restaurado en `sendPasswordResetEmail`
### P1 — Seguridad prod
- [x] Default de `app.jwt.secret` quitado de `application.yml` (dev/test tienen el suyo)
- [ ] ROTAR `APP_JWT_SECRET` en EC2 (el viejo está en el historial git)
- [x] `SecurityConfig`: actuator/h2/swagger solo dev/test o ROLE_ADMIN; `health`/`info` públicos; `httpBasic` y `frameOptions.disable` solo dev/test
- [ ] Verificar en EC2: Prometheus/Grafana scrapeaban `/actuator/prometheus` sin auth (prod solo expone `health`; si se usa, darle auth)
- [ ] Rate limit `/auth/login`, `/auth/forgot-password`
### P2 — Datos
- [ ] `ddl-auto: update` en prod → Flyway
- [ ] No ejecutar `data.sql` en prod
### P3 — Consistencia
- [x] Lombok eliminado: 22 clases (domain/model + entities) con getters/setters/equals/hashCode/toString/Builder manuales, 2 mappers con constructor, `pom.xml` limpio. Quitados 4 tests `canEqual` (método solo de Lombok)
### P4 — Calidad/CI
- [ ] `deploy.yml` usa `-DskipTests` → job `mvn verify` antes de build
- [ ] Tag imagen con SHA (no solo `latest`)
- [ ] Más tests: Order/Product/Supplier controllers, Stripe, email (GreenMail); revisar `AuthServiceTest` duplicado (application vs domain)
### P5 — Operación
- [ ] Alertas Grafana, backups PostgreSQL, limpiar `infra/docker-compose.yml.save` y `render.yaml`

## Cómo correr tests (Windows + Docker Desktop 29)
`JAVA_TOOL_OPTIONS="-Dapi.version=1.44" mvn -o verify` — Testcontainers 1.19 usa API Docker 1.32 y Docker Desktop 29 exige >=1.44 (sin esto: "Could not find a valid Docker environment"). Pendiente: subir Testcontainers (>=1.20) o fijar `api.version` en surefire.

## Bitácora (más reciente arriba)
- 2026-10-06 — Integración corrida con Docker: 515 tests OK. Fix: SecurityConfig devolvía 403 en vez de 401 sin token (al quitar httpBasic en prod) → `HttpStatusEntryPoint(401)`. Sin commit.
- 2026-10-06 — P0 config mail (prod/compose/.env.example) + P1 JWT secret y SecurityConfig por perfil. Compila; 483 tests OK, 5 integration fallan solo por Docker no disponible local (Testcontainers). Sin commit.
- 2026-10-06 — Revisión general; creado PROGRESS.md; definida ruta P0–P5. Sin cambios de código.
- (previo, git) 38a16f9 DTOs→record · df23673 Lombok fuera de services/ctrl/config/adapters · 57a9f9e N+1 · e49d4c3 bugs negocio · e5989c9 seguridad

## Criterios de éxito globales
- Correo reset llega sin bounce desde EC2
- Sin secretos en repo; actuator/swagger cerrados en prod
- `mvn verify` verde en CI antes de deploy
- Cero `import lombok`
