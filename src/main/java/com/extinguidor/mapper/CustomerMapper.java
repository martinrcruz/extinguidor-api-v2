package com.extinguidor.mapper;

import com.extinguidor.dto.CustomerRequest;
import com.extinguidor.dto.CustomerResponse;
import com.extinguidor.model.entity.Customer;
import com.extinguidor.model.entity.CustomerContractSystem;
import com.extinguidor.model.entity.CustomerDocument;
import com.extinguidor.model.entity.Zone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    
    @Mapping(target = "zone", source = "zone", qualifiedByName = "zoneToZoneBasicResponse")
    @Mapping(target = "contractSystems", source = "contractSystems", qualifiedByName = "contractSystemsToStringList")
    @Mapping(target = "documents", source = "documents", qualifiedByName = "documentsToDTOList")
    CustomerResponse toResponse(Customer customer);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "zone", ignore = true)
    @Mapping(target = "contractSystems", ignore = true)
    @Mapping(target = "documents", ignore = true)
    Customer toEntity(CustomerRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "zone", ignore = true)
    @Mapping(target = "contractSystems", ignore = true)
    @Mapping(target = "documents", ignore = true)
    void updateEntity(@MappingTarget Customer customer, CustomerRequest request);
    
    @Named("contractSystemsToStringList")
    default List<String> contractSystemsToStringList(List<CustomerContractSystem> systems) {
        if (systems == null) {
            return new ArrayList<>();
        }
        return systems.stream()
                .map(CustomerContractSystem::getContractSystem)
                .collect(Collectors.toList());
    }
    
    @Named("documentsToDTOList")
    default List<CustomerResponse.DocumentDTO> documentsToDTOList(List<CustomerDocument> documents) {
        if (documents == null) {
            return new ArrayList<>();
        }
        return documents.stream()
                .map(doc -> new CustomerResponse.DocumentDTO(doc.getName(), doc.getUrl()))
                .collect(Collectors.toList());
    }
    
    @Named("zoneToZoneBasicResponse")
    default CustomerResponse.ZoneBasicResponse zoneToZoneBasicResponse(Zone zone) {
        if (zone == null) {
            return null;
        }
        return new CustomerResponse.ZoneBasicResponse(
            zone.getId(),
            zone.getName(),
            zone.getCode()
        );
    }
}

