package com.extinguidor.model.entity;

import com.extinguidor.model.enums.Categoria;
import com.extinguidor.model.enums.CoordinationMethod;
import com.extinguidor.model.enums.Frequency;
import com.extinguidor.model.enums.ParteType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "parte_templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ParteTemplate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @Column(nullable = false)
    private String address;
    
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
    private Boolean periodico = false;
    
    @Enumerated(EnumType.STRING)
    private Frequency frequency;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CoordinationMethod coordinationMethod = CoordinationMethod.COORDINAR_HORARIOS;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer gestiona = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Double facturacion = 0.0;
    
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}

