package com.utn.foodstore.dto;

import com.utn.foodstore.model.Rol;

public record UsuarioDto(
        Long id,
        String nombre,
        String apellido,
        String mail,
        String celular,
        Rol rol
) {}