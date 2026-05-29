package com.utn.foodstore.repository;

import com.utn.foodstore.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, Long> {

    // Busca un usuario por su email (lo usaremos para validar unicidad y para login)
    Optional<Usuario> findByMail(String mail);

    // Indica si ya existe un usuario con ese email
    boolean existsByMail(String mail);
}