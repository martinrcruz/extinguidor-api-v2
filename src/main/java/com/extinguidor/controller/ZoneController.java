package com.extinguidor.controller;

import com.extinguidor.dto.StandardApiResponse;
import com.extinguidor.dto.ZoneRequest;
import com.extinguidor.dto.ZoneResponse;
import com.extinguidor.service.ZoneService;
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
@RequestMapping("/zone")
@RequiredArgsConstructor
@Tag(name = "Zonas", description = "Endpoints para gestión de zonas")
@SecurityRequirement(name = "bearer-jwt")
public class ZoneController {
    
    private final ZoneService zoneService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todas las zonas")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getAllZones() {
        List<ZoneResponse> zones = zoneService.findAllDTOs();
        Map<String, Object> data = new HashMap<>();
        data.put("zones", zones);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener zona por ID")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getZoneById(@PathVariable Long id) {
        ZoneResponse zone = zoneService.findByIdDTO(id);
        Map<String, Object> data = new HashMap<>();
        data.put("zone", zone);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear zona")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> createZone(@RequestBody ZoneRequest zoneRequest) {
        ZoneResponse created = zoneService.createDTO(zoneRequest);
        Map<String, Object> data = new HashMap<>();
        data.put("zone", created);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(StandardApiResponse.success(data, "Zona creada exitosamente"));
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar zona")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> updateZone(
            @PathVariable Long id,
            @RequestBody ZoneRequest zoneRequest) {
        ZoneResponse updated = zoneService.updateDTO(id, zoneRequest);
        Map<String, Object> data = new HashMap<>();
        data.put("zone", updated);
        return ResponseEntity.ok(StandardApiResponse.success(data, "Zona actualizada exitosamente"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar zona")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> deleteZone(@PathVariable Long id) {
        zoneService.delete(id);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Zona eliminada exitosamente");
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
}

