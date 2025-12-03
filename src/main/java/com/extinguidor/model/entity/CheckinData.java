package com.extinguidor.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckinData {
    @Column(name = "start_time", nullable = false)
    private Long startTime;
    
    @Embedded
    private Ubication ubication;
}

