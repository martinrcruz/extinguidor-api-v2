package com.extinguidor.service;

import com.extinguidor.dto.CustomerRequest;
import com.extinguidor.dto.CustomerResponse;
import com.extinguidor.exception.BusinessException;
import com.extinguidor.exception.ResourceNotFoundException;
import com.extinguidor.mapper.CustomerMapper;
import com.extinguidor.model.entity.Customer;
import com.extinguidor.model.entity.CustomerContractSystem;
import com.extinguidor.model.entity.CustomerDocument;
import com.extinguidor.repository.CustomerRepository;
import com.extinguidor.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private final ZoneRepository zoneRepository;
    private final CustomerMapper customerMapper;
    
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
    }
    
    @Transactional(readOnly = true)
    public Customer findByCode(String code) {
        return customerRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente con código: " + code));
    }
    
    @Transactional
    public Customer create(Customer customer) {
        if (customerRepository.existsByCode(customer.getCode())) {
            throw new BusinessException("El código de cliente ya está en uso");
        }
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new BusinessException("El email ya está en uso");
        }
        if (customer.getActive() == null) {
            customer.setActive(true);
        }
        return customerRepository.save(customer);
    }
    
    @Transactional
    public Customer update(Long id, Customer customerDetails) {
        Customer customer = findById(id);
        
        if (!customer.getCode().equals(customerDetails.getCode()) && 
            customerRepository.existsByCode(customerDetails.getCode())) {
            throw new BusinessException("El código ya está en uso");
        }
        
        customer.setName(customerDetails.getName());
        customer.setEmail(customerDetails.getEmail());
        customer.setNifCif(customerDetails.getNifCif());
        customer.setAddress(customerDetails.getAddress());
        customer.setPhone(customerDetails.getPhone());
        customer.setContactName(customerDetails.getContactName());
        customer.setActive(customerDetails.getActive());
        customer.setZone(customerDetails.getZone());
        customer.setDescription(customerDetails.getDescription());
        customer.setGestiona(customerDetails.getGestiona());
        customer.setPhoto(customerDetails.getPhoto());
        customer.setStartDate(customerDetails.getStartDate());
        customer.setEndDate(customerDetails.getEndDate());
        customer.setType(customerDetails.getType());
        customer.setContractSystems(customerDetails.getContractSystems());
        customer.setAverageTime(customerDetails.getAverageTime());
        customer.setDelegation(customerDetails.getDelegation());
        customer.setRevisionFrequency(customerDetails.getRevisionFrequency());
        customer.setRate(customerDetails.getRate());
        customer.setMi(customerDetails.getMi());
        customer.setTipo(customerDetails.getTipo());
        customer.setTotal(customerDetails.getTotal());
        customer.setDocuments(customerDetails.getDocuments());
        
        return customerRepository.save(customer);
    }
    
    @Transactional
    public void delete(Long id) {
        Customer customer = findById(id);
        customerRepository.delete(customer);
    }
    
    @Transactional(readOnly = true)
    public boolean isCustomerActive(Long customerId) {
        return customerRepository.findById(customerId)
            .map(Customer::getActive)
            .orElse(false);
    }
    
    // Métodos que devuelven DTOs
    
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAllDTOs() {
        return customerRepository.findAll().stream()
            .map(customerMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CustomerResponse findByIdDTO(Long id) {
        Customer customer = findById(id);
        return customerMapper.toResponse(customer);
    }
    
    @Transactional
    public CustomerResponse createDTO(CustomerRequest request) {
        if (customerRepository.existsByCode(request.getCode())) {
            throw new BusinessException("El código de cliente ya está en uso");
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("El email ya está en uso");
        }
        
        Customer customer = customerMapper.toEntity(request);
        
        // Mapear zone si existe (soporta tanto zoneId como zone)
        Long zoneIdToUse = request.getZoneId();
        if (zoneIdToUse == null && request.getZone() != null) {
            // Convertir zone (puede ser String o Long) a Long
            if (request.getZone() instanceof String) {
                try {
                    zoneIdToUse = Long.parseLong((String) request.getZone());
                } catch (NumberFormatException e) {
                    throw new BusinessException("El ID de zona debe ser un número válido");
                }
            } else if (request.getZone() instanceof Number) {
                zoneIdToUse = ((Number) request.getZone()).longValue();
            }
        }
        
        if (zoneIdToUse != null) {
            Long finalZoneIdToUse = zoneIdToUse;
            customer.setZone(zoneRepository.findById(zoneIdToUse)
                .orElseThrow(() -> new ResourceNotFoundException("Zona", finalZoneIdToUse)));
        }
        
        // Asegurar que mi se asigne correctamente (ya mapeado por Jackson con @JsonAlias)
        if (request.getMi() != null) {
            customer.setMi(request.getMi());
        }
        
        // Mapear contract systems
        if (request.getContractSystems() != null && !request.getContractSystems().isEmpty()) {
            List<CustomerContractSystem> contractSystems = request.getContractSystems().stream()
                .map(system -> CustomerContractSystem.builder()
                    .customer(customer)
                    .contractSystem(system)
                    .build())
                .collect(Collectors.toList());
            customer.setContractSystems(contractSystems);
        }
        
        // Mapear documents
        if (request.getDocuments() != null && !request.getDocuments().isEmpty()) {
            List<CustomerDocument> documents = request.getDocuments().stream()
                .map(doc -> CustomerDocument.builder()
                    .customer(customer)
                    .name(doc.getName())
                    .url(doc.getUrl())
                    .build())
                .collect(Collectors.toList());
            customer.setDocuments(documents);
        }
        
        if (customer.getActive() == null) {
            customer.setActive(true);
        }
        
        Customer saved = customerRepository.save(customer);
        return customerMapper.toResponse(saved);
    }
    
    @Transactional
    public CustomerResponse updateDTO(Long id, CustomerRequest request) {
        Customer customer = findById(id);
        
        if (!customer.getCode().equals(request.getCode()) && 
            customerRepository.existsByCode(request.getCode())) {
            throw new BusinessException("El código ya está en uso");
        }
        
        customerMapper.updateEntity(customer, request);
        
        // Mapear zone si existe (soporta tanto zoneId como zone)
        Long zoneIdToUse = request.getZoneId();
        if (zoneIdToUse == null && request.getZone() != null) {
            // Convertir zone (puede ser String o Long) a Long
            if (request.getZone() instanceof String) {
                try {
                    zoneIdToUse = Long.parseLong((String) request.getZone());
                } catch (NumberFormatException e) {
                    throw new BusinessException("El ID de zona debe ser un número válido");
                }
            } else if (request.getZone() instanceof Number) {
                zoneIdToUse = ((Number) request.getZone()).longValue();
            }
        }
        
        if (zoneIdToUse != null) {
            Long finalZoneIdToUse = zoneIdToUse;
            customer.setZone(zoneRepository.findById(zoneIdToUse)
                .orElseThrow(() -> new ResourceNotFoundException("Zona", finalZoneIdToUse)));
        } else {
            customer.setZone(null);
        }
        
        // Asegurar que mi se asigne correctamente (ya mapeado por Jackson con @JsonAlias)
        if (request.getMi() != null) {
            customer.setMi(request.getMi());
        }
        
        // Actualizar contract systems
        if (request.getContractSystems() != null) {
            customer.getContractSystems().clear();
            if (!request.getContractSystems().isEmpty()) {
                List<CustomerContractSystem> contractSystems = request.getContractSystems().stream()
                    .map(system -> CustomerContractSystem.builder()
                        .customer(customer)
                        .contractSystem(system)
                        .build())
                    .collect(Collectors.toList());
                customer.getContractSystems().addAll(contractSystems);
            }
        }
        
        // Actualizar documents
        if (request.getDocuments() != null) {
            customer.getDocuments().clear();
            if (!request.getDocuments().isEmpty()) {
                List<CustomerDocument> documents = request.getDocuments().stream()
                    .map(doc -> CustomerDocument.builder()
                        .customer(customer)
                        .name(doc.getName())
                        .url(doc.getUrl())
                        .build())
                    .collect(Collectors.toList());
                customer.getDocuments().addAll(documents);
            }
        }
        
        if (customer.getActive() == null) {
            customer.setActive(true);
        }
        
        Customer saved = customerRepository.save(customer);
        return customerMapper.toResponse(saved);
    }
}

