package com.extinguidor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReportDTO {
    private Long customerId;
    private String customerName;
    private Long totalPartes;
    private Double totalFacturacion;
}

