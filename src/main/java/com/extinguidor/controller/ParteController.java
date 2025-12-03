package com.extinguidor.controller;

import com.extinguidor.model.enums.ParteState;
import com.extinguidor.service.ParteService;
import com.extinguidor.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/partes")
@RequiredArgsConstructor
@Tag(name = "Partes", description = "Endpoints para gestión de partes de trabajo")
@SecurityRequirement(name = "bearer-jwt")
public class ParteController {
    
    private final ParteService parteService;
    private final RouteService routeService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar partes con paginación", description = "Obtiene una lista paginada de todas las partes de trabajo. Requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de partes obtenida exitosamente"),
        @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMIN")
    })
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getAllPartes(
            @Parameter(description = "Número de página (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página", example = "10") @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        org.springframework.data.domain.Page<com.extinguidor.dto.ParteResponse> partesPage = parteService.findAllDTOs(pageable);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("partes", partesPage.getContent());
        data.put("totalPages", partesPage.getTotalPages());
        data.put("totalItems", partesPage.getTotalElements());
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    // Endpoints específicos deben ir ANTES de los genéricos como /{id}
    // para evitar conflictos con el mapeo de recursos estáticos
    
    @GetMapping("/calendario/{date}/rutas")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener rutas por fecha para calendario")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getRutasByDateForCalendar(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<com.extinguidor.dto.RouteResponse> rutas = routeService.findByDateDTOs(date);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("rutas", rutas);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @GetMapping("/calendario/{date}/partes-no-asignados")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener partes no asignados hasta fin de mes para calendario")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getUnassignedUntilEndOfMonthForCalendar(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<com.extinguidor.dto.ParteResponse> partes = parteService.findUnassignedUntilEndOfMonthDTOs(date);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("partes", partes);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @GetMapping("/calendario/{date}/partes-finalizados")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener partes finalizados en mes para calendario")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getFinalizedInMonthForCalendar(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<com.extinguidor.dto.ParteResponse> partes = parteService.findFinalizedInMonthDTOs(date);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("partes", partes);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @GetMapping("/contrato/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener partes por cliente")
    public ResponseEntity<List<com.extinguidor.dto.ParteResponse>> getPartesByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(parteService.findByCustomerIdDTOs(customerId));
    }
    
    @GetMapping("/ruta/{routeId}")
    @Operation(summary = "Obtener partes por ruta")
    public ResponseEntity<List<com.extinguidor.dto.ParteResponse>> getPartesByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(parteService.findByRouteIdDTOs(routeId));
    }
    
    @GetMapping("/worker/{workerId}")
    @Operation(summary = "Obtener partes por trabajador")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getPartesByWorker(
            @PathVariable Long workerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<com.extinguidor.dto.ParteResponse> partes = parteService.findByWorkerIdDTOs(workerId, date);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("partes", partes);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @GetMapping("/noasignados")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener partes no asignados")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getUnassignedPartes(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<com.extinguidor.dto.ParteResponse> partes = parteService.findUnassignedPartesDTOs(date);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("partes", partes);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @GetMapping("/finalizadasEnMes")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener partes finalizados en mes")
    public ResponseEntity<List<com.extinguidor.dto.ParteResponse>> getFinalizedInMonth(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(parteService.findFinalizedInMonthDTOs(date));
    }
    
    @GetMapping("/noAsignadosEnMes")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener partes no asignados hasta fin de mes")
    public ResponseEntity<List<com.extinguidor.dto.ParteResponse>> getUnassignedUntilEndOfMonth(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(parteService.findUnassignedUntilEndOfMonthDTOs(date));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener parte por ID", description = "Obtiene los detalles de una parte de trabajo específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parte encontrada"),
        @ApiResponse(responseCode = "404", description = "Parte no encontrada")
    })
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getParteById(
            @Parameter(description = "ID de la parte", required = true, example = "1") @PathVariable Long id) {
        com.extinguidor.dto.ParteResponse parte = parteService.findByIdDTO(id);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("parte", parte);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear parte")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> createParte(@RequestBody com.extinguidor.dto.ParteRequest parteRequest) {
        com.extinguidor.dto.ParteResponse created = parteService.createDTO(parteRequest);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("parte", created);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(com.extinguidor.dto.StandardApiResponse.success(data, "Parte creado exitosamente"));
    }
    
    @PostMapping("/update/{id}")
    @Operation(summary = "Actualizar parte")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> updateParte(
            @PathVariable Long id,
            @RequestBody com.extinguidor.dto.ParteRequest parteRequest) {
        com.extinguidor.dto.ParteResponse updated = parteService.updateDTO(id, parteRequest);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("parte", updated);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data, "Parte actualizado exitosamente"));
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de parte")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> updateParteStatus(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> request) {
        String statusStr = request.get("status");
        ParteState state = ParteState.valueOf(statusStr);
        com.extinguidor.dto.ParteResponse updated = parteService.updateStatusDTO(id, state);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("parte", updated);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data, "Estado actualizado exitosamente"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar parte")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> deleteParte(@PathVariable Long id) {
        parteService.delete(id);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("message", "Parte eliminado exitosamente");
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
}

