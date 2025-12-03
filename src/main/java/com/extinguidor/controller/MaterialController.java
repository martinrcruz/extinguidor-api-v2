package com.extinguidor.controller;

import com.extinguidor.dto.StandardApiResponse;
import com.extinguidor.service.MaterialService;
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
@RequestMapping("/material")
@RequiredArgsConstructor
@Tag(name = "Materiales", description = "Endpoints para gestión de materiales")
@SecurityRequirement(name = "bearer-jwt")
public class MaterialController {
    
    private final MaterialService materialService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos los materiales")
    public ResponseEntity<StandardApiResponse<List<com.extinguidor.dto.MaterialResponse>>> getAllMaterials() {
        List<com.extinguidor.dto.MaterialResponse> materials = materialService.findAllDTOs();
        return ResponseEntity.ok(StandardApiResponse.success(materials));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener material por ID")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getMaterialById(@PathVariable Long id) {
        com.extinguidor.dto.MaterialResponse material = materialService.findByIdDTO(id);
        Map<String, Object> data = new HashMap<>();
        data.put("material", material);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear material")
    public ResponseEntity<StandardApiResponse<com.extinguidor.dto.MaterialResponse>> createMaterial(@RequestBody com.extinguidor.dto.MaterialRequest materialRequest) {
        com.extinguidor.dto.MaterialResponse created = materialService.createDTO(materialRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(StandardApiResponse.success(created, "Material creado exitosamente"));
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar material")
    public ResponseEntity<StandardApiResponse<com.extinguidor.dto.MaterialResponse>> updateMaterial(
            @PathVariable Long id,
            @RequestBody com.extinguidor.dto.MaterialRequest materialRequest) {
        com.extinguidor.dto.MaterialResponse updated = materialService.updateDTO(id, materialRequest);
        return ResponseEntity.ok(StandardApiResponse.success(updated, "Material actualizado exitosamente"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar material")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> deleteMaterial(@PathVariable Long id) {
        materialService.delete(id);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Material eliminado exitosamente");
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
}

