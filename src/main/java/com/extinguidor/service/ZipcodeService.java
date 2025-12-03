package com.extinguidor.service;

import com.extinguidor.dto.ZipcodeRequest;
import com.extinguidor.dto.ZipcodeResponse;
import com.extinguidor.exception.BusinessException;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.mapper.ZipcodeMapper;
import com.extinguidor.model.entity.Zipcode;
import com.extinguidor.repository.ZipcodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ZipcodeService {
    
    private final ZipcodeRepository zipcodeRepository;
    private final ZipcodeMapper zipcodeMapper;
    
    @Transactional(readOnly = true)
    public List<Zipcode> findAll() {
        return zipcodeRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Zipcode findById(Long id) {
        return zipcodeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Zipcode", id));
    }
    
    @Transactional
    public Zipcode create(Zipcode zipcode) {
        if (zipcodeRepository.existsByCodezip(zipcode.getCodezip())) {
            throw new BusinessException("El código postal ya está en uso");
        }
        return zipcodeRepository.save(zipcode);
    }
    
    @Transactional
    public Zipcode update(Long id, Zipcode zipcodeDetails) {
        Zipcode zipcode = findById(id);
        
        if (!zipcode.getCodezip().equals(zipcodeDetails.getCodezip()) && 
            zipcodeRepository.existsByCodezip(zipcodeDetails.getCodezip())) {
            throw new BusinessException("El código postal ya está en uso");
        }
        
        zipcode.setCodezip(zipcodeDetails.getCodezip());
        zipcode.setName(zipcodeDetails.getName());
        
        return zipcodeRepository.save(zipcode);
    }
    
    @Transactional
    public void delete(Long id) {
        Zipcode zipcode = findById(id);
        zipcodeRepository.delete(zipcode);
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public List<ZipcodeResponse> findAllDTOs() {
        return zipcodeRepository.findAll().stream()
            .map(zipcodeMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ZipcodeResponse findByIdDTO(Long id) {
        Zipcode zipcode = findById(id);
        return zipcodeMapper.toResponse(zipcode);
    }
    
    @Transactional
    public ZipcodeResponse createDTO(ZipcodeRequest request) {
        if (zipcodeRepository.existsByCodezip(request.getCodezip())) {
            throw new BusinessException("El código postal ya está en uso");
        }
        Zipcode zipcode = zipcodeMapper.toEntity(request);
        Zipcode saved = zipcodeRepository.save(zipcode);
        return zipcodeMapper.toResponse(saved);
    }
    
    @Transactional
    public ZipcodeResponse updateDTO(Long id, ZipcodeRequest request) {
        Zipcode zipcode = findById(id);
        
        if (!zipcode.getCodezip().equals(request.getCodezip()) && 
            zipcodeRepository.existsByCodezip(request.getCodezip())) {
            throw new BusinessException("El código postal ya está en uso");
        }
        
        zipcodeMapper.updateEntity(zipcode, request);
        Zipcode saved = zipcodeRepository.save(zipcode);
        return zipcodeMapper.toResponse(saved);
    }
}

