package com.extinguidor.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

/**
 * Componente que carga datos de prueba desde el archivo insert_test_data.sql
 * Se ejecuta automáticamente al iniciar la aplicación.
 * Usa INSERT IGNORE para evitar errores si los datos ya existen.
 */
@Component
@RequiredArgsConstructor
@Log4j2
@Order(2) // Se ejecuta después de DataInitializer (Order 1)
public class TestDataLoader implements CommandLineRunner {
    
    private final JdbcTemplate jdbcTemplate;
    
    private static final String SQL_SCRIPT_PATH = "insert_test_data.sql";
    private static final boolean ENABLE_TEST_DATA_LOADING = false; // Cambiar a false para deshabilitar
    
    @Override
    public void run(String... args) throws Exception {
        if (!ENABLE_TEST_DATA_LOADING) {
            log.debug("Carga de datos de prueba deshabilitada");
            return;
        }
        
        try {
            log.info("═══════════════════════════════════════════════════════════════");
            log.info("🔄 Iniciando carga de datos de prueba...");
            log.info("═══════════════════════════════════════════════════════════════");
            
            // Leer el archivo SQL
            ClassPathResource resource = new ClassPathResource(SQL_SCRIPT_PATH);
            
            if (!resource.exists()) {
                log.warn("⚠️  No se encontró el archivo {} en el classpath", SQL_SCRIPT_PATH);
                log.warn("   Intentando leer desde el directorio raíz del proyecto...");
                
                // Intentar leer desde el directorio raíz del proyecto
                java.io.File file = new java.io.File("backend/" + SQL_SCRIPT_PATH);
                if (!file.exists()) {
                    file = new java.io.File(SQL_SCRIPT_PATH);
                }
                
                if (file.exists()) {
                    log.info("✅ Archivo encontrado en: {}", file.getAbsolutePath());
                    loadScriptFromFile(file);
                } else {
                    log.warn("⚠️  No se pudo encontrar el archivo SQL. Saltando carga de datos de prueba.");
                    return;
                }
            } else {
                log.info("✅ Archivo SQL encontrado en classpath");
                loadScriptFromResource(resource);
            }
            
            log.info("═══════════════════════════════════════════════════════════════");
            log.info("✅ Carga de datos de prueba completada exitosamente");
            log.info("═══════════════════════════════════════════════════════════════");
            
        } catch (Exception e) {
            log.error("❌ Error al cargar datos de prueba: {}", e.getMessage(), e);
            // No lanzamos la excepción para que la aplicación pueda continuar
        }
    }
    
    private void loadScriptFromResource(ClassPathResource resource) throws Exception {
        String sqlScript = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        executeScript(sqlScript);
    }
    
    private void loadScriptFromFile(java.io.File file) throws Exception {
        String sqlScript = new String(java.nio.file.Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        executeScript(sqlScript);
    }
    
    private void executeScript(String sqlScript) {
        // Dividir el script en sentencias individuales
        // Eliminar comentarios y líneas vacías
        String[] statements = sqlScript
                .replaceAll("--.*", "") // Eliminar comentarios de línea
                .replaceAll("/\\*[\\s\\S]*?\\*/", "") // Eliminar comentarios de bloque
                .split(";");
        
        int executedStatements = 0;
        int skippedStatements = 0;
        int errorStatements = 0;
        int statementNumber = 0;
        
        for (String statement : statements) {
            String trimmedStatement = statement.trim();
            statementNumber++;
            
            // Saltar líneas vacías o que solo contengan espacios
            if (trimmedStatement.isEmpty() || trimmedStatement.matches("^\\s*$")) {
                continue;
            }
            
            // Saltar sentencias que no sean INSERT
            if (!trimmedStatement.toUpperCase().startsWith("INSERT")) {
                continue;
            }
            
            // Extraer el nombre de la tabla para el log
            String tableName = extractTableName(trimmedStatement);
            
            try {
                // Ejecutar la sentencia
                int rowsAffected = jdbcTemplate.update(trimmedStatement);
                
                if (rowsAffected > 0) {
                    executedStatements++;
                    log.info("✅ [{}] Tabla '{}': {} fila(s) insertada(s)", statementNumber, tableName, rowsAffected);
                } else {
                    skippedStatements++;
                    log.debug("⏭️  [{}] Tabla '{}': Sin filas afectadas (posiblemente ya existen)", statementNumber, tableName);
                }
                
            } catch (Exception e) {
                String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
                String sqlState = "";
                
                // Intentar obtener el SQLState si está disponible
                if (e instanceof java.sql.SQLException) {
                    sqlState = ((java.sql.SQLException) e).getSQLState();
                }
                
                // Verificar si es un error esperado (duplicado, foreign key, etc.)
                boolean isExpectedError = errorMessage.contains("Duplicate entry") ||
                                        errorMessage.contains("already exists") ||
                                        errorMessage.contains("foreign key constraint") ||
                                        errorMessage.contains("Cannot add or update a child row") ||
                                        errorMessage.contains("Cannot find") ||
                                        errorMessage.contains("does not exist");
                
                if (isExpectedError) {
                    skippedStatements++;
                    log.warn("⏭️  [{}] Tabla '{}': Registro ignorado - {}", 
                            statementNumber, tableName, 
                            getErrorSummary(errorMessage, sqlState));
                    log.debug("   Detalle: {}", errorMessage.substring(0, Math.min(150, errorMessage.length())));
                } else {
                    errorStatements++;
                    log.error("❌ [{}] Tabla '{}': Error al ejecutar sentencia", statementNumber, tableName);
                    log.error("   Tipo: {}", e.getClass().getSimpleName());
                    log.error("   Mensaje: {}", errorMessage.substring(0, Math.min(200, errorMessage.length())));
                    if (!sqlState.isEmpty()) {
                        log.error("   SQLState: {}", sqlState);
                    }
                    log.debug("   Sentencia (primeros 300 caracteres): {}", 
                            trimmedStatement.substring(0, Math.min(300, trimmedStatement.length())));
                    if (trimmedStatement.length() > 300) {
                        log.debug("   ... (sentencia truncada)");
                    }
                }
            }
        }
        
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("📊 RESUMEN DE CARGA DE DATOS DE PRUEBA");
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("   ✅ Sentencias ejecutadas exitosamente: {}", executedStatements);
        log.info("   ⏭️  Sentencias ignoradas (datos ya existían): {}", skippedStatements);
        if (errorStatements > 0) {
            log.warn("   ❌ Sentencias con errores inesperados: {}", errorStatements);
        }
        log.info("   📝 Total de sentencias procesadas: {}", executedStatements + skippedStatements + errorStatements);
        log.info("═══════════════════════════════════════════════════════════════");
    }
    
    private String extractTableName(String sql) {
        try {
            // Buscar el nombre de la tabla después de INSERT IGNORE INTO o INSERT INTO
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "(?i)INSERT\\s+(?:IGNORE\\s+)?INTO\\s+([a-z_]+)",
                java.util.regex.Pattern.CASE_INSENSITIVE
            );
            java.util.regex.Matcher matcher = pattern.matcher(sql);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception e) {
            // Si falla, devolver "desconocida"
        }
        return "desconocida";
    }
    
    private String getErrorSummary(String errorMessage, String sqlState) {
        if (errorMessage.contains("Duplicate entry")) {
            return "Registro duplicado (ya existe)";
        } else if (errorMessage.contains("foreign key constraint")) {
            return "Restricción de clave foránea (referencia inválida)";
        } else if (errorMessage.contains("Cannot add or update a child row")) {
            return "Referencia de clave foránea no encontrada";
        } else if (errorMessage.contains("Cannot find") || errorMessage.contains("does not exist")) {
            return "Recurso no encontrado";
        } else if (!sqlState.isEmpty()) {
            return "Error SQL: " + sqlState;
        }
        return "Error desconocido";
    }
}

