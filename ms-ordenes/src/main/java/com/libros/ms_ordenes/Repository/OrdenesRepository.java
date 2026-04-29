package com.libros.ms_ordenes.Repository;

import com.libros.ms_ordenes.Entity.Ordenes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenesRepository extends JpaRepository<Ordenes, Long> {
    List<Ordenes> findByUsuarioId(Long usuarioId);
}
