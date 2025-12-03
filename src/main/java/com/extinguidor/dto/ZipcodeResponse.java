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
public class ZipcodeResponse {
    private Long id;
    private String codezip;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}

