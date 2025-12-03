package com.extinguidor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZipcodeRequest {
    
    @NotBlank(message = "El código postal es obligatorio")
    private String codezip;
    
    private String name;
}

