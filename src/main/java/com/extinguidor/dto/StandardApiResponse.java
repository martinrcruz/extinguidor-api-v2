package com.extinguidor.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardApiResponse<T> {
    private Boolean ok;
    private T data;
    private String error;
    private String message;
    
    public static <T> StandardApiResponse<T> success(T data) {
        return StandardApiResponse.<T>builder()
                .ok(true)
                .data(data)
                .build();
    }
    
    public static <T> StandardApiResponse<T> success(T data, String message) {
        return StandardApiResponse.<T>builder()
                .ok(true)
                .data(data)
                .message(message)
                .build();
    }
    
    public static <T> StandardApiResponse<T> error(String error) {
        return StandardApiResponse.<T>builder()
                .ok(false)
                .error(error)
                .build();
    }
    
    public static <T> StandardApiResponse<T> error(String error, String message) {
        return StandardApiResponse.<T>builder()
                .ok(false)
                .error(error)
                .message(message)
                .build();
    }
}

