package com.libros.ms_libros.Service;

import com.libros.ms_libros.Entity.Libro;
import com.libros.ms_libros.Repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {
    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public Libro guardar(Libro libro) {
        libro.setDisponible(libro.getStock() != null && libro.getStock() > 0);
        return libroRepository.save(libro);
    }

    public List<Libro> listar() {
        return libroRepository.findAll();
    }

    public Optional<Libro> buscarPorId(Long id) {
        return libroRepository.findById(id);
    }

    public Libro reducirStock(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        if (libro.getStock() <= 0) {
            throw new RuntimeException("No hay stock disponible");
        }

        libro.setStock(libro.getStock() - 1);
        libro.setDisponible(libro.getStock() > 0);

        return libroRepository.save(libro);
    }
    public Libro aumentarStock(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        libro.setStock(libro.getStock() + 1);
        libro.setDisponible(true);

        return libroRepository.save(libro);
    }
}
