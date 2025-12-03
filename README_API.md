# Guía de Uso - API Extinguidor App

## 📋 Contenido de la Documentación

Se han generado dos archivos principales para la documentación de la API:

### 1. **API_DOCUMENTATION.md**
Documentación completa en formato Markdown que incluye:
- ✅ **Todos los endpoints** del backend (21 módulos, 150+ endpoints)
- ✅ **Permisos y roles** necesarios para cada endpoint
- ✅ **Request bodies** con ejemplos completos
- ✅ **Response bodies** con ejemplos de respuestas exitosas y de error
- ✅ **Códigos HTTP** y su significado
- ✅ **Enumeraciones** del sistema
- ✅ **Estructura de respuestas** estándar
- ✅ **Notas de implementación**

### 2. **extinguidor-api.postman_collection.json**
Colección completa de Postman que incluye:
- ✅ **Todos los endpoints** organizados por módulos
- ✅ **Datos de prueba** para cada request
- ✅ **Variables de entorno** configuradas
- ✅ **Autenticación JWT** automática
- ✅ **Scripts de test** para guardar el token automáticamente

---

## 🚀 Cómo Usar la Colección de Postman

### Paso 1: Importar la Colección

1. Abre **Postman**
2. Haz clic en **"Import"** (botón superior izquierdo)
3. Selecciona el archivo **`extinguidor-api.postman_collection.json`**
4. La colección aparecerá en tu sidebar con el nombre **"Extinguidor API"**

### Paso 2: Configurar Variables de Entorno

La colección incluye dos variables principales:

- **`base_url`**: URL base del servidor (por defecto: `http://localhost:8080`)
- **`jwt_token`**: Token JWT (se guarda automáticamente después del login)

Para cambiar la URL base (por ejemplo, para producción):

1. Haz clic en la colección **"Extinguidor API"**
2. Ve a la pestaña **"Variables"**
3. Cambia el valor de `base_url` a:
   - **Desarrollo**: `http://localhost:8080`
   - **Producción**: `https://extinguidor-backend-tj94j.ondigitalocean.app`

### Paso 3: Autenticarse

1. Ve a la carpeta **"1. Autenticación"**
2. Ejecuta el request **"Login"**
   - Email por defecto: `admin@example.com`
   - Password por defecto: `admin123`
3. El token JWT se guardará automáticamente en la variable `{{jwt_token}}`
4. Todos los demás requests usarán este token automáticamente

### Paso 4: Usar los Endpoints

Todos los endpoints están organizados en 21 carpetas:

1. **Autenticación** - Login y registro
2. **Usuarios** - Gestión de usuarios y trabajadores
3. **Clientes** - Gestión de clientes
4. **Partes** - Gestión de partes de trabajo
5. **Rutas** - Gestión de rutas
6. **Nombres de Rutas** - Gestión de nombres de rutas
7. **Vehículos** - Gestión de vehículos
8. **Zonas** - Gestión de zonas
9. **Materiales** - Gestión de materiales/herramientas
10. **Artículos** - Gestión de artículos del catálogo
11. **Alertas** - Gestión de alertas del sistema
12. **Checkins** - Gestión de checkins de trabajadores
13. **Reportes** - Gestión de reportes
14. **Comentarios** - Gestión de comentarios
15. **Facturación** - Gestión de facturación
16. **Archivos** - Upload/download de archivos
17. **Estadísticas** - Dashboard y métricas
18. **Exportación** - Exportar datos a Excel/PDF
19. **Plantillas de Partes** - Gestión de plantillas
20. **Reportes Avanzados** - Reportes con análisis detallado
21. **Auditoría** - Historial de cambios

Cada carpeta contiene todos los endpoints CRUD correspondientes con datos de ejemplo listos para usar.

---

## 🔑 Sistema de Roles y Permisos

### Roles Disponibles

El sistema tiene dos roles principales:

| Rol | Descripción | Acceso |
|-----|-------------|--------|
| **ADMIN** | Administrador | Acceso completo a todos los endpoints |
| **WORKER** | Trabajador | Acceso limitado a funciones operativas |

### Permisos por Módulo

| Módulo | ADMIN | WORKER | Público |
|--------|-------|--------|---------|
| Autenticación | ✅ | ✅ | ✅ Login/Registro |
| Usuarios | ✅ Completo | ⚠️ Solo perfil propio | ❌ |
| Clientes | ✅ Completo | ❌ | ❌ |
| Partes | ✅ Completo | ⚠️ Ver y actualizar propios | ❌ |
| Rutas | ✅ Completo | ⚠️ Ver propias | ❌ |
| Vehículos | ✅ Completo | ❌ | ❌ |
| Zonas | ✅ Completo | ❌ | ❌ |
| Materiales | ✅ Completo | ❌ | ❌ |
| Artículos | ✅ Completo | ❌ | ❌ |
| Alertas | ✅ Completo | ✅ Completo | ❌ |
| Checkins | ✅ Completo | ✅ Completo | ❌ |
| Reportes | ✅ Completo | ✅ Completo | ❌ |
| Comentarios | ✅ Completo | ✅ Completo | ❌ |
| Facturación | ✅ Completo | ❌ | ❌ |
| Archivos | ✅ Completo | ⚠️ Upload/Download | ❌ |
| Estadísticas | ✅ Completo | ❌ | ❌ |
| Exportación | ✅ Completo | ❌ | ❌ |
| Plantillas | ✅ Completo | ❌ | ❌ |
| Reportes Avanzados | ✅ Completo | ❌ | ❌ |
| Auditoría | ✅ Completo | ❌ | ❌ |

---

## 📊 Ejemplos de Uso Común

### Flujo Básico: Crear y Asignar un Parte

```
1. Login → Obtener token
   POST /auth/login

2. Crear cliente (si no existe)
   POST /customers/create

3. Crear parte
   POST /partes/create

4. Crear ruta
   POST /rutas/create

5. Asignar parte a ruta
   POST /rutas/{id}/asignarPartes

6. Ver partes del trabajador
   GET /partes/worker/{workerId}

7. Actualizar estado del parte
   PUT /partes/{id}/status

8. Crear facturación
   POST /facturacion/create
```

### Flujo: Consultar Estadísticas

```
1. Login como ADMIN
   POST /auth/login

2. Ver estadísticas del dashboard
   GET /statistics/dashboard

3. Ver estadísticas de trabajadores
   GET /statistics/workers

4. Generar reporte avanzado
   GET /reports/advanced?startDate=2024-11-01&endDate=2024-11-30

5. Exportar datos
   GET /export/partes/excel
```

### Flujo: Worker Mobile

```
1. Login como WORKER
   POST /user/login

2. Ver mis rutas de hoy
   GET /rutas/worker/{workerId}

3. Ver partes asignados
   GET /partes/worker/{workerId}

4. Crear checkin
   POST /checkin

5. Subir fotos del trabajo
   POST /file/upload/image

6. Actualizar estado del parte
   PUT /partes/{id}/status

7. Añadir comentario
   POST /comment

8. Crear reporte
   POST /report
```

---

## 🧪 Datos de Prueba

### Usuarios de Ejemplo

La colección incluye datos de prueba para dos tipos de usuarios:

**Administrador:**
```json
{
  "email": "admin@example.com",
  "password": "admin123"
}
```

**Trabajador:**
```json
{
  "email": "worker@example.com",
  "password": "worker123"
}
```

### Crear Datos de Prueba

Para poblar el sistema con datos de prueba, ejecuta en orden:

1. **Usuarios** → Crear trabajadores
2. **Zonas** → Crear zonas geográficas
3. **Clientes** → Crear clientes
4. **Vehículos** → Crear vehículos
5. **Materiales** → Crear materiales/herramientas
6. **Artículos** → Crear artículos del catálogo
7. **Nombres de Rutas** → Crear nombres de rutas
8. **Plantillas** → Crear plantillas de partes
9. **Partes** → Crear partes desde plantillas
10. **Rutas** → Crear rutas y asignar partes

Todos los requests de creación en la colección incluyen datos de ejemplo completos.

---

## 🔍 Estructura de Respuestas

### Respuesta Exitosa

```json
{
  "ok": true,
  "data": {
    // ... datos solicitados
  },
  "message": "Mensaje opcional"
}
```

### Respuesta de Error

```json
{
  "ok": false,
  "error": "Descripción del error",
  "message": "Mensaje adicional opcional"
}
```

### Códigos HTTP

| Código | Significado | Cuándo se usa |
|--------|-------------|---------------|
| 200 | OK | Operación exitosa |
| 201 | Created | Recurso creado exitosamente |
| 204 | No Content | Operación exitosa sin contenido |
| 400 | Bad Request | Datos inválidos en la petición |
| 401 | Unauthorized | No autenticado o token inválido |
| 403 | Forbidden | Sin permisos para la operación |
| 404 | Not Found | Recurso no encontrado |
| 500 | Internal Server Error | Error interno del servidor |

---

## 📝 Notas Importantes

### Autenticación JWT

- El token JWT tiene una duración de **24 horas**
- Debe incluirse en el header: `Authorization: Bearer {token}`
- Se guarda automáticamente en Postman después del login
- Para endpoints públicos (login, registro), no se requiere token

### Paginación

Algunos endpoints soportan paginación:

```
GET /partes?page=0&limit=10
GET /articulos?page=1&limit=100
```

- `page`: Número de página (0-indexed)
- `limit`: Elementos por página

### Fechas

Las fechas se manejan en formato ISO 8601:

- **LocalDate**: `YYYY-MM-DD` (ej: `2024-11-29`)
- **LocalDateTime**: `YYYY-MM-DDTHH:mm:ss` (ej: `2024-11-29T08:30:00`)

### Archivos

Para subir archivos, usar `multipart/form-data`:

```
POST /file/upload/parte
Content-Type: multipart/form-data

file: [archivo a subir]
```

### Eliminación Lógica

La mayoría de endpoints de eliminación realizan **soft delete** (eliminación lógica):

- No se elimina el registro de la base de datos
- Se marca con `eliminado: true` o `active: false`
- Los registros eliminados no aparecen en listados

### Auditoría

Todas las entidades principales tienen auditoría automática:

- Se registran todas las operaciones CREATE, UPDATE, DELETE
- Consultar historial: `GET /audit/{entityName}/{entityId}`
- Solo accesible por ADMIN

---

## 🛠️ Desarrollo

### Configurar Entorno Local

1. Asegurarse de que el backend está corriendo en `http://localhost:8080`
2. Importar la colección de Postman
3. Usar la variable `base_url` con valor `http://localhost:8080`
4. Ejecutar el login para obtener el token

### Configurar Entorno de Producción

1. Importar la colección de Postman
2. Cambiar la variable `base_url` a `https://extinguidor-backend-tj94j.ondigitalocean.app`
3. Ejecutar el login para obtener el token

### Swagger UI

El backend también incluye documentación interactiva con Swagger:

- **Local**: http://localhost:8080/swagger-ui.html
- **Producción**: https://extinguidor-backend-tj94j.ondigitalocean.app/swagger-ui.html

---

## 📞 Soporte

Para problemas, preguntas o sugerencias sobre la API:

1. Revisar la documentación completa en **API_DOCUMENTATION.md**
2. Verificar que el backend esté corriendo correctamente
3. Comprobar que el token JWT sea válido
4. Verificar que tienes los permisos necesarios para el endpoint

---

## 📚 Recursos Adicionales

- **API_DOCUMENTATION.md** - Documentación completa de todos los endpoints
- **extinguidor-api.postman_collection.json** - Colección de Postman con todos los endpoints
- **README.md** - Información general del proyecto backend
- **REPORTE_INTEGRACION.md** - Reporte de integración del sistema

---

**Versión**: 1.0  
**Última actualización**: Noviembre 2024  
**Total de Endpoints**: 150+  
**Módulos**: 21

