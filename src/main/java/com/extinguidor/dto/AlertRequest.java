package com.extinguidor.dto;

import com.extinguidor.model.enums.AlertState;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertRequest {
    
    @NotBlank(message = "El mensaje es obligatorio")
    private String message;
    
    private AlertState state;
}

