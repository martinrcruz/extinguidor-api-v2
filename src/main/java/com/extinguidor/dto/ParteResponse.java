package com.extinguidor.dto;

import com.extinguidor.model.enums.Categoria;
import com.extinguidor.model.enums.CoordinationMethod;
import com.extinguidor.model.enums.Frequency;
import com.extinguidor.model.enums.ParteState;
import com.extinguidor.model.enums.ParteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParteResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private CustomerBasicResponse customer;
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
    private RouteBasicResponse ruta;
    private UserBasicResponse worker;
    private LocalDateTime finalizadoTime;
    private List<ParteComentarioDTO> comentarios = new ArrayList<>();
    private List<ParteDocumentoDTO> documentos = new ArrayList<>();
    private List<ParteArticuloDTO> articulos = new ArrayList<>();
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParteComentarioDTO {
        private String texto;
        private LocalDateTime fecha;
        private Long usuario;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParteDocumentoDTO {
        private String nombre;
        private String url;
        private String tipo;
        private LocalDateTime fecha;
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
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerBasicResponse {
        private Long id;
        private String name;
        private String code;
        private String email;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteBasicResponse {
        private Long id;
        private String name;
        private LocalDate date;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserBasicResponse {
        private Long id;
        private String name;
        private String code;
        private String email;
    }
}

