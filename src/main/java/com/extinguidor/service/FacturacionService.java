package com.extinguidor.service;

import com.extinguidor.dto.FacturacionDailyAggregation;
import com.extinguidor.dto.FacturacionRequest;
import com.extinguidor.dto.FacturacionResponse;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.mapper.FacturacionMapper;
import com.extinguidor.model.entity.Facturacion;
import com.extinguidor.model.entity.Parte;
import com.extinguidor.model.entity.Route;
import com.extinguidor.repository.FacturacionRepository;
import com.extinguidor.repository.ParteRepository;
import com.extinguidor.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class FacturacionService {
    
    private final FacturacionRepository facturacionRepository;
    private final RouteRepository routeRepository;
    private final ParteRepository parteRepository;
    private final FacturacionMapper facturacionMapper;
    
    @Transactional(readOnly = true)
    public List<Facturacion> findAll() {
        return facturacionRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Facturacion findById(Long id) {
        return facturacionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Facturación", id));
    }
    
    @Transactional(readOnly = true)
    public List<Facturacion> findByRouteId(Long routeId) {
        routeRepository.findById(routeId)
            .orElseThrow(() -> new ResourceNotFoundException("Ruta", routeId));
        Route route = routeRepository.findById(routeId).get();
        return facturacionRepository.findByRuta(route);
    }
    
    @Transactional(readOnly = true)
    public List<Facturacion> findByParteId(Long parteId) {
        parteRepository.findById(parteId)
            .orElseThrow(() -> new ResourceNotFoundException("Parte", parteId));
        Parte parte = parteRepository.findById(parteId).get();
        return facturacionRepository.findByParte(parte);
    }
    
    @Transactional
    public Facturacion create(Facturacion facturacion) {
        if (facturacion.getRuta() != null && facturacion.getRuta().getId() != null) {
            Route route = routeRepository.findById(facturacion.getRuta().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ruta", facturacion.getRuta().getId()));
            facturacion.setRuta(route);
        }
        
        if (facturacion.getParte() != null && facturacion.getParte().getId() != null) {
            Parte parte = parteRepository.findById(facturacion.getParte().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Parte", facturacion.getParte().getId()));
            facturacion.setParte(parte);
        }
        
        if (facturacion.getFacturacion() == null) {
            facturacion.setFacturacion(0.0);
        }
        
        return facturacionRepository.save(facturacion);
    }
    
    @Transactional
    public Facturacion update(Long id, Facturacion facturacionDetails) {
        Facturacion facturacion = findById(id);
        
        facturacion.setFacturacion(facturacionDetails.getFacturacion());
        
        if (facturacionDetails.getRuta() != null && facturacionDetails.getRuta().getId() != null) {
            Route route = routeRepository.findById(facturacionDetails.getRuta().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ruta", facturacionDetails.getRuta().getId()));
            facturacion.setRuta(route);
        }
        
        if (facturacionDetails.getParte() != null && facturacionDetails.getParte().getId() != null) {
            Parte parte = parteRepository.findById(facturacionDetails.getParte().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Parte", facturacionDetails.getParte().getId()));
            facturacion.setParte(parte);
        }
        
        return facturacionRepository.save(facturacion);
    }
    
    @Transactional
    public void delete(Long id) {
        Facturacion facturacion = findById(id);
        facturacionRepository.delete(facturacion);
    }
    
    /**
     * Agrega la facturación por día.
     * Agrupa todas las facturaciones por la fecha de creación (día) y calcula el total.
     * 
     * @return Lista de agregaciones diarias ordenadas por fecha
     */
    @Transactional(readOnly = true)
    public List<FacturacionDailyAggregation> getDailyAggregation() {
        List<Facturacion> allFacturacion = facturacionRepository.findAll();
        
        // Agrupar por fecha (solo día, sin hora)
        Map<LocalDate, List<Facturacion>> groupedByDate = allFacturacion.stream()
            .collect(Collectors.groupingBy(facturacion -> {
                if (facturacion.getCreatedDate() != null) {
                    return facturacion.getCreatedDate()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                }
                // Si no tiene fecha de creación, usar fecha actual
                return LocalDate.now();
            }));
        
        // Calcular totales por día
        return groupedByDate.entrySet().stream()
            .map(entry -> {
                LocalDate date = entry.getKey();
                List<Facturacion> facturaciones = entry.getValue();
                Double total = facturaciones.stream()
                    .mapToDouble(f -> f.getFacturacion() != null ? f.getFacturacion() : 0.0)
                    .sum();
                Long count = (long) facturaciones.size();
                
                return FacturacionDailyAggregation.builder()
                    .date(date)
                    .totalFacturacion(total)
                    .count(count)
                    .build();
            })
            .sorted((a, b) -> b.getDate().compareTo(a.getDate())) // Ordenar por fecha descendente
            .collect(Collectors.toList());
    }
    
    /**
     * Agrega la facturación por día dentro de un rango de fechas.
     * 
     * @param startDate Fecha de inicio (inclusive)
     * @param endDate Fecha de fin (inclusive)
     * @return Lista de agregaciones diarias en el rango especificado
     */
    @Transactional(readOnly = true)
    public List<FacturacionDailyAggregation> getDailyAggregationByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Facturacion> allFacturacion = facturacionRepository.findAll();
        
        // Filtrar por rango de fechas y agrupar por día
        Map<LocalDate, List<Facturacion>> groupedByDate = allFacturacion.stream()
            .filter(facturacion -> {
                if (facturacion.getCreatedDate() == null) {
                    return false;
                }
                LocalDate facturacionDate = facturacion.getCreatedDate()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
                return !facturacionDate.isBefore(startDate) && !facturacionDate.isAfter(endDate);
            })
            .collect(Collectors.groupingBy(facturacion -> {
                return facturacion.getCreatedDate()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            }));
        
        // Calcular totales por día
        return groupedByDate.entrySet().stream()
            .map(entry -> {
                LocalDate date = entry.getKey();
                List<Facturacion> facturaciones = entry.getValue();
                Double total = facturaciones.stream()
                    .mapToDouble(f -> f.getFacturacion() != null ? f.getFacturacion() : 0.0)
                    .sum();
                Long count = (long) facturaciones.size();
                
                return FacturacionDailyAggregation.builder()
                    .date(date)
                    .totalFacturacion(total)
                    .count(count)
                    .build();
            })
            .sorted((a, b) -> b.getDate().compareTo(a.getDate())) // Ordenar por fecha descendente
            .collect(Collectors.toList());
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public List<FacturacionResponse> findAllDTOs() {
        return facturacionRepository.findAll().stream()
            .map(facturacionMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public FacturacionResponse findByIdDTO(Long id) {
        Facturacion facturacion = findById(id);
        return facturacionMapper.toResponse(facturacion);
    }
    
    @Transactional(readOnly = true)
    public List<FacturacionResponse> findByRouteIdDTOs(Long routeId) {
        routeRepository.findById(routeId)
            .orElseThrow(() -> new ResourceNotFoundException("Ruta", routeId));
        Route route = routeRepository.findById(routeId).get();
        return facturacionRepository.findByRuta(route).stream()
            .map(facturacionMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<FacturacionResponse> findByParteIdDTOs(Long parteId) {
        parteRepository.findById(parteId)
            .orElseThrow(() -> new ResourceNotFoundException("Parte", parteId));
        Parte parte = parteRepository.findById(parteId).get();
        return facturacionRepository.findByParte(parte).stream()
            .map(facturacionMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public FacturacionResponse createDTO(FacturacionRequest request) {
        Facturacion facturacion = facturacionMapper.toEntity(request);
        
        if (request.getRutaId() != null) {
            Route route = routeRepository.findById(request.getRutaId())
                .orElseThrow(() -> new ResourceNotFoundException("Ruta", request.getRutaId()));
            facturacion.setRuta(route);
        }
        
        if (request.getParteId() != null) {
            Parte parte = parteRepository.findById(request.getParteId())
                .orElseThrow(() -> new ResourceNotFoundException("Parte", request.getParteId()));
            facturacion.setParte(parte);
        }
        
        if (facturacion.getFacturacion() == null) {
            facturacion.setFacturacion(0.0);
        }
        
        Facturacion saved = facturacionRepository.save(facturacion);
        return facturacionMapper.toResponse(saved);
    }
    
    @Transactional
    public FacturacionResponse updateDTO(Long id, FacturacionRequest request) {
        Facturacion facturacion = findById(id);
        
        facturacionMapper.updateEntity(facturacion, request);
        
        if (request.getRutaId() != null) {
            Route route = routeRepository.findById(request.getRutaId())
                .orElseThrow(() -> new ResourceNotFoundException("Ruta", request.getRutaId()));
            facturacion.setRuta(route);
        } else {
            facturacion.setRuta(null);
        }
        
        if (request.getParteId() != null) {
            Parte parte = parteRepository.findById(request.getParteId())
                .orElseThrow(() -> new ResourceNotFoundException("Parte", request.getParteId()));
            facturacion.setParte(parte);
        } else {
            facturacion.setParte(null);
        }
        
        Facturacion saved = facturacionRepository.save(facturacion);
        return facturacionMapper.toResponse(saved);
    }
}

