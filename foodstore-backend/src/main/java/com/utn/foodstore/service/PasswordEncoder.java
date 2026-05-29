package com.utn.foodstore.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Convierte una contraseña en texto plano a un hash BCrypt
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    // Compara una contraseña en texto plano contra un hash guardado
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}