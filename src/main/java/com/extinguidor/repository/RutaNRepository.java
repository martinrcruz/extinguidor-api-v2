package com.extinguidor.repository;

import com.extinguidor.model.entity.RutaN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RutaNRepository extends JpaRepository<RutaN, Long> {
    
    Optional<RutaN> findByName(String name);
    
    boolean existsByName(String name);
}

