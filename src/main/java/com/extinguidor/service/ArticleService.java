package com.extinguidor.service;

import com.extinguidor.dto.ArticleRequest;
import com.extinguidor.dto.ArticleResponse;
import com.extinguidor.exception.BusinessException;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.mapper.ArticleMapper;
import com.extinguidor.model.entity.Article;
import com.extinguidor.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ArticleService {
    
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    
    @Transactional(readOnly = true)
    public List<Article> findAll() {
        return articleRepository.findByEliminadoFalse();
    }
    
    @Transactional(readOnly = true)
    public Article findById(Long id) {
        return articleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Artículo", id));
    }
    
    @Transactional
    public Article create(Article article) {
        if (articleRepository.existsByCodigo(article.getCodigo())) {
            throw new BusinessException("El código de artículo ya está en uso");
        }
        
        if (article.getPrecioVenta() == null) {
            article.setPrecioVenta(0.0);
        }
        
        return articleRepository.save(article);
    }
    
    @Transactional
    public Article update(Long id, Article articleDetails) {
        Article article = findById(id);
        
        if (!article.getCodigo().equals(articleDetails.getCodigo()) && 
            articleRepository.existsByCodigo(articleDetails.getCodigo())) {
            throw new BusinessException("El código ya está en uso");
        }
        
        article.setCantidad(articleDetails.getCantidad());
        article.setCodigo(articleDetails.getCodigo());
        article.setGrupo(articleDetails.getGrupo());
        article.setFamilia(articleDetails.getFamilia());
        article.setDescripcionArticulo(articleDetails.getDescripcionArticulo());
        article.setPrecioVenta(articleDetails.getPrecioVenta());
        
        return articleRepository.save(article);
    }
    
    @Transactional
    public void delete(Long id) {
        Article article = findById(id);
        article.setEliminado(true);
        articleRepository.save(article);
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public List<ArticleResponse> findAllDTOs() {
        return articleRepository.findByEliminadoFalse().stream()
            .map(articleMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ArticleResponse findByIdDTO(Long id) {
        Article article = findById(id);
        return articleMapper.toResponse(article);
    }
    
    @Transactional
    public ArticleResponse createDTO(ArticleRequest request) {
        if (articleRepository.existsByCodigo(request.getCodigo())) {
            throw new BusinessException("El código de artículo ya está en uso");
        }
        
        Article article = articleMapper.toEntity(request);
        
        if (article.getPrecioVenta() == null) {
            article.setPrecioVenta(0.0);
        }
        
        Article saved = articleRepository.save(article);
        return articleMapper.toResponse(saved);
    }
    
    @Transactional
    public ArticleResponse updateDTO(Long id, ArticleRequest request) {
        Article article = findById(id);
        
        if (!article.getCodigo().equals(request.getCodigo()) && 
            articleRepository.existsByCodigo(request.getCodigo())) {
            throw new BusinessException("El código ya está en uso");
        }
        
        articleMapper.updateEntity(article, request);
        Article saved = articleRepository.save(article);
        return articleMapper.toResponse(saved);
    }
}

