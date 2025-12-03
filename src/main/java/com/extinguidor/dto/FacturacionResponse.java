package com.extinguidor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturacionResponse {
    private Long id;
    private Double facturacion;
    private RouteBasicResponse ruta;
    private ParteBasicResponse parte;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteBasicResponse {
        private Long id;
        private String name;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParteBasicResponse {
        private Long id;
        private String title;
        private String description;
    }
}

