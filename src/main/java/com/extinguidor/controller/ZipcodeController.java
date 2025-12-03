package com.extinguidor.controller;

import com.extinguidor.dto.StandardApiResponse;
import com.extinguidor.dto.ZipcodeRequest;
import com.extinguidor.dto.ZipcodeResponse;
import com.extinguidor.service.ZipcodeService;
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
@RequestMapping("/zipcode")
@RequiredArgsConstructor
@Tag(name = "Zipcodes", description = "Endpoints para gestión de códigos postales")
@SecurityRequirement(name = "bearer-jwt")
public class ZipcodeController {
    
    private final ZipcodeService zipcodeService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos los códigos postales")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getAllZipcodes() {
        List<ZipcodeResponse> zipcodes = zipcodeService.findAllDTOs();
        Map<String, Object> data = new HashMap<>();
        data.put("zipcodes", zipcodes);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener código postal por ID")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getZipcodeById(@PathVariable Long id) {
        ZipcodeResponse zipcode = zipcodeService.findByIdDTO(id);
        Map<String, Object> data = new HashMap<>();
        data.put("zipcode", zipcode);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear código postal")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> createZipcode(@RequestBody ZipcodeRequest zipcodeRequest) {
        ZipcodeResponse created = zipcodeService.createDTO(zipcodeRequest);
        Map<String, Object> data = new HashMap<>();
        data.put("zipcode", created);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(StandardApiResponse.success(data, "Código postal creado exitosamente"));
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar código postal")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> updateZipcode(
            @PathVariable Long id,
            @RequestBody ZipcodeRequest zipcodeRequest) {
        ZipcodeResponse updated = zipcodeService.updateDTO(id, zipcodeRequest);
        Map<String, Object> data = new HashMap<>();
        data.put("zipcode", updated);
        return ResponseEntity.ok(StandardApiResponse.success(data, "Código postal actualizado exitosamente"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar código postal")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> deleteZipcode(@PathVariable Long id) {
        zipcodeService.delete(id);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Código postal eliminado exitosamente");
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
}

