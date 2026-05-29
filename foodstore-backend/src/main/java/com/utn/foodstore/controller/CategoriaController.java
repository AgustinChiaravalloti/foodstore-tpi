package com.utn.foodstore.controller;

import com.utn.foodstore.dto.CategoriaCreate;
import com.utn.foodstore.dto.CategoriaDto;
import com.utn.foodstore.dto.CategoriaEdit;
import com.utn.foodstore.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
@CrossOrigin("*")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    // HU-001: POST /categoria -> 201 Created
    @PostMapping
    public ResponseEntity<CategoriaDto> crear(@Valid @RequestBody CategoriaCreate dto) {
        CategoriaDto creada = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // HU-002: GET /categoria -> 200 OK
    @GetMapping
    public ResponseEntity<List<CategoriaDto>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    // HU-003: GET /categoria/{id} -> 200 OK (o 404)
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // HU-004: PUT /categoria/{id} -> 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaEdit dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // HU-005: DELETE /categoria/{id} -> 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}