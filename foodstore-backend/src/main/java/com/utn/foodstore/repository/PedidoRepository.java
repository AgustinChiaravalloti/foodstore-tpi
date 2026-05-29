package com.utn.foodstore.repository;

import com.utn.foodstore.model.Pedido;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends BaseRepository<Pedido, Long> {

    // Pedidos de un usuario (no eliminados) - HU-020
    List<Pedido> findAllByUsuarioIdAndEliminadoFalse(Long usuarioId);
}