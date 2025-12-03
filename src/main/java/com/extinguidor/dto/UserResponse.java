package com.extinguidor.dto;

import com.extinguidor.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String code;
    private String email;
    private String phone;
    private Role role;
    private String photo;
    private Boolean activo;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}

