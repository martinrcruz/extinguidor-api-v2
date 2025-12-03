package com.extinguidor.model.entity;

import com.extinguidor.model.enums.VehicleFuel;
import com.extinguidor.model.enums.VehicleType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String modelo;
    
    @Column(nullable = false)
    private String brand;
    
    @Column(nullable = false, unique = true)
    private String matricula;
    
    @Enumerated(EnumType.STRING)
    private VehicleFuel fuel;
    
    @Enumerated(EnumType.STRING)
    private VehicleType type;
    
    @Column(name = "photo", columnDefinition = "TEXT")
    private String photo = "auto.jpg";
    
    @Column(name = "kilometraje")
    private Integer kilometraje;
    
    @Column(name = "fecha_adquisicion")
    private LocalDate fechaAdquisicion;
    
    @Column(name = "fecha_ultimo_mantenimiento")
    private LocalDate fechaUltimoMantenimiento;
    
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}

