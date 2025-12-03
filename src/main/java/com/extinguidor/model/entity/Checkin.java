package com.extinguidor.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "checkins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Checkin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "startTime", column = @Column(name = "checkin_start_time", nullable = false)),
        @AttributeOverride(name = "ubication.lat", column = @Column(name = "checkin_lat")),
        @AttributeOverride(name = "ubication.lng", column = @Column(name = "checkin_lng"))
    })
    private CheckinData checkin;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "startTime", column = @Column(name = "checkout_start_time")),
        @AttributeOverride(name = "ubication.lat", column = @Column(name = "checkout_lat")),
        @AttributeOverride(name = "ubication.lng", column = @Column(name = "checkout_lng"))
    })
    private CheckinData checkout;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report reportId;
    
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}

