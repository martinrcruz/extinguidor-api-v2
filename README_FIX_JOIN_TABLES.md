# Solución para Tablas Intermedias Faltantes

## Problema
Hibernate intenta agregar claves foráneas sobre tablas intermedias que no existen físicamente en MySQL:
- `report_tools`
- `report_vehicles`
- `report_workers`
- `route_materials`

## Solución Aplicada: Crear Tablas Manualmente (Opción B)

Se ha creado el script SQL `create_join_tables.sql` que crea las tablas faltantes con sus claves foráneas.

### Pasos para Aplicar la Solución

1. **Conectarse a MySQL**:
   ```bash
   mysql -h db-mysql-nyc3-92655-do-user-3126449-0.i.db.ondigitalocean.com -P 25060 -u doadmin -p extinguidordb
   ```

2. **Ejecutar el script SQL**:
   ```bash
   source create_join_tables.sql
   ```
   
   O desde la línea de comandos de MySQL:
   ```sql
   USE extinguidordb;
   source /ruta/completa/a/create_join_tables.sql;
   ```

3. **Verificar que las tablas se crearon**:
   ```sql
   SHOW TABLES LIKE 'report_%';
   SHOW TABLES LIKE 'route_materials';
   ```

4. **Iniciar la aplicación**:
   ```bash
   mvn spring-boot:run
   ```

   Hibernate ya no debería intentar crear las tablas (porque ya existen) y solo agregará las claves foráneas si no existen.

## Alternativa: Recrear Base de Datos (Opción A)

Si estás en desarrollo y no tienes datos importantes que conservar, puedes:

1. **Dropear y recrear la base de datos**:
   ```sql
   DROP DATABASE extinguidordb;
   CREATE DATABASE extinguidordb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **Cambiar temporalmente `application.yml`**:
   ```yaml
   spring:
     jpa:
       hibernate:
         ddl-auto: create  # Cambiar de 'update' a 'create'
   ```

3. **Iniciar la aplicación** para que Hibernate cree todo desde cero.

4. **Volver a cambiar a `update`**:
   ```yaml
   spring:
     jpa:
       hibernate:
         ddl-auto: update  # Volver a 'update'
   ```

## Verificación de Entidades

Las siguientes entidades tienen las relaciones `@ManyToMany` correctamente configuradas:

- **Report.java**:
  - `report_workers` (líneas 40-47)
  - `report_tools` (líneas 75-82)
  - `report_vehicles` (líneas 88-95)

- **Route.java**:
  - `route_materials` (líneas 64-71)

Los nombres de las tablas y columnas en las anotaciones `@JoinTable` coinciden exactamente con el script SQL.

## Notas

- Las claves foráneas tienen `ON DELETE CASCADE` y `ON UPDATE CASCADE` para mantener la integridad referencial.
- Se agregaron índices para mejorar el rendimiento de las consultas.
- El script usa `CREATE TABLE IF NOT EXISTS` para evitar errores si las tablas ya existen.

