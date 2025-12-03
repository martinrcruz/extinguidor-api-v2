package com.extinguidor.controller;

import com.extinguidor.dto.CustomerRequest;
import com.extinguidor.dto.CustomerResponse;
import com.extinguidor.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Endpoints para gestión de clientes")
@SecurityRequirement(name = "bearer-jwt")
public class CustomerController {
    
    private final CustomerService customerService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos los clientes", description = "Obtiene una lista de todos los clientes registrados. Requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente"),
        @ApiResponse(responseCode = "403", description = "No autorizado - Se requiere rol ADMIN")
    })
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.findAllDTOs();
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("customers", customers);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener cliente por ID", description = "Obtiene los detalles de un cliente específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> getCustomerById(
            @Parameter(description = "ID del cliente", required = true, example = "1") @PathVariable Long id) {
        CustomerResponse customer = customerService.findByIdDTO(id);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("customer", customer);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear cliente")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> createCustomer(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse created = customerService.createDTO(customerRequest);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("customer", created);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(com.extinguidor.dto.StandardApiResponse.success(data, "Cliente creado exitosamente"));
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar cliente")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerRequest customerRequest) {
        CustomerResponse updated = customerService.updateDTO(id, customerRequest);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("customer", updated);
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data, "Cliente actualizado exitosamente"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar cliente")
    public ResponseEntity<com.extinguidor.dto.StandardApiResponse<java.util.Map<String, Object>>> deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("message", "Cliente eliminado exitosamente");
        return ResponseEntity.ok(com.extinguidor.dto.StandardApiResponse.success(data));
    }
}

