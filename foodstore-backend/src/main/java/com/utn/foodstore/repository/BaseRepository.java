package com.utn.foodstore.repository;

import com.utn.foodstore.model.Base;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import com.utn.foodstore.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<E extends Base, ID> extends JpaRepository<E, ID> {

    // Trae solo las entidades NO eliminadas
    List<E> findAllByEliminadoFalse();

    // Busca por id pero solo si no esta eliminada
    Optional<E> findByIdAndEliminadoFalse(ID id);

    // Reemplaza el findAll() estandar: filtra los eliminados
    default List<E> findAllActive() {
        return findAllByEliminadoFalse();
    }

    // Busca por id y si no existe lanza excepcion (sera el 404)
    default E findByIdOrThrow(ID id) {
        return findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Entidad con id " + id + " no encontrado"));
    }

    // Soft delete: marca como eliminado en vez de borrar
    @Transactional
    @Modifying
    default void softDeleteById(ID id) {
        E entity = findByIdOrThrow(id);
        entity.setEliminado(true);
        save(entity);
    }
}