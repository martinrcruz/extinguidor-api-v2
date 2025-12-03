package com.extinguidor.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturacionRequest {
    
    @NotNull(message = "La facturación es obligatoria")
    @PositiveOrZero(message = "La facturación debe ser positiva o cero")
    private Double facturacion;
    
    private Long rutaId;
    
    private Long parteId;
}

