package com.utn.foodstore.service;

import com.utn.foodstore.dto.CategoriaDto;
import com.utn.foodstore.dto.ProductoCreate;
import com.utn.foodstore.dto.ProductoDto;
import com.utn.foodstore.dto.ProductoEdit;
import com.utn.foodstore.model.Categoria;
import com.utn.foodstore.model.Producto;
import com.utn.foodstore.repository.CategoriaRepository;
import com.utn.foodstore.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository repository,
                           CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    // HU-011: Crear producto
    public ProductoDto save(ProductoCreate dto) {
        Categoria categoria = categoriaRepository.findByIdOrThrow(dto.idCategoria());

        Producto producto = Producto.builder()
                .nombre(dto.nombre())
                .precio(dto.precio())
                .descripcion(dto.descripcion())
                .stock(dto.stock())
                .imagen(dto.imagen())
                .disponible(dto.disponible() == null ? true : dto.disponible())
                .categoria(categoria)
                .build();

        return toDto(repository.save(producto));
    }

    // HU-012: Listar productos
    public List<ProductoDto> findAll() {
        return repository.findAllActive()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // HU-013: Obtener producto por id
    public ProductoDto findById(Long id) {
        return toDto(repository.findByIdOrThrow(id));
    }

    // HU-014: Listar productos por categoria
    public List<ProductoDto> findByCategoria(Long idCategoria) {
        // Valida que la categoria exista (404 si no) - RN-014-01
        categoriaRepository.findByIdOrThrow(idCategoria);

        return repository.findAllByCategoriaIdAndEliminadoFalse(idCategoria)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // HU-015: Actualizar producto (parcial)
    public ProductoDto update(Long id, ProductoEdit dto) {
        Producto producto = repository.findByIdOrThrow(id);

        if (dto.nombre() != null)       producto.setNombre(dto.nombre());
        if (dto.precio() != null)       producto.setPrecio(dto.precio());
        if (dto.descripcion() != null)  producto.setDescripcion(dto.descripcion());
        if (dto.stock() != null)        producto.setStock(dto.stock());
        if (dto.imagen() != null)       producto.setImagen(dto.imagen());
        if (dto.disponible() != null)   producto.setDisponible(dto.disponible());
        if (dto.idCategoria() != null) {
            Categoria categoria = categoriaRepository.findByIdOrThrow(dto.idCategoria());
            producto.setCategoria(categoria);
        }

        return toDto(repository.save(producto));
    }

    // HU-016: Eliminar producto (soft delete)
    public void delete(Long id) {
        repository.softDeleteById(id);
    }

    // Traductor: entidad -> DTO (con categoria anidada)
    private ProductoDto toDto(Producto p) {
        Categoria c = p.getCategoria();
        CategoriaDto categoriaDto = new CategoriaDto(c.getId(), c.getNombre(), c.getDescripcion());

        return new ProductoDto(
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getDescripcion(),
                p.getStock(),
                p.getImagen(),
                p.isDisponible(),
                categoriaDto
        );
    }
}