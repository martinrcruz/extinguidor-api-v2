package com.extinguidor.repository;

import com.extinguidor.model.entity.ParteTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParteTemplateRepository extends JpaRepository<ParteTemplate, Long> {
    
    Optional<ParteTemplate> findByName(String name);
    
    boolean existsByName(String name);
    
    List<ParteTemplate> findByCustomerId(Long customerId);
}

