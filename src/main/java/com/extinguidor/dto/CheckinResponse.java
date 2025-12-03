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
public class CheckinResponse {
    private Long id;
    private CheckinDataDTO checkin;
    private CheckinDataDTO checkout;
    private UserBasicResponse userId;
    private ReportBasicResponse reportId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
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
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserBasicResponse {
        private Long id;
        private String name;
        private String code;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportBasicResponse {
        private Long id;
        private String title;
        private String description;
    }
}

