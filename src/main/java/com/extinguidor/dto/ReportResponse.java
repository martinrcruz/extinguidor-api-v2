package com.extinguidor.dto;

import com.extinguidor.model.enums.ReportStatus;
import com.extinguidor.model.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {
    private Long id;
    private Long startTime;
    private Long endTime;
    private Long createdDate;
    private List<UserBasicResponse> workersId = new ArrayList<>();
    private ReportType type;
    private ReportStatus status;
    private CustomerBasicResponse customerId;
    private String title;
    private String description;
    private CustomerBasicResponse contractId;
    private RouteBasicResponse routeId;
    private List<MaterialBasicResponse> tools = new ArrayList<>();
    private List<VehicleBasicResponse> vehicle = new ArrayList<>();
    private String number;
    private Long lastModification;
    private UserBasicResponse userId;
    private LocalDateTime createdDateAudit;
    private LocalDateTime updatedDate;
    
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
    public static class CustomerBasicResponse {
        private Long id;
        private String name;
        private String code;
    }
    
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
    public static class MaterialBasicResponse {
        private Long id;
        private String name;
        private String code;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VehicleBasicResponse {
        private Long id;
        private String modelo;
        private String matricula;
    }
}

