package com.utn.foodstore.dto;

import com.utn.foodstore.model.Estado;
import com.utn.foodstore.model.FormaPago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoDto(
        Long id,
        LocalDate fecha,
        Estado estado,
        BigDecimal total,
        FormaPago formaPago,
        Long idUsuario,
        List<DetallePedidoDto> detalles
) {}