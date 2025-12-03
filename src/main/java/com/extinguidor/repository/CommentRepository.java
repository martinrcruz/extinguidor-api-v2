package com.extinguidor.repository;

import com.extinguidor.model.entity.Comment;
import com.extinguidor.model.entity.Parte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByParte(Parte parte);
    
    List<Comment> findByParteId(Long parteId);
}

