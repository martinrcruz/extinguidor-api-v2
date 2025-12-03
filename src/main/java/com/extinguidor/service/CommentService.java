package com.extinguidor.service;

import com.extinguidor.dto.CommentRequest;
import com.extinguidor.dto.CommentResponse;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.mapper.CommentMapper;
import com.extinguidor.model.entity.Comment;
import com.extinguidor.model.entity.CommentFoto;
import com.extinguidor.model.entity.Parte;
import com.extinguidor.repository.CommentRepository;
import com.extinguidor.repository.ParteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final ParteRepository parteRepository;
    private final CommentMapper commentMapper;
    
    @Transactional(readOnly = true)
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Comment findById(Long id) {
        return commentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comentario", id));
    }
    
    @Transactional(readOnly = true)
    public List<Comment> findByParteId(Long parteId) {
        parteRepository.findById(parteId)
            .orElseThrow(() -> new ResourceNotFoundException("Parte", parteId));
        return commentRepository.findByParteId(parteId);
    }
    
    @Transactional
    public Comment create(Comment comment) {
        if (comment.getParte() != null && comment.getParte().getId() != null) {
            Parte parte = parteRepository.findById(comment.getParte().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Parte", comment.getParte().getId()));
            comment.setParte(parte);
        }
        return commentRepository.save(comment);
    }
    
    @Transactional
    public Comment update(Long id, Comment commentDetails) {
        Comment comment = findById(id);
        
        comment.setComentario(commentDetails.getComentario());
        comment.setDate(commentDetails.getDate());
        comment.setLat(commentDetails.getLat());
        comment.setLgn(commentDetails.getLgn());
        comment.setFotos(commentDetails.getFotos());
        
        if (commentDetails.getParte() != null && commentDetails.getParte().getId() != null) {
            Parte parte = parteRepository.findById(commentDetails.getParte().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Parte", commentDetails.getParte().getId()));
            comment.setParte(parte);
        }
        
        return commentRepository.save(comment);
    }
    
    @Transactional
    public void delete(Long id) {
        Comment comment = findById(id);
        commentRepository.delete(comment);
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public List<CommentResponse> findAllDTOs() {
        return commentRepository.findAll().stream()
            .map(commentMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CommentResponse findByIdDTO(Long id) {
        Comment comment = findById(id);
        return commentMapper.toResponse(comment);
    }
    
    @Transactional(readOnly = true)
    public List<CommentResponse> findByParteIdDTOs(Long parteId) {
        parteRepository.findById(parteId)
            .orElseThrow(() -> new ResourceNotFoundException("Parte", parteId));
        return commentRepository.findByParteId(parteId).stream()
            .map(commentMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public CommentResponse createDTO(CommentRequest request) {
        Parte parte = parteRepository.findById(request.getParteId())
            .orElseThrow(() -> new ResourceNotFoundException("Parte", request.getParteId()));
        
        Comment comment = commentMapper.toEntity(request);
        comment.setParte(parte);
        
        // Mapear fotos
        if (request.getFotos() != null && !request.getFotos().isEmpty()) {
            List<CommentFoto> fotos = request.getFotos().stream()
                .map(foto -> CommentFoto.builder()
                    .comment(comment)
                    .foto(foto)
                    .build())
                .collect(Collectors.toList());
            comment.setFotos(fotos);
        }
        
        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponse(saved);
    }
    
    @Transactional
    public CommentResponse updateDTO(Long id, CommentRequest request) {
        Comment comment = findById(id);
        
        commentMapper.updateEntity(comment, request);
        
        if (request.getParteId() != null) {
            Parte parte = parteRepository.findById(request.getParteId())
                .orElseThrow(() -> new ResourceNotFoundException("Parte", request.getParteId()));
            comment.setParte(parte);
        }
        
        // Actualizar fotos
        if (request.getFotos() != null) {
            comment.getFotos().clear();
            if (!request.getFotos().isEmpty()) {
                List<CommentFoto> fotos = request.getFotos().stream()
                    .map(foto -> CommentFoto.builder()
                        .comment(comment)
                        .foto(foto)
                        .build())
                    .collect(Collectors.toList());
                comment.getFotos().addAll(fotos);
            }
        }
        
        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponse(saved);
    }
}

