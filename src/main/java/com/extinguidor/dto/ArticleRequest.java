package com.extinguidor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequest {
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer cantidad;
    
    @NotBlank(message = "El código es obligatorio")
    private String codigo;
    
    @NotBlank(message = "El grupo es obligatorio")
    private String grupo;
    
    @NotBlank(message = "La familia es obligatoria")
    private String familia;
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcionArticulo;
    
    @NotNull(message = "El precio de venta es obligatorio")
    @Positive(message = "El precio de venta debe ser positivo")
    private Double precioVenta;
    
    private Boolean eliminado;
}

