package com.utn.foodstore.dto;

import com.utn.foodstore.model.Estado;
import com.utn.foodstore.model.FormaPago;

public record PedidoEdit(
        Estado estado,
        FormaPago formaPago
) {}