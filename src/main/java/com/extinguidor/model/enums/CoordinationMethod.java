package com.extinguidor.model.enums;

public enum CoordinationMethod {
    LLAMAR_ANTES("Llamar antes"),
    COORDINAR_EMAIL("Coordinar por email"),
    COORDINAR_HORARIOS("Coordinar según horarios");
    
    private final String displayName;
    
    CoordinationMethod(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

