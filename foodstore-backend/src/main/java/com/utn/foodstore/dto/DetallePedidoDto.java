package com.utn.foodstore.dto;

import java.math.BigDecimal;

public record DetallePedidoDto(
        Long id,
        Integer cantidad,
        BigDecimal subtotal,
        ProductoDto producto
) {}