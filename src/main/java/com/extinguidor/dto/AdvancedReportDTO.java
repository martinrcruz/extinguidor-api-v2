package com.extinguidor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedReportDTO {
    private String reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long totalPartes;
    private Long completedPartes;
    private Long pendingPartes;
    private Double totalFacturacion;
    private Double averageFacturacion;
    private Map<String, Long> partesByType;
    private Map<String, Long> partesByCategory;
    private Map<String, Long> partesByState;
    private List<CustomerReportDTO> topCustomers;
    private List<WorkerReportDTO> topWorkers;
    private List<DailyReportDTO> dailyReports;
}

