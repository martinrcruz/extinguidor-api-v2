package com.extinguidor.mapper;

import com.extinguidor.dto.CommentRequest;
import com.extinguidor.dto.CommentResponse;
import com.extinguidor.model.entity.Comment;
import com.extinguidor.model.entity.CommentFoto;
import com.extinguidor.model.entity.Parte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {
    
    @Mapping(target = "parte", source = "parte", qualifiedByName = "parteToParteBasicResponse")
    @Mapping(target = "fotos", source = "fotos", qualifiedByName = "fotosToStringList")
    CommentResponse toResponse(Comment comment);
    
    @Named("parteToParteBasicResponse")
    default CommentResponse.ParteBasicResponse parteToParteBasicResponse(Parte parte) {
        if (parte == null) {
            return null;
        }
        return new CommentResponse.ParteBasicResponse(
            parte.getId(),
            parte.getTitle(),
            parte.getDescription()
        );
    }
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "parte", ignore = true)
    @Mapping(target = "fotos", ignore = true)
    Comment toEntity(CommentRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "parte", ignore = true)
    @Mapping(target = "fotos", ignore = true)
    void updateEntity(@MappingTarget Comment comment, CommentRequest request);
    
    @Named("fotosToStringList")
    default List<String> fotosToStringList(List<CommentFoto> fotos) {
        if (fotos == null) {
            return new ArrayList<>();
        }
        return fotos.stream()
                .map(CommentFoto::getFoto)
                .collect(Collectors.toList());
    }
}

