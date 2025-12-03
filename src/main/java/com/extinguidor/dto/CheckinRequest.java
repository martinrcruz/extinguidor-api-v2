package com.extinguidor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckinRequest {
    
    @NotNull(message = "El usuario es obligatorio")
    private Long userId;
    
    @NotNull(message = "El reporte es obligatorio")
    private Long reportId;
    
    private CheckinDataDTO checkin;
    
    private CheckinDataDTO checkout;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckinDataDTO {
        private Long startTime;
        private UbicationDTO ubication;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UbicationDTO {
        private Double lat;
        private Double lng;
    }
}

