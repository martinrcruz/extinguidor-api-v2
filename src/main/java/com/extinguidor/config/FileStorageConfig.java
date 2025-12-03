package com.extinguidor.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Getter
public class FileStorageConfig {
    
    @Value("${app.file.upload-dir}")
    private String uploadDir;
    
    @Value("${app.file.partes-dir}")
    private String partesDir;
    
    @Value("${app.file.clientes-dir}")
    private String clientesDir;
    
    @PostConstruct
    public void init() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            Path partesPath = Paths.get(partesDir);
            Path clientesPath = Paths.get(clientesDir);
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            if (!Files.exists(partesPath)) {
                Files.createDirectories(partesPath);
            }
            if (!Files.exists(clientesPath)) {
                Files.createDirectories(clientesPath);
            }
        } catch (Exception ex) {
            throw new RuntimeException("No se pudieron crear los directorios de almacenamiento", ex);
        }
    }
}

