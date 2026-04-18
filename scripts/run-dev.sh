#!/bin/bash

# Script para ejecutar la aplicación en modo desarrollo

echo "🚀 Iniciando Droguería Bellavista Backend..."
echo ""
echo "📋 Perfil: development"
echo "🗄️  Base de datos: H2 (en memoria)"
echo "🌐 URL: http://localhost:8080/api"
echo "💾 H2 Console: http://localhost:8080/h2-console"
echo ""

# Ejecutar con perfil de desarrollo
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# O si prefieres compilar primero:
# mvn clean package
# java -jar target/bellavista-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
