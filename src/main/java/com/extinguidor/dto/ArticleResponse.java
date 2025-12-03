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
public class ArticleResponse {
    private Long id;
    private Integer cantidad;
    private String codigo;
    private String grupo;
    private String familia;
    private String descripcionArticulo;
    private Double precioVenta;
    private Boolean eliminado;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}

