package com.extinguidor.mapper;

import com.extinguidor.dto.RouteRequest;
import com.extinguidor.dto.RouteResponse;
import com.extinguidor.model.entity.Material;
import com.extinguidor.model.entity.Route;
import com.extinguidor.model.entity.RutaN;
import com.extinguidor.model.entity.User;
import com.extinguidor.model.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RouteMapper {
    
    @Mapping(target = "encargado", source = "encargado", qualifiedByName = "userToUserBasicResponse")
    @Mapping(target = "name", source = "name", qualifiedByName = "rutaNToRutaNBasicResponse")
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleToVehicleBasicResponse")
    @Mapping(target = "users", source = "users", qualifiedByName = "usersToUserBasicResponseList")
    @Mapping(target = "herramientas", source = "herramientas", qualifiedByName = "materialsToMaterialBasicResponseList")
    RouteResponse toResponse(Route route);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "encargado", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "herramientas", ignore = true)
    Route toEntity(RouteRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "encargado", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "herramientas", ignore = true)
    void updateEntity(@MappingTarget Route route, RouteRequest request);
    
    @Named("userToUserBasicResponse")
    default RouteResponse.UserBasicResponse userToUserBasicResponse(User user) {
        if (user == null) {
            return null;
        }
        return new RouteResponse.UserBasicResponse(
            user.getId(),
            user.getName(),
            user.getCode(),
            user.getEmail()
        );
    }
    
    @Named("rutaNToRutaNBasicResponse")
    default RouteResponse.RutaNBasicResponse rutaNToRutaNBasicResponse(RutaN rutaN) {
        if (rutaN == null) {
            return null;
        }
        return new RouteResponse.RutaNBasicResponse(
            rutaN.getId(),
            rutaN.getName()
        );
    }
    
    @Named("vehicleToVehicleBasicResponse")
    default RouteResponse.VehicleBasicResponse vehicleToVehicleBasicResponse(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return new RouteResponse.VehicleBasicResponse(
            vehicle.getId(),
            vehicle.getModelo(),
            vehicle.getBrand(),
            vehicle.getMatricula()
        );
    }
    
    @Named("usersToUserBasicResponseList")
    default List<RouteResponse.UserBasicResponse> usersToUserBasicResponseList(List<User> users) {
        if (users == null) {
            return new ArrayList<>();
        }
        return users.stream()
            .map(this::userToUserBasicResponse)
            .collect(Collectors.toList());
    }
    
    @Named("materialsToMaterialBasicResponseList")
    default List<RouteResponse.MaterialBasicResponse> materialsToMaterialBasicResponseList(List<Material> materials) {
        if (materials == null) {
            return new ArrayList<>();
        }
        return materials.stream()
            .map(m -> new RouteResponse.MaterialBasicResponse(
                m.getId(),
                m.getName(),
                m.getCode()
            ))
            .collect(Collectors.toList());
    }
}

