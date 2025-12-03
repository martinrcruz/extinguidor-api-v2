package com.extinguidor.dto;

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
public class CommentResponse {
    private Long id;
    private String comentario;
    private LocalDateTime date;
    private ParteBasicResponse parte;
    private Double lat;
    private Double lgn;
    private List<String> fotos = new ArrayList<>();
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParteBasicResponse {
        private Long id;
        private String title;
        private String description;
    }
}

