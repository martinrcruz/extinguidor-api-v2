package com.extinguidor.repository;

import com.extinguidor.model.entity.Checkin;
import com.extinguidor.model.entity.Report;
import com.extinguidor.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long> {
    
    List<Checkin> findByReportId(Report report);
    
    List<Checkin> findByUserId(User user);
    
    List<Checkin> findByReportIdId(Long reportId);
    
    List<Checkin> findByUserIdId(Long userId);
}

