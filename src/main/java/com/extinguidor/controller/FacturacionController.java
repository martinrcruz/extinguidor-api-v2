package com.extinguidor.controller;

import com.extinguidor.dto.FacturacionDailyAggregation;
import com.extinguidor.service.FacturacionService;
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
@RequestMapping("/facturacion")
@RequiredArgsConstructor
@Tag(name = "Facturación", description = "Endpoints para gestión de facturación")
@SecurityRequirement(name = "bearer-jwt")
public class FacturacionController {
    
    private final FacturacionService facturacionService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todas las facturaciones")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getAllFacturacion() {
        List<com.extinguidor.dto.FacturacionResponse> facturacion = facturacionService.findAllDTOs();
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("facturacion", facturacion);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener facturación por ID")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getFacturacionById(@PathVariable Long id) {
        com.extinguidor.dto.FacturacionResponse facturacion = facturacionService.findByIdDTO(id);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("facturacion", facturacion);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @GetMapping("/ruta/{rutaId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar facturaciones por ruta")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getFacturacionByRoute(@PathVariable Long rutaId) {
        List<com.extinguidor.dto.FacturacionResponse> facturacion = facturacionService.findByRouteIdDTOs(rutaId);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("facturacion", facturacion);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @GetMapping("/parte/{parteId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar facturaciones por parte")
    public ResponseEntity<List<com.extinguidor.dto.FacturacionResponse>> getFacturacionByParte(@PathVariable Long parteId) {
        return ResponseEntity.ok(facturacionService.findByParteIdDTOs(parteId));
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear facturación")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> createFacturacion(@RequestBody com.extinguidor.dto.FacturacionRequest facturacionRequest) {
        com.extinguidor.dto.FacturacionResponse created = facturacionService.createDTO(facturacionRequest);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("facturacion", created);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(com.extinguidor.dto.StandardApiResponse.success(data, "Facturación creada exitosamente"));
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar facturación")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> updateFacturacion(
            @PathVariable Long id,
            @RequestBody com.extinguidor.dto.FacturacionRequest facturacionRequest) {
        com.extinguidor.dto.FacturacionResponse updated = facturacionService.updateDTO(id, facturacionRequest);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("facturacion", updated);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data, "Facturación actualizada exitosamente"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar facturación")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> deleteFacturacion(@PathVariable Long id) {
        facturacionService.delete(id);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("message", "Facturación eliminada exitosamente");
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @GetMapping("/daily-aggregation")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener agregación de facturación por día")
    public ResponseEntity<List<FacturacionDailyAggregation>> getDailyAggregation() {
        return ResponseEntity.ok(facturacionService.getDailyAggregation());
    }
    
    @GetMapping("/daily-aggregation/range")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener agregación de facturación por día en un rango de fechas")
    public ResponseEntity<List<FacturacionDailyAggregation>> getDailyAggregationByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(facturacionService.getDailyAggregationByDateRange(startDate, endDate));
    }
}

