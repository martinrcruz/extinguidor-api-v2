package com.extinguidor.repository;

import com.extinguidor.model.entity.User;
import com.extinguidor.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByPhone(String phone);
    
    Optional<User> findByCode(String code);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);
    
    boolean existsByCode(String code);
    
    List<User> findByRole(Role role);
    
    List<User> findByActivoTrue();
    
    long countByActivoTrue();
    
    long countByRole(Role role);
}

