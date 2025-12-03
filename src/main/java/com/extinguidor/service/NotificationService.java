package com.extinguidor.service;

import com.extinguidor.model.entity.Alert;
import com.extinguidor.model.entity.Parte;
import com.extinguidor.model.entity.User;
import com.extinguidor.repository.AlertRepository;
import com.extinguidor.repository.ParteRepository;
import com.extinguidor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationService {
    
    private final AlertRepository alertRepository;
    private final ParteRepository parteRepository;
    private final UserRepository userRepository;
    
    /**
     * Crea una alerta automática
     */
    @Transactional
    public Alert createAlert(String message) {
        Alert alert = Alert.builder()
            .message(message)
            .state(com.extinguidor.model.enums.AlertState.Pendiente)
            .createdDate(LocalDateTime.now())
            .build();
        return alertRepository.save(alert);
    }
    
    /**
     * Verifica partes vencidos y crea alertas
     */
    @Scheduled(cron = "0 0 8 * * *") // Todos los días a las 8 AM
    @Transactional
    public void checkOverduePartes() {
        LocalDate today = LocalDate.now();
        List<Parte> overduePartes = parteRepository.findByEliminadoFalse().stream()
            .filter(parte -> {
                if (parte.getDate() == null || parte.getState() == null) {
                    return false;
                }
                return parte.getDate().isBefore(today) && 
                       parte.getState() != com.extinguidor.model.enums.ParteState.Finalizado;
            })
            .toList();
        
        if (!overduePartes.isEmpty()) {
            String message = String.format("Hay %d partes vencidos que requieren atención", overduePartes.size());
            createAlert(message);
            log.info("Alerta creada: {}", message);
        }
    }
    
    /**
     * Verifica partes sin asignar y crea alertas
     */
    @Scheduled(cron = "0 0 9 * * *") // Todos los días a las 9 AM
    @Transactional
    public void checkUnassignedPartes() {
        List<Parte> unassignedPartes = parteRepository.findByEliminadoFalse().stream()
            .filter(parte -> !Boolean.TRUE.equals(parte.getAsignado()) && 
                   parte.getDate() != null &&
                   parte.getDate().isBefore(LocalDate.now().plusDays(7)))
            .toList();
        
        if (!unassignedPartes.isEmpty()) {
            String message = String.format("Hay %d partes sin asignar que requieren atención", unassignedPartes.size());
            createAlert(message);
            log.info("Alerta creada: {}", message);
        }
    }
    
    /**
     * Notifica a usuarios sobre partes asignados
     */
    @Transactional
    public void notifyUsersAboutPartes(Long userId, List<Parte> partes) {
        User user = userRepository.findById(userId)
            .orElse(null);
        
        if (user != null && !partes.isEmpty()) {
            String message = String.format("Tienes %d partes asignados pendientes", partes.size());
            createAlert(message);
            log.info("Notificación enviada al usuario {}: {}", user.getEmail(), message);
        }
    }
}

