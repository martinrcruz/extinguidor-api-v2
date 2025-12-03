package com.extinguidor.mapper;

import com.extinguidor.dto.ZipcodeRequest;
import com.extinguidor.dto.ZipcodeResponse;
import com.extinguidor.model.entity.Zipcode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ZipcodeMapper {
    
    ZipcodeResponse toResponse(Zipcode zipcode);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    Zipcode toEntity(ZipcodeRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    void updateEntity(@MappingTarget Zipcode zipcode, ZipcodeRequest request);
}

