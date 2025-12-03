package com.extinguidor.controller;

import com.extinguidor.model.entity.Parte;
import com.extinguidor.model.entity.ParteTemplate;
import com.extinguidor.service.ParteTemplateService;
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
@RequestMapping("/parte-templates")
@RequiredArgsConstructor
@Tag(name = "Plantillas de Partes", description = "Endpoints para gestión de plantillas de partes")
@SecurityRequirement(name = "bearer-jwt")
public class ParteTemplateController {
    
    private final ParteTemplateService templateService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todas las plantillas")
    public ResponseEntity<List<ParteTemplate>> getAllTemplates() {
        return ResponseEntity.ok(templateService.findAll());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener plantilla por ID")
    public ResponseEntity<ParteTemplate> getTemplateById(@PathVariable Long id) {
        return ResponseEntity.ok(templateService.findById(id));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener plantillas por cliente")
    public ResponseEntity<List<ParteTemplate>> getTemplatesByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(templateService.findByCustomerId(customerId));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear plantilla")
    public ResponseEntity<ParteTemplate> createTemplate(@RequestBody ParteTemplate template) {
        return ResponseEntity.status(HttpStatus.CREATED).body(templateService.create(template));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar plantilla")
    public ResponseEntity<ParteTemplate> updateTemplate(@PathVariable Long id, @RequestBody ParteTemplate template) {
        return ResponseEntity.ok(templateService.update(id, template));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar plantilla")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        templateService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/create-parte")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear parte desde plantilla")
    public ResponseEntity<Parte> createParteFromTemplate(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(templateService.createParteFromTemplate(id, date));
    }
}

