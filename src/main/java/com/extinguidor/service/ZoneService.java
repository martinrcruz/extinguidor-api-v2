package com.extinguidor.service;

import com.extinguidor.dto.ZoneRequest;
import com.extinguidor.dto.ZoneResponse;
import com.extinguidor.exception.BusinessException;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.mapper.ZoneMapper;
import com.extinguidor.model.entity.Zone;
import com.extinguidor.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ZoneService {
    
    private final ZoneRepository zoneRepository;
    private final ZoneMapper zoneMapper;
    
    @Transactional(readOnly = true)
    public List<Zone> findAll() {
        return zoneRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Zone findById(Long id) {
        return zoneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Zona", id));
    }
    
    @Transactional
    public Zone create(Zone zone) {
        if (zoneRepository.existsByName(zone.getName())) {
            throw new BusinessException("El nombre de zona ya está en uso");
        }
        if (zoneRepository.existsByCode(zone.getCode())) {
            throw new BusinessException("El código de zona ya está en uso");
        }
        return zoneRepository.save(zone);
    }
    
    @Transactional
    public Zone update(Long id, Zone zoneDetails) {
        Zone zone = findById(id);
        
        if (!zone.getName().equals(zoneDetails.getName()) && 
            zoneRepository.existsByName(zoneDetails.getName())) {
            throw new BusinessException("El nombre ya está en uso");
        }
        if (!zone.getCode().equals(zoneDetails.getCode()) && 
            zoneRepository.existsByCode(zoneDetails.getCode())) {
            throw new BusinessException("El código ya está en uso");
        }
        
        zone.setName(zoneDetails.getName());
        zone.setCode(zoneDetails.getCode());
        zone.setCodezip(zoneDetails.getCodezip());
        
        return zoneRepository.save(zone);
    }
    
    @Transactional
    public void delete(Long id) {
        Zone zone = findById(id);
        zoneRepository.delete(zone);
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public List<ZoneResponse> findAllDTOs() {
        return zoneRepository.findAll().stream()
            .map(zoneMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ZoneResponse findByIdDTO(Long id) {
        Zone zone = findById(id);
        return zoneMapper.toResponse(zone);
    }
    
    @Transactional
    public ZoneResponse createDTO(ZoneRequest request) {
        if (zoneRepository.existsByName(request.getName())) {
            throw new BusinessException("El nombre de zona ya está en uso");
        }
        if (zoneRepository.existsByCode(request.getCode())) {
            throw new BusinessException("El código de zona ya está en uso");
        }
        Zone zone = zoneMapper.toEntity(request);
        Zone saved = zoneRepository.save(zone);
        return zoneMapper.toResponse(saved);
    }
    
    @Transactional
    public ZoneResponse updateDTO(Long id, ZoneRequest request) {
        Zone zone = findById(id);
        
        if (!zone.getName().equals(request.getName()) && 
            zoneRepository.existsByName(request.getName())) {
            throw new BusinessException("El nombre ya está en uso");
        }
        if (!zone.getCode().equals(request.getCode()) && 
            zoneRepository.existsByCode(request.getCode())) {
            throw new BusinessException("El código ya está en uso");
        }
        
        zoneMapper.updateEntity(zone, request);
        Zone saved = zoneRepository.save(zone);
        return zoneMapper.toResponse(saved);
    }
}

