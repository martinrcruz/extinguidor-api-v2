package com.extinguidor.dto;

import com.extinguidor.model.enums.AlertState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertResponse {
    private Long id;
    private String message;
    private AlertState state;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}

