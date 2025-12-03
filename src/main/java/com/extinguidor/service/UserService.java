package com.extinguidor.service;

import com.extinguidor.dto.UserRequest;
import com.extinguidor.dto.UserResponse;
import com.extinguidor.exception.BusinessException;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.mapper.UserMapper;
import com.extinguidor.model.entity.User;
import com.extinguidor.model.enums.Role;
import com.extinguidor.repository.UserRepository;
import com.extinguidor.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;
    
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<User> findWorkers() {
        return userRepository.findByRole(Role.WORKER);
    }
    
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }
    
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario con email: " + email));
    }
    
    @Transactional
    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException("El email ya está en uso");
        }
        if (userRepository.existsByPhone(user.getPhone())) {
            throw new BusinessException("El teléfono ya está en uso");
        }
        if (userRepository.existsByCode(user.getCode())) {
            throw new BusinessException("El código ya está en uso");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    @Transactional
    public User update(Long id, User userDetails) {
        User user = findById(id);
        
        if (!user.getEmail().equals(userDetails.getEmail()) && 
            userRepository.existsByEmail(userDetails.getEmail())) {
            throw new BusinessException("El email ya está en uso");
        }
        
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setRole(userDetails.getRole());
        user.setPhoto(userDetails.getPhoto());
        user.setActivo(userDetails.getActivo());
        
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        return userRepository.save(user);
    }
    
    @Transactional
    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }
    
    @Transactional(readOnly = true)
    public boolean isUserActive(Long userId) {
        return userRepository.findById(userId)
            .map(User::getActivo)
            .orElse(false);
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public List<UserResponse> findAllDTOs() {
        return userRepository.findAll().stream()
            .map(userMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<UserResponse> findWorkersDTOs() {
        return userRepository.findByRole(Role.WORKER).stream()
            .map(userMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public UserResponse findByIdDTO(Long id) {
        User user = findById(id);
        return userMapper.toResponse(user);
    }
    
    @Transactional
    public UserResponse createDTO(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("El email ya está en uso");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("El teléfono ya está en uso");
        }
        if (userRepository.existsByCode(request.getCode())) {
            throw new BusinessException("El código ya está en uso");
        }
        
        User user = userMapper.toEntity(request);
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }
    
    @Transactional
    public UserResponse updateDTO(Long id, UserRequest request) {
        User user = findById(id);
        
        if (!user.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("El email ya está en uso");
        }
        
        userMapper.updateEntity(user, request);
        
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }
}

