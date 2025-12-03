package com.extinguidor.model.entity;

import com.extinguidor.model.enums.ContractType;
import com.extinguidor.model.enums.Rate;
import com.extinguidor.model.enums.RevisionFrequency;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers", uniqueConstraints = {
    @UniqueConstraint(columnNames = "code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String nifCif;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    private String phone;
    
    @Column(nullable = false)
    private String contactName;
    
    @Column(nullable = false, unique = true)
    private String code;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String gestiona;
    
    @Column(name = "photo", columnDefinition = "TEXT")
    private String photo = "foto.jpg";
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Enumerated(EnumType.STRING)
    private ContractType type;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<CustomerContractSystem> contractSystems = new ArrayList<>();
    
    @Column(name = "average_time")
    private Integer averageTime;
    
    private String delegation;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "revision_frequency")
    private RevisionFrequency revisionFrequency;
    
    @Enumerated(EnumType.STRING)
    private Rate rate;
    
    @Column(name = "mi")
    @Builder.Default
    private Integer mi = 0;
    
    @Column(name = "tipo")
    @Builder.Default
    private String tipo = "Normal";
    
    private Double total;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<CustomerDocument> documents = new ArrayList<>();
    
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}

