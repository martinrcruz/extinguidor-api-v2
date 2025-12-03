package com.extinguidor.mapper;

import com.extinguidor.dto.FacturacionRequest;
import com.extinguidor.dto.FacturacionResponse;
import com.extinguidor.model.entity.Facturacion;
import com.extinguidor.model.entity.Parte;
import com.extinguidor.model.entity.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FacturacionMapper {
    
    @Mapping(target = "ruta", source = "ruta", qualifiedByName = "routeToRouteBasicResponse")
    @Mapping(target = "parte", source = "parte", qualifiedByName = "parteToParteBasicResponse")
    FacturacionResponse toResponse(Facturacion facturacion);
    
    @Named("routeToRouteBasicResponse")
    default FacturacionResponse.RouteBasicResponse routeToRouteBasicResponse(Route route) {
        if (route == null || route.getName() == null) {
            return null;
        }
        return new FacturacionResponse.RouteBasicResponse(
            route.getId(),
            route.getName().getName()
        );
    }
    
    @Named("parteToParteBasicResponse")
    default FacturacionResponse.ParteBasicResponse parteToParteBasicResponse(Parte parte) {
        if (parte == null) {
            return null;
        }
        return new FacturacionResponse.ParteBasicResponse(
            parte.getId(),
            parte.getTitle(),
            parte.getDescription()
        );
    }
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "ruta", ignore = true)
    @Mapping(target = "parte", ignore = true)
    Facturacion toEntity(FacturacionRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "ruta", ignore = true)
    @Mapping(target = "parte", ignore = true)
    void updateEntity(@MappingTarget Facturacion facturacion, FacturacionRequest request);
}

