package com.extinguidor.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ubication {
    @Column(name = "lat")
    private Double lat;
    
    @Column(name = "lng")
    private Double lng;
}

