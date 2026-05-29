package com.utn.foodstore.controller;

import com.utn.foodstore.dto.LoginRequest;
import com.utn.foodstore.dto.UsuarioDto;
import com.utn.foodstore.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final UsuarioService service;

    public AuthController(UsuarioService service) {
        this.service = service;
    }

    // POST /auth/login -> 200 OK con los datos del usuario (o 400 si credenciales invalidas)
    @PostMapping("/login")
    public ResponseEntity<UsuarioDto> login(@Valid @RequestBody LoginRequest dto) {
        return ResponseEntity.ok(service.login(dto));
    }
}