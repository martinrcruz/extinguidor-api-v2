package com.extinguidor.controller;

import com.extinguidor.model.enums.ReportStatus;
import com.extinguidor.model.enums.ReportType;
import com.extinguidor.service.ReportService;
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
@RequestMapping("/report")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Endpoints para gestión de reportes")
@SecurityRequirement(name = "bearer-jwt")
public class ReportController {
    
    private final ReportService reportService;
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Listar todos los reportes")
    public ResponseEntity<List<com.extinguidor.dto.ReportResponse>> getAllReports() {
        return ResponseEntity.ok(reportService.findAllDTOs());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Obtener reporte por ID")
    public ResponseEntity<com.extinguidor.dto.ReportResponse> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.findByIdDTO(id));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Listar reportes por cliente")
    public ResponseEntity<List<com.extinguidor.dto.ReportResponse>> getReportsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(reportService.findByCustomerIdDTOs(customerId));
    }
    
    @GetMapping("/route/{routeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Listar reportes por ruta")
    public ResponseEntity<List<com.extinguidor.dto.ReportResponse>> getReportsByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(reportService.findByRouteIdDTOs(routeId));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Listar reportes por usuario")
    public ResponseEntity<List<com.extinguidor.dto.ReportResponse>> getReportsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.findByUserIdDTOs(userId));
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Listar reportes por estado")
    public ResponseEntity<List<com.extinguidor.dto.ReportResponse>> getReportsByStatus(@PathVariable ReportStatus status) {
        return ResponseEntity.ok(reportService.findByStatusDTOs(status));
    }
    
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Listar reportes por tipo")
    public ResponseEntity<List<com.extinguidor.dto.ReportResponse>> getReportsByType(@PathVariable ReportType type) {
        return ResponseEntity.ok(reportService.findByTypeDTOs(type));
    }
    
    @GetMapping("/worker/{workerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Listar reportes por trabajador")
    public ResponseEntity<List<com.extinguidor.dto.ReportResponse>> getReportsByWorker(@PathVariable Long workerId) {
        return ResponseEntity.ok(reportService.findByWorkerIdDTOs(workerId));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Crear reporte")
    public ResponseEntity<com.extinguidor.dto.ReportResponse> createReport(@RequestBody com.extinguidor.dto.ReportRequest reportRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.createDTO(reportRequest));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Actualizar reporte")
    public ResponseEntity<com.extinguidor.dto.ReportResponse> updateReport(@PathVariable Long id, @RequestBody com.extinguidor.dto.ReportRequest reportRequest) {
        return ResponseEntity.ok(reportService.updateDTO(id, reportRequest));
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Actualizar estado de reporte")
    public ResponseEntity<com.extinguidor.dto.ReportResponse> updateReportStatus(@PathVariable Long id, @RequestBody ReportStatus status) {
        return ResponseEntity.ok(reportService.updateStatusDTO(id, status));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar reporte")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

