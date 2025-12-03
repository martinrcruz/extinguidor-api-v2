package com.extinguidor.mapper;

import com.extinguidor.dto.ParteRequest;
import com.extinguidor.dto.ParteResponse;
import com.extinguidor.model.entity.Customer;
import com.extinguidor.model.entity.Parte;
import com.extinguidor.model.entity.ParteArticulo;
import com.extinguidor.model.entity.ParteComentario;
import com.extinguidor.model.entity.ParteDocumento;
import com.extinguidor.model.entity.Route;
import com.extinguidor.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ParteMapper {
    
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerToCustomerBasicResponse")
    @Mapping(target = "ruta", source = "ruta", qualifiedByName = "routeToRouteBasicResponse")
    @Mapping(target = "worker", source = "worker", qualifiedByName = "userToUserBasicResponse")
    @Mapping(target = "comentarios", source = "comentarios", qualifiedByName = "comentariosToDTOList")
    @Mapping(target = "documentos", source = "documentos", qualifiedByName = "documentosToDTOList")
    @Mapping(target = "articulos", source = "articulos", qualifiedByName = "articulosToDTOList")
    ParteResponse toResponse(Parte parte);
    
    @Named("customerToCustomerBasicResponse")
    default ParteResponse.CustomerBasicResponse customerToCustomerBasicResponse(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new ParteResponse.CustomerBasicResponse(
            customer.getId(),
            customer.getName(),
            customer.getCode(),
            customer.getEmail()
        );
    }
    
    @Named("routeToRouteBasicResponse")
    default ParteResponse.RouteBasicResponse routeToRouteBasicResponse(Route route) {
        if (route == null || route.getName() == null) {
            return null;
        }
        return new ParteResponse.RouteBasicResponse(
            route.getId(),
            route.getName().getName(),
            route.getDate()
        );
    }
    
    @Named("userToUserBasicResponse")
    default ParteResponse.UserBasicResponse userToUserBasicResponse(User user) {
        if (user == null) {
            return null;
        }
        return new ParteResponse.UserBasicResponse(
            user.getId(),
            user.getName(),
            user.getCode(),
            user.getEmail()
        );
    }
    
    @Named("comentariosToDTOList")
    default List<ParteResponse.ParteComentarioDTO> comentariosToDTOList(List<ParteComentario> comentarios) {
        if (comentarios == null) {
            return new ArrayList<>();
        }
        return comentarios.stream()
                .map(com -> new ParteResponse.ParteComentarioDTO(com.getTexto(), com.getFecha(), com.getUsuario()))
                .collect(Collectors.toList());
    }
    
    @Named("documentosToDTOList")
    default List<ParteResponse.ParteDocumentoDTO> documentosToDTOList(List<ParteDocumento> documentos) {
        if (documentos == null) {
            return new ArrayList<>();
        }
        return documentos.stream()
                .map(doc -> new ParteResponse.ParteDocumentoDTO(doc.getNombre(), doc.getUrl(), doc.getTipo(), doc.getFecha()))
                .collect(Collectors.toList());
    }
    
    @Named("articulosToDTOList")
    default List<ParteResponse.ParteArticuloDTO> articulosToDTOList(List<ParteArticulo> articulos) {
        if (articulos == null) {
            return new ArrayList<>();
        }
        return articulos.stream()
                .map(art -> new ParteResponse.ParteArticuloDTO(
                        art.getCantidad(), art.getCodigo(), art.getGrupo(), 
                        art.getFamilia(), art.getDescripcionArticulo(), art.getPrecioVenta()))
                .collect(Collectors.toList());
    }
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "ruta", ignore = true)
    @Mapping(target = "worker", ignore = true)
    @Mapping(target = "finalizadoTime", ignore = true)
    @Mapping(target = "comentarios", ignore = true)
    @Mapping(target = "documentos", ignore = true)
    @Mapping(target = "articulos", ignore = true)
    Parte toEntity(ParteRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "ruta", ignore = true)
    @Mapping(target = "worker", ignore = true)
    @Mapping(target = "finalizadoTime", ignore = true)
    @Mapping(target = "comentarios", ignore = true)
    @Mapping(target = "documentos", ignore = true)
    @Mapping(target = "articulos", ignore = true)
    void updateEntity(@MappingTarget Parte parte, ParteRequest request);
}

