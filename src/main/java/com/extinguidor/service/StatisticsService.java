package com.extinguidor.service;

import com.extinguidor.dto.DashboardStatsDTO;
import com.extinguidor.dto.WorkerStatsDTO;
import com.extinguidor.model.entity.Parte;
import com.extinguidor.model.entity.Route;
import com.extinguidor.model.entity.User;
import com.extinguidor.model.enums.ParteState;
import com.extinguidor.model.enums.Role;
import com.extinguidor.repository.CustomerRepository;
import com.extinguidor.repository.ParteRepository;
import com.extinguidor.repository.RouteRepository;
import com.extinguidor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class StatisticsService {
    
    private final ParteRepository parteRepository;
    private final CustomerRepository customerRepository;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final FacturacionService facturacionService;
    
    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {
        List<Parte> allPartes = parteRepository.findByEliminadoFalse();
        
        // Estadísticas de partes
        long totalPartes = allPartes.size();
        long pendingPartes = allPartes.stream()
            .filter(p -> p.getState() == ParteState.Pendiente)
            .count();
        long inProcessPartes = allPartes.stream()
            .filter(p -> p.getState() == ParteState.EnProceso)
            .count();
        long finishedPartes = allPartes.stream()
            .filter(p -> p.getState() == ParteState.Finalizado)
            .count();
        long unassignedPartes = allPartes.stream()
            .filter(p -> !Boolean.TRUE.equals(p.getAsignado()))
            .count();
        long periodicPartes = allPartes.stream()
            .filter(p -> Boolean.TRUE.equals(p.getPeriodico()))
            .count();
        
        // Estadísticas por tipo
        Map<String, Long> partesByType = allPartes.stream()
            .filter(p -> p.getType() != null)
            .collect(Collectors.groupingBy(
                p -> p.getType().name(),
                Collectors.counting()
            ));
        
        // Estadísticas por categoría
        Map<String, Long> partesByCategory = allPartes.stream()
            .filter(p -> p.getCategoria() != null)
            .collect(Collectors.groupingBy(
                p -> p.getCategoria().name(),
                Collectors.counting()
            ));
        
        // Estadísticas por estado
        Map<String, Long> partesByState = allPartes.stream()
            .filter(p -> p.getState() != null)
            .collect(Collectors.groupingBy(
                p -> p.getState().name(),
                Collectors.counting()
            ));
        
        // Estadísticas de clientes
        long totalCustomers = customerRepository.count();
        long activeCustomers = customerRepository.countByActiveTrue();
        long inactiveCustomers = totalCustomers - activeCustomers;
        
        // Estadísticas de rutas
        long totalRoutes = routeRepository.countByEliminadoFalse();
        long pendingRoutes = routeRepository.countByStateAndEliminadoFalse(
            ParteState.Pendiente);
        long inProcessRoutes = routeRepository.countByStateAndEliminadoFalse(
            ParteState.EnProceso);
        long finishedRoutes = routeRepository.countByStateAndEliminadoFalse(
            ParteState.Finalizado);
        
        // Estadísticas de usuarios
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByActivoTrue();
        long totalWorkers = userRepository.countByRole(
            com.extinguidor.model.enums.Role.WORKER);
        
        // Estadísticas de facturación
        double totalFacturacion = facturacionService.findAll().stream()
            .mapToDouble(f -> f.getFacturacion() != null ? f.getFacturacion() : 0.0)
            .sum();
        
        LocalDate now = LocalDate.now();
        LocalDate firstDayThisMonth = now.withDayOfMonth(1);
        LocalDate firstDayLastMonth = firstDayThisMonth.minusMonths(1);
        LocalDate lastDayLastMonth = firstDayThisMonth.minusDays(1);
        
        double facturacionThisMonth = facturacionService.getDailyAggregationByDateRange(
            firstDayThisMonth, now).stream()
            .mapToDouble(f -> f.getTotalFacturacion() != null ? f.getTotalFacturacion() : 0.0)
            .sum();
        
        double facturacionLastMonth = facturacionService.getDailyAggregationByDateRange(
            firstDayLastMonth, lastDayLastMonth).stream()
            .mapToDouble(f -> f.getTotalFacturacion() != null ? f.getTotalFacturacion() : 0.0)
            .sum();
        
        return DashboardStatsDTO.builder()
            .totalPartes(totalPartes)
            .pendingPartes(pendingPartes)
            .inProcessPartes(inProcessPartes)
            .finishedPartes(finishedPartes)
            .totalCustomers(totalCustomers)
            .activeCustomers(activeCustomers)
            .inactiveCustomers(inactiveCustomers)
            .totalRoutes(totalRoutes)
            .pendingRoutes(pendingRoutes)
            .inProcessRoutes(inProcessRoutes)
            .finishedRoutes(finishedRoutes)
            .totalUsers(totalUsers)
            .activeUsers(activeUsers)
            .totalWorkers(totalWorkers)
            .totalFacturacion(totalFacturacion)
            .facturacionThisMonth(facturacionThisMonth)
            .facturacionLastMonth(facturacionLastMonth)
            .partesByType(partesByType)
            .partesByCategory(partesByCategory)
            .partesByState(partesByState)
            .unassignedPartes(unassignedPartes)
            .periodicPartes(periodicPartes)
            .build();
    }
    
    @Transactional(readOnly = true)
    public List<WorkerStatsDTO> getWorkersStats() {
        List<User> workers = userRepository.findByRole(Role.WORKER);
        
        return workers.stream().map(worker -> {
            // Partes del trabajador
            List<Parte> workerPartes = parteRepository.findByWorkerIdAndEliminadoFalse(worker.getId());
            long totalPartes = workerPartes.size();
            long pendingPartes = workerPartes.stream()
                .filter(p -> p.getState() == ParteState.Pendiente)
                .count();
            long inProcessPartes = workerPartes.stream()
                .filter(p -> p.getState() == ParteState.EnProceso)
                .count();
            long completedPartes = workerPartes.stream()
                .filter(p -> p.getState() == ParteState.Finalizado)
                .count();
            
            // Rutas del trabajador
            List<Route> workerRoutes = routeRepository.findByUsersIdAndEliminadoFalse(worker.getId());
            long totalRoutes = workerRoutes.size();
            long activeRoutes = workerRoutes.stream()
                .filter(r -> r.getState() == ParteState.Pendiente || r.getState() == ParteState.EnProceso)
                .count();
            
            // Facturación total del trabajador
            double totalFacturacion = workerPartes.stream()
                .filter(p -> p.getFacturacion() != null)
                .mapToDouble(Parte::getFacturacion)
                .sum();
            
            // Última actividad (última actualización de parte o ruta)
            LocalDateTime lastActivity = workerPartes.stream()
                .filter(p -> p.getUpdatedDate() != null)
                .map(Parte::getUpdatedDate)
                .max(LocalDateTime::compareTo)
                .orElse(null);
            
            if (lastActivity == null) {
                lastActivity = workerRoutes.stream()
                    .filter(r -> r.getUpdatedDate() != null)
                    .map(Route::getUpdatedDate)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
            }
            
            return WorkerStatsDTO.builder()
                .workerId(worker.getId())
                .workerName(worker.getName())
                .workerEmail(worker.getEmail())
                .workerPhoto(worker.getPhoto())
                .totalPartes(totalPartes)
                .pendingPartes(pendingPartes)
                .inProcessPartes(inProcessPartes)
                .completedPartes(completedPartes)
                .totalRoutes(totalRoutes)
                .activeRoutes(activeRoutes)
                .totalFacturacion(totalFacturacion)
                .lastActivity(lastActivity)
                .isActive(worker.getActivo())
                .build();
        }).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public WorkerStatsDTO getWorkerStats(Long workerId) {
        User worker = userRepository.findById(workerId)
            .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));
        
        if (worker.getRole() != Role.WORKER) {
            throw new RuntimeException("El usuario no es un trabajador");
        }
        
        // Partes del trabajador
        List<Parte> workerPartes = parteRepository.findByWorkerIdAndEliminadoFalse(workerId);
        long totalPartes = workerPartes.size();
        long pendingPartes = workerPartes.stream()
            .filter(p -> p.getState() == ParteState.Pendiente)
            .count();
        long inProcessPartes = workerPartes.stream()
            .filter(p -> p.getState() == ParteState.EnProceso)
            .count();
        long completedPartes = workerPartes.stream()
            .filter(p -> p.getState() == ParteState.Finalizado)
            .count();
        
        // Rutas del trabajador
        List<Route> workerRoutes = routeRepository.findByUsersIdAndEliminadoFalse(workerId);
        long totalRoutes = workerRoutes.size();
        long activeRoutes = workerRoutes.stream()
            .filter(r -> r.getState() == ParteState.Pendiente || r.getState() == ParteState.EnProceso)
            .count();
        
        // Facturación total del trabajador
        double totalFacturacion = workerPartes.stream()
            .filter(p -> p.getFacturacion() != null)
            .mapToDouble(Parte::getFacturacion)
            .sum();
        
        // Última actividad
        LocalDateTime lastActivity = workerPartes.stream()
            .filter(p -> p.getUpdatedDate() != null)
            .map(Parte::getUpdatedDate)
            .max(LocalDateTime::compareTo)
            .orElse(null);
        
        if (lastActivity == null) {
            lastActivity = workerRoutes.stream()
                .filter(r -> r.getUpdatedDate() != null)
                .map(Route::getUpdatedDate)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        }
        
        return WorkerStatsDTO.builder()
            .workerId(worker.getId())
            .workerName(worker.getName())
            .workerEmail(worker.getEmail())
            .workerPhoto(worker.getPhoto())
            .totalPartes(totalPartes)
            .pendingPartes(pendingPartes)
            .inProcessPartes(inProcessPartes)
            .completedPartes(completedPartes)
            .totalRoutes(totalRoutes)
            .activeRoutes(activeRoutes)
            .totalFacturacion(totalFacturacion)
            .lastActivity(lastActivity)
            .isActive(worker.getActivo())
            .build();
    }
}

