package com.extinguidor.controller;

import com.extinguidor.dto.StandardApiResponse;
import com.extinguidor.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/articulos")
@RequiredArgsConstructor
@Tag(name = "Artículos", description = "Endpoints para gestión de artículos")
@SecurityRequirement(name = "bearer-jwt")
public class ArticleController {
    
    private final ArticleService articleService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos los artículos")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getAllArticles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String grupo,
            @RequestParam(defaultValue = "") String familia) {
        List<com.extinguidor.dto.ArticleResponse> articulos = articleService.findAllDTOs();
        Map<String, Object> data = new HashMap<>();
        data.put("articulos", articulos);
        data.put("pagination", Map.of(
            "currentPage", page,
            "totalPages", 1,
            "totalItems", articulos.size(),
            "itemsPerPage", limit,
            "hasNextPage", false,
            "hasPrevPage", false
        ));
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener artículo por ID")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> getArticleById(@PathVariable Long id) {
        com.extinguidor.dto.ArticleResponse articulo = articleService.findByIdDTO(id);
        Map<String, Object> data = new HashMap<>();
        data.put("articulo", articulo);
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear artículo")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> createArticle(@RequestBody com.extinguidor.dto.ArticleRequest articleRequest) {
        com.extinguidor.dto.ArticleResponse created = articleService.createDTO(articleRequest);
        Map<String, Object> data = new HashMap<>();
        data.put("articulo", created);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(StandardApiResponse.success(data, "Artículo creado exitosamente"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar artículo")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> updateArticle(@PathVariable Long id, @RequestBody com.extinguidor.dto.ArticleRequest articleRequest) {
        com.extinguidor.dto.ArticleResponse updated = articleService.updateDTO(id, articleRequest);
        Map<String, Object> data = new HashMap<>();
        data.put("articulo", updated);
        return ResponseEntity.ok(StandardApiResponse.success(data, "Artículo actualizado exitosamente"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar artículo (lógica)")
    public ResponseEntity<StandardApiResponse<Map<String, Object>>> deleteArticle(@PathVariable Long id) {
        articleService.delete(id);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Artículo eliminado exitosamente");
        return ResponseEntity.ok(StandardApiResponse.success(data));
    }
}

