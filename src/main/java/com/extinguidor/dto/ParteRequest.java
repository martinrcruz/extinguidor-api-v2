package com.extinguidor.dto;

import com.extinguidor.model.enums.Categoria;
import com.extinguidor.model.enums.CoordinationMethod;
import com.extinguidor.model.enums.Frequency;
import com.extinguidor.model.enums.ParteState;
import com.extinguidor.model.enums.ParteType;
import jakarta.validation.constraints.NotBlank;
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
public class ParteRequest {
    
    @NotBlank(message = "El título es obligatorio")
    private String title;
    
    @NotBlank(message = "La descripción es obligatoria")
    private String description;
    
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate date;
    
    @NotNull(message = "El cliente es obligatorio")
    private Long customerId;
    
    @NotBlank(message = "La dirección es obligatoria")
    private String address;
    
    private ParteState state;
    
    private ParteType type;
    
    private Categoria categoria;
    
    private Boolean asignado;
    
    private Boolean eliminado;
    
    private Boolean periodico;
    
    private Frequency frequency;
    
    private LocalDate endDate;
    
    private CoordinationMethod coordinationMethod;
    
    private Integer gestiona;
    
    private Double facturacion;
    
    private Long rutaId;
    
    private Long workerId;
    
    private List<ParteComentarioDTO> comentarios = new ArrayList<>();
    
    private List<ParteDocumentoDTO> documentos = new ArrayList<>();
    
    private List<ParteArticuloDTO> articulos = new ArrayList<>();
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParteComentarioDTO {
        private String texto;
        private java.time.LocalDateTime fecha;
        private Long usuario;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParteDocumentoDTO {
        private String nombre;
        private String url;
        private String tipo;
        private java.time.LocalDateTime fecha;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParteArticuloDTO {
        private Integer cantidad;
        private String codigo;
        private String grupo;
        private String familia;
        private String descripcionArticulo;
        private Double precioVenta;
    }
}

