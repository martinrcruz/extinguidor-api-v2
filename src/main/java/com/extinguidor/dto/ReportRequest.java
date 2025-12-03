package com.extinguidor.dto;

import com.extinguidor.model.enums.ReportStatus;
import com.extinguidor.model.enums.ReportType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    
    @NotNull(message = "El tiempo de inicio es obligatorio")
    private Long startTime;
    
    @NotNull(message = "El tiempo de fin es obligatorio")
    private Long endTime;
    
    @NotNull(message = "La fecha de creación es obligatoria")
    private Long createdDate;
    
    private List<Long> workersId = new ArrayList<>();
    
    @NotNull(message = "El tipo es obligatorio")
    private ReportType type;
    
    @NotNull(message = "El estado es obligatorio")
    private ReportStatus status;
    
    @NotNull(message = "El cliente es obligatorio")
    private Long customerId;
    
    private String title;
    
    private String description;
    
    private Long contractId;
    
    private Long routeId;
    
    private List<Long> toolIds = new ArrayList<>();
    
    private List<Long> vehicleIds = new ArrayList<>();
    
    private String number;
    
    private Long lastModification;
    
    private Long userId;
}

