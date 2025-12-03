package com.extinguidor.controller;

import com.extinguidor.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Tag(name = "Comentarios", description = "Endpoints para gestión de comentarios")
@SecurityRequirement(name = "bearer-jwt")
public class CommentController {
    
    private final CommentService commentService;
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Listar todos los comentarios")
    public ResponseEntity<List<com.extinguidor.dto.CommentResponse>> getAllComments() {
        return ResponseEntity.ok(commentService.findAllDTOs());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Obtener comentario por ID")
    public ResponseEntity<com.extinguidor.dto.CommentResponse> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findByIdDTO(id));
    }
    
    @GetMapping("/parte/{parteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Listar comentarios por parte")
    public ResponseEntity<List<com.extinguidor.dto.CommentResponse>> getCommentsByParte(@PathVariable Long parteId) {
        return ResponseEntity.ok(commentService.findByParteIdDTOs(parteId));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Crear comentario")
    public ResponseEntity<com.extinguidor.dto.CommentResponse> createComment(@RequestBody com.extinguidor.dto.CommentRequest commentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createDTO(commentRequest));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Actualizar comentario")
    public ResponseEntity<com.extinguidor.dto.CommentResponse> updateComment(@PathVariable Long id, @RequestBody com.extinguidor.dto.CommentRequest commentRequest) {
        return ResponseEntity.ok(commentService.updateDTO(id, commentRequest));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @Operation(summary = "Eliminar comentario")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

