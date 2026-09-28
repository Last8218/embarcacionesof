package com.last.embarcaciones.exceptions;

public class EntityNotFoundException extends RuntimeException{

    private final String entityName;
    private final String details;

    public EntityNotFoundException(String entityName, String details) {
        super(String.format("%s no encontrado: %s", entityName, details));
        this.entityName = entityName;
        this.details = details;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getDetails() {
        return details;
    }
}
