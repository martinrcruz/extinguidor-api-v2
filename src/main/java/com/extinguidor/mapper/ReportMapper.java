package com.extinguidor.mapper;

import com.extinguidor.dto.ReportRequest;
import com.extinguidor.dto.ReportResponse;
import com.extinguidor.model.entity.Customer;
import com.extinguidor.model.entity.Material;
import com.extinguidor.model.entity.Report;
import com.extinguidor.model.entity.Route;
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
public interface ReportMapper {
    
    @Mapping(target = "workersId", source = "workersId", qualifiedByName = "usersToUserBasicResponseList")
    @Mapping(target = "customerId", source = "customerId", qualifiedByName = "customerToCustomerBasicResponse")
    @Mapping(target = "contractId", source = "contractId", qualifiedByName = "customerToCustomerBasicResponse")
    @Mapping(target = "routeId", source = "routeId", qualifiedByName = "routeToRouteBasicResponse")
    @Mapping(target = "tools", source = "tools", qualifiedByName = "materialsToMaterialBasicResponseList")
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehiclesToVehicleBasicResponseList")
    @Mapping(target = "userId", source = "userId", qualifiedByName = "userToUserBasicResponse")
    ReportResponse toResponse(Report report);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDateAudit", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "workersId", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "contractId", ignore = true)
    @Mapping(target = "routeId", ignore = true)
    @Mapping(target = "tools", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "checkins", ignore = true)
    Report toEntity(ReportRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDateAudit", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "workersId", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "contractId", ignore = true)
    @Mapping(target = "routeId", ignore = true)
    @Mapping(target = "tools", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "checkins", ignore = true)
    void updateEntity(@MappingTarget Report report, ReportRequest request);
    
    @Named("usersToUserBasicResponseList")
    default List<ReportResponse.UserBasicResponse> usersToUserBasicResponseList(List<User> users) {
        if (users == null) {
            return new ArrayList<>();
        }
        return users.stream()
            .map(this::userToUserBasicResponse)
            .collect(Collectors.toList());
    }
    
    @Named("userToUserBasicResponse")
    default ReportResponse.UserBasicResponse userToUserBasicResponse(User user) {
        if (user == null) {
            return null;
        }
        return new ReportResponse.UserBasicResponse(
            user.getId(),
            user.getName(),
            user.getCode()
        );
    }
    
    @Named("customerToCustomerBasicResponse")
    default ReportResponse.CustomerBasicResponse customerToCustomerBasicResponse(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new ReportResponse.CustomerBasicResponse(
            customer.getId(),
            customer.getName(),
            customer.getCode()
        );
    }
    
    @Named("routeToRouteBasicResponse")
    default ReportResponse.RouteBasicResponse routeToRouteBasicResponse(Route route) {
        if (route == null || route.getName() == null) {
            return null;
        }
        return new ReportResponse.RouteBasicResponse(
            route.getId(),
            route.getName().getName()
        );
    }
    
    @Named("materialsToMaterialBasicResponseList")
    default List<ReportResponse.MaterialBasicResponse> materialsToMaterialBasicResponseList(List<Material> materials) {
        if (materials == null) {
            return new ArrayList<>();
        }
        return materials.stream()
            .map(m -> new ReportResponse.MaterialBasicResponse(
                m.getId(),
                m.getName(),
                m.getCode()
            ))
            .collect(Collectors.toList());
    }
    
    @Named("vehiclesToVehicleBasicResponseList")
    default List<ReportResponse.VehicleBasicResponse> vehiclesToVehicleBasicResponseList(List<Vehicle> vehicles) {
        if (vehicles == null) {
            return new ArrayList<>();
        }
        return vehicles.stream()
            .map(v -> new ReportResponse.VehicleBasicResponse(
                v.getId(),
                v.getModelo(),
                v.getMatricula()
            ))
            .collect(Collectors.toList());
    }
}

