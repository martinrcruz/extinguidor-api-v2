package com.extinguidor.dto;

import com.extinguidor.model.enums.ParteState;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteRequest {
    
    @NotNull(message = "El encargado es obligatorio")
    private Long encargadoId;
    
    @NotNull(message = "El nombre de ruta es obligatorio")
    private Long rutaNId;
    
    private LocalDate date;
    
    private ParteState state;
    
    private Long vehicleId;
    
    private List<Long> userIds = new ArrayList<>();
    
    private String comentarios;
    
    private List<Long> materialIds = new ArrayList<>();
    
    private Boolean eliminado;
}

