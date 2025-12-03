package com.extinguidor.model.entity;

import com.extinguidor.model.enums.ParteState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Route {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encargado_id", nullable = false)
    private User encargado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruta_n_id", nullable = false)
    private RutaN name;
    
    @Column(name = "date")
    private LocalDate date;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ParteState state = ParteState.Pendiente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "route_users",
        joinColumns = @JoinColumn(name = "route_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @BatchSize(size = 20)
    @Builder.Default
    private List<User> users = new ArrayList<>();
    
    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String comentarios = "";
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "route_materials",
        joinColumns = @JoinColumn(name = "route_id"),
        inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    @BatchSize(size = 20)
    @Builder.Default
    private List<Material> herramientas = new ArrayList<>();
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean eliminado = false;
    
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}

