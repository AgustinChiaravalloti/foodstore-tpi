package com.utn.foodstore.repository;

import com.utn.foodstore.model.Categoria;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends BaseRepository<Categoria, Long> {
}