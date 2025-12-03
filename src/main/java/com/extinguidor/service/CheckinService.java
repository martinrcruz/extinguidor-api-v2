package com.extinguidor.service;

import com.extinguidor.dto.CheckinRequest;
import com.extinguidor.dto.CheckinResponse;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.mapper.CheckinMapper;
import com.extinguidor.model.entity.Checkin;
import com.extinguidor.model.entity.CheckinData;
import com.extinguidor.model.entity.Report;
import com.extinguidor.model.entity.Ubication;
import com.extinguidor.model.entity.User;
import com.extinguidor.repository.CheckinRepository;
import com.extinguidor.repository.ReportRepository;
import com.extinguidor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CheckinService {
    
    private final CheckinRepository checkinRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final CheckinMapper checkinMapper;
    
    @Transactional(readOnly = true)
    public List<Checkin> findAll() {
        return checkinRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Checkin findById(Long id) {
        return checkinRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Checkin", id));
    }
    
    @Transactional(readOnly = true)
    public List<Checkin> findByReportId(Long reportId) {
        reportRepository.findById(reportId)
            .orElseThrow(() -> new ResourceNotFoundException("Reporte", reportId));
        return checkinRepository.findByReportIdId(reportId);
    }
    
    @Transactional(readOnly = true)
    public List<Checkin> findByUserId(Long userId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", userId));
        return checkinRepository.findByUserIdId(userId);
    }
    
    @Transactional
    public Checkin create(Checkin checkin) {
        if (checkin.getUserId() != null && checkin.getUserId().getId() != null) {
            User user = userRepository.findById(checkin.getUserId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", checkin.getUserId().getId()));
            checkin.setUserId(user);
        }
        
        if (checkin.getReportId() != null && checkin.getReportId().getId() != null) {
            Report report = reportRepository.findById(checkin.getReportId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporte", checkin.getReportId().getId()));
            checkin.setReportId(report);
        }
        
        return checkinRepository.save(checkin);
    }
    
    @Transactional
    public Checkin update(Long id, Checkin checkinDetails) {
        Checkin checkin = findById(id);
        
        checkin.setCheckin(checkinDetails.getCheckin());
        checkin.setCheckout(checkinDetails.getCheckout());
        
        if (checkinDetails.getUserId() != null && checkinDetails.getUserId().getId() != null) {
            User user = userRepository.findById(checkinDetails.getUserId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", checkinDetails.getUserId().getId()));
            checkin.setUserId(user);
        }
        
        if (checkinDetails.getReportId() != null && checkinDetails.getReportId().getId() != null) {
            Report report = reportRepository.findById(checkinDetails.getReportId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporte", checkinDetails.getReportId().getId()));
            checkin.setReportId(report);
        }
        
        return checkinRepository.save(checkin);
    }
    
    @Transactional
    public void delete(Long id) {
        Checkin checkin = findById(id);
        checkinRepository.delete(checkin);
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public List<CheckinResponse> findAllDTOs() {
        return checkinRepository.findAll().stream()
            .map(checkinMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CheckinResponse findByIdDTO(Long id) {
        Checkin checkin = findById(id);
        return checkinMapper.toResponse(checkin);
    }
    
    @Transactional(readOnly = true)
    public List<CheckinResponse> findByReportIdDTOs(Long reportId) {
        reportRepository.findById(reportId)
            .orElseThrow(() -> new ResourceNotFoundException("Reporte", reportId));
        return checkinRepository.findByReportIdId(reportId).stream()
            .map(checkinMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CheckinResponse> findByUserIdDTOs(Long userId) {
        userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", userId));
        return checkinRepository.findByUserIdId(userId).stream()
            .map(checkinMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public CheckinResponse createDTO(CheckinRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", request.getUserId()));
        
        Report report = reportRepository.findById(request.getReportId())
            .orElseThrow(() -> new ResourceNotFoundException("Reporte", request.getReportId()));
        
        Checkin checkin = checkinMapper.toEntity(request);
        checkin.setUserId(user);
        checkin.setReportId(report);
        
        // Mapear checkin data
        if (request.getCheckin() != null) {
            CheckinData checkinData = new CheckinData();
            checkinData.setStartTime(request.getCheckin().getStartTime());
            if (request.getCheckin().getUbication() != null) {
                Ubication ubication = new Ubication();
                ubication.setLat(request.getCheckin().getUbication().getLat());
                ubication.setLng(request.getCheckin().getUbication().getLng());
                checkinData.setUbication(ubication);
            }
            checkin.setCheckin(checkinData);
        }
        
        // Mapear checkout data
        if (request.getCheckout() != null) {
            CheckinData checkoutData = new CheckinData();
            checkoutData.setStartTime(request.getCheckout().getStartTime());
            if (request.getCheckout().getUbication() != null) {
                Ubication ubication = new Ubication();
                ubication.setLat(request.getCheckout().getUbication().getLat());
                ubication.setLng(request.getCheckout().getUbication().getLng());
                checkoutData.setUbication(ubication);
            }
            checkin.setCheckout(checkoutData);
        }
        
        Checkin saved = checkinRepository.save(checkin);
        return checkinMapper.toResponse(saved);
    }
    
    @Transactional
    public CheckinResponse updateDTO(Long id, CheckinRequest request) {
        Checkin checkin = findById(id);
        
        checkinMapper.updateEntity(checkin, request);
        
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", request.getUserId()));
            checkin.setUserId(user);
        }
        
        if (request.getReportId() != null) {
            Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporte", request.getReportId()));
            checkin.setReportId(report);
        }
        
        // Mapear checkin data
        if (request.getCheckin() != null) {
            CheckinData checkinData = new CheckinData();
            checkinData.setStartTime(request.getCheckin().getStartTime());
            if (request.getCheckin().getUbication() != null) {
                Ubication ubication = new Ubication();
                ubication.setLat(request.getCheckin().getUbication().getLat());
                ubication.setLng(request.getCheckin().getUbication().getLng());
                checkinData.setUbication(ubication);
            }
            checkin.setCheckin(checkinData);
        }
        
        // Mapear checkout data
        if (request.getCheckout() != null) {
            CheckinData checkoutData = new CheckinData();
            checkoutData.setStartTime(request.getCheckout().getStartTime());
            if (request.getCheckout().getUbication() != null) {
                Ubication ubication = new Ubication();
                ubication.setLat(request.getCheckout().getUbication().getLat());
                ubication.setLng(request.getCheckout().getUbication().getLng());
                checkoutData.setUbication(ubication);
            }
            checkin.setCheckout(checkoutData);
        }
        
        Checkin saved = checkinRepository.save(checkin);
        return checkinMapper.toResponse(saved);
    }
}

