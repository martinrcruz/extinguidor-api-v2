package com.extinguidor.repository;

import com.extinguidor.model.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    
    List<Route> findByEliminadoFalseOrderByIdDesc();
    
    @Query("SELECT r FROM Route r WHERE :userId MEMBER OF r.users " +
           "AND r.eliminado = false")
    List<Route> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT r FROM Route r WHERE :userId MEMBER OF r.users " +
           "AND r.eliminado = false " +
           "AND YEAR(r.date) = :year AND MONTH(r.date) = :month")
    List<Route> findByUserIdAndMonth(@Param("userId") Long userId,
                                      @Param("year") int year,
                                      @Param("month") int month);
    
    @Query("SELECT r FROM Route r WHERE r.eliminado = false " +
           "AND r.date >= :startDate AND r.date < :endDate")
    List<Route> findAvailableRoutesByDateRange(@Param("startDate") LocalDate startDate, 
                                               @Param("endDate") LocalDate endDate);
    
    @Query("SELECT r FROM Route r WHERE r.eliminado = false " +
           "AND FUNCTION('YEAR', r.date) = :year AND FUNCTION('MONTH', r.date) = :month")
    List<Route> findAvailableRoutes(@Param("year") int year, @Param("month") int month);
    
    @Query("SELECT DISTINCT r FROM Route r " +
           "LEFT JOIN FETCH r.encargado " +
           "LEFT JOIN FETCH r.name " +
           "LEFT JOIN FETCH r.vehicle " +
           "WHERE r.eliminado = false " +
           "AND r.date >= :startDate AND r.date < :endDate")
    List<Route> findByDateRange(@Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate);
    
    long countByEliminadoFalse();
    
    @Query("SELECT COUNT(r) FROM Route r WHERE r.eliminado = false AND r.state = :state")
    long countByStateAndEliminadoFalse(@Param("state") com.extinguidor.model.enums.ParteState state);
    
    @Query("SELECT r FROM Route r WHERE :userId MEMBER OF r.users " +
           "AND r.eliminado = false")
    List<Route> findByUsersIdAndEliminadoFalse(@Param("userId") Long userId);
}

