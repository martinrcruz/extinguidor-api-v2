package com.extinguidor.mapper;

import com.extinguidor.dto.CheckinRequest;
import com.extinguidor.dto.CheckinResponse;
import com.extinguidor.model.entity.Checkin;
import com.extinguidor.model.entity.Report;
import com.extinguidor.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CheckinMapper {
    
    @Mapping(target = "userId", source = "userId", qualifiedByName = "userToUserBasicResponse")
    @Mapping(target = "reportId", source = "reportId", qualifiedByName = "reportToReportBasicResponse")
    CheckinResponse toResponse(Checkin checkin);
    
    @Named("userToUserBasicResponse")
    default CheckinResponse.UserBasicResponse userToUserBasicResponse(User user) {
        if (user == null) {
            return null;
        }
        return new CheckinResponse.UserBasicResponse(
            user.getId(),
            user.getName(),
            user.getCode()
        );
    }
    
    @Named("reportToReportBasicResponse")
    default CheckinResponse.ReportBasicResponse reportToReportBasicResponse(Report report) {
        if (report == null) {
            return null;
        }
        return new CheckinResponse.ReportBasicResponse(
            report.getId(),
            report.getTitle(),
            report.getDescription()
        );
    }
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "reportId", ignore = true)
    Checkin toEntity(CheckinRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "reportId", ignore = true)
    void updateEntity(@MappingTarget Checkin checkin, CheckinRequest request);
}

