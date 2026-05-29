package com.utn.foodstore.service;

import com.utn.foodstore.dto.LoginRequest;
import com.utn.foodstore.dto.UsuarioCreate;
import com.utn.foodstore.dto.UsuarioDto;
import com.utn.foodstore.dto.UsuarioEdit;
import com.utn.foodstore.exception.BusinessException;
import com.utn.foodstore.model.Rol;
import com.utn.foodstore.model.Usuario;
import com.utn.foodstore.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    // Spring inyecta las DOS dependencias por el constructor
    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // HU-006: Registrar usuario
    public UsuarioDto save(UsuarioCreate dto) {
        // Validar email unico (RN-006-03)
        if (repository.existsByMail(dto.mail())) {
            throw new BusinessException(
                    "Ya existe un usuario con el email: " + dto.mail());
        }

        Usuario usuario = Usuario.builder()
                .nombre(dto.nombre())
                .apellido(dto.apellido())
                .mail(dto.mail())
                .celular(dto.celular())
                .password(passwordEncoder.encode(dto.password())) // encripta (RN-006-06)
                .rol(Rol.USUARIO)                                  // rol por defecto (RN-006-07)
                .build();

        return toDto(repository.save(usuario));
    }

    // Login: verifica credenciales y devuelve los datos del usuario
    public UsuarioDto login(LoginRequest dto) {
        // Busca el usuario por mail
        Usuario usuario = repository.findByMail(dto.mail())
                .orElseThrow(() -> new BusinessException("Credenciales invalidas"));

        // Verifica la contraseña contra el hash guardado
        if (!passwordEncoder.matches(dto.password(), usuario.getPassword())) {
            throw new BusinessException("Credenciales invalidas");
        }

        return toDto(usuario);
    }

    // HU-007: Listar usuarios
    public List<UsuarioDto> findAll() {
        return repository.findAllActive()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // HU-008: Obtener usuario por id
    public UsuarioDto findById(Long id) {
        return toDto(repository.findByIdOrThrow(id));
    }

    // HU-009: Actualizar usuario (parcial)
    public UsuarioDto update(Long id, UsuarioEdit dto) {
        Usuario usuario = repository.findByIdOrThrow(id);

        if (dto.nombre() != null) {
            usuario.setNombre(dto.nombre());
        }
        if (dto.apellido() != null) {
            usuario.setApellido(dto.apellido());
        }
        if (dto.mail() != null) {
            // Si cambia el email, validar que el nuevo no exista (RN-009-02)
            if (!dto.mail().equals(usuario.getMail()) && repository.existsByMail(dto.mail())) {
                throw new BusinessException(
                        "Ya existe un usuario con el email: " + dto.mail());
            }
            usuario.setMail(dto.mail());
        }
        if (dto.celular() != null) {
            usuario.setCelular(dto.celular());
        }
        if (dto.password() != null) {
            usuario.setPassword(passwordEncoder.encode(dto.password())); // re-encripta (RN-009-03)
        }

        return toDto(repository.save(usuario));
    }

    // HU-010: Eliminar usuario (soft delete)
    public void delete(Long id) {
        repository.softDeleteById(id);
    }

    // Traductor: entidad -> DTO (SIN password)
    private UsuarioDto toDto(Usuario usuario) {
        return new UsuarioDto(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getMail(),
                usuario.getCelular(),
                usuario.getRol()
        );
    }
}