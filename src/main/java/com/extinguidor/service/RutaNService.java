package com.extinguidor.service;

import com.extinguidor.dto.RutaNRequest;
import com.extinguidor.dto.RutaNResponse;
import com.extinguidor.exception.BusinessException;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.mapper.RutaNMapper;
import com.extinguidor.model.entity.RutaN;
import com.extinguidor.repository.RutaNRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class RutaNService {
    
    private final RutaNRepository rutaNRepository;
    private final RutaNMapper rutaNMapper;
    
    @Transactional(readOnly = true)
    public List<RutaN> findAll() {
        return rutaNRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public RutaN findById(Long id) {
        return rutaNRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("RutaN", id));
    }
    
    @Transactional(readOnly = true)
    public RutaN findByName(String name) {
        return rutaNRepository.findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException("RutaN con nombre: " + name));
    }
    
    @Transactional
    public RutaN create(RutaN rutaN) {
        if (rutaNRepository.existsByName(rutaN.getName())) {
            throw new BusinessException("El nombre de ruta ya está en uso");
        }
        return rutaNRepository.save(rutaN);
    }
    
    @Transactional
    public RutaN update(Long id, RutaN rutaNDetails) {
        RutaN rutaN = findById(id);
        
        if (!rutaN.getName().equals(rutaNDetails.getName()) && 
            rutaNRepository.existsByName(rutaNDetails.getName())) {
            throw new BusinessException("El nombre ya está en uso");
        }
        
        rutaN.setName(rutaNDetails.getName());
        return rutaNRepository.save(rutaN);
    }
    
    @Transactional
    public void delete(Long id) {
        RutaN rutaN = findById(id);
        rutaNRepository.delete(rutaN);
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public List<RutaNResponse> findAllDTOs() {
        return rutaNRepository.findAll().stream()
            .map(rutaNMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public RutaNResponse findByIdDTO(Long id) {
        RutaN rutaN = findById(id);
        return rutaNMapper.toResponse(rutaN);
    }
    
    @Transactional
    public RutaNResponse createDTO(RutaNRequest request) {
        if (rutaNRepository.existsByName(request.getName())) {
            throw new BusinessException("El nombre de ruta ya está en uso");
        }
        RutaN rutaN = rutaNMapper.toEntity(request);
        RutaN saved = rutaNRepository.save(rutaN);
        return rutaNMapper.toResponse(saved);
    }
    
    @Transactional
    public RutaNResponse updateDTO(Long id, RutaNRequest request) {
        RutaN rutaN = findById(id);
        
        if (!rutaN.getName().equals(request.getName()) && 
            rutaNRepository.existsByName(request.getName())) {
            throw new BusinessException("El nombre ya está en uso");
        }
        
        rutaNMapper.updateEntity(rutaN, request);
        RutaN saved = rutaNRepository.save(rutaN);
        return rutaNMapper.toResponse(saved);
    }
}

