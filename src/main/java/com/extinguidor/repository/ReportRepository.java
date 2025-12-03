package com.extinguidor.repository;

import com.extinguidor.model.entity.Report;
import com.extinguidor.model.entity.Customer;
import com.extinguidor.model.entity.Route;
import com.extinguidor.model.entity.User;
import com.extinguidor.model.enums.ReportStatus;
import com.extinguidor.model.enums.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    
    List<Report> findByCustomerId(Customer customer);
    
    List<Report> findByRouteId(Route route);
    
    List<Report> findByUserId(User user);
    
    List<Report> findByStatus(ReportStatus status);
    
    List<Report> findByType(ReportType type);
    
    @Query("SELECT r FROM Report r WHERE r.userId = :user AND r.status = :status")
    List<Report> findByUserIdAndStatus(@Param("user") User user, @Param("status") ReportStatus status);
    
    @Query("SELECT r FROM Report r JOIN r.workersId w WHERE w.id = :workerId")
    List<Report> findByWorkerId(@Param("workerId") Long workerId);
    
    @Query("SELECT r FROM Report r WHERE r.customerId = :customer AND r.status = :status")
    List<Report> findByCustomerIdAndStatus(@Param("customer") Customer customer, @Param("status") ReportStatus status);
}

