package com.extinguidor.service;

import com.extinguidor.exception.BusinessException;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.model.entity.Parte;
import com.extinguidor.model.entity.ParteTemplate;
import com.extinguidor.repository.ParteTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ParteTemplateService {
    
    private final ParteTemplateRepository templateRepository;
    private final ParteService parteService;
    
    @Transactional(readOnly = true)
    public List<ParteTemplate> findAll() {
        return templateRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public ParteTemplate findById(Long id) {
        return templateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Plantilla de Parte", id));
    }
    
    @Transactional(readOnly = true)
    public List<ParteTemplate> findByCustomerId(Long customerId) {
        return templateRepository.findByCustomerId(customerId);
    }
    
    @Transactional
    public ParteTemplate create(ParteTemplate template) {
        if (templateRepository.existsByName(template.getName())) {
            throw new BusinessException("Ya existe una plantilla con ese nombre");
        }
        return templateRepository.save(template);
    }
    
    @Transactional
    public ParteTemplate update(Long id, ParteTemplate template) {
        ParteTemplate existing = findById(id);
        
        if (!existing.getName().equals(template.getName()) && 
            templateRepository.existsByName(template.getName())) {
            throw new BusinessException("Ya existe una plantilla con ese nombre");
        }
        
        existing.setName(template.getName());
        existing.setTitle(template.getTitle());
        existing.setDescription(template.getDescription());
        existing.setCustomer(template.getCustomer());
        existing.setAddress(template.getAddress());
        existing.setType(template.getType());
        existing.setCategoria(template.getCategoria());
        existing.setPeriodico(template.getPeriodico());
        existing.setFrequency(template.getFrequency());
        existing.setCoordinationMethod(template.getCoordinationMethod());
        existing.setGestiona(template.getGestiona());
        existing.setFacturacion(template.getFacturacion());
        
        return templateRepository.save(existing);
    }
    
    @Transactional
    public void delete(Long id) {
        ParteTemplate template = findById(id);
        templateRepository.delete(template);
    }
    
    @Transactional
    public Parte createParteFromTemplate(Long templateId, LocalDate date) {
        ParteTemplate template = findById(templateId);
        
        Parte parte = Parte.builder()
            .title(template.getTitle())
            .description(template.getDescription())
            .customer(template.getCustomer())
            .address(template.getAddress())
            .date(date)
            .type(template.getType())
            .categoria(template.getCategoria())
            .periodico(template.getPeriodico())
            .frequency(template.getFrequency())
            .coordinationMethod(template.getCoordinationMethod())
            .gestiona(template.getGestiona())
            .facturacion(template.getFacturacion())
            .build();
        
        return parteService.create(parte);
    }
}

