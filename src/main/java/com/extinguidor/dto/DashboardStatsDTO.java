package com.extinguidor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalPartes;
    private Long pendingPartes;
    private Long inProcessPartes;
    private Long finishedPartes;
    private Long totalCustomers;
    private Long activeCustomers;
    private Long inactiveCustomers;
    private Long totalRoutes;
    private Long pendingRoutes;
    private Long inProcessRoutes;
    private Long finishedRoutes;
    private Long totalUsers;
    private Long activeUsers;
    private Long totalWorkers;
    private Double totalFacturacion;
    private Double facturacionThisMonth;
    private Double facturacionLastMonth;
    private Map<String, Long> partesByType;
    private Map<String, Long> partesByCategory;
    private Map<String, Long> partesByState;
    private Long unassignedPartes;
    private Long periodicPartes;
}

