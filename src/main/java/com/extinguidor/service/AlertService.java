package com.extinguidor.service;

import com.extinguidor.dto.AlertRequest;
import com.extinguidor.dto.AlertResponse;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.mapper.AlertMapper;
import com.extinguidor.model.entity.Alert;
import com.extinguidor.model.enums.AlertState;
import com.extinguidor.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class AlertService {
    
    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;
    
    @Transactional(readOnly = true)
    public List<Alert> findAll() {
        return alertRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Alert findById(Long id) {
        return alertRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Alerta", id));
    }
    
    @Transactional
    public Alert create(Alert alert) {
        return alertRepository.save(alert);
    }
    
    @Transactional
    public Alert update(Long id, AlertState state) {
        Alert alert = findById(id);
        alert.setState(state);
        return alertRepository.save(alert);
    }
    
    @Transactional
    public void delete(Long id) {
        Alert alert = findById(id);
        alertRepository.delete(alert);
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public List<AlertResponse> findAllDTOs() {
        return alertRepository.findAll().stream()
            .map(alertMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public AlertResponse findByIdDTO(Long id) {
        Alert alert = findById(id);
        return alertMapper.toResponse(alert);
    }
    
    @Transactional
    public AlertResponse createDTO(AlertRequest request) {
        Alert alert = alertMapper.toEntity(request);
        Alert saved = alertRepository.save(alert);
        return alertMapper.toResponse(saved);
    }
    
    @Transactional
    public AlertResponse updateDTO(Long id, AlertState state) {
        Alert alert = findById(id);
        alert.setState(state);
        Alert saved = alertRepository.save(alert);
        return alertMapper.toResponse(saved);
    }
}

