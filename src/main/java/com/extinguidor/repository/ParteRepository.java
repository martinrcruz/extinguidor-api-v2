package com.extinguidor.repository;

import com.extinguidor.model.entity.Parte;
import com.extinguidor.model.entity.Route;
import com.extinguidor.model.enums.ParteState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ParteRepository extends JpaRepository<Parte, Long> {
    
    Page<Parte> findByEliminadoFalseOrderByCreatedDateDesc(Pageable pageable);
    
    List<Parte> findByEliminadoFalse();
    
    List<Parte> findByCustomerIdAndEliminadoFalseOrderByCreatedDateDesc(Long customerId);
    
    @Query("SELECT DISTINCT p FROM Parte p " +
           "LEFT JOIN FETCH p.customer " +
           "LEFT JOIN FETCH p.worker " +
           "LEFT JOIN FETCH p.ruta r " +
           "LEFT JOIN FETCH r.name " +
           "WHERE p.ruta = :ruta AND p.eliminado = false")
    List<Parte> findByRutaAndEliminadoFalse(@Param("ruta") Route ruta);
    
    @Query("SELECT p FROM Parte p WHERE p.ruta IN " +
           "(SELECT r FROM Route r WHERE :workerId MEMBER OF r.users) " +
           "AND p.eliminado = false")
    List<Parte> findByWorkerId(@Param("workerId") Long workerId);
    
    @Query("SELECT p FROM Parte p WHERE p.ruta IN " +
           "(SELECT r FROM Route r WHERE :workerId MEMBER OF r.users) " +
           "AND p.eliminado = false " +
           "AND YEAR(p.date) = :year AND MONTH(p.date) = :month")
    List<Parte> findByWorkerIdAndMonth(@Param("workerId") Long workerId, 
                                       @Param("year") int year, 
                                       @Param("month") int month);
    
    @Query("SELECT DISTINCT p FROM Parte p " +
           "LEFT JOIN FETCH p.customer " +
           "LEFT JOIN FETCH p.worker " +
           "WHERE p.asignado = false " +
           "AND p.eliminado = false " +
           "AND p.date >= :startDate AND p.date <= :endDate")
    List<Parte> findUnassignedPartes(@Param("startDate") LocalDate startDate, 
                                     @Param("endDate") LocalDate endDate);
    
    @Query("SELECT DISTINCT p FROM Parte p " +
           "LEFT JOIN FETCH p.customer " +
           "LEFT JOIN FETCH p.worker " +
           "LEFT JOIN FETCH p.ruta r " +
           "LEFT JOIN FETCH r.name " +
           "WHERE p.state = :state " +
           "AND YEAR(p.date) = :year AND MONTH(p.date) = :month " +
           "AND p.eliminado = false")
    List<Parte> findFinalizedInMonth(@Param("state") ParteState state,
                                      @Param("year") int year,
                                      @Param("month") int month);
    
    @Query("SELECT DISTINCT p FROM Parte p " +
           "LEFT JOIN FETCH p.customer " +
           "LEFT JOIN FETCH p.worker " +
           "WHERE p.asignado = false " +
           "AND p.eliminado = false " +
           "AND p.date <= :endDate")
    List<Parte> findUnassignedUntilEndOfMonth(@Param("endDate") LocalDate endDate);
    
    @Query("SELECT p FROM Parte p WHERE p.customer.id = :customerId " +
           "AND p.date >= :startDate AND p.date <= :endDate " +
           "AND p.eliminado = false")
    List<Parte> findOverlappingPartes(@Param("customerId") Long customerId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);
    
    @Query("SELECT p FROM Parte p WHERE p.worker.id = :workerId " +
           "AND p.eliminado = false")
    List<Parte> findByWorkerIdAndEliminadoFalse(@Param("workerId") Long workerId);
}

