# Script de Datos de Prueba

Este documento explica cómo usar el script `insert_test_data.sql` para poblar la base de datos con datos de prueba.

## Descripción

El script `insert_test_data.sql` contiene datos de prueba completos para todas las entidades del sistema:

- **Usuarios**: 6 usuarios (1 admin, 5 workers)
- **Zonas**: 7 zonas geográficas
- **Clientes**: 6 clientes con contratos y documentos
- **Vehículos**: 6 vehículos (furgones y turismos)
- **Materiales**: 8 materiales/herramientas
- **Artículos**: 8 artículos para venta
- **Rutas**: 5 rutas con usuarios y materiales asignados
- **Partes**: 6 partes de trabajo con diferentes estados
- **Reportes**: 5 reportes de trabajo
- **Checkins**: 4 checkins asociados a reportes
- **Comentarios**: 4 comentarios con fotos
- **Alertas**: 5 alertas del sistema
- **Facturación**: 5 registros de facturación
- **Plantillas**: 3 plantillas de partes

## Requisitos Previos

1. Tener acceso a la base de datos MySQL configurada en `application.yml`
2. Tener las tablas creadas (el sistema las crea automáticamente con `ddl-auto: update`)
3. Tener permisos de escritura en la base de datos

## Cómo Ejecutar el Script

### Opción 1: Desde MySQL Command Line

```bash
mysql -h [HOST] -u [USER] -p [DATABASE] < insert_test_data.sql
```

Ejemplo con los datos de `application.yml`:
```bash
mysql -h db-mysql-nyc3-92655-do-user-3126449-0.i.db.ondigitalocean.com \
      -P 25060 \
      -u doadmin \
      -p extinguidordb < insert_test_data.sql
```

### Opción 2: Desde MySQL Workbench o cliente gráfico

1. Abre tu cliente MySQL (Workbench, DBeaver, etc.)
2. Conéctate a la base de datos
3. Abre el archivo `insert_test_data.sql`
4. Ejecuta el script completo

### Opción 3: Desde la línea de comandos MySQL

```bash
mysql -h [HOST] -u [USER] -p
```

Luego dentro de MySQL:
```sql
USE extinguidordb;
SOURCE /ruta/completa/insert_test_data.sql;
```

## Credenciales de Prueba

**IMPORTANTE**: Las contraseñas en el script están hasheadas con BCrypt. Si necesitas usar contraseñas diferentes, puedes:

1. **Usar el DataInitializer**: El sistema crea automáticamente un usuario admin al iniciar:
   - Email: `admin@extinguidor.com`
   - Password: `Admin123!`

2. **Generar nuevos hashes BCrypt**: Puedes usar un generador online o el siguiente código Java:
```java
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
String hash = encoder.encode("tu_contraseña");
```

### Usuarios de Prueba Incluidos

| Email | Password | Rol | Código |
|-------|----------|-----|--------|
| admin@extinguidor.com | password123 | ADMIN | ADMIN001 |
| juan.perez@extinguidor.com | password123 | WORKER | WORKER001 |
| maria.garcia@extinguidor.com | password123 | WORKER | WORKER002 |
| carlos.lopez@extinguidor.com | password123 | WORKER | WORKER003 |
| ana.martinez@extinguidor.com | password123 | WORKER | WORKER004 |
| pedro.sanchez@extinguidor.com | password123 | WORKER | WORKER005 |

**Nota**: Si las contraseñas hasheadas no funcionan, puedes cambiar las contraseñas desde la aplicación usando el endpoint de actualización de usuario, o regenerar los hashes BCrypt.

## Estructura de Datos

### Relaciones Creadas

- **Clientes** → Asociados a **Zonas**
- **Rutas** → Asociadas a **Usuarios**, **Vehículos**, **Materiales** y **RutaN**
- **Partes** → Asociadas a **Clientes**, **Rutas**, **Usuarios** (workers)
- **Reportes** → Asociados a **Clientes**, **Rutas**, **Usuarios**, **Vehículos**, **Materiales**
- **Checkins** → Asociados a **Usuarios** y **Reportes**
- **Comentarios** → Asociados a **Partes**
- **Facturación** → Asociada a **Rutas** y **Partes**

## Limpiar Datos de Prueba

Si necesitas limpiar los datos de prueba, puedes descomentar las líneas al inicio del script que contienen los comandos `TRUNCATE`, o ejecutar manualmente:

```sql
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE comments;
TRUNCATE TABLE checkins;
-- ... (ver script completo para todas las tablas)
SET FOREIGN_KEY_CHECKS = 1;
```

## Notas Importantes

1. **Timestamps**: Los reportes usan timestamps en milisegundos (formato Unix). El script incluye ejemplos válidos.

2. **Fechas**: Las fechas están en formato `YYYY-MM-DD` para campos `LocalDate`.

3. **Coordenadas**: Los checkins y comentarios incluyen coordenadas de ejemplo (lat/lng) para Madrid, Barcelona, Valencia, etc.

4. **URLs de Documentos**: Las URLs de documentos y fotos son ejemplos. En producción, deberían apuntar a archivos reales.

5. **Integridad Referencial**: El script respeta las relaciones entre tablas y las claves foráneas.

## Solución de Problemas

### Error: "Duplicate entry"
Si obtienes errores de duplicados, significa que algunos datos ya existen. Puedes:
- Limpiar las tablas primero (ver sección anterior)
- Modificar los códigos/emails únicos en el script

### Error: "Foreign key constraint fails"
Asegúrate de que las tablas se crean en el orden correcto. El script ya está ordenado correctamente.

### Las contraseñas no funcionan
Si no puedes iniciar sesión con las contraseñas del script:
1. Usa el usuario admin creado por `DataInitializer` (admin@extinguidor.com / Admin123!)
2. O regenera los hashes BCrypt para las contraseñas que necesites

## Personalización

Puedes modificar el script para:
- Agregar más usuarios, clientes, vehículos, etc.
- Cambiar las fechas y horarios
- Modificar las relaciones entre entidades
- Agregar más datos de prueba según tus necesidades

## Soporte

Si encuentras problemas con el script, verifica:
1. Que la base de datos esté accesible
2. Que las tablas existan (el sistema las crea automáticamente)
3. Que tengas permisos de escritura
4. Que no haya conflictos con datos existentes

