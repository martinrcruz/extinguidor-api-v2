package com.extinguidor.controller;

import com.extinguidor.dto.StandardApiResponse;
import com.extinguidor.service.RutaNService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rutasn")
@RequiredArgsConstructor
@Tag(name = "RutaN", description = "Endpoints para gestión de nombres de rutas")
@SecurityRequirement(name = "bearer-jwt")
public class RutaNController {
    
    private final RutaNService rutaNService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos los nombres de rutas")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getAllRutaN() {
        List<com.extinguidor.dto.RutaNResponse> rutasN = rutaNService.findAllDTOs();
        Map<String, Object> data = new HashMap<>();
        data.put("rutasN", rutasN);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener nombre de ruta por ID")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getRutaNById(@PathVariable Long id) {
        com.extinguidor.dto.RutaNResponse rutaN = rutaNService.findByIdDTO(id);
        Map<String, Object> data = new HashMap<>();
        data.put("rutaN", rutaN);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nombre de ruta")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> createRutaN(@RequestBody com.extinguidor.dto.RutaNRequest rutaNRequest) {
        com.extinguidor.dto.RutaNResponse created = rutaNService.createDTO(rutaNRequest);
        Map<String, Object> data = new HashMap<>();
        data.put("rutaN", created);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(StandardApiResponse.success(data, "Nombre de ruta creado exitosamente"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar nombre de ruta")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> updateRutaN(
            @PathVariable Long id,
            @RequestBody com.extinguidor.dto.RutaNRequest rutaNRequest) {
        com.extinguidor.dto.RutaNResponse updated = rutaNService.updateDTO(id, rutaNRequest);
        Map<String, Object> data = new HashMap<>();
        data.put("rutaN", updated);
        return ResponseEntity.ok(StandardApiResponse.success(data, "Nombre de ruta actualizado exitosamente"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar nombre de ruta")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> deleteRutaN(@PathVariable Long id) {
        rutaNService.delete(id);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Nombre de ruta eliminado exitosamente");
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
}

