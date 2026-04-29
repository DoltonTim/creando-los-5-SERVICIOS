package com.libros.ms_libros.Repository;

import com.libros.ms_libros.Entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {
}
