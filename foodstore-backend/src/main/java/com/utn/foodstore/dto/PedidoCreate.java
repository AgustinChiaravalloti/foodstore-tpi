package com.utn.foodstore.dto;

import com.utn.foodstore.model.Estado;
import com.utn.foodstore.model.FormaPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoCreate(

        @NotNull(message = "El estado es obligatorio")
        Estado estado,

        @NotNull(message = "La forma de pago es obligatoria")
        FormaPago formaPago,

        @NotNull(message = "El idUsuario es obligatorio")
        Long idUsuario,

        @NotEmpty(message = "Debe haber al menos un detalle de pedido")
        @Valid
        List<DetallePedidoCreate> detallePedido
) {}