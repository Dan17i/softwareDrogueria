# ============================================
# STAGE 1: Build del front (Vite)
# ============================================
FROM node:22-alpine AS builder
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build

# ============================================
# STAGE 2: Nginx (estáticos + proxy /api -> backend)
# ============================================
FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
