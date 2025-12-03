package com.extinguidor.controller;

import com.extinguidor.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Archivos", description = "Endpoints para gestión de archivos")
@SecurityRequirement(name = "bearer-jwt")
public class FileController {
    
    private final FileStorageService fileStorageService;
    
    @PostMapping("/upload/parte")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Subir archivo de parte")
    public ResponseEntity<String> uploadParteFile(@RequestParam("file") MultipartFile file) {
        String filename = fileStorageService.storeParteFile(file);
        return ResponseEntity.ok(filename);
    }
    
    @PostMapping("/upload/cliente")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Subir archivo de cliente")
    public ResponseEntity<String> uploadClienteFile(@RequestParam("file") MultipartFile file) {
        String filename = fileStorageService.storeClienteFile(file);
        return ResponseEntity.ok(filename);
    }
    
    @PostMapping("/upload/image")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Subir imagen")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String filename = fileStorageService.storeImage(file);
        return ResponseEntity.ok(filename);
    }
    
    @GetMapping("/download/parte/{filename:.+}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Descargar archivo de parte")
    public ResponseEntity<Resource> downloadParteFile(@PathVariable String filename) {
        try {
            Path filePath = fileStorageService.getParteFilePath(filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error al descargar archivo de parte: {}", filename, e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/download/cliente/{filename:.+}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Descargar archivo de cliente")
    public ResponseEntity<Resource> downloadClienteFile(@PathVariable String filename) {
        try {
            Path filePath = fileStorageService.getClienteFilePath(filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error al descargar archivo de cliente: {}", filename, e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/image/{filename:.+}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Obtener imagen")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = fileStorageService.getImageFilePath(filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                String contentType = "image/jpeg";
                if (filename.toLowerCase().endsWith(".png")) {
                    contentType = "image/png";
                }
                
                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error al obtener imagen: {}", filename, e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/parte/{filename:.+}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar archivo de parte")
    public ResponseEntity<Void> deleteParteFile(@PathVariable String filename) {
        fileStorageService.deleteParteFile(filename);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/cliente/{filename:.+}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar archivo de cliente")
    public ResponseEntity<Void> deleteClienteFile(@PathVariable String filename) {
        fileStorageService.deleteClienteFile(filename);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/image/{filename:.+}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar imagen")
    public ResponseEntity<Void> deleteImage(@PathVariable String filename) {
        fileStorageService.deleteImage(filename);
        return ResponseEntity.noContent().build();
    }
}

