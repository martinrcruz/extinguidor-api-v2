package com.extinguidor.dto;

import com.extinguidor.model.enums.VehicleFuel;
import com.extinguidor.model.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponse {
    private Long id;
    private String modelo;
    private String brand;
    private String matricula;
    private VehicleFuel fuel;
    private VehicleType type;
    private String photo;
    private Integer kilometraje;
    private LocalDate fechaAdquisicion;
    private LocalDate fechaUltimoMantenimiento;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}

