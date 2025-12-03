# Backend - Sistema de Gestión de Extintores

## 🚀 Inicio Rápido

### Requisitos Previos
- Java 17 o superior
- Maven 3.8+
- MySQL 8.0+
- (Opcional) Docker para desarrollo

### Instalación y Ejecución

#### 1. Clonar y configurar
```bash
cd backend
```

#### 2. Configurar Base de Datos

Crear una base de datos MySQL:
```sql
CREATE DATABASE extinguidordb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 3. Configurar Variables de Entorno (Opcional)

```bash
export DB_USERNAME=tu_usuario
export DB_PASSWORD=tu_password
export JWT_SECRET=tu_clave_secreta_muy_larga_y_segura
```

O editar directamente `src/main/resources/application.yml`

#### 4. Compilar y Ejecutar

```bash
# Compilar
mvn clean package -DskipTests

# Ejecutar
java -jar target/extinguidor-backend-1.0.0.jar

# O con Maven
mvn spring-boot:run
```

El servidor estará disponible en: `http://localhost:3000`

---

## 📝 Configuración

### Archivo application.yml

```yaml
server:
  port: 3000  # Puerto configurado para coincidir con el frontend

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/extinguidordb
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:password}
  
  jpa:
    hibernate:
      ddl-auto: update  # CAMBIAR A 'validate' en producción
  
  security:
    jwt:
      secret: ${JWT_SECRET:ExtinguidorSecretKeyForJWTTokenGeneration2025}
      expiration: 86400000  # 24 horas
```

---

## 🔐 Autenticación

### Login
```bash
POST /user/login
Content-Type: application/json

{
  "email": "admin@example.com",
  "password": "admin123"
}
```

### Respuesta
```json
{
  "ok": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "role": "ADMIN",
    "user": {
      "id": 1,
      "name": "Admin User",
      "email": "admin@example.com"
    }
  }
}
```

### Usar Token en Peticiones
```bash
GET /user/list
x-token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 📚 Documentación API

### Swagger UI
Una vez iniciado el servidor, acceder a:
```
http://localhost:3000/swagger-ui.html
```

### OpenAPI JSON
```
http://localhost:3000/api-docs
```

---

## 🏗️ Estructura del Proyecto

```
backend/
├── src/main/java/com/extinguidor/
│   ├── config/              # Configuraciones
│   │   ├── SecurityConfig.java
│   │   └── SwaggerConfig.java
│   ├── controller/          # Controladores REST
│   │   ├── UserController.java
│   │   ├── CustomerController.java
│   │   ├── RouteController.java
│   │   └── ...
│   ├── dto/                 # Data Transfer Objects
│   │   ├── StandardApiResponse.java
│   │   └── ...
│   ├── model/              # Modelos de datos
│   │   ├── entity/
│   │   └── enums/
│   ├── repository/         # Repositorios JPA
│   ├── service/            # Lógica de negocio
│   ├── security/           # Seguridad JWT
│   └── exception/          # Manejo de excepciones
├── src/main/resources/
│   ├── application.yml     # Configuración principal
│   └── log4j2.xml         # Configuración de logs
└── pom.xml                # Dependencias Maven
```

---

## 🔧 Endpoints Principales

### Usuarios
- `POST /user/login` - Iniciar sesión
- `POST /user/create` - Crear usuario
- `GET /user/list` - Listar todos los usuarios
- `GET /user/worker` - Listar trabajadores
- `PUT /user/update` - Actualizar usuario
- `DELETE /user/delete/{id}` - Eliminar usuario

### Clientes
- `GET /customers` - Listar clientes
- `POST /customers/create` - Crear cliente
- `GET /customers/{id}` - Obtener cliente
- `PUT /customers/update` - Actualizar cliente
- `DELETE /customers/{id}` - Eliminar cliente

### Rutas
- `GET /rutas` - Listar rutas
- `POST /rutas/create` - Crear ruta
- `GET /rutas/{id}` - Obtener ruta
- `POST /rutas/update` - Actualizar ruta
- `DELETE /rutas/{id}` - Eliminar ruta
- `GET /rutas/worker/{workerId}` - Rutas por trabajador
- `GET /rutas/disponibles` - Rutas disponibles
- `POST /rutas/{id}/asignarPartes` - Asignar partes a ruta

### Partes
- `GET /partes` - Listar partes (paginado)
- `POST /partes/create` - Crear parte
- `GET /partes/{id}` - Obtener parte
- `POST /partes/update` - Actualizar parte
- `DELETE /partes/{id}` - Eliminar parte
- `GET /partes/noasignados` - Partes no asignados
- `GET /partes/worker/{workerId}` - Partes por trabajador
- `PUT /partes/{id}/status` - Actualizar estado

Ver todos los endpoints en Swagger UI.

---

## 🐳 Docker

### Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/extinguidor-backend-1.0.0.jar app.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose
```yaml
version: '3.8'
services:
  backend:
    build: .
    ports:
      - "3000:3000"
    environment:
      DB_USERNAME: root
      DB_PASSWORD: password
      JWT_SECRET: your-secret-key
    depends_on:
      - db
  
  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: extinguidordb
    ports:
      - "3306:3306"
```

---

## 🌐 Despliegue en Digital Ocean

### App Platform

1. **Conectar Repositorio**
   - Conectar con GitHub/GitLab

2. **Configurar Build**
   ```
   Build Command: mvn clean package -DskipTests
   Run Command: java -jar target/extinguidor-backend-1.0.0.jar
   HTTP Port: 3000
   ```

3. **Variables de Entorno**
   ```
   DB_USERNAME=<tu-usuario>
   DB_PASSWORD=<tu-password>
   JWT_SECRET=<tu-clave-secreta>
   ```

4. **Base de Datos**
   - Crear un Managed Database (MySQL)
   - Conectar con el App

### Verificación del Despliegue
```bash
# Health check
curl https://tu-app.ondigitalocean.app/actuator/health

# Test login
curl -X POST https://tu-app.ondigitalocean.app/user/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"admin123"}'
```

---

## 🧪 Testing

### Ejecutar Tests
```bash
# Todos los tests
mvn test

# Tests específicos
mvn test -Dtest=UserControllerTest

# Con cobertura
mvn clean test jacoco:report
```

### Tests de Integración
Los tests de integración están en `src/test/java` y utilizan una base de datos H2 en memoria.

---

## 📊 Monitoreo y Logs

### Actuator Endpoints
- `/actuator/health` - Estado del servidor
- `/actuator/info` - Información de la aplicación
- `/actuator/metrics` - Métricas del sistema

### Logs
Los logs se guardan en:
- Consola: Nivel INFO
- Archivo: `logs/application.log` (Nivel DEBUG)

### Configurar Log Level
```yaml
logging:
  level:
    com.extinguidor: DEBUG
    org.springframework: INFO
    org.hibernate: WARN
```

---

## 🔒 Seguridad

### CORS
Configurado para permitir:
- localhost:4200 (Angular)
- localhost:8100 (Ionic)
- Dominio de producción

### JWT
- Algoritmo: HS256
- Expiración: 24 horas
- Header personalizado: `x-token`

### Roles
- `ADMIN`: Acceso completo
- `WORKER`: Acceso limitado a rutas y partes asignados

---

## 🐛 Troubleshooting

### Error: Puerto en Uso
```bash
# Verificar puerto 3000
lsof -i :3000

# Matar proceso
kill -9 <PID>
```

### Error: Conexión a Base de Datos
1. Verificar que MySQL esté corriendo
2. Verificar credenciales en `application.yml`
3. Verificar que la base de datos exista

### Error: Token JWT Inválido
1. Verificar que el token no haya expirado
2. Verificar que la clave secreta sea la misma
3. Verificar el header `x-token`

---

## 📦 Dependencias Principales

- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- MySQL Connector
- JWT (jjwt 0.12.3)
- MapStruct 1.5.5
- Lombok 1.18.30
- Swagger/OpenAPI 2.3.0
- Log4j2

---

## 🤝 Contribuir

1. Fork el proyecto
2. Crear una rama feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

---

## 📄 Licencia

Este proyecto es privado y confidencial.

---

## 📞 Contacto

Para soporte o consultas sobre la integración, revisar el archivo `REPORTE_INTEGRACION.md`.

---

*Última actualización: Noviembre 29, 2025*

