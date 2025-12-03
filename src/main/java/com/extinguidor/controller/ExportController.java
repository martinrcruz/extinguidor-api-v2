package com.extinguidor.controller;

import com.extinguidor.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Exportación", description = "Endpoints para exportar datos a Excel y PDF")
@SecurityRequirement(name = "bearer-jwt")
public class ExportController {
    
    private final ExportService exportService;
    
    @GetMapping("/partes/excel")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Exportar partes a Excel")
    public ResponseEntity<byte[]> exportPartesToExcel() {
        try {
            byte[] excelData = exportService.exportPartesToExcel();
            String filename = "partes_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelData);
        } catch (IOException e) {
            log.error("Error al exportar partes a Excel", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/partes/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Exportar partes a PDF")
    public ResponseEntity<byte[]> exportPartesToPDF() {
        try {
            byte[] pdfData = exportService.exportPartesToPDF();
            String filename = "partes_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
        } catch (IOException e) {
            log.error("Error al exportar partes a PDF", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/clientes/excel")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Exportar clientes a Excel")
    public ResponseEntity<byte[]> exportCustomersToExcel() {
        try {
            byte[] excelData = exportService.exportCustomersToExcel();
            String filename = "clientes_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelData);
        } catch (IOException e) {
            log.error("Error al exportar clientes a Excel", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

