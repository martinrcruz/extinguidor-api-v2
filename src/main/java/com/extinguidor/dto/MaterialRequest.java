package com.extinguidor.dto;

import com.extinguidor.model.enums.MaterialCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    
    @NotBlank(message = "El código es obligatorio")
    private String code;
    
    private String description;
    
    private String type;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaUltimoMantenimiento;
    
    private String color;
    
    private MaterialCategory categoria;
}

