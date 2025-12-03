package com.extinguidor.exception;

import com.extinguidor.dto.StandardApiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return new ResponseEntity<>(
            StandardApiResponse.error(ex.getMessage(), "Recurso no encontrado"), 
            HttpStatus.NOT_FOUND
        );
    }
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardApiResponse<Object>> handleBusinessException(BusinessException ex) {
        log.warn("Error de negocio: {}", ex.getMessage());
        return new ResponseEntity<>(
            StandardApiResponse.error(ex.getMessage(), "Error de negocio"), 
            HttpStatus.BAD_REQUEST
        );
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<StandardApiResponse<Object>> handleValidationException(ValidationException ex) {
        log.warn("Error de validación: {}", ex.getMessage());
        return new ResponseEntity<>(
            StandardApiResponse.error(ex.getMessage(), "Error de validación"), 
            HttpStatus.BAD_REQUEST
        );
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Credenciales inválidas: {}", ex.getMessage());
        return new ResponseEntity<>(
            StandardApiResponse.error("Email o contraseña incorrectos", "Credenciales inválidas"), 
            HttpStatus.UNAUTHORIZED
        );
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Acceso denegado: {}", ex.getMessage());
        return new ResponseEntity<>(
            StandardApiResponse.error("No tiene permisos para realizar esta acción", "Acceso denegado"), 
            HttpStatus.FORBIDDEN
        );
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.warn("Error de validación de argumentos: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return new ResponseEntity<>(
            StandardApiResponse.error("Errores en los campos del formulario: " + errors.toString(), "Error de validación"), 
            HttpStatus.BAD_REQUEST
        );
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("Error inesperado: ", ex);
        return new ResponseEntity<>(
            StandardApiResponse.error(
                "Ha ocurrido un error inesperado. Por favor, intente nuevamente más tarde.", 
                "Error interno del servidor"
            ), 
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

