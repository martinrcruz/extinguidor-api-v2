package com.extinguidor.controller;

import com.extinguidor.service.CheckinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkin")
@RequiredArgsConstructor
@Tag(name = "Checkins", description = "Endpoints para gestión de checkins")
@SecurityRequirement(name = "bearer-jwt")
public class CheckinController {
    
    private final CheckinService checkinService;
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Listar todos los checkins")
    public ResponseEntity<List<com.extinguidor.dto.CheckinResponse>> getAllCheckins() {
        return ResponseEntity.ok(checkinService.findAllDTOs());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Obtener checkin por ID")
    public ResponseEntity<com.extinguidor.dto.CheckinResponse> getCheckinById(@PathVariable Long id) {
        return ResponseEntity.ok(checkinService.findByIdDTO(id));
    }
    
    @GetMapping("/report/{reportId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Listar checkins por reporte")
    public ResponseEntity<List<com.extinguidor.dto.CheckinResponse>> getCheckinsByReport(@PathVariable Long reportId) {
        return ResponseEntity.ok(checkinService.findByReportIdDTOs(reportId));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Listar checkins por usuario")
    public ResponseEntity<List<com.extinguidor.dto.CheckinResponse>> getCheckinsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(checkinService.findByUserIdDTOs(userId));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Crear checkin")
    public ResponseEntity<com.extinguidor.dto.CheckinResponse> createCheckin(@RequestBody com.extinguidor.dto.CheckinRequest checkinRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checkinService.createDTO(checkinRequest));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Actualizar checkin")
    public ResponseEntity<com.extinguidor.dto.CheckinResponse> updateCheckin(@PathVariable Long id, @RequestBody com.extinguidor.dto.CheckinRequest checkinRequest) {
        return ResponseEntity.ok(checkinService.updateDTO(id, checkinRequest));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Eliminar checkin")
    public ResponseEntity<Void> deleteCheckin(@PathVariable Long id) {
        checkinService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

