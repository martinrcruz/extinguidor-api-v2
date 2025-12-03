package com.extinguidor.controller;

import com.extinguidor.dto.StandardApiResponse;
import com.extinguidor.dto.VehicleRequest;
import com.extinguidor.dto.VehicleResponse;
import com.extinguidor.service.VehicleService;
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
@RequestMapping("/vehicle")
@RequiredArgsConstructor
@Tag(name = "Vehículos", description = "Endpoints para gestión de vehículos")
@SecurityRequirement(name = "bearer-jwt")
public class VehicleController {
    
    private final VehicleService vehicleService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos los vehículos")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getAllVehicles() {
        List<VehicleResponse> vehicles = vehicleService.findAllDTOs();
        Map<String, Object> data = new HashMap<>();
        data.put("vehicles", vehicles);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener vehículo por ID")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getVehicleById(@PathVariable Long id) {
        VehicleResponse vehicle = vehicleService.findByIdDTO(id);
        Map<String, Object> data = new HashMap<>();
        data.put("vehicle", vehicle);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear vehículo")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> createVehicle(@RequestBody VehicleRequest vehicleRequest) {
        VehicleResponse created = vehicleService.createDTO(vehicleRequest);
        Map<String, Object> data = new HashMap<>();
        data.put("vehicle", created);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(StandardApiResponse.success(data, "Vehículo creado exitosamente"));
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar vehículo")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> updateVehicle(
            @PathVariable Long id,
            @RequestBody VehicleRequest vehicleRequest) {
        VehicleResponse updated = vehicleService.updateDTO(id, vehicleRequest);
        Map<String, Object> data = new HashMap<>();
        data.put("vehicle", updated);
        return ResponseEntity.ok(StandardApiResponse.success(data, "Vehículo actualizado exitosamente"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar vehículo")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> deleteVehicle(@PathVariable Long id) {
        vehicleService.delete(id);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Vehículo eliminado exitosamente");
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
}

