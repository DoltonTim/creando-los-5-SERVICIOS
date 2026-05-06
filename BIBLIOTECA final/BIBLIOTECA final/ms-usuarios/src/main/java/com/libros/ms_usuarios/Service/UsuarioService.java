package com.libros.ms_usuarios.Service;

import com.libros.ms_usuarios.Entity.Usuario;
import com.libros.ms_usuarios.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario guardar(Usuario usuario) {
        Optional<Usuario> existente = usuarioRepository.findByemail(usuario.getEmail());

        if (existente.isPresent()) {
            throw new RuntimeException("Ya existe un usuario con ese correo");
        }
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByemail(email);
    }
}
