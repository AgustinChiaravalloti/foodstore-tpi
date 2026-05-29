package com.utn.foodstore.controller;

import com.utn.foodstore.dto.ProductoCreate;
import com.utn.foodstore.dto.ProductoDto;
import com.utn.foodstore.dto.ProductoEdit;
import com.utn.foodstore.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producto")
@CrossOrigin("*")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // HU-011: POST /producto -> 201 Created
    @PostMapping
    public ResponseEntity<ProductoDto> crear(@Valid @RequestBody ProductoCreate dto) {
        ProductoDto creado = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // HU-012: GET /producto -> 200 OK
    @GetMapping
    public ResponseEntity<List<ProductoDto>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    // HU-013: GET /producto/{id} -> 200 OK (o 404)
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // HU-014: GET /producto/categoria/{idCategoria} -> 200 OK (o 404)
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<ProductoDto>> listarPorCategoria(@PathVariable Long idCategoria) {
        return ResponseEntity.ok(service.findByCategoria(idCategoria));
    }

    // HU-015: PUT /producto/{id} -> 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoEdit dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // HU-016: DELETE /producto/{id} -> 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
