package com.libros.ms_usuarios.Controller;

import com.libros.ms_usuarios.DTO.AuthResponse;
import com.libros.ms_usuarios.DTO.LoginRequest;
import com.libros.ms_usuarios.Entity.Usuario;
import com.libros.ms_usuarios.Security.JwtService;
import com.libros.ms_usuarios.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public AuthController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioService.buscarPorEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generarToken(usuario.getEmail());

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
