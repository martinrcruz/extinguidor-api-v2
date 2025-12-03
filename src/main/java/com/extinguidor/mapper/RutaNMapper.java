package com.extinguidor.mapper;

import com.extinguidor.dto.RutaNRequest;
import com.extinguidor.dto.RutaNResponse;
import com.extinguidor.model.entity.RutaN;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RutaNMapper {
    
    RutaNResponse toResponse(RutaN rutaN);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    RutaN toEntity(RutaNRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    void updateEntity(@MappingTarget RutaN rutaN, RutaNRequest request);
}

