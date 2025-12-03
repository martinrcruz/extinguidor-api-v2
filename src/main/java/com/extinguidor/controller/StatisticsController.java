package com.extinguidor.controller;

import com.extinguidor.dto.DashboardStatsDTO;
import com.extinguidor.dto.WorkerStatsDTO;
import com.extinguidor.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Tag(name = "Estadísticas", description = "Endpoints para estadísticas y métricas del sistema")
@SecurityRequirement(name = "bearer-jwt")
public class StatisticsController {
    
    private final StatisticsService statisticsService;
    
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener estadísticas del dashboard", 
               description = "Retorna todas las métricas y estadísticas para el dashboard principal")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(statisticsService.getDashboardStats());
    }
    
    @GetMapping("/workers")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener estadísticas de todos los trabajadores",
               description = "Retorna estadísticas detalladas de todos los trabajadores del sistema")
    public ResponseEntity<List<WorkerStatsDTO>> getWorkersStats() {
        return ResponseEntity.ok(statisticsService.getWorkersStats());
    }
    
    @GetMapping("/workers/{workerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener estadísticas de un trabajador específico",
               description = "Retorna estadísticas detalladas de un trabajador por su ID")
    public ResponseEntity<WorkerStatsDTO> getWorkerStats(@PathVariable Long workerId) {
        return ResponseEntity.ok(statisticsService.getWorkerStats(workerId));
    }
}

