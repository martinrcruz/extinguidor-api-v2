# Sistema Extinguidor - Backend

Backend desarrollado con Spring Boot 3.2.0, Java 17, PostgreSQL, JWT, Spring Security, Hibernate Envers, MapStruct, Swagger y Log4j2.

## Requisitos

- Java 17 o superior
- Maven 3.8+
- PostgreSQL 12+

## Configuración

1. Crear base de datos PostgreSQL:
```sql
CREATE DATABASE extinguidor_db;
```

2. Configurar variables de entorno o editar `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/extinguidor_db
    username: postgres
    password: postgres
  security:
    jwt:
      secret: TuClaveSecretaJWT
```

3. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

## Endpoints

- API Base: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- API Docs: `http://localhost:8080/api/api-docs`

## Autenticación

Todas las rutas protegidas requieren un token JWT en el header `x-token`.

## Estructura del Proyecto

```
backend/
├── src/main/java/com/extinguidor/
│   ├── config/          # Configuraciones
│   ├── controller/      # Controladores REST
│   ├── dto/             # Data Transfer Objects
│   ├── exception/       # Manejo de excepciones
│   ├── model/           # Entidades y Enums
│   ├── repository/      # Repositorios JPA
│   ├── security/        # Configuración de seguridad
│   ├── service/         # Lógica de negocio
│   └── util/            # Utilidades
└── src/main/resources/
    ├── application.yml  # Configuración
    └── log4j2.xml       # Configuración de logs
```

