package com.extinguidor.mapper;

import com.extinguidor.dto.MaterialRequest;
import com.extinguidor.dto.MaterialResponse;
import com.extinguidor.model.entity.Material;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MaterialMapper {
    
    MaterialResponse toResponse(Material material);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    Material toEntity(MaterialRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "fechaUltimoMantenimiento", source = "fechaUltimoMantenimiento")
    @Mapping(target = "color", source = "color")
    @Mapping(target = "categoria", source = "categoria")
    void updateEntity(@MappingTarget Material material, MaterialRequest request);
}

