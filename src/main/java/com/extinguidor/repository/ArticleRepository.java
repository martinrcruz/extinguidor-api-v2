package com.extinguidor.repository;

import com.extinguidor.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    List<Article> findByEliminadoFalse();
    
    Optional<Article> findByCodigo(String codigo);
    
    boolean existsByCodigo(String codigo);
}

