package com.extinguidor.dto;

import com.extinguidor.model.enums.ParteState;
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
public class RouteResponse {
    private Long id;
    private UserBasicResponse encargado;
    private RutaNBasicResponse name;
    private LocalDate date;
    private ParteState state;
    private VehicleBasicResponse vehicle;
    private List<UserBasicResponse> users = new ArrayList<>();
    private String comentarios;
    private List<MaterialBasicResponse> herramientas = new ArrayList<>();
    private Boolean eliminado;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserBasicResponse {
        private Long id;
        private String name;
        private String code;
        private String email;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RutaNBasicResponse {
        private Long id;
        private String name;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VehicleBasicResponse {
        private Long id;
        private String modelo;
        private String brand;
        private String matricula;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaterialBasicResponse {
        private Long id;
        private String name;
        private String code;
    }
}

