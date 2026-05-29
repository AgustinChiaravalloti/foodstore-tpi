package com.utn.foodstore.controller;

import com.utn.foodstore.dto.UsuarioCreate;
import com.utn.foodstore.dto.UsuarioDto;
import com.utn.foodstore.dto.UsuarioEdit;
import com.utn.foodstore.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // HU-006: POST /usuario -> 201 Created
    @PostMapping
    public ResponseEntity<UsuarioDto> registrar(@Valid @RequestBody UsuarioCreate dto) {
        UsuarioDto creado = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // HU-007: GET /usuario -> 200 OK
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    // HU-008: GET /usuario/{id} -> 200 OK (o 404)
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // HU-009: PUT /usuario/{id} -> 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioEdit dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // HU-010: DELETE /usuario/{id} -> 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}