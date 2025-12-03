package com.extinguidor.service;

import com.extinguidor.config.FileStorageConfig;
import com.extinguidor.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class FileStorageService {
    
    private final FileStorageConfig fileStorageConfig;
    
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png"
    );
    
    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList(
        "application/pdf"
    );
    
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    
    /**
     * Valida el tipo y tamaño de un archivo
     */
    public void validateFile(MultipartFile file, boolean isImage) {
        if (file.isEmpty()) {
            throw new BusinessException("El archivo está vacío");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException("El archivo excede el tamaño máximo permitido (5MB)");
        }
        
        String contentType = file.getContentType();
        if (isImage) {
            if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
                throw new BusinessException("Solo se permiten archivos de imagen (JPEG, PNG)");
            }
        } else {
            if (contentType == null || !ALLOWED_DOCUMENT_TYPES.contains(contentType)) {
                throw new BusinessException("Solo se permiten archivos PDF");
            }
        }
    }
    
    /**
     * Guarda un archivo en el directorio de partes
     */
    public String storeParteFile(MultipartFile file) {
        validateFile(file, false); // PDFs para partes
        return storeFile(file, fileStorageConfig.getPartesDir());
    }
    
    /**
     * Guarda un archivo en el directorio de clientes
     */
    public String storeClienteFile(MultipartFile file) {
        validateFile(file, false); // PDFs para clientes
        return storeFile(file, fileStorageConfig.getClientesDir());
    }
    
    /**
     * Guarda una imagen
     */
    public String storeImage(MultipartFile file) {
        validateFile(file, true);
        return storeFile(file, fileStorageConfig.getUploadDir());
    }
    
    /**
     * Guarda un archivo en el directorio especificado
     */
    private String storeFile(MultipartFile file, String directory) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new BusinessException("El nombre del archivo no es válido");
            }
            
            String extension = "";
            int lastDotIndex = originalFilename.lastIndexOf('.');
            if (lastDotIndex > 0) {
                extension = originalFilename.substring(lastDotIndex);
            }
            
            String filename = UUID.randomUUID().toString() + extension;
            Path targetLocation = Paths.get(directory).resolve(filename);
            
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("Archivo guardado: {}", targetLocation.toString());
            return filename;
        } catch (IOException ex) {
            log.error("Error al guardar el archivo", ex);
            throw new BusinessException("Error al guardar el archivo: " + ex.getMessage());
        }
    }
    
    /**
     * Obtiene la ruta completa de un archivo
     */
    public Path getFilePath(String filename, String directory) {
        return Paths.get(directory).resolve(filename).normalize();
    }
    
    /**
     * Obtiene la ruta completa de un archivo de parte
     */
    public Path getParteFilePath(String filename) {
        return getFilePath(filename, fileStorageConfig.getPartesDir());
    }
    
    /**
     * Obtiene la ruta completa de un archivo de cliente
     */
    public Path getClienteFilePath(String filename) {
        return getFilePath(filename, fileStorageConfig.getClientesDir());
    }
    
    /**
     * Obtiene la ruta completa de una imagen
     */
    public Path getImageFilePath(String filename) {
        return getFilePath(filename, fileStorageConfig.getUploadDir());
    }
    
    /**
     * Elimina un archivo
     */
    public void deleteFile(String filename, String directory) {
        try {
            Path filePath = getFilePath(filename, directory);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Archivo eliminado: {}", filePath.toString());
            }
        } catch (IOException ex) {
            log.error("Error al eliminar el archivo: {}", filename, ex);
            throw new BusinessException("Error al eliminar el archivo: " + ex.getMessage());
        }
    }
    
    /**
     * Elimina un archivo de parte
     */
    public void deleteParteFile(String filename) {
        deleteFile(filename, fileStorageConfig.getPartesDir());
    }
    
    /**
     * Elimina un archivo de cliente
     */
    public void deleteClienteFile(String filename) {
        deleteFile(filename, fileStorageConfig.getClientesDir());
    }
    
    /**
     * Elimina una imagen
     */
    public void deleteImage(String filename) {
        deleteFile(filename, fileStorageConfig.getUploadDir());
    }
    
    /**
     * Verifica si un archivo existe
     */
    public boolean fileExists(String filename, String directory) {
        Path filePath = getFilePath(filename, directory);
        return Files.exists(filePath);
    }
}

