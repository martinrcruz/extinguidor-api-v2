package com.extinguidor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    
    @NotNull(message = "El comentario es obligatorio")
    private String comentario;
    
    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime date;
    
    @NotNull(message = "El parte es obligatorio")
    private Long parteId;
    
    private Double lat;
    
    private Double lgn;
    
    private List<String> fotos = new ArrayList<>();
}

