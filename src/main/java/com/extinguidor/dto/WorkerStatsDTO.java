package com.extinguidor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerStatsDTO {
    private Long workerId;
    private String workerName;
    private String workerEmail;
    private String workerPhoto;
    private Long totalPartes;
    private Long pendingPartes;
    private Long inProcessPartes;
    private Long completedPartes;
    private Long totalRoutes;
    private Long activeRoutes;
    private Double totalFacturacion;
    private LocalDateTime lastActivity;
    private Boolean isActive;
}

