package com.utn.foodstore.repository;

import com.utn.foodstore.model.Producto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends BaseRepository<Producto, Long> {

    // Productos de una categoria (no eliminados) - HU-014
    List<Producto> findAllByCategoriaIdAndEliminadoFalse(Long categoriaId);
}