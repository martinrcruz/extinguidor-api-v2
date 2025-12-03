package com.extinguidor.service;

import com.extinguidor.dto.AdvancedReportDTO;
import com.extinguidor.model.entity.Parte;
import com.extinguidor.model.entity.Route;
import com.extinguidor.model.entity.User;
import com.extinguidor.dto.CustomerReportDTO;
import com.extinguidor.dto.WorkerReportDTO;
import com.extinguidor.dto.DailyReportDTO;
import com.extinguidor.repository.ParteRepository;
import com.extinguidor.repository.RouteRepository;
import com.extinguidor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdvancedReportService {
    
    private final ParteRepository parteRepository;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final FacturacionService facturacionService;
    
    @Transactional(readOnly = true)
    public AdvancedReportDTO generateReport(LocalDate startDate, LocalDate endDate) {
        List<Parte> partes = parteRepository.findByEliminadoFalse().stream()
            .filter(p -> {
                if (p.getDate() == null) return false;
                return !p.getDate().isBefore(startDate) && !p.getDate().isAfter(endDate);
            })
            .collect(Collectors.toList());
        
        long totalPartes = partes.size();
        long completedPartes = partes.stream()
            .filter(p -> p.getState() == com.extinguidor.model.enums.ParteState.Finalizado)
            .count();
        long pendingPartes = totalPartes - completedPartes;
        
        double totalFacturacion = partes.stream()
            .mapToDouble(p -> p.getFacturacion() != null ? p.getFacturacion() : 0.0)
            .sum();
        double averageFacturacion = totalPartes > 0 ? totalFacturacion / totalPartes : 0.0;
        
        Map<String, Long> partesByType = partes.stream()
            .filter(p -> p.getType() != null)
            .collect(Collectors.groupingBy(
                p -> p.getType().name(),
                Collectors.counting()
            ));
        
        Map<String, Long> partesByCategory = partes.stream()
            .filter(p -> p.getCategoria() != null)
            .collect(Collectors.groupingBy(
                p -> p.getCategoria().name(),
                Collectors.counting()
            ));
        
        Map<String, Long> partesByState = partes.stream()
            .filter(p -> p.getState() != null)
            .collect(Collectors.groupingBy(
                p -> p.getState().name(),
                Collectors.counting()
            ));
        
        // Top clientes
        List<CustomerReportDTO> topCustomers = partes.stream()
            .filter(p -> p.getCustomer() != null)
            .collect(Collectors.groupingBy(
                Parte::getCustomer,
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    list -> {
                        long count = list.size();
                        double facturacion = list.stream()
                            .mapToDouble(p -> p.getFacturacion() != null ? p.getFacturacion() : 0.0)
                            .sum();
                        return CustomerReportDTO.builder()
                            .customerId(list.get(0).getCustomer().getId())
                            .customerName(list.get(0).getCustomer().getName())
                            .totalPartes(count)
                            .totalFacturacion(facturacion)
                            .build();
                    }
                )
            ))
            .values()
            .stream()
            .sorted((a, b) -> Double.compare(b.getTotalFacturacion(), a.getTotalFacturacion()))
            .limit(10)
            .collect(Collectors.toList());
        
        // Top trabajadores (simplificado)
        List<WorkerReportDTO> topWorkers = userRepository.findByRole(com.extinguidor.model.enums.Role.WORKER)
            .stream()
            .limit(10)
            .map(worker -> {
                List<Route> workerRoutes = routeRepository.findByUserId(worker.getId());
                long total = workerRoutes.stream()
                    .mapToLong(r -> parteRepository.findByRutaAndEliminadoFalse(r).size())
                    .sum();
                long completed = workerRoutes.stream()
                    .mapToLong(r -> parteRepository.findByRutaAndEliminadoFalse(r).stream()
                        .filter(p -> p.getState() == com.extinguidor.model.enums.ParteState.Finalizado)
                        .count())
                    .sum();
                return WorkerReportDTO.builder()
                    .workerId(worker.getId())
                    .workerName(worker.getName())
                    .totalPartes(total)
                    .completedPartes(completed)
                    .build();
            })
            .sorted((a, b) -> Long.compare(b.getTotalPartes(), a.getTotalPartes()))
            .collect(Collectors.toList());
        
        // Reportes diarios
        List<DailyReportDTO> dailyReports = partes.stream()
            .filter(p -> p.getDate() != null)
            .collect(Collectors.groupingBy(
                Parte::getDate,
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    list -> {
                        long total = list.size();
                        long completed = list.stream()
                            .filter(p -> p.getState() == com.extinguidor.model.enums.ParteState.Finalizado)
                            .count();
                        double facturacion = list.stream()
                            .mapToDouble(p -> p.getFacturacion() != null ? p.getFacturacion() : 0.0)
                            .sum();
                        return DailyReportDTO.builder()
                            .date(list.get(0).getDate())
                            .totalPartes(total)
                            .completedPartes(completed)
                            .totalFacturacion(facturacion)
                            .build();
                    }
                )
            ))
            .values()
            .stream()
            .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
            .collect(Collectors.toList());
        
        return AdvancedReportDTO.builder()
            .reportType("ADVANCED")
            .startDate(startDate)
            .endDate(endDate)
            .totalPartes(totalPartes)
            .completedPartes(completedPartes)
            .pendingPartes(pendingPartes)
            .totalFacturacion(totalFacturacion)
            .averageFacturacion(averageFacturacion)
            .partesByType(partesByType)
            .partesByCategory(partesByCategory)
            .partesByState(partesByState)
            .topCustomers(topCustomers)
            .topWorkers(topWorkers)
            .dailyReports(dailyReports)
            .build();
    }
}

