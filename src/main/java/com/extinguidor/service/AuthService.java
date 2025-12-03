package com.extinguidor.service;

import com.extinguidor.dto.AuthRequest;
import com.extinguidor.dto.AuthResponse;
import com.extinguidor.exception.BusinessException;
import com.extinguidor.model.entity.User;
import com.extinguidor.repository.UserRepository;
import com.extinguidor.security.CustomUserDetails;
import com.extinguidor.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    
    @Transactional
    public AuthResponse login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            
            if (!user.getActivo()) {
                throw new BusinessException("Usuario inactivo. No puede realizar operaciones.");
            }
            
            String token = tokenProvider.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId()
            );
            
            return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .userId(user.getId())
                .name(user.getName())
                .photo(user.getPhoto())
                .build();
                
        } catch (BadCredentialsException ex) {
            log.warn("Credenciales inválidas para email: {}", request.getEmail());
            throw new BadCredentialsException("Email o contraseña incorrectos");
        }
    }
    
    public boolean comparePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

