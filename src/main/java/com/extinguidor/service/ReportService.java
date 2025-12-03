package com.extinguidor.service;

import com.extinguidor.dto.ReportRequest;
import com.extinguidor.dto.ReportResponse;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.mapper.ReportMapper;
import com.extinguidor.model.entity.Checkin;
import com.extinguidor.model.entity.Customer;
import com.extinguidor.model.entity.Material;
import com.extinguidor.model.entity.Report;
import com.extinguidor.model.entity.Route;
import com.extinguidor.model.entity.User;
import com.extinguidor.model.entity.Vehicle;
import com.extinguidor.model.enums.ReportStatus;
import com.extinguidor.model.enums.ReportType;
import com.extinguidor.repository.CheckinRepository;
import com.extinguidor.repository.CustomerRepository;
import com.extinguidor.repository.MaterialRepository;
import com.extinguidor.repository.ReportRepository;
import com.extinguidor.repository.RouteRepository;
import com.extinguidor.repository.UserRepository;
import com.extinguidor.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReportService {
    
    private final ReportRepository reportRepository;
    private final CustomerRepository customerRepository;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final CheckinRepository checkinRepository;
    private final ReportMapper reportMapper;
    private final MaterialRepository materialRepository;
    private final VehicleRepository vehicleRepository;
    
    /**
     * Calcula dinámicamente el estado de un reporte.
     * Un reporte está en estado 'doing' si tiene al menos un checkin activo (con checkin pero sin checkout).
     * 
     * @param report El reporte a evaluar
     * @return El estado calculado del reporte
     */
    private ReportStatus calculateStatus(Report report) {
        // Si el estado guardado es 'done', no cambiar
        if (report.getStatus() == ReportStatus.done) {
            return ReportStatus.done;
        }
        
        // Verificar si hay checkins activos (con checkin pero sin checkout)
        List<Checkin> checkins = checkinRepository.findByReportId(report);
        boolean hasActiveCheckin = checkins.stream()
            .anyMatch(checkin -> checkin.getCheckin() != null && checkin.getCheckout() == null);
        
        if (hasActiveCheckin) {
            return ReportStatus.doing;
        }
        
        // Si no hay checkin activo, devolver el estado guardado
        return report.getStatus() != null ? report.getStatus() : ReportStatus.pending;
    }
    
    /**
     * Aplica el cálculo dinámico de estado a una lista de reportes
     */
    private List<Report> applyDynamicStatus(List<Report> reports) {
        return reports.stream()
            .map(report -> {
                ReportStatus calculatedStatus = calculateStatus(report);
                // Crear una copia del reporte con el estado calculado para la respuesta
                // Nota: No guardamos el estado 'doing' en la BD, solo lo calculamos para la respuesta
                if (calculatedStatus == ReportStatus.doing && report.getStatus() != ReportStatus.doing) {
                    report.setStatus(calculatedStatus);
                }
                return report;
            })
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<Report> findAll() {
        List<Report> reports = reportRepository.findAll();
        return applyDynamicStatus(reports);
    }
    
    @Transactional(readOnly = true)
    public Report findById(Long id) {
        Report report = reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reporte", id));
        ReportStatus calculatedStatus = calculateStatus(report);
        if (calculatedStatus == ReportStatus.doing && report.getStatus() != ReportStatus.doing) {
            report.setStatus(calculatedStatus);
        }
        return report;
    }
    
    @Transactional(readOnly = true)
    public List<Report> findByCustomerId(Long customerId) {
        customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente", customerId));
        Customer customer = customerRepository.findById(customerId).get();
        List<Report> reports = reportRepository.findByCustomerId(customer);
        return applyDynamicStatus(reports);
    }
    
    @Transactional(readOnly = true)
    public List<Report> findByRouteId(Long routeId) {
        routeRepository.findById(routeId)
            .orElseThrow(() -> new ResourceNotFoundException("Ruta", routeId));
        Route route = routeRepository.findById(routeId).get();
        List<Report> reports = reportRepository.findByRouteId(route);
        return applyDynamicStatus(reports);
    }
    
    @Transactional(readOnly = true)
    public List<Report> findByUserId(Long userId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", userId));
        User user = userRepository.findById(userId).get();
        List<Report> reports = reportRepository.findByUserId(user);
        return applyDynamicStatus(reports);
    }
    
    @Transactional(readOnly = true)
    public List<Report> findByStatus(ReportStatus status) {
        // Si se busca por 'doing', necesitamos calcular dinámicamente
        if (status == ReportStatus.doing) {
            List<Report> allReports = reportRepository.findAll();
            return applyDynamicStatus(allReports).stream()
                .filter(report -> report.getStatus() == ReportStatus.doing)
                .collect(Collectors.toList());
        }
        List<Report> reports = reportRepository.findByStatus(status);
        return applyDynamicStatus(reports);
    }
    
    @Transactional(readOnly = true)
    public List<Report> findByType(ReportType type) {
        List<Report> reports = reportRepository.findByType(type);
        return applyDynamicStatus(reports);
    }
    
    @Transactional(readOnly = true)
    public List<Report> findByWorkerId(Long workerId) {
        List<Report> reports = reportRepository.findByWorkerId(workerId);
        return applyDynamicStatus(reports);
    }
    
    @Transactional
    public Report create(Report report) {
        if (report.getCustomerId() != null && report.getCustomerId().getId() != null) {
            Customer customer = customerRepository.findById(report.getCustomerId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", report.getCustomerId().getId()));
            report.setCustomerId(customer);
        }
        
        if (report.getRouteId() != null && report.getRouteId().getId() != null) {
            Route route = routeRepository.findById(report.getRouteId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ruta", report.getRouteId().getId()));
            report.setRouteId(route);
        }
        
        if (report.getUserId() != null && report.getUserId().getId() != null) {
            User user = userRepository.findById(report.getUserId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", report.getUserId().getId()));
            report.setUserId(user);
        }
        
        if (report.getContractId() != null && report.getContractId().getId() != null) {
            Customer contract = customerRepository.findById(report.getContractId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contrato", report.getContractId().getId()));
            report.setContractId(contract);
        }
        
        // Si no tiene estado, establecer como pending
        if (report.getStatus() == null) {
            report.setStatus(ReportStatus.pending);
        }
        
        return reportRepository.save(report);
    }
    
    @Transactional
    public Report update(Long id, Report reportDetails) {
        Report report = findById(id);
        
        report.setStartTime(reportDetails.getStartTime());
        report.setEndTime(reportDetails.getEndTime());
        report.setCreatedDate(reportDetails.getCreatedDate());
        report.setType(reportDetails.getType());
        report.setStatus(reportDetails.getStatus());
        report.setTitle(reportDetails.getTitle());
        report.setDescription(reportDetails.getDescription());
        report.setNumber(reportDetails.getNumber());
        report.setLastModification(reportDetails.getLastModification());
        report.setWorkersId(reportDetails.getWorkersId());
        report.setTools(reportDetails.getTools());
        report.setVehicle(reportDetails.getVehicle());
        
        if (reportDetails.getCustomerId() != null && reportDetails.getCustomerId().getId() != null) {
            Customer customer = customerRepository.findById(reportDetails.getCustomerId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", reportDetails.getCustomerId().getId()));
            report.setCustomerId(customer);
        }
        
        if (reportDetails.getRouteId() != null && reportDetails.getRouteId().getId() != null) {
            Route route = routeRepository.findById(reportDetails.getRouteId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ruta", reportDetails.getRouteId().getId()));
            report.setRouteId(route);
        }
        
        if (reportDetails.getUserId() != null && reportDetails.getUserId().getId() != null) {
            User user = userRepository.findById(reportDetails.getUserId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", reportDetails.getUserId().getId()));
            report.setUserId(user);
        }
        
        if (reportDetails.getContractId() != null && reportDetails.getContractId().getId() != null) {
            Customer contract = customerRepository.findById(reportDetails.getContractId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contrato", reportDetails.getContractId().getId()));
            report.setContractId(contract);
        }
        
        return reportRepository.save(report);
    }
    
    @Transactional
    public Report updateStatus(Long id, ReportStatus status) {
        Report report = findById(id);
        report.setStatus(status);
        return reportRepository.save(report);
    }
    
    @Transactional
    public void delete(Long id) {
        Report report = findById(id);
        reportRepository.delete(report);
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public List<ReportResponse> findAllDTOs() {
        List<Report> reports = reportRepository.findAll();
        List<Report> reportsWithStatus = applyDynamicStatus(reports);
        return reportsWithStatus.stream()
            .map(reportMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ReportResponse findByIdDTO(Long id) {
        Report report = findById(id);
        return reportMapper.toResponse(report);
    }
    
    @Transactional(readOnly = true)
    public List<ReportResponse> findByCustomerIdDTOs(Long customerId) {
        customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente", customerId));
        Customer customer = customerRepository.findById(customerId).get();
        List<Report> reports = reportRepository.findByCustomerId(customer);
        List<Report> reportsWithStatus = applyDynamicStatus(reports);
        return reportsWithStatus.stream()
            .map(reportMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReportResponse> findByRouteIdDTOs(Long routeId) {
        routeRepository.findById(routeId)
            .orElseThrow(() -> new ResourceNotFoundException("Ruta", routeId));
        Route route = routeRepository.findById(routeId).get();
        List<Report> reports = reportRepository.findByRouteId(route);
        List<Report> reportsWithStatus = applyDynamicStatus(reports);
        return reportsWithStatus.stream()
            .map(reportMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReportResponse> findByUserIdDTOs(Long userId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", userId));
        User user = userRepository.findById(userId).get();
        List<Report> reports = reportRepository.findByUserId(user);
        List<Report> reportsWithStatus = applyDynamicStatus(reports);
        return reportsWithStatus.stream()
            .map(reportMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReportResponse> findByStatusDTOs(ReportStatus status) {
        List<Report> reports;
        if (status == ReportStatus.doing) {
            List<Report> allReports = reportRepository.findAll();
            reports = applyDynamicStatus(allReports).stream()
                .filter(report -> report.getStatus() == ReportStatus.doing)
                .collect(Collectors.toList());
        } else {
            reports = reportRepository.findByStatus(status);
            reports = applyDynamicStatus(reports);
        }
        return reports.stream()
            .map(reportMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReportResponse> findByTypeDTOs(ReportType type) {
        List<Report> reports = reportRepository.findByType(type);
        List<Report> reportsWithStatus = applyDynamicStatus(reports);
        return reportsWithStatus.stream()
            .map(reportMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReportResponse> findByWorkerIdDTOs(Long workerId) {
        List<Report> reports = reportRepository.findByWorkerId(workerId);
        List<Report> reportsWithStatus = applyDynamicStatus(reports);
        return reportsWithStatus.stream()
            .map(reportMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public ReportResponse createDTO(ReportRequest request) {
        Report report = reportMapper.toEntity(request);
        
        // Mapear Customer
        if (request.getCustomerId() != null) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", request.getCustomerId()));
            report.setCustomerId(customer);
        }
        
        // Mapear Contract
        if (request.getContractId() != null) {
            Customer contract = customerRepository.findById(request.getContractId())
                .orElseThrow(() -> new ResourceNotFoundException("Contrato", request.getContractId()));
            report.setContractId(contract);
        }
        
        // Mapear Route
        if (request.getRouteId() != null) {
            Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Ruta", request.getRouteId()));
            report.setRouteId(route);
        }
        
        // Mapear User
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", request.getUserId()));
            report.setUserId(user);
        }
        
        // Mapear Workers
        if (request.getWorkersId() != null && !request.getWorkersId().isEmpty()) {
            List<User> workers = userRepository.findAllById(request.getWorkersId());
            report.setWorkersId(workers);
        }
        
        // Mapear Tools
        if (request.getToolIds() != null && !request.getToolIds().isEmpty()) {
            List<Material> tools = materialRepository.findAllById(request.getToolIds());
            report.setTools(tools);
        }
        
        // Mapear Vehicles
        if (request.getVehicleIds() != null && !request.getVehicleIds().isEmpty()) {
            List<Vehicle> vehicles = vehicleRepository.findAllById(request.getVehicleIds());
            report.setVehicle(vehicles);
        }
        
        // Si no tiene estado, establecer como pending
        if (report.getStatus() == null) {
            report.setStatus(ReportStatus.pending);
        }
        
        Report saved = reportRepository.save(report);
        return reportMapper.toResponse(saved);
    }
    
    @Transactional
    public ReportResponse updateDTO(Long id, ReportRequest request) {
        Report report = findById(id);
        
        reportMapper.updateEntity(report, request);
        
        // Actualizar relaciones
        if (request.getCustomerId() != null) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", request.getCustomerId()));
            report.setCustomerId(customer);
        }
        
        if (request.getContractId() != null) {
            Customer contract = customerRepository.findById(request.getContractId())
                .orElseThrow(() -> new ResourceNotFoundException("Contrato", request.getContractId()));
            report.setContractId(contract);
        } else if (request.getContractId() == null && report.getContractId() != null) {
            report.setContractId(null);
        }
        
        if (request.getRouteId() != null) {
            Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Ruta", request.getRouteId()));
            report.setRouteId(route);
        } else if (request.getRouteId() == null && report.getRouteId() != null) {
            report.setRouteId(null);
        }
        
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", request.getUserId()));
            report.setUserId(user);
        } else if (request.getUserId() == null && report.getUserId() != null) {
            report.setUserId(null);
        }
        
        if (request.getWorkersId() != null) {
            if (request.getWorkersId().isEmpty()) {
                report.getWorkersId().clear();
            } else {
                List<User> workers = userRepository.findAllById(request.getWorkersId());
                report.setWorkersId(workers);
            }
        }
        
        if (request.getToolIds() != null) {
            if (request.getToolIds().isEmpty()) {
                report.getTools().clear();
            } else {
                List<Material> tools = materialRepository.findAllById(request.getToolIds());
                report.setTools(tools);
            }
        }
        
        if (request.getVehicleIds() != null) {
            if (request.getVehicleIds().isEmpty()) {
                report.getVehicle().clear();
            } else {
                List<Vehicle> vehicles = vehicleRepository.findAllById(request.getVehicleIds());
                report.setVehicle(vehicles);
            }
        }
        
        Report saved = reportRepository.save(report);
        return reportMapper.toResponse(saved);
    }
    
    @Transactional
    public ReportResponse updateStatusDTO(Long id, ReportStatus status) {
        Report report = findById(id);
        report.setStatus(status);
        Report saved = reportRepository.save(report);
        return reportMapper.toResponse(saved);
    }
}

