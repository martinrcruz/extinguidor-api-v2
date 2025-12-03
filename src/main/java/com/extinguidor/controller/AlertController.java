package com.extinguidor.controller;

import com.extinguidor.dto.AlertRequest;
import com.extinguidor.dto.AlertResponse;
import com.extinguidor.dto.StandardApiResponse;
import com.extinguidor.model.enums.AlertState;
import com.extinguidor.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alertas")
@RequiredArgsConstructor
@Tag(name = "Alertas", description = "Endpoints para gestión de alertas")
public class AlertController {
    
    private final AlertService alertService;
    
    @GetMapping
    @Operation(summary = "Listar todas las alertas")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getAllAlerts() {
        List<AlertResponse> alertas = alertService.findAllDTOs();
        Map<String, Object> data = new HashMap<>();
        data.put("alertas", alertas);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener alerta por ID")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getAlertById(@PathVariable Long id) {
        AlertResponse alerta = alertService.findByIdDTO(id);
        Map<String, Object> data = new HashMap<>();
        data.put("alerta", alerta);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @PostMapping
    @Operation(summary = "Crear alerta")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> createAlert(@RequestBody AlertRequest alertRequest) {
        AlertResponse created = alertService.createDTO(alertRequest);
        Map<String, Object> data = new HashMap<>();
        data.put("alerta", created);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(StandardApiResponse.success(data, "Alerta creada exitosamente"));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar estado de alerta")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> updateAlert(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> request) {
        AlertState state = AlertState.valueOf(request.get("state").toString());
        AlertResponse updated = alertService.updateDTO(id, state);
        Map<String, Object> data = new HashMap<>();
        data.put("alerta", updated);
        return ResponseEntity.ok(StandardApiResponse.success(data, "Alerta actualizada exitosamente"));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar alerta")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> deleteAlert(@PathVariable Long id) {
        alertService.delete(id);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Alerta eliminada exitosamente");
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
}

