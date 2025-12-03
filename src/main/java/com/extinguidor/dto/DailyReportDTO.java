package com.extinguidor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyReportDTO {
    private LocalDate date;
    private Long totalPartes;
    private Long completedPartes;
    private Double totalFacturacion;
}

