package com.extinguidor.model.entity;

import com.extinguidor.model.enums.Categoria;
import com.extinguidor.model.enums.CoordinationMethod;
import com.extinguidor.model.enums.Frequency;
import com.extinguidor.model.enums.ParteState;
import com.extinguidor.model.enums.ParteType;
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
@Table(name = "partes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Parte {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @Column(nullable = false)
    private String address;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ParteState state = ParteState.Pendiente;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ParteType type = ParteType.Mantenimiento;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Categoria categoria = Categoria.Extintores;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean asignado = false;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean eliminado = false;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean periodico = false;
    
    @Enumerated(EnumType.STRING)
    private Frequency frequency;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "coordination_method")
    @Builder.Default
    private CoordinationMethod coordinationMethod = CoordinationMethod.COORDINAR_HORARIOS;
    
    @Column(name = "gestiona")
    @Builder.Default
    private Integer gestiona = 0;
    
    @Column(name = "facturacion")
    @Builder.Default
    private Double facturacion = 0.0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruta_id")
    private Route ruta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private User worker;
    
    @Column(name = "finalizado_time")
    private LocalDateTime finalizadoTime;
    
    @OneToMany(mappedBy = "parte", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @BatchSize(size = 20)
    @Builder.Default
    private List<ParteComentario> comentarios = new ArrayList<>();
    
    @OneToMany(mappedBy = "parte", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @BatchSize(size = 20)
    @Builder.Default
    private List<ParteDocumento> documentos = new ArrayList<>();
    
    @OneToMany(mappedBy = "parte", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @BatchSize(size = 20)
    @Builder.Default
    private List<ParteArticulo> articulos = new ArrayList<>();
    
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}

