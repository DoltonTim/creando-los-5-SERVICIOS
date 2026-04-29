package com.libros.ms_prestamos.Controller;

import com.libros.ms_prestamos.Entity.Prestamo;
import com.libros.ms_prestamos.Service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {
    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping("/saludo")
    public String saludo() {
        return "ms-prestamos funcionando";
    }

    @PostMapping
    public ResponseEntity<Prestamo> guardar(@Valid @RequestBody Prestamo prestamo) {
        return ResponseEntity.ok(prestamoService.guardar(prestamo));
    }
    @GetMapping
    public ResponseEntity<List<Prestamo>> listar() {
        return ResponseEntity.ok(prestamoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> buscarPorId(@PathVariable Long id) {
        return prestamoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Prestamo>> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(prestamoService.buscarPorUsuario(usuarioId));
    }
    @PutMapping("/devolver/{id}")
    public ResponseEntity<Prestamo> devolver(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.devolver(id));
    }
}
