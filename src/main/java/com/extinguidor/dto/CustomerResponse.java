package com.extinguidor.dto;

import com.extinguidor.model.enums.ContractType;
import com.extinguidor.model.enums.Rate;
import com.extinguidor.model.enums.RevisionFrequency;
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
public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private String nifCif;
    private String address;
    private String phone;
    private String contactName;
    private String code;
    private Boolean active;
    private ZoneBasicResponse zone;
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
    private Integer mi;
    private String tipo;
    private Double total;
    private List<DocumentDTO> documents = new ArrayList<>();
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentDTO {
        private String name;
        private String url;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ZoneBasicResponse {
        private Long id;
        private String name;
        private String code;
    }
}

