package com.extinguidor.repository;

import com.extinguidor.model.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    
    Optional<Material> findByName(String name);
    
    Optional<Material> findByCode(String code);
    
    boolean existsByName(String name);
    
    boolean existsByCode(String code);
}

