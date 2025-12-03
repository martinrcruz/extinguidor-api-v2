package com.extinguidor.service;

import com.extinguidor.dto.VehicleRequest;
import com.extinguidor.dto.VehicleResponse;
import com.extinguidor.exception.BusinessException;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.mapper.VehicleMapper;
import com.extinguidor.model.entity.Vehicle;
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
public class VehicleService {
    
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    
    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Vehicle findById(Long id) {
        return vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo", id));
    }
    
    @Transactional
    public Vehicle create(Vehicle vehicle) {
        if (vehicleRepository.existsByMatricula(vehicle.getMatricula())) {
            throw new BusinessException("La matrícula ya está en uso");
        }
        return vehicleRepository.save(vehicle);
    }
    
    @Transactional
    public Vehicle update(Long id, Vehicle vehicleDetails) {
        Vehicle vehicle = findById(id);
        
        if (!vehicle.getMatricula().equals(vehicleDetails.getMatricula()) && 
            vehicleRepository.existsByMatricula(vehicleDetails.getMatricula())) {
            throw new BusinessException("La matrícula ya está en uso");
        }
        
        vehicle.setModelo(vehicleDetails.getModelo());
        vehicle.setBrand(vehicleDetails.getBrand());
        vehicle.setMatricula(vehicleDetails.getMatricula());
        vehicle.setFuel(vehicleDetails.getFuel());
        vehicle.setType(vehicleDetails.getType());
        vehicle.setPhoto(vehicleDetails.getPhoto());
        
        return vehicleRepository.save(vehicle);
    }
    
    @Transactional
    public void delete(Long id) {
        Vehicle vehicle = findById(id);
        vehicleRepository.delete(vehicle);
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public List<VehicleResponse> findAllDTOs() {
        return vehicleRepository.findAll().stream()
            .map(vehicleMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public VehicleResponse findByIdDTO(Long id) {
        Vehicle vehicle = findById(id);
        return vehicleMapper.toResponse(vehicle);
    }
    
    @Transactional
    public VehicleResponse createDTO(VehicleRequest request) {
        if (vehicleRepository.existsByMatricula(request.getMatricula())) {
            throw new BusinessException("La matrícula ya está en uso");
        }
        Vehicle vehicle = vehicleMapper.toEntity(request);
        Vehicle saved = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(saved);
    }
    
    @Transactional
    public VehicleResponse updateDTO(Long id, VehicleRequest request) {
        Vehicle vehicle = findById(id);
        
        if (!vehicle.getMatricula().equals(request.getMatricula()) && 
            vehicleRepository.existsByMatricula(request.getMatricula())) {
            throw new BusinessException("La matrícula ya está en uso");
        }
        
        vehicleMapper.updateEntity(vehicle, request);
        Vehicle saved = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(saved);
    }
}

