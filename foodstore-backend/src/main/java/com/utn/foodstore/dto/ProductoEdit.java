package com.utn.foodstore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoEdit(

        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        BigDecimal precio,

        @Size(max = 500, message = "La descripcion no puede exceder 500 caracteres")
        String descripcion,

        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,

        String imagen,

        Boolean disponible,

        Long idCategoria
) {}