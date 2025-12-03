package com.extinguidor.model.entity;

import com.extinguidor.model.enums.ReportStatus;
import com.extinguidor.model.enums.ReportType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Report {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "start_time", nullable = false)
    private Long startTime;
    
    @Column(name = "end_time", nullable = false)
    private Long endTime;
    
    @Column(name = "created_date", nullable = false)
    private Long createdDate;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "report_workers",
        joinColumns = @JoinColumn(name = "report_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private List<User> workersId = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customerId;
    
    @Column(columnDefinition = "TEXT")
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Customer contractId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route routeId;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "report_tools",
        joinColumns = @JoinColumn(name = "report_id"),
        inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    @Builder.Default
    private List<Material> tools = new ArrayList<>();
    
    @OneToMany(mappedBy = "reportId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Checkin> checkins = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "report_vehicles",
        joinColumns = @JoinColumn(name = "report_id"),
        inverseJoinColumns = @JoinColumn(name = "vehicle_id")
    )
    @Builder.Default
    private List<Vehicle> vehicle = new ArrayList<>();
    
    private String number;
    
    @Column(name = "last_modification")
    private Long lastModification;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;
    
    @CreatedDate
    @Column(name = "created_date_audit", updatable = false)
    private LocalDateTime createdDateAudit;
    
    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}

