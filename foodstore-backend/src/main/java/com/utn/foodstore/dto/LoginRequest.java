package com.utn.foodstore.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "El email es obligatorio")
        String mail,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}