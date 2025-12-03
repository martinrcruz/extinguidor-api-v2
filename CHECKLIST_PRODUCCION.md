# ✅ Checklist de Producción - Backend Extinguidor

## 📋 Pre-Despliegue

### Configuración
- [ ] Variables de entorno configuradas en el servidor de producción
  - [ ] `DB_USERNAME` - Usuario de base de datos
  - [ ] `DB_PASSWORD` - Contraseña segura de base de datos
  - [ ] `JWT_SECRET` - Clave secreta larga y aleatoria (mínimo 32 caracteres)
  - [ ] `UPLOAD_DIR` - Directorio para uploads (con permisos de escritura)
  
- [ ] Base de datos MySQL creada y accesible
  - [ ] Base de datos: `extinguidordb`
  - [ ] Charset: `utf8mb4`
  - [ ] Collation: `utf8mb4_unicode_ci`
  - [ ] Usuario con permisos adecuados

- [ ] Configuración de `application.yml` revisada
  - [ ] Puerto: `3000`
  - [ ] Context-path: `/`
  - [ ] CORS: Orígenes de producción configurados
  - [ ] DDL-auto: Considerar cambiar a `validate` o usar Flyway

### Seguridad
- [ ] JWT Secret generado de forma segura (no usar el default)
- [ ] CORS configurado solo para orígenes necesarios
- [ ] SSL/HTTPS habilitado en el servidor
- [ ] Credenciales de base de datos no en el código
- [ ] Logs no exponen información sensible

### Dependencias
- [ ] Todas las dependencias actualizadas
- [ ] Sin vulnerabilidades conocidas (verificar con `mvn dependency-check:check`)
- [ ] Build exitoso sin warnings críticos

---

## 🔍 Verificación de Integración

### Endpoints de Autenticación
- [ ] `POST /user/login` funciona correctamente
  ```bash
  curl -X POST http://localhost:3000/user/login \
    -H "Content-Type: application/json" \
    -d '{"email":"admin@example.com","password":"admin123"}'
  ```
- [ ] Respuesta incluye token JWT válido
- [ ] Token tiene formato correcto: `{ ok: true, data: { token, role, user } }`

### Endpoints Principales
- [ ] `GET /user/list` - Requiere autenticación
- [ ] `GET /customers` - Retorna lista de clientes
- [ ] `GET /rutas` - Retorna lista de rutas
- [ ] `GET /partes` - Retorna lista de partes con paginación
- [ ] `GET /vehicle` - Retorna lista de vehículos
- [ ] `GET /zone` - Retorna lista de zonas
- [ ] `GET /material` - Retorna lista de materiales
- [ ] `GET /articulos` - Retorna lista de artículos
- [ ] `GET /alertas` - Retorna lista de alertas (público)
- [ ] `GET /facturacion` - Retorna facturación
- [ ] `GET /rutasn` - Retorna nombres de rutas

### CORS
- [ ] Preflight requests funcionan correctamente
  ```bash
  curl -X OPTIONS http://localhost:3000/user/list \
    -H "Origin: http://localhost:4200" \
    -H "Access-Control-Request-Method: GET" \
    -H "Access-Control-Request-Headers: x-token"
  ```
- [ ] Headers CORS correctos en respuestas
- [ ] Frontend puede hacer peticiones sin errores de CORS

### Formato de Respuestas
- [ ] Todas las respuestas exitosas tienen formato: `{ ok: true, data: {...} }`
- [ ] Todas las respuestas de error tienen formato: `{ ok: false, error: "..." }`
- [ ] Status codes HTTP correctos (200, 201, 400, 401, 403, 404, 500)

---

## 🚀 Despliegue

### Build
- [ ] Compilación exitosa
  ```bash
  mvn clean package -DskipTests
  ```
- [ ] JAR generado en `target/extinguidor-backend-1.0.0.jar`
- [ ] Tamaño del JAR razonable (< 100MB)

### Tests
- [ ] Tests unitarios pasan
  ```bash
  mvn test
  ```
- [ ] Tests de integración pasan (si existen)
- [ ] Cobertura de código aceptable (>70%)

### Servidor
- [ ] Java 17+ instalado en servidor
- [ ] Puertos necesarios abiertos (3000)
- [ ] Firewall configurado correctamente
- [ ] Recursos del servidor adecuados (mínimo 1GB RAM, 1 CPU)

### Digital Ocean (si aplica)
- [ ] App Platform configurado
- [ ] Build command: `mvn clean package -DskipTests`
- [ ] Run command: `java -jar target/extinguidor-backend-1.0.0.jar`
- [ ] HTTP Port: `3000`
- [ ] Environment variables configuradas
- [ ] Database component conectado

---

## 🔧 Post-Despliegue

### Verificación Básica
- [ ] Aplicación inicia sin errores
- [ ] Health check responde
  ```bash
  curl https://tu-dominio.com/actuator/health
  ```
- [ ] Swagger UI accesible (solo en desarrollo)
  ```
  https://tu-dominio.com/swagger-ui.html
  ```

### Verificación Funcional
- [ ] Login desde frontend funciona
- [ ] CRUD de clientes funciona
- [ ] CRUD de rutas funciona
- [ ] CRUD de partes funciona
- [ ] Asignación de partes a rutas funciona
- [ ] Filtros y búsquedas funcionan
- [ ] Upload de archivos funciona

### Verificación de Seguridad
- [ ] Endpoints protegidos requieren autenticación
- [ ] Usuarios sin permisos reciben 403
- [ ] Tokens expirados son rechazados
- [ ] No se puede acceder sin token válido

### Performance
- [ ] Tiempo de respuesta < 500ms para endpoints principales
- [ ] Paginación funciona correctamente
- [ ] No hay memory leaks (verificar después de 24h)
- [ ] Logs no crecen descontroladamente

---

## 📊 Monitoreo

### Logs
- [ ] Logs se están escribiendo correctamente
- [ ] Nivel de log apropiado (INFO en producción)
- [ ] Rotación de logs configurada
- [ ] Logs accesibles para debugging

### Métricas
- [ ] Actuator endpoints funcionando
- [ ] Monitoreo de memoria
- [ ] Monitoreo de CPU
- [ ] Monitoreo de conexiones de base de datos

### Alertas
- [ ] Alertas configuradas para errores críticos
- [ ] Alertas para alta latencia
- [ ] Alertas para uso alto de recursos
- [ ] Alertas para caídas del servicio

---

## 🔄 Backup y Recuperación

### Base de Datos
- [ ] Backups automáticos configurados
- [ ] Backups probados (restauración funciona)
- [ ] Política de retención definida
- [ ] Backups guardados en ubicación segura

### Código
- [ ] Código en repositorio git
- [ ] Tags de versión creados
- [ ] Rama de producción protegida
- [ ] CI/CD configurado (opcional)

---

## 📚 Documentación

- [ ] README actualizado
- [ ] REPORTE_INTEGRACION.md completo
- [ ] API documentada en Swagger
- [ ] Variables de entorno documentadas
- [ ] Procedimientos de despliegue documentados
- [ ] Contactos de soporte definidos

---

## 🐛 Plan de Rollback

### En caso de problemas
- [ ] Procedimiento de rollback documentado
- [ ] Versión anterior disponible
- [ ] Backup de base de datos reciente
- [ ] Contactos de emergencia disponibles

### Pasos de Rollback
1. [ ] Detener la aplicación actual
2. [ ] Restaurar versión anterior del JAR
3. [ ] Verificar compatibilidad de base de datos
4. [ ] Restaurar backup de BD si es necesario
5. [ ] Reiniciar aplicación
6. [ ] Verificar funcionamiento

---

## ✅ Aprobación Final

- [ ] Pruebas de QA completadas
- [ ] Product Owner aprueba
- [ ] Equipo técnico aprueba
- [ ] Documentación completa
- [ ] Plan de rollback listo

### Firmas de Aprobación

**Desarrollador Backend:**
- Nombre: _________________
- Fecha: __________________

**QA/Tester:**
- Nombre: _________________
- Fecha: __________________

**Product Owner:**
- Nombre: _________________
- Fecha: __________________

---

## 📝 Notas Adicionales

### Versión Actual
- Versión: 1.0.0
- Fecha de despliegue: __________
- Commit SHA: __________

### Cambios en esta versión
- Integración completa con frontend Angular/Ionic
- Endpoints estandarizados
- CORS configurado para producción
- Autenticación JWT mejorada
- Respuestas API estandarizadas

### Problemas Conocidos
- Ninguno (actualizar si se encuentran)

---

## 🎉 Lista de Verificación Completa

Una vez que todos los ítems estén marcados:

1. ✅ **Configuración completa**
2. ✅ **Verificación de integración exitosa**
3. ✅ **Despliegue exitoso**
4. ✅ **Verificación post-despliegue exitosa**
5. ✅ **Monitoreo configurado**
6. ✅ **Backup configurado**
7. ✅ **Documentación completa**
8. ✅ **Plan de rollback listo**
9. ✅ **Aprobaciones recibidas**

**🚀 SISTEMA LISTO PARA PRODUCCIÓN 🚀**

---

*Checklist actualizado: Noviembre 29, 2025*
*Sistema de Gestión de Extintores v1.0.0*

