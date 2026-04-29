package com.libros.ms_libros.Controller;

import com.libros.ms_libros.Entity.Libro;
import com.libros.ms_libros.Service.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libros")
public class LibroControlller {
    private final LibroService libroService;

    public LibroControlller(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping("/saludo")
    public String saludo() {
        return "ms-libros funcionando";
    }

    @PostMapping
    public ResponseEntity<Libro> guardar(@Valid @RequestBody Libro libro) {
        return ResponseEntity.ok(libroService.guardar(libro));
    }

    @GetMapping
    public ResponseEntity<List<Libro>> listar() {
        return ResponseEntity.ok(libroService.listar());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Libro> buscarPorId(@PathVariable Long id) {
        return libroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/reducir-stock/{id}")
    public ResponseEntity<Libro> reducirStock(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.reducirStock(id));
    }

    @PutMapping("/aumentar-stock/{id}")
    public ResponseEntity<Libro> aumentarStock(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.aumentarStock(id));
    }
}
