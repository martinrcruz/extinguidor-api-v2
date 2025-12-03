package com.extinguidor.config;

import com.extinguidor.model.entity.User;
import com.extinguidor.model.enums.Role;
import com.extinguidor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
@Order(1) // Se ejecuta primero, antes de TestDataLoader
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    // Credenciales por defecto del administrador
    private static final String DEFAULT_ADMIN_EMAIL = "admin@extinguidor.com";
    private static final String DEFAULT_ADMIN_PASSWORD = "Admin123!";
    private static final String DEFAULT_ADMIN_NAME = "Administrador";
    private static final String DEFAULT_ADMIN_CODE = "ADMIN001";
    private static final String DEFAULT_ADMIN_PHONE = "+34600000000";
    
    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }
    
    private void initializeAdminUser() {
        // Verificar si existe algún usuario administrador
        List<User> adminUsers = userRepository.findByRole(Role.ADMIN);
        
        if (adminUsers.isEmpty()) {
            log.warn("═══════════════════════════════════════════════════════════════");
            log.warn("⚠️  NO SE ENCONTRÓ NINGÚN USUARIO ADMINISTRADOR");
            log.warn("═══════════════════════════════════════════════════════════════");
            log.info("Creando usuario administrador por defecto...");
            
            // Verificar si el email ya existe (por si acaso)
            if (userRepository.existsByEmail(DEFAULT_ADMIN_EMAIL)) {
                log.error("El email {} ya está en uso. No se puede crear el usuario administrador.", DEFAULT_ADMIN_EMAIL);
                return;
            }
            
            // Crear usuario administrador
            User admin = User.builder()
                    .name(DEFAULT_ADMIN_NAME)
                    .code(DEFAULT_ADMIN_CODE)
                    .email(DEFAULT_ADMIN_EMAIL)
                    .phone(DEFAULT_ADMIN_PHONE)
                    .password(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD))
                    .role(Role.ADMIN)
                    .activo(true)
                    .build();
            
            userRepository.save(admin);
            
            log.info("✅ Usuario administrador creado exitosamente");
            log.warn("═══════════════════════════════════════════════════════════════");
            log.warn("🔐 CREDENCIALES DE ACCESO ADMINISTRADOR");
            log.warn("═══════════════════════════════════════════════════════════════");
            log.warn("📧 Email:    {}", DEFAULT_ADMIN_EMAIL);
            log.warn("🔑 Password: {}", DEFAULT_ADMIN_PASSWORD);
            log.warn("═══════════════════════════════════════════════════════════════");
            log.warn("⚠️  IMPORTANTE: Cambia estas credenciales después del primer acceso");
            log.warn("═══════════════════════════════════════════════════════════════");
        } else {
            log.info("✅ Usuario(s) administrador(es) encontrado(s): {}", adminUsers.size());
            log.debug("Usuarios admin: {}", adminUsers.stream()
                    .map(u -> u.getEmail() + " (" + u.getName() + ")")
                    .toList());
        }
    }
}

