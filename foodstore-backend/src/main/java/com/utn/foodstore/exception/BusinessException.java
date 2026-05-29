package com.utn.foodstore.exception;

// Se lanza cuando se viola una regla de negocio -> 400
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}