package com.extinguidor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutaNRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
}

