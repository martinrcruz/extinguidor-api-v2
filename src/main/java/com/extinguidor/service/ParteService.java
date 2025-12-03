package com.extinguidor.service;

import com.extinguidor.dto.ParteRequest;
import com.extinguidor.dto.ParteResponse;
import com.extinguidor.exception.BusinessException;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.exception.ValidationException;
import com.extinguidor.mapper.ParteMapper;
import com.extinguidor.model.entity.Article;
import com.extinguidor.model.entity.Customer;
import com.extinguidor.model.entity.Parte;
import com.extinguidor.model.entity.ParteArticulo;
import com.extinguidor.model.entity.ParteComentario;
import com.extinguidor.model.entity.ParteDocumento;
import com.extinguidor.model.entity.Route;
import com.extinguidor.model.entity.User;
import com.extinguidor.model.enums.Frequency;
import com.extinguidor.model.enums.ParteState;
import com.extinguidor.repository.ArticleRepository;
import com.extinguidor.repository.CustomerRepository;
import com.extinguidor.repository.ParteRepository;
import com.extinguidor.repository.RouteRepository;
import com.extinguidor.repository.UserRepository;
import com.extinguidor.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ParteService {
    
    private final ParteRepository parteRepository;
    private final CustomerService customerService;
    private final RouteRepository routeRepository;
    private final ParteMapper parteMapper;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final EntityManager entityManager;
    
    @Transactional(readOnly = true)
    public Page<Parte> findAll(Pageable pageable) {
        return parteRepository.findByEliminadoFalseOrderByCreatedDateDesc(pageable);
    }
    
    @Transactional(readOnly = true)
    public Parte findById(Long id) {
        return parteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Parte", id));
    }
    
    @Transactional(readOnly = true)
    public List<Parte> findByCustomerId(Long customerId) {
        return parteRepository.findByCustomerIdAndEliminadoFalseOrderByCreatedDateDesc(customerId);
    }
    
    @Transactional(readOnly = true)
    public List<Parte> findByRouteId(Long routeId) {
        Route route = routeRepository.findById(routeId)
            .orElseThrow(() -> new ResourceNotFoundException("Ruta", routeId));
        return parteRepository.findByRutaAndEliminadoFalse(route);
    }
    
    @Transactional(readOnly = true)
    public List<Parte> findByWorkerId(Long workerId, LocalDate date) {
        if (date != null) {
            return parteRepository.findByWorkerIdAndMonth(workerId, date.getYear(), date.getMonthValue());
        }
        return parteRepository.findByWorkerId(workerId);
    }
    
    @Transactional(readOnly = true)
    public List<Parte> findUnassignedPartes(LocalDate date) {
        LocalDate startDate = date.minusDays(360);
        LocalDate endDate = date.plusDays(30);
        return parteRepository.findUnassignedPartes(startDate, endDate);
    }
    
    @Transactional(readOnly = true)
    public List<Parte> findFinalizedInMonth(LocalDate date) {
        return parteRepository.findFinalizedInMonth(ParteState.Finalizado, date.getYear(), date.getMonthValue());
    }
    
    @Transactional(readOnly = true)
    public List<Parte> findUnassignedUntilEndOfMonth(LocalDate date) {
        LocalDate endDate = DateUtil.getLastDayOfMonth(date);
        return parteRepository.findUnassignedUntilEndOfMonth(endDate);
    }
    
    @Transactional
    public Parte create(Parte parte) {
        // Validar cliente activo
        if (!customerService.isCustomerActive(parte.getCustomer().getId())) {
            throw new BusinessException("No se pueden crear partes para clientes inactivos");
        }
        
        // Validar fecha no anterior al mes actual
        LocalDate normalizedDate = DateUtil.normalizeToFirstOfMonth(parte.getDate());
//        if (DateUtil.isBeforeCurrentMonth(normalizedDate)) {
//            throw new ValidationException("La fecha del parte no puede ser anterior al mes actual");
//        }
        
        parte.setDate(normalizedDate);
        
        // Si es periódico, validar y generar partes
        if (Boolean.TRUE.equals(parte.getPeriodico())) {
            validatePeriodicParte(parte);
            return createPeriodicPartes(parte);
        }
        
        return parteRepository.save(parte);
    }
    
    @Transactional
    public Parte update(Long id, Parte parteDetails) {
        Parte parte = findById(id);
        
        // Validar cliente activo si cambió
        if (!parte.getCustomer().getId().equals(parteDetails.getCustomer().getId()) &&
            !customerService.isCustomerActive(parteDetails.getCustomer().getId())) {
            throw new BusinessException("No se pueden asignar partes a clientes inactivos");
        }
        
        // Actualizar campos
        parte.setTitle(parteDetails.getTitle());
        parte.setDescription(parteDetails.getDescription());
        parte.setDate(DateUtil.normalizeToFirstOfMonth(parteDetails.getDate()));
        parte.setCustomer(parteDetails.getCustomer());
        parte.setAddress(parteDetails.getAddress());
        parte.setState(parteDetails.getState());
        parte.setType(parteDetails.getType());
        parte.setCategoria(parteDetails.getCategoria());
        parte.setCoordinationMethod(parteDetails.getCoordinationMethod());
        parte.setGestiona(parteDetails.getGestiona());
        parte.setFacturacion(parteDetails.getFacturacion());
        parte.setArticulos(parteDetails.getArticulos());
        parte.setComentarios(parteDetails.getComentarios());
        parte.setDocumentos(parteDetails.getDocumentos());
        parte.setEliminado(false);


        // Si cambió a Finalizado, guardar fecha
        if (parteDetails.getState() == ParteState.Finalizado && parte.getFinalizadoTime() == null) {
            parte.setFinalizadoTime(LocalDateTime.now());
        }
        
        return parteRepository.save(parte);
    }
    
    @Transactional
    public Parte updateStatus(Long id, ParteState newState) {
        Parte parte = findById(id);
        
        if (parte.getState() == newState) {
            return parte; // Debouncing
        }
        
        parte.setState(newState);
        
        if (newState == ParteState.Finalizado && parte.getFinalizadoTime() == null) {
            parte.setFinalizadoTime(LocalDateTime.now());
        }
        
        return parteRepository.save(parte);
    }
    
    @Transactional
    public void assignToRoute(Long parteId, Long routeId) {
        Parte parte = findById(parteId);
        Route route = routeRepository.findById(routeId)
            .orElseThrow(() -> new ResourceNotFoundException("Ruta", routeId));
        
        parte.setRuta(route);
        parte.setAsignado(true);
        parteRepository.save(parte);
    }
    
    @Transactional
    public void assignMultipleToRoute(List<Long> parteIds, Long routeId) {
        Route route = routeRepository.findById(routeId)
            .orElseThrow(() -> new ResourceNotFoundException("Ruta", routeId));
        
        for (Long parteId : parteIds) {
            Parte parte = findById(parteId);
            parte.setRuta(route);
            parte.setAsignado(true);
            parteRepository.save(parte);
        }
    }
    
    @Transactional
    public void delete(Long id) {
        Parte parte = findById(id);
        parteRepository.delete(parte);
    }
    
    private void validatePeriodicParte(Parte parte) {
        if (parte.getEndDate() == null) {
            throw new ValidationException("El campo endDate es obligatorio para partes periódicos");
        }
        
        if (parte.getFrequency() == null) {
            throw new ValidationException("El campo frequency es obligatorio para partes periódicos");
        }
        
        // Validar solapamiento
        List<Parte> overlapping = parteRepository.findOverlappingPartes(
            parte.getCustomer().getId(),
            parte.getDate(),
            parte.getEndDate()
        );
        
        if (!overlapping.isEmpty()) {
            throw new BusinessException("No se pueden crear partes periódicos que se solapen con partes existentes");
        }
    }
    
    private Parte createPeriodicPartes(Parte baseParte) {
        List<Parte> createdPartes = new ArrayList<>();
        LocalDate currentDate = baseParte.getDate();
        LocalDate endDate = baseParte.getEndDate();
        Frequency frequency = baseParte.getFrequency();
        
        int monthsToAdd = getMonthsToAdd(frequency);
        
        while (!currentDate.isAfter(endDate)) {
            Parte newParte = createParteCopy(baseParte);
            newParte.setDate(currentDate);
            newParte.setPeriodico(false); // Los generados no son periódicos
            newParte.setEndDate(null);
            newParte.setFrequency(null);
            
            createdPartes.add(parteRepository.save(newParte));
            
            currentDate = currentDate.plusMonths(monthsToAdd);
        }
        
        return createdPartes.get(0); // Retornar el primero
    }
    
    private Parte createParteCopy(Parte original) {
        Parte copy = new Parte();
        copy.setTitle(original.getTitle());
        copy.setDescription(original.getDescription());
        copy.setCustomer(original.getCustomer());
        copy.setAddress(original.getAddress());
        copy.setState(original.getState());
        copy.setType(original.getType());
        copy.setCategoria(original.getCategoria());
        copy.setCoordinationMethod(original.getCoordinationMethod());
        copy.setGestiona(original.getGestiona());
        copy.setFacturacion(original.getFacturacion());
        copy.setArticulos(new ArrayList<>(original.getArticulos()));
        return copy;
    }
    
    private int getMonthsToAdd(Frequency frequency) {
        return switch (frequency) {
            case Mensual -> 1;
            case Trimestral -> 3;
            case Semestral -> 6;
            case Anual -> 12;
        };
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public Page<ParteResponse> findAllDTOs(Pageable pageable) {
        Page<Parte> partesPage = parteRepository.findByEliminadoFalseOrderByCreatedDateDesc(pageable);
        List<ParteResponse> content = partesPage.getContent().stream()
            .map(parteMapper::toResponse)
            .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, partesPage.getTotalElements());
    }
    
    @Transactional(readOnly = true)
    public ParteResponse findByIdDTO(Long id) {
        Parte parte = findById(id);
        return parteMapper.toResponse(parte);
    }
    
    @Transactional(readOnly = true)
    public List<ParteResponse> findByCustomerIdDTOs(Long customerId) {
        return parteRepository.findByCustomerIdAndEliminadoFalseOrderByCreatedDateDesc(customerId).stream()
            .map(parteMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ParteResponse> findByRouteIdDTOs(Long routeId) {
        Route route = routeRepository.findById(routeId)
            .orElseThrow(() -> new ResourceNotFoundException("Ruta", routeId));
        return parteRepository.findByRutaAndEliminadoFalse(route).stream()
            .map(parteMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ParteResponse> findByWorkerIdDTOs(Long workerId, LocalDate date) {
        List<Parte> partes;
        if (date != null) {
            partes = parteRepository.findByWorkerIdAndMonth(workerId, date.getYear(), date.getMonthValue());
        } else {
            partes = parteRepository.findByWorkerId(workerId);
        }
        return partes.stream()
            .map(parteMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ParteResponse> findUnassignedPartesDTOs(LocalDate date) {
        LocalDate startDate = date.minusDays(360);
        LocalDate endDate = date.plusDays(30);
        return parteRepository.findUnassignedPartes(startDate, endDate).stream()
            .map(parteMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ParteResponse> findFinalizedInMonthDTOs(LocalDate date) {
        return parteRepository.findFinalizedInMonth(ParteState.Finalizado, date.getYear(), date.getMonthValue()).stream()
            .map(parteMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ParteResponse> findUnassignedUntilEndOfMonthDTOs(LocalDate date) {
        LocalDate endDate = DateUtil.getLastDayOfMonth(date);
        return parteRepository.findUnassignedUntilEndOfMonth(endDate).stream()
            .map(parteMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public ParteResponse createDTO(ParteRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente", request.getCustomerId()));
        
        // Validar cliente activo
        if (!customer.getActive()) {
            throw new BusinessException("No se pueden crear partes para clientes inactivos");
        }
        
        Parte parte = parteMapper.toEntity(request);
        parte.setCustomer(customer);
        
        // Mapear ruta si existe
        if (request.getRutaId() != null) {
            Route route = routeRepository.findById(request.getRutaId())
                .orElseThrow(() -> new ResourceNotFoundException("Ruta", request.getRutaId()));
            parte.setRuta(route);
            parte.setAsignado(true);
        }
        
        // Mapear worker si existe
        if (request.getWorkerId() != null) {
            User worker = userRepository.findById(request.getWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", request.getWorkerId()));
            parte.setWorker(worker);
        }
        
        // Mapear comentarios, documentos y artículos
        if (request.getComentarios() != null && !request.getComentarios().isEmpty()) {
            List<ParteComentario> comentarios = request.getComentarios().stream()
                .map(com -> ParteComentario.builder()
                    .parte(parte)
                    .texto(com.getTexto())
                    .fecha(com.getFecha())
                    .usuario(com.getUsuario())
                    .build())
                .collect(Collectors.toList());
            parte.setComentarios(comentarios);
        }
        
        if (request.getDocumentos() != null && !request.getDocumentos().isEmpty()) {
            List<ParteDocumento> documentos = request.getDocumentos().stream()
                .map(doc -> ParteDocumento.builder()
                    .parte(parte)
                    .nombre(doc.getNombre())
                    .url(doc.getUrl())
                    .tipo(doc.getTipo())
                    .fecha(doc.getFecha())
                    .build())
                .collect(Collectors.toList());
            parte.setDocumentos(documentos);
        }
        
        if (request.getArticulos() != null && !request.getArticulos().isEmpty()) {
            List<ParteArticulo> articulos = request.getArticulos().stream()
                .map(art -> ParteArticulo.builder()
                    .parte(parte)
                    .cantidad(art.getCantidad())
                    .codigo(art.getCodigo())
                    .grupo(art.getGrupo())
                    .familia(art.getFamilia())
                    .descripcionArticulo(art.getDescripcionArticulo())
                    .precioVenta(art.getPrecioVenta())
                    .build())
                .collect(Collectors.toList());
            parte.setArticulos(articulos);
        }
        
        // Validar fecha no anterior al mes actual
        LocalDate normalizedDate = DateUtil.normalizeToFirstOfMonth(parte.getDate());
//        if (DateUtil.isBeforeCurrentMonth(normalizedDate)) {
//            throw new ValidationException("La fecha del parte no puede ser anterior al mes actual");
//        }

        parte.setEliminado(false);
        parte.setDate(normalizedDate);
        
        // Si es periódico, validar y generar partes
        if (Boolean.TRUE.equals(parte.getPeriodico())) {
            validatePeriodicParte(parte);
            Parte created = createPeriodicPartes(parte);
            return parteMapper.toResponse(created);
        }
        
        Parte saved = parteRepository.save(parte);
        return parteMapper.toResponse(saved);
    }
    
    @Transactional
    public ParteResponse updateDTO(Long id, ParteRequest request) {
        Parte parte = findById(id);
        
        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente", request.getCustomerId()));
        
        // Validar cliente activo si cambió
        if (!parte.getCustomer().getId().equals(customer.getId()) && !customer.getActive()) {
            throw new BusinessException("No se pueden asignar partes a clientes inactivos");
        }
        
        parteMapper.updateEntity(parte, request);
        parte.setCustomer(customer);
        
        // Mapear ruta si existe
        if (request.getRutaId() != null) {
            Route route = routeRepository.findById(request.getRutaId())
                .orElseThrow(() -> new ResourceNotFoundException("Ruta", request.getRutaId()));
            parte.setRuta(route);
            parte.setAsignado(true);
        } else {
            parte.setRuta(null);
            parte.setAsignado(false);
        }
        
        // Mapear worker si existe
        if (request.getWorkerId() != null) {
            User worker = userRepository.findById(request.getWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", request.getWorkerId()));
            parte.setWorker(worker);
        } else {
            parte.setWorker(null);
        }
        
        // Actualizar comentarios, documentos y artículos
        if (request.getComentarios() != null) {
            parte.getComentarios().clear();
            if (!request.getComentarios().isEmpty()) {
                Parte finalParte = parte;
                List<ParteComentario> comentarios = request.getComentarios().stream()
                    .map(com -> ParteComentario.builder()
                        .parte(finalParte)
                        .texto(com.getTexto())
                        .fecha(com.getFecha())
                        .usuario(com.getUsuario())
                        .build())
                    .collect(Collectors.toList());
                parte.getComentarios().addAll(comentarios);
            }
        }
        
        if (request.getDocumentos() != null) {
            parte.getDocumentos().clear();
            if (!request.getDocumentos().isEmpty()) {
                Parte finalParte2 = parte;
                List<ParteDocumento> documentos = request.getDocumentos().stream()
                    .map(doc -> ParteDocumento.builder()
                        .parte(finalParte2)
                        .nombre(doc.getNombre())
                        .url(doc.getUrl())
                        .tipo(doc.getTipo())
                        .fecha(doc.getFecha())
                        .build())
                    .collect(Collectors.toList());
                parte.getDocumentos().addAll(documentos);
            }
        }
        
        if (request.getArticulos() != null) {
            // Guardar el parte primero para asegurar que todos los cambios estén persistidos
            parte = parteRepository.save(parte);
            entityManager.flush(); // Asegurar que el parte esté guardado
            
            // Obtener una copia de los artículos existentes antes de limpiar
            List<ParteArticulo> articulosExistentes = new ArrayList<>(parte.getArticulos());
            
            // Limpiar la colección primero
            parte.getArticulos().clear();
            
            // Eliminar cada artículo individualmente usando entityManager
            for (ParteArticulo articulo : articulosExistentes) {
                // Asegurarse de que el artículo esté gestionado
                if (!entityManager.contains(articulo)) {
                    articulo = entityManager.merge(articulo);
                }
                entityManager.remove(articulo);
            }
            
            // Forzar flush para procesar todas las eliminaciones
            // Esto es crítico: debe ejecutarse antes de crear nuevos artículos
            entityManager.flush();
            
            // Ahora crear los nuevos artículos
            // Usar el constructor directamente para evitar problemas con el builder y los IDs
            if (!request.getArticulos().isEmpty()) {
                Parte finalParte1 = parte;
                for (var art : request.getArticulos()) {
                    ParteArticulo nuevoArticulo = new ParteArticulo();
                    nuevoArticulo.setId(null); // Asegurar que el ID sea null para generar uno nuevo
                    nuevoArticulo.setParte(finalParte1);
                    nuevoArticulo.setCantidad(art.getCantidad());
                    nuevoArticulo.setCodigo(art.getCodigo());
                    nuevoArticulo.setGrupo(art.getGrupo());
                    nuevoArticulo.setFamilia(art.getFamilia());
                    nuevoArticulo.setDescripcionArticulo(art.getDescripcionArticulo());
                    nuevoArticulo.setPrecioVenta(art.getPrecioVenta());
                    parte.getArticulos().add(nuevoArticulo);
                }
            }
        }
        
        // Si cambió a Finalizado, guardar fecha
        if (request.getState() == ParteState.Finalizado && parte.getFinalizadoTime() == null) {
            parte.setFinalizadoTime(LocalDateTime.now());
        }
        
        Parte saved = parteRepository.save(parte);
        return parteMapper.toResponse(saved);
    }
    
    @Transactional
    public ParteResponse updateStatusDTO(Long id, ParteState newState) {
        Parte parte = findById(id);
        
        if (parte.getState() == newState) {
            return parteMapper.toResponse(parte); // Debouncing
        }
        
        parte.setState(newState);
        
        if (newState == ParteState.Finalizado && parte.getFinalizadoTime() == null) {
            parte.setFinalizadoTime(LocalDateTime.now());
        }
        
        Parte saved = parteRepository.save(parte);
        return parteMapper.toResponse(saved);
    }
}

