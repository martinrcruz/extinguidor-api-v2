package com.extinguidor.dto;

import com.extinguidor.model.enums.VehicleFuel;
import com.extinguidor.model.enums.VehicleType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest {
    
    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;
    
    @NotBlank(message = "La marca es obligatoria")
    private String brand;
    
    @NotBlank(message = "La matrícula es obligatoria")
    private String matricula;
    
    private VehicleFuel fuel;
    
    private VehicleType type;
    
    private String photo;
    
    private Integer kilometraje;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaAdquisicion;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaUltimoMantenimiento;
}

