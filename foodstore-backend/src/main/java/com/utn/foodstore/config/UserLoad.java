package com.utn.foodstore.config;

import com.utn.foodstore.model.Rol;
import com.utn.foodstore.model.Usuario;
import com.utn.foodstore.repository.UsuarioRepository;
import com.utn.foodstore.service.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserLoad implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UserLoad(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Solo crea el admin si NO hay usuarios (RN HU-027)
        if (usuarioRepository.count() == 0) {
            Usuario admin = Usuario.builder()
                    .nombre("Admin")
                    .apellido("Admin")
                    .mail("admin@admin.com")
                    .password(passwordEncoder.encode("123456"))
                    .rol(Rol.ADMIN)
                    .build();
            usuarioRepository.save(admin);
            log.info("Usuario administrador creado: admin@admin.com");
        }
    }
}