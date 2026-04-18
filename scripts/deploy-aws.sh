#!/usr/bin/env bash
# =============================================================================
# deploy-aws.sh — Despliegue inicial en EC2 Ubuntu 24.04 (t2.micro)
# Uso: bash deploy-aws.sh
# =============================================================================
set -euo pipefail

# ------------------------------------------------------------------------------
# Variables globales
# ------------------------------------------------------------------------------
REPO_URL="https://github.com/Dan17i/softwareDrogueria.git"
APP_DIR="$HOME/softwareDrogueria"
PROD_YML="$APP_DIR/src/main/resources/application-prod.yml"

# ------------------------------------------------------------------------------
# Helpers de salida
# ------------------------------------------------------------------------------
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'
CYAN='\033[0;36m'; BOLD='\033[1m'; NC='\033[0m'

info()    { echo -e "${CYAN}[INFO]${NC}  $*"; }
success() { echo -e "${GREEN}[OK]${NC}    $*"; }
warn()    { echo -e "${YELLOW}[WARN]${NC}  $*"; }
die()     { echo -e "${RED}[ERROR]${NC} $*" >&2; exit 1; }
step()    { echo -e "\n${BOLD}==> $*${NC}"; }

# ------------------------------------------------------------------------------
# 1. Swap — crítico en t2.micro para que el build Maven no mate la instancia
# ------------------------------------------------------------------------------
setup_swap() {
  step "Configurando swap"

  if swapon --show | grep -q /swapfile 2>/dev/null; then
    success "Swap ya está activo, se omite."
    return
  fi

  info "Creando swapfile de 1 GB..."
  sudo fallocate -l 1G /swapfile
  sudo chmod 600 /swapfile
  sudo mkswap /swapfile
  sudo swapon /swapfile
  # Persistir tras reinicios
  grep -q '/swapfile' /etc/fstab || echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab > /dev/null
  success "Swap de 1 GB activo."
}

# ------------------------------------------------------------------------------
# 2. Docker + Docker Compose plugin (método oficial, idempotente)
# ------------------------------------------------------------------------------
install_docker() {
  step "Instalando Docker"

  if command -v docker &>/dev/null; then
    success "Docker $(docker --version | awk '{print $3}' | tr -d ',') ya instalado, se omite."
    return
  fi

  info "Actualizando paquetes..."
  sudo apt-get update -qq

  info "Instalando dependencias..."
  sudo apt-get install -y -qq ca-certificates curl

  info "Agregando repositorio oficial de Docker..."
  sudo install -m 0755 -d /etc/apt/keyrings
  sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg \
    -o /etc/apt/keyrings/docker.asc
  sudo chmod a+r /etc/apt/keyrings/docker.asc

  echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] \
https://download.docker.com/linux/ubuntu \
$(. /etc/os-release && echo "$VERSION_CODENAME") stable" \
    | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

  sudo apt-get update -qq
  sudo apt-get install -y -qq \
    docker-ce docker-ce-cli containerd.io \
    docker-buildx-plugin docker-compose-plugin

  # Permitir usar docker sin sudo en la sesión actual
  sudo usermod -aG docker "$USER"
  info "Usuario '$USER' agregado al grupo docker."
  warn "NOTA: Para usar 'docker' sin sudo en futuras sesiones SSH, cierra y vuelve a conectarte."

  sudo systemctl enable --now docker
  success "Docker $(docker --version | awk '{print $3}' | tr -d ',') instalado."
}

# ------------------------------------------------------------------------------
# 3. Clonar o actualizar el repositorio
# ------------------------------------------------------------------------------
clone_repo() {
  step "Clonando repositorio"

  if [ -d "$APP_DIR/.git" ]; then
    info "Repositorio ya existe, actualizando con git pull..."
    git -C "$APP_DIR" pull --ff-only
    success "Repositorio actualizado."
  else
    info "Clonando $REPO_URL..."
    git clone "$REPO_URL" "$APP_DIR"
    success "Repositorio clonado en $APP_DIR."
  fi
}

# ------------------------------------------------------------------------------
# 4. application-prod.yml — ignorado en .gitignore, hay que crearlo en el servidor
# ------------------------------------------------------------------------------
create_prod_config() {
  step "Creando application-prod.yml"

  if [ -f "$PROD_YML" ]; then
    success "application-prod.yml ya existe, se omite."
    return
  fi

  mkdir -p "$(dirname "$PROD_YML")"

  cat > "$PROD_YML" << 'EOF'
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 3
      minimum-idle: 1
      connection-timeout: 30000

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    open-in-view: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

  sql:
    init:
      mode: never

  mail:
    host: ${MAIL_HOST:smtp.gmail.com}
    port: ${MAIL_PORT:587}
    username: ${MAIL_USERNAME:noreply@bellavista.com}
    password: ${MAIL_PASSWORD:dummy}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          connectiontimeout: 5000
          timeout: 5000
          writetimeout: 5000

server:
  port: ${PORT:8080}
  servlet:
    context-path: /api

app:
  jwt:
    secret: ${APP_JWT_SECRET}
    expiration: 86400000
  mail:
    from: ${MAIL_FROM:noreply@bellavista.com}
  frontend:
    url: ${FRONTEND_URL:https://drogueria-bellavista.netlify.app}

logging:
  level:
    root: WARN
    com.drogueria.bellavista: INFO

management:
  endpoints:
    web:
      exposure:
        include: health
  endpoint:
    health:
      show-details: never
      probes:
        enabled: true
  health:
    db:
      enabled: false
    mail:
      enabled: false
EOF

  success "application-prod.yml creado."
}

# ------------------------------------------------------------------------------
# 5. Archivo .env con los secretos (interactivo, idempotente)
# ------------------------------------------------------------------------------
create_env() {
  step "Configurando variables de entorno (.env)"

  ENV_FILE="$APP_DIR/.env"

  if [ -f "$ENV_FILE" ]; then
    warn ".env ya existe en $ENV_FILE"
    read -r -p "    ¿Deseas sobreescribirlo? [s/N]: " overwrite
    if [[ ! "$overwrite" =~ ^[sS]$ ]]; then
      success "Se conserva el .env existente."
      return
    fi
  fi

  echo ""
  info "Ingresa los valores para el archivo .env."
  info "Deja en blanco los campos opcionales para usar el valor por defecto."
  echo ""

  # DB_USER
  read -r -p "  DB_USER [drogueria_user]: " db_user
  db_user="${db_user:-drogueria_user}"

  # DB_PASSWORD
  while true; do
    read -r -s -p "  DB_PASSWORD (obligatorio): " db_password; echo ""
    [ -n "$db_password" ] && break
    warn "  DB_PASSWORD no puede estar vacío."
  done

  # APP_JWT_SECRET
  echo ""
  info "  Tip: genera el JWT secret con:  openssl rand -hex 32"
  read -r -p "  APP_JWT_SECRET (obligatorio, min 32 chars): " jwt_secret
  while [ ${#jwt_secret} -lt 32 ]; do
    warn "  El secret debe tener al menos 32 caracteres."
    read -r -p "  APP_JWT_SECRET: " jwt_secret
  done

  # Email (opcional)
  echo ""
  info "  Configuración de email (opcional — deja vacío para desactivar envíos):"
  read -r -p "  MAIL_USERNAME [vacío]: " mail_user
  read -r -s -p "  MAIL_PASSWORD [vacío]: " mail_pass; echo ""
  read -r -p "  MAIL_FROM [noreply@bellavista.com]: " mail_from
  mail_from="${mail_from:-noreply@bellavista.com}"

  # Frontend URL
  read -r -p "  FRONTEND_URL [https://drogueria-bellavista.netlify.app]: " frontend_url
  frontend_url="${frontend_url:-https://drogueria-bellavista.netlify.app}"

  # Escribir .env
  cat > "$ENV_FILE" << EOF
# Generado por deploy-aws.sh — $(date '+%Y-%m-%d %H:%M:%S')

# Base de datos
DB_USER=${db_user}
DB_PASSWORD=${db_password}

# JWT
APP_JWT_SECRET=${jwt_secret}

# Email
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=${mail_user}
MAIL_PASSWORD=${mail_pass}
MAIL_FROM=${mail_from}

# Frontend
FRONTEND_URL=${frontend_url}
EOF

  chmod 600 "$ENV_FILE"
  success ".env creado con permisos 600."
}

# ------------------------------------------------------------------------------
# 6. Levantar contenedores
# ------------------------------------------------------------------------------
start_services() {
  step "Levantando contenedores"

  cd "$APP_DIR"

  info "Construyendo imagen y levantando servicios (puede tardar 3-5 min la primera vez)..."
  # Usar 'sudo docker' como fallback si el grupo docker no tomó efecto aún en esta sesión
  if docker info &>/dev/null 2>&1; then
    DOCKER_CMD="docker"
  else
    warn "Usando sudo para docker (cierra sesión y vuelve a conectar para evitarlo)."
    DOCKER_CMD="sudo docker"
  fi

  $DOCKER_CMD compose up -d --build

  success "Servicios levantados."
}

# ------------------------------------------------------------------------------
# 7. Estado final
# ------------------------------------------------------------------------------
show_status() {
  step "Estado del despliegue"

  cd "$APP_DIR"

  if docker info &>/dev/null 2>&1; then
    DOCKER_CMD="docker"
  else
    DOCKER_CMD="sudo docker"
  fi

  $DOCKER_CMD compose ps

  # Obtener IP pública de la instancia
  PUBLIC_IP=$(curl -sf --max-time 3 http://169.254.169.254/latest/meta-data/public-ipv4 || echo "<ip-publica-de-tu-ec2>")

  echo ""
  echo -e "${GREEN}${BOLD}Despliegue completado.${NC}"
  echo ""
  echo "  Health check : http://${PUBLIC_IP}:8080/api/actuator/health"
  echo "  API          : http://${PUBLIC_IP}:8080/api"
  echo "  Swagger UI   : http://${PUBLIC_IP}:8080/api/swagger-ui/index.html"
  echo ""
  echo "  Ver logs     : docker compose -C $APP_DIR logs -f app"
  echo "  Reiniciar    : docker compose -C $APP_DIR restart app"
  echo "  Detener todo : docker compose -C $APP_DIR down"
  echo ""
  warn "Recuerda abrir el puerto 8080 en el Security Group de la instancia EC2."
}

# ------------------------------------------------------------------------------
# Main
# ------------------------------------------------------------------------------
main() {
  echo -e "${BOLD}"
  echo "  ============================================"
  echo "   Droguería Bellavista — Deploy en AWS EC2"
  echo "  ============================================"
  echo -e "${NC}"

  [[ "$(uname -s)" == "Linux" ]] || die "Este script es solo para Linux (Ubuntu 24.04)."

  setup_swap
  install_docker
  clone_repo
  create_prod_config
  create_env
  start_services
  show_status
}

main "$@"
