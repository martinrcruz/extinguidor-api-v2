package com.extinguidor.dto;

import com.extinguidor.model.enums.VehicleFuel;
import com.extinguidor.model.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}

