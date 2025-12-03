package com.extinguidor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerReportDTO {
    private Long workerId;
    private String workerName;
    private Long totalPartes;
    private Long completedPartes;
}

