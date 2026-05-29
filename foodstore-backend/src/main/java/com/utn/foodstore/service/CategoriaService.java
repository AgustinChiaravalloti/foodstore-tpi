package com.utn.foodstore.service;

import com.utn.foodstore.dto.CategoriaCreate;
import com.utn.foodstore.dto.CategoriaDto;
import com.utn.foodstore.dto.CategoriaEdit;
import com.utn.foodstore.model.Categoria;
import com.utn.foodstore.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    // Spring inyecta el repository por el constructor
    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    // HU-001: Crear categoria
    public CategoriaDto save(CategoriaCreate dto) {
        Categoria categoria = Categoria.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .build();
        Categoria guardada = repository.save(categoria);
        return toDto(guardada);
    }

    // HU-002: Listar categorias (solo no eliminadas)
    public List<CategoriaDto> findAll() {
        return repository.findAllActive()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // HU-003: Obtener una categoria por id (404 si no existe)
    public CategoriaDto findById(Long id) {
        Categoria categoria = repository.findByIdOrThrow(id);
        return toDto(categoria);
    }

    // HU-004: Actualizar categoria (parcial: solo campos enviados)
    public CategoriaDto update(Long id, CategoriaEdit dto) {
        Categoria categoria = repository.findByIdOrThrow(id);

        if (dto.nombre() != null) {
            categoria.setNombre(dto.nombre());
        }
        if (dto.descripcion() != null) {
            categoria.setDescripcion(dto.descripcion());
        }

        Categoria actualizada = repository.save(categoria);
        return toDto(actualizada);
    }

    // HU-005: Eliminar categoria (soft delete)
    public void delete(Long id) {
        repository.softDeleteById(id);
    }

    // Traductor: entidad -> DTO de respuesta
    private CategoriaDto toDto(Categoria categoria) {
        return new CategoriaDto(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }
}