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
- [x] Rate limit por IP (`RateLimitFilter`, memoria, 1 instancia): login 10/min, forgot-password 5/15min, register y reset-password 10/15min → 429 + `Retry-After`. Config `app.rate-limit.{enabled,login-max,forgot-password-max,register-max}`; desactivado en tests. Usa `getRemoteAddr()`: si hay proxy/ALB delante, configurar `server.forward-headers-strategy` o todas las IPs serán la del proxy
### P2 — Datos
- [x] Flyway 9 (Boot-managed) en prod: `db/migration/V1__baseline_schema.sql` (generado por Hibernate, 11 tablas), `baseline-on-migrate` + `baseline-version: 1` (BD existente se marca V1 sin re-ejecutar), `ddl-auto: none`. Flyway desactivado en dev/test (siguen con ddl-auto)
- [x] `data.sql` no corre en prod (`sql.init.mode: never`, ya estaba)
- [x] Tests: `FlywayMigrationTest` (BD vacía y BD existente) + `SchemaMatchesEntitiesTest` (esquema Flyway pasa `ddl-auto=validate`; falla si cambias una entidad sin migración)
- [ ] Antes del 1er deploy con Flyway: respaldar BD prod (`pg_dump`) y comparar esquema real vs V1 (`pg_dump -s`); luego cambiar prod a `ddl-auto: validate`
- [ ] REGLA: todo cambio de entidad → nuevo `V{n}__desc.sql` (nunca editar V1)
- [x] Limpieza: `application-dev.yml` sin secretos (usa `DEV_DB_PASSWORD`, `MAIL_USERNAME/PASSWORD/FROM`, `APP_JWT_SECRET` con default solo-dev); `bin/` (580 archivos), `infra/.env.save`, `infra/docker-compose.yml.save` y `.claude/settings.local.json` des-versionados (siguen en disco); `.gitignore` cubre `.env.*`, `*.save`, `bin/`, `.kilo/`
- [ ] **URGENTE — ROTAR credenciales filtradas en el historial de git** (`infra/.env.save` y `application-dev.yml`): contraseña BD prod (`DB_PASSWORD`, cambiar también en Postgres), `APP_JWT_SECRET` de prod, contraseña de aplicación de Gmail (revocar en la cuenta Google), contraseña BD dev. Opcional: purgar historial con `git filter-repo` (reescribe historia + force-push: coordinar)
### P3 — Consistencia
- [x] Lombok eliminado: 22 clases (domain/model + entities) con getters/setters/equals/hashCode/toString/Builder manuales, 2 mappers con constructor, `pom.xml` limpio. Quitados 4 tests `canEqual` (método solo de Lombok)
### P4 — Calidad/CI
- [x] `deploy.yml`: job `test` (`mvn -B verify`, con Testcontainers) bloquea el job `build-and-deploy` (`needs: test`)
- [x] Imagen publicada con tags `latest` y `${{ github.sha }}` (rollback: cambiar tag en compose del EC2)
- [x] `AuthServiceTest` duplicado eliminado (domain/service era subconjunto del de application/service)
- [x] Surefire fija `api.version=1.44` (Docker 29); Testcontainers 1.19.0 → 1.21.4
- [x] Tests de controllers Customer/Order/Product/Supplier (incluyen guardas de `@PreAuthorize`). EmailService y Payment ya tenían tests; Stripe no tiene controller
- [ ] Tests de mappers con baja cobertura (`GoodsReceiptMapper`, `GoodsReceiptUseCaseMapper`, `SupplierMapper`, `SupplierUseCaseMapper`) y adapters (`GoodsReceiptRepositoryAdapter`, `OrderRepositoryAdapter`)
### P5 — Operación
- [ ] Alertas Grafana, backups PostgreSQL, limpiar `infra/docker-compose.yml.save` y `render.yaml`

## Cómo correr tests
`mvn verify` (necesita Docker Desktop activo para Testcontainers).

## SonarCloud (proyecto público: https://sonarcloud.io/project/overview?id=Dan17i_softwareDrogueria)
- API sin token: `curl -s "https://sonarcloud.io/api/issues/search?componentKeys=Dan17i_softwareDrogueria&types=BUG&resolved=false&ps=100&additionalFields=_all"` y `.../api/qualitygates/project_status?projectKey=Dan17i_softwareDrogueria`
- Quality gate (new code): coverage >=80, duplicación <=3, reliability A. Tras quitar Lombok falló por boilerplate escrito a mano.
- [x] 12 bugs de Reliability corregidos: 11x S2259 (flujos null: mappers devuelven null y el servicio dereferencia → `Objects.requireNonNull` en services/adapters; `GoodsReceipt.getItems()` null-safe en mapper) y S2445 (`RateLimitFilter` ya no sincroniza sobre un parámetro)
- [x] `PojoContractTest` cubre getters/setters/ctor/Builder/equals/hashCode/toString de 22 clases; `pom.xml`: `sonar.cpd.exclusions` (dto, model, persistence) y `sonar.coverage.exclusions` (dto)
- [x] Gate tras 1er push: coverage 70.3% (faltaban GoodsReceiptRepositoryAdapter, GoodsReceiptUseCaseMapper, RateLimitFilter) → tests añadidos; 2 issues de PojoContractTest (S5863, S1872) corregidos
- [ ] Verificar el gate tras el siguiente push

## Bitácora (más reciente arriba)
- 2026-10-06 — Sonar 2ª vuelta: cobertura new code 70.3% → tests de adapter/mapper/RateLimitFilter (Window, limpieza), PojoContractTest sin issues. 581 tests OK. Sin commit.
- 2026-10-06 — Sonar: 12 bugs corregidos, PojoContractTest, exclusiones cpd/coverage. 575 tests OK. Sin commit.
- 2026-10-06 — Limpieza de secretos y archivos basura versionados (ver P2/Limpieza). Se detectó `infra/.env.save` con credenciales de prod en el historial. Sin commit.
- 2026-10-06 — P1 rate limit auth: RateLimitFilter + tests unit/integración. `mvn verify` OK. Sin commit.
- 2026-10-06 — P2 Flyway: V1 baseline + config prod + 3 tests. `mvn verify`: 546 OK. Sin commit.
- 2026-10-06 — P4 tests: +4 ControllerTest (Customer, Order, Product, Supplier). `mvn verify`: 543 OK. Sin commit.
- 2026-10-06 — P4 CI: job `test` antes del deploy, tags SHA, Testcontainers 1.21.4 + surefire api.version, test duplicado eliminado. `mvn verify`: 510 OK. Sin commit.
- 2026-10-06 — Integración corrida con Docker: 515 tests OK. Fix: SecurityConfig devolvía 403 en vez de 401 sin token (al quitar httpBasic en prod) → `HttpStatusEntryPoint(401)`. Sin commit.
- 2026-10-06 — P0 config mail (prod/compose/.env.example) + P1 JWT secret y SecurityConfig por perfil. Compila; 483 tests OK, 5 integration fallan solo por Docker no disponible local (Testcontainers). Sin commit.
- 2026-10-06 — Revisión general; creado PROGRESS.md; definida ruta P0–P5. Sin cambios de código.
- (previo, git) 38a16f9 DTOs→record · df23673 Lombok fuera de services/ctrl/config/adapters · 57a9f9e N+1 · e49d4c3 bugs negocio · e5989c9 seguridad

## Criterios de éxito globales
- Correo reset llega sin bounce desde EC2
- Sin secretos en repo; actuator/swagger cerrados en prod
- `mvn verify` verde en CI antes de deploy
- Cero `import lombok`
