package com.extinguidor.dto;

import com.extinguidor.model.enums.ContractType;
import com.extinguidor.model.enums.Rate;
import com.extinguidor.model.enums.RevisionFrequency;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    private String name;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;
    
    @NotBlank(message = "El NIF/CIF es obligatorio")
    private String nifCif;
    
    @NotBlank(message = "La dirección es obligatoria")
    private String address;
    
    @NotBlank(message = "El teléfono es obligatorio")
    private String phone;
    
    @NotBlank(message = "El nombre de contacto es obligatorio")
    private String contactName;
    
    @NotBlank(message = "El código es obligatorio")
    private String code;
    
    private Boolean active;
    
    @JsonProperty("zoneId")
    private Long zoneId;
    
    @JsonProperty("zone")
    private Object zone; // Acepta tanto String como Long para compatibilidad con frontend
    
    private String description;
    
    private String gestiona;
    
    private String photo;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private ContractType type;
    
    private List<String> contractSystems = new ArrayList<>();
    
    private Integer averageTime;
    
    private String delegation;
    
    private RevisionFrequency revisionFrequency;
    
    private Rate rate;
    
    @JsonAlias({"MI"})
    private Integer mi; // Acepta tanto "mi" como "MI" para compatibilidad con frontend
    
    private String tipo;
    
    private Double total;
    
    private List<DocumentDTO> documents = new ArrayList<>();
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentDTO {
        private String name;
        private String url;
    }
}

