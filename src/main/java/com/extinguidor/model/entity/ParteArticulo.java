package com.extinguidor.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "parte_articulos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ParteArticulo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parte_id", nullable = false)
    private Parte parte;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(nullable = false)
    private String codigo;
    
    @Column(nullable = false)
    private String grupo;
    
    @Column(nullable = false)
    private String familia;
    
    @Column(nullable = false, name = "descripcion_articulo")
    private String descripcionArticulo;
    
    @Column(nullable = false, name = "precio_venta")
    private Double precioVenta;
    
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}

