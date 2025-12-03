# Dockerfile para Extinguidor Backend - Digital Ocean App Platform
# Usa Java 17 y Spring Boot 3.2.0

# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar archivos de configuración de Maven primero (para cachear dependencias)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Compilar y empaquetar la aplicación
RUN mvn clean package -DskipTests -B

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Instalar curl para health checks
RUN apk add --no-cache curl

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Crear directorio para uploads con permisos correctos
RUN mkdir -p /app/uploads/partes /app/uploads/clientes && \
    chown -R spring:spring /app

# Cambiar a usuario no-root
USER spring:spring

# Exponer el puerto (Digital Ocean App Platform usa PORT env var)
EXPOSE 3000

# Health check para Digital Ocean
# Nota: Digital Ocean también puede configurar health checks desde su panel
# El health check usa el puerto por defecto; Digital Ocean puede configurar su propio health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:3000/actuator/health || exit 1

# Ejecutar la aplicación
# Digital Ocean App Platform inyecta la variable PORT automáticamente
ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=${PORT:-3000} app.jar"]

