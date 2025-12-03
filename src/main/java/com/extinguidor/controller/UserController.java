package com.extinguidor.controller;

import com.extinguidor.dto.AuthRequest;
import com.extinguidor.dto.StandardApiResponse;
import com.extinguidor.model.entity.User;
import com.extinguidor.security.CustomUserDetails;
import com.extinguidor.security.JwtTokenProvider;
import com.extinguidor.service.AuthService;
import com.extinguidor.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios")
public class UserController {
    
    private final UserService userService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> login(@RequestBody AuthRequest request) {
        try {
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userService.findById(userDetails.getUserId());
            com.extinguidor.dto.UserResponse userResponse = userService.findByIdDTO(userDetails.getUserId());
            
            // Generar token
            String token = tokenProvider.generateToken(user.getEmail(), user.getRole().name(), user.getId());
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("role", user.getRole().name());
            data.put("user", userResponse);
            
            return ResponseEntity.ok(StandardApiResponse.success(data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(StandardApiResponse.error("Credenciales incorrectas"));
        }
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getAllUsers() {
        List<com.extinguidor.dto.UserResponse> users = userService.findAllDTOs();
        Map<String, Object> data = new HashMap<>();
        data.put("users", users);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @GetMapping("/worker")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Listar trabajadores")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getWorkers() {
        List<com.extinguidor.dto.UserResponse> workers = userService.findWorkersDTOs();
        Map<String, Object> data = new HashMap<>();
        data.put("workers", workers);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getUserById(@PathVariable Long id) {
        com.extinguidor.dto.UserResponse user = userService.findByIdDTO(id);
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @PostMapping("/create")
    @Operation(summary = "Crear usuario")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> createUser(@RequestBody com.extinguidor.dto.UserRequest userRequest) {
        com.extinguidor.dto.UserResponse createdUser = userService.createDTO(userRequest);
        Map<String, Object> data = new HashMap<>();
        data.put("user", createdUser);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(StandardApiResponse.success(data, "Usuario creado exitosamente"));
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> updateUser(
            @PathVariable Long id,
            @RequestBody com.extinguidor.dto.UserRequest userRequest) {
        com.extinguidor.dto.UserResponse updatedUser = userService.updateDTO(id, userRequest);
        
        // Generar nuevo token
        String token = tokenProvider.generateToken(updatedUser.getEmail(), updatedUser.getRole().name(), updatedUser.getId());
        
        Map<String, Object> data = new HashMap<>();
        data.put("user", updatedUser);
        data.put("token", token);
        
        return ResponseEntity.ok(StandardApiResponse.success(data, "Usuario actualizado exitosamente"));
    }
    
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Eliminar usuario")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Usuario eliminado exitosamente");
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Obtener perfil del usuario autenticado")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        com.extinguidor.dto.UserResponse user = userService.findByIdDTO(userDetails.getUserId());
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
}

