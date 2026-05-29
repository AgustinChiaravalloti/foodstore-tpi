package com.utn.foodstore.dto;

import java.math.BigDecimal;

public record ProductoDto(
        Long id,
        String nombre,
        BigDecimal precio,
        String descripcion,
        Integer stock,
        String imagen,
        boolean disponible,
        CategoriaDto categoria
) {}