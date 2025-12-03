package com.extinguidor.mapper;

import com.extinguidor.dto.ZoneRequest;
import com.extinguidor.dto.ZoneResponse;
import com.extinguidor.model.entity.Zone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ZoneMapper {
    
    ZoneResponse toResponse(Zone zone);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    Zone toEntity(ZoneRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    void updateEntity(@MappingTarget Zone zone, ZoneRequest request);
}

