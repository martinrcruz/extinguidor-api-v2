package com.extinguidor.controller;

import com.extinguidor.dto.AdvancedReportDTO;
import com.extinguidor.service.AdvancedReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports/advanced")
@RequiredArgsConstructor
@Tag(name = "Reportes Avanzados", description = "Endpoints para reportes avanzados con análisis detallado")
@SecurityRequirement(name = "bearer-jwt")
public class AdvancedReportController {
    
    private final AdvancedReportService reportService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Generar reporte avanzado", 
               description = "Genera un reporte avanzado con análisis detallado en un rango de fechas")
    public ResponseEntity<AdvancedReportDTO> generateReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.generateReport(startDate, endDate));
    }
}

