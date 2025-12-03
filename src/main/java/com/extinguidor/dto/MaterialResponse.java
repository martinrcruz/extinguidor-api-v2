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
public class MaterialResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String type;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}

