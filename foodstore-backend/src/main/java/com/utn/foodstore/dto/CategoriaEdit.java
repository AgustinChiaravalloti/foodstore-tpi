package com.utn.foodstore.dto;

import jakarta.validation.constraints.Size;

public record CategoriaEdit(

        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripcion no puede exceder 500 caracteres")
        String descripcion
) {}