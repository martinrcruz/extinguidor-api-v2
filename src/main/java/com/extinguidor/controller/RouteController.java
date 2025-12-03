package com.extinguidor.controller;

import com.extinguidor.model.entity.Parte;
import com.extinguidor.service.ParteService;
import com.extinguidor.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rutas")
@RequiredArgsConstructor
@Tag(name = "Rutas", description = "Endpoints para gestión de rutas")
@SecurityRequirement(name = "bearer-jwt")
public class RouteController {
    
    private final RouteService routeService;
    private final ParteService parteService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todas las rutas")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getAllRoutes() {
        List<com.extinguidor.dto.RouteResponse> rutas = routeService.findAllDTOs();
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("rutas", rutas);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener ruta por ID")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getRouteById(@PathVariable Long id) {
        com.extinguidor.dto.RouteResponse ruta = routeService.findByIdDTO(id);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("ruta", ruta);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @GetMapping("/worker/{workerId}")
    @Operation(summary = "Obtener rutas por trabajador")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getRoutesByWorker(
            @PathVariable Long workerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<com.extinguidor.dto.RouteResponse> rutas = routeService.findByUserIdDTOs(workerId, date);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("rutas", rutas);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @GetMapping("/disponibles")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener rutas disponibles")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getAvailableRoutes(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<com.extinguidor.dto.RouteResponse> rutas = routeService.findAvailableRoutesDTOs(date);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("rutas", rutas);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @GetMapping("/porFecha/{fecha}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener rutas por fecha")
    public ResponseEntity<List<com.extinguidor.dto.RouteResponse>> getRoutesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(routeService.findByDateDTOs(fecha));
    }
    
    @GetMapping("/{routeId}/partes")
    @Operation(summary = "Obtener partes de una ruta")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getPartesByRoute(@PathVariable Long routeId) {
        List<com.extinguidor.dto.ParteResponse> partes = parteService.findByRouteIdDTOs(routeId);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("partes", partes);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear ruta")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> createRoute(@RequestBody com.extinguidor.dto.RouteRequest routeRequest) {
        com.extinguidor.dto.RouteResponse created = routeService.createDTO(routeRequest);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("ruta", created);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(com.extinguidor.dto.StandardApiResponse.success(data, "Ruta creada exitosamente"));
    }
    
    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar ruta")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> updateRoute(
            @PathVariable Long id,
            @RequestBody com.extinguidor.dto.RouteRequest routeRequest) {
        com.extinguidor.dto.RouteResponse updated = routeService.updateDTO(id, routeRequest);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("ruta", updated);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data, "Ruta actualizada exitosamente"));
    }
    
    @PostMapping("/{id}/asignarPartes")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Asignar partes a una ruta")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> assignPartesToRoute(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, Object> request) {
        routeService.findById(id); // Validar que existe
        @SuppressWarnings("unchecked")
        List<Object> parteIdsRaw = (List<Object>) request.get("parteIds");
        if (parteIdsRaw == null || parteIdsRaw.isEmpty()) {
            java.util.Map<String, Object> data = new java.util.HashMap<>();
            data.put("message", "No se proporcionaron IDs de partes");
            return ResponseEntity.badRequest()
                .body(com.extinguidor.dto.StandardApiResponse.error("No se proporcionaron IDs de partes"));
        }
        // Convertir a Long, manejando tanto Integer como Long
        List<Long> parteIds = parteIdsRaw.stream()
            .map(idObj -> {
                if (idObj instanceof Long) {
                    return (Long) idObj;
                } else if (idObj instanceof Integer) {
                    return ((Integer) idObj).longValue();
                } else if (idObj instanceof Number) {
                    return ((Number) idObj).longValue();
                } else {
                    throw new IllegalArgumentException("Los IDs de partes deben ser números");
                }
            })
            .collect(java.util.stream.Collectors.toList());
        parteService.assignMultipleToRoute(parteIds, id);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("message", "Partes asignados exitosamente");
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar ruta (lógica)")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

