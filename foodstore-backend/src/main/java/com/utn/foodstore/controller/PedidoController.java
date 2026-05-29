package com.utn.foodstore.controller;

import com.utn.foodstore.dto.PedidoCreate;
import com.utn.foodstore.dto.PedidoDto;
import com.utn.foodstore.dto.PedidoEdit;
import com.utn.foodstore.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
@CrossOrigin("*")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    // HU-017: POST /pedido -> 201 Created
    @PostMapping
    public ResponseEntity<PedidoDto> crear(@Valid @RequestBody PedidoCreate dto) {
        PedidoDto creado = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // HU-018: GET /pedido -> 200 OK
    @GetMapping
    public ResponseEntity<List<PedidoDto>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    // HU-019: GET /pedido/{id} -> 200 OK (o 404)
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // HU-020: GET /pedido/usuario/{idUsuario} -> 200 OK (o 404)
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PedidoDto>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.findByUsuario(idUsuario));
    }

    // HU-021: PUT /pedido/{id} -> 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<PedidoDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoEdit dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // HU-022: DELETE /pedido/{id} -> 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}