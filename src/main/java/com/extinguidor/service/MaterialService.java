package com.extinguidor.service;

import com.extinguidor.dto.MaterialRequest;
import com.extinguidor.dto.MaterialResponse;
import com.extinguidor.exception.BusinessException;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.mapper.MaterialMapper;
import com.extinguidor.model.entity.Material;
import com.extinguidor.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MaterialService {
    
    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;
    
    @Transactional(readOnly = true)
    public List<Material> findAll() {
        return materialRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Material findById(Long id) {
        return materialRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Material", id));
    }
    
    @Transactional
    public Material create(Material material) {
        if (materialRepository.existsByName(material.getName())) {
            throw new BusinessException("El nombre de material ya está en uso");
        }
        if (materialRepository.existsByCode(material.getCode())) {
            throw new BusinessException("El código de material ya está en uso");
        }
        return materialRepository.save(material);
    }
    
    @Transactional
    public Material update(Long id, Material materialDetails) {
        Material material = findById(id);
        
        if (!material.getName().equals(materialDetails.getName()) && 
            materialRepository.existsByName(materialDetails.getName())) {
            throw new BusinessException("El nombre ya está en uso");
        }
        if (!material.getCode().equals(materialDetails.getCode()) && 
            materialRepository.existsByCode(materialDetails.getCode())) {
            throw new BusinessException("El código ya está en uso");
        }
        
        material.setName(materialDetails.getName());
        material.setCode(materialDetails.getCode());
        material.setDescription(materialDetails.getDescription());
        material.setType(materialDetails.getType());
        
        return materialRepository.save(material);
    }
    
    @Transactional
    public void delete(Long id) {
        Material material = findById(id);
        materialRepository.delete(material);
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public List<MaterialResponse> findAllDTOs() {
        return materialRepository.findAll().stream()
            .map(materialMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public MaterialResponse findByIdDTO(Long id) {
        Material material = findById(id);
        return materialMapper.toResponse(material);
    }
    
    @Transactional
    public MaterialResponse createDTO(MaterialRequest request) {
        if (materialRepository.existsByName(request.getName())) {
            throw new BusinessException("El nombre de material ya está en uso");
        }
        if (materialRepository.existsByCode(request.getCode())) {
            throw new BusinessException("El código de material ya está en uso");
        }
        log.debug("Creando material con request: name={}, code={}, fechaUltimoMantenimiento={}, color={}, categoria={}", 
                  request.getName(), request.getCode(), request.getFechaUltimoMantenimiento(), 
                  request.getColor(), request.getCategoria());
        Material material = materialMapper.toEntity(request);
        log.debug("Material mapeado: fechaUltimoMantenimiento={}, color={}, categoria={}", 
                  material.getFechaUltimoMantenimiento(), material.getColor(), material.getCategoria());
        Material saved = materialRepository.save(material);
        log.debug("Material guardado: fechaUltimoMantenimiento={}, color={}, categoria={}", 
                  saved.getFechaUltimoMantenimiento(), saved.getColor(), saved.getCategoria());
        return materialMapper.toResponse(saved);
    }
    
    @Transactional
    public MaterialResponse updateDTO(Long id, MaterialRequest request) {
        Material material = findById(id);
        
        if (!material.getName().equals(request.getName()) && 
            materialRepository.existsByName(request.getName())) {
            throw new BusinessException("El nombre ya está en uso");
        }
        if (!material.getCode().equals(request.getCode()) && 
            materialRepository.existsByCode(request.getCode())) {
            throw new BusinessException("El código ya está en uso");
        }
        
        log.debug("Actualizando material ID={} con request: fechaUltimoMantenimiento={}, color={}, categoria={}", 
                  id, request.getFechaUltimoMantenimiento(), request.getColor(), request.getCategoria());
        log.debug("Material antes de actualizar: fechaUltimoMantenimiento={}, color={}, categoria={}", 
                  material.getFechaUltimoMantenimiento(), material.getColor(), material.getCategoria());
        
        materialMapper.updateEntity(material, request);
        
        // Asegurar que los campos se actualicen manualmente si el mapper no los mapea correctamente
        material.setFechaUltimoMantenimiento(request.getFechaUltimoMantenimiento());
        material.setColor(request.getColor());
        material.setCategoria(request.getCategoria());
        
        log.debug("Material después de mapeo: fechaUltimoMantenimiento={}, color={}, categoria={}", 
                  material.getFechaUltimoMantenimiento(), material.getColor(), material.getCategoria());
        
        Material saved = materialRepository.save(material);
        log.debug("Material guardado: fechaUltimoMantenimiento={}, color={}, categoria={}", 
                  saved.getFechaUltimoMantenimiento(), saved.getColor(), saved.getCategoria());
        return materialMapper.toResponse(saved);
    }
}

