package com.libros.ms_usuarios.Repository;

import com.libros.ms_usuarios.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByemail(String email);
}
