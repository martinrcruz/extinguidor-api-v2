package com.extinguidor.service;

import com.extinguidor.dto.RouteRequest;
import com.extinguidor.dto.RouteResponse;
import com.extinguidor.exception.BusinessException;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.mapper.RouteMapper;
import com.extinguidor.model.entity.Material;
import com.extinguidor.model.entity.Route;
import com.extinguidor.model.entity.RutaN;
import com.extinguidor.model.entity.User;
import com.extinguidor.model.entity.Vehicle;
import com.extinguidor.repository.MaterialRepository;
import com.extinguidor.repository.RouteRepository;
import com.extinguidor.repository.RutaNRepository;
import com.extinguidor.repository.UserRepository;
import com.extinguidor.repository.VehicleRepository;
import com.extinguidor.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class RouteService {
    
    private final RouteRepository routeRepository;
    private final UserService userService;
    private final RouteMapper routeMapper;
    private final UserRepository userRepository;
    private final RutaNRepository rutaNRepository;
    private final VehicleRepository vehicleRepository;
    private final MaterialRepository materialRepository;
    
    @Transactional(readOnly = true)
    public List<Route> findAll() {
        return routeRepository.findByEliminadoFalseOrderByIdDesc();
    }
    
    @Transactional(readOnly = true)
    public Route findById(Long id) {
        return routeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ruta", id));
    }
    
    @Transactional(readOnly = true)
    public List<Route> findByUserId(Long userId, LocalDate date) {
        if (date != null) {
            return routeRepository.findByUserIdAndMonth(userId, date.getYear(), date.getMonthValue());
        }
        return routeRepository.findByUserId(userId);
    }
    
    @Transactional(readOnly = true)
    public List<Route> findAvailableRoutes(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return routeRepository.findAvailableRoutes(date.getYear(), date.getMonthValue());
    }
    
    @Transactional(readOnly = true)
    public List<Route> findByDate(LocalDate date) {
        LocalDate startDate = date.atStartOfDay().toLocalDate();
        LocalDate endDate = date.atTime(23, 59, 59).toLocalDate();
        return routeRepository.findByDateRange(startDate, endDate);
    }
    
    @Transactional
    public Route create(Route route) {
        if (route.getEncargado() == null || route.getEncargado().getId() == null) {
            throw new BusinessException("El encargado es obligatorio");
        }
        
        // Validar que el encargado existe
        userService.findById(route.getEncargado().getId());
        
        // Normalizar fecha
        if (route.getDate() != null) {
            route.setDate(DateUtil.normalizeToNoon(route.getDate()));
        }
        
        if (route.getEliminado() == null) {
            route.setEliminado(false);
        }
        
        return routeRepository.save(route);
    }
    
    @Transactional
    public Route update(Long id, Route routeDetails) {
        Route route = findById(id);
        
        if (routeDetails.getEncargado() == null || routeDetails.getEncargado().getId() == null) {
            throw new BusinessException("El encargado es obligatorio");
        }
        
        // Validar que el encargado existe
        userService.findById(routeDetails.getEncargado().getId());
        
        route.setEncargado(routeDetails.getEncargado());
        route.setName(routeDetails.getName());
        
        if (routeDetails.getDate() != null) {
            route.setDate(DateUtil.normalizeToNoon(routeDetails.getDate()));
        }
        
        route.setState(routeDetails.getState());
        route.setVehicle(routeDetails.getVehicle());
        route.setUsers(routeDetails.getUsers());
        route.setComentarios(routeDetails.getComentarios());
        route.setHerramientas(routeDetails.getHerramientas());
        
        if (route.getEliminado() == null) {
            route.setEliminado(false);
        }
        
        return routeRepository.save(route);
    }
    
    @Transactional
    public void delete(Long id) {
        Route route = findById(id);
        route.setEliminado(true);
        routeRepository.save(route);
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public List<RouteResponse> findAllDTOs() {
        return routeRepository.findByEliminadoFalseOrderByIdDesc().stream()
            .map(routeMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public RouteResponse findByIdDTO(Long id) {
        Route route = findById(id);
        return routeMapper.toResponse(route);
    }
    
    @Transactional(readOnly = true)
    public List<RouteResponse> findByUserIdDTOs(Long userId, LocalDate date) {
        List<Route> routes;
        if (date != null) {
            routes = routeRepository.findByUserIdAndMonth(userId, date.getYear(), date.getMonthValue());
        } else {
            routes = routeRepository.findByUserId(userId);
        }
        return routes.stream()
            .map(routeMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<RouteResponse> findAvailableRoutesDTOs(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        
        // Calcular el primer y último día del mes para usar rango de fechas
        // startDate: primer día del mes (ej: 2025-12-01)
        // endDate: primer día del mes siguiente (exclusivo, ej: 2026-01-01)
        LocalDate startDate = date.withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1);
        
        log.debug("Buscando rutas disponibles para fecha: {} (rango: {} a {})", 
                  date, startDate, endDate);
        
        // Usar rango de fechas (más confiable que YEAR/MONTH)
        List<Route> routes = routeRepository.findAvailableRoutesByDateRange(startDate, endDate);
        
        log.debug("Rutas encontradas: {} para el mes de {}/{}", 
                  routes.size(), date.getMonthValue(), date.getYear());
        
        return routes.stream()
            .map(routeMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<RouteResponse> findByDateDTOs(LocalDate date) {
        LocalDate startDate = date.atStartOfDay().toLocalDate();
        LocalDate endDate = date.atTime(23, 59, 59).toLocalDate();
        return routeRepository.findByDateRange(startDate, endDate).stream()
            .map(routeMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public RouteResponse createDTO(RouteRequest request) {
        if (request.getEncargadoId() == null) {
            throw new BusinessException("El encargado es obligatorio");
        }
        
        User encargado = userRepository.findById(request.getEncargadoId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", request.getEncargadoId()));
        
        Route route = routeMapper.toEntity(request);
        route.setEncargado(encargado);
        
        // Mapear RutaN
        if (request.getRutaNId() != null) {
            RutaN rutaN = rutaNRepository.findById(request.getRutaNId())
                .orElseThrow(() -> new ResourceNotFoundException("RutaN", request.getRutaNId()));
            route.setName(rutaN);
        }
        
        // Mapear Vehicle
        if (request.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo", request.getVehicleId()));
            route.setVehicle(vehicle);
        }
        
        // Mapear Users
        if (request.getUserIds() != null && !request.getUserIds().isEmpty()) {
            List<User> users = userRepository.findAllById(request.getUserIds());
            route.setUsers(users);
        }
        
        // Mapear Materials
        if (request.getMaterialIds() != null && !request.getMaterialIds().isEmpty()) {
            List<Material> materials = materialRepository.findAllById(request.getMaterialIds());
            route.setHerramientas(materials);
        }
        
        // Normalizar fecha
        if (route.getDate() != null) {
            route.setDate(DateUtil.normalizeToNoon(route.getDate()));
        }
        
        if (route.getEliminado() == null) {
            route.setEliminado(false);
        }
        
        Route saved = routeRepository.save(route);
        return routeMapper.toResponse(saved);
    }
    
    @Transactional
    public RouteResponse updateDTO(Long id, RouteRequest request) {
        Route route = findById(id);
        
        if (request.getEncargadoId() == null) {
            throw new BusinessException("El encargado es obligatorio");
        }
        
        User encargado = userRepository.findById(request.getEncargadoId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", request.getEncargadoId()));
        
        routeMapper.updateEntity(route, request);
        route.setEncargado(encargado);
        
        // Mapear RutaN
        if (request.getRutaNId() != null) {
            RutaN rutaN = rutaNRepository.findById(request.getRutaNId())
                .orElseThrow(() -> new ResourceNotFoundException("RutaN", request.getRutaNId()));
            route.setName(rutaN);
        }
        
        // Mapear Vehicle
        if (request.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo", request.getVehicleId()));
            route.setVehicle(vehicle);
        } else {
            route.setVehicle(null);
        }
        
        // Mapear Users
        if (request.getUserIds() != null) {
            if (request.getUserIds().isEmpty()) {
                route.getUsers().clear();
            } else {
                List<User> users = userRepository.findAllById(request.getUserIds());
                route.setUsers(users);
            }
        }
        
        // Mapear Materials
        if (request.getMaterialIds() != null) {
            if (request.getMaterialIds().isEmpty()) {
                route.getHerramientas().clear();
            } else {
                List<Material> materials = materialRepository.findAllById(request.getMaterialIds());
                route.setHerramientas(materials);
            }
        }
        
        if (request.getDate() != null) {
            route.setDate(DateUtil.normalizeToNoon(request.getDate()));
        }
        
        if (route.getEliminado() == null) {
            route.setEliminado(false);
        }
        
        Route saved = routeRepository.save(route);
        return routeMapper.toResponse(saved);
    }
}

