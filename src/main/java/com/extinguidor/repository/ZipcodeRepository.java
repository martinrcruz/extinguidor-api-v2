package com.extinguidor.repository;

import com.extinguidor.model.entity.Zipcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZipcodeRepository extends JpaRepository<Zipcode, Long> {
    
    Optional<Zipcode> findByCodezip(String codezip);
    
    boolean existsByCodezip(String codezip);
}

