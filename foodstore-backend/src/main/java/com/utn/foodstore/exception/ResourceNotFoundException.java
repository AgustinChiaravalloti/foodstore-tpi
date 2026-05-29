package com.utn.foodstore.exception;

// Se lanza cuando se busca una entidad que no existe -> 404
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}