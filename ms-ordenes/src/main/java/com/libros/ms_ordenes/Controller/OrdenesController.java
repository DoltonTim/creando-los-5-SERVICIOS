package com.libros.ms_ordenes.Controller;

import com.libros.ms_ordenes.Entity.Ordenes;
import com.libros.ms_ordenes.Service.OrdenesService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
public class OrdenesController {
    private final OrdenesService ordenService;

    public OrdenesController(OrdenesService ordenService) {
        this.ordenService = ordenService;
    }

    @GetMapping("/saludo")
    public String saludo() {
        return "ms-ordenes funcionando";
    }

    @PostMapping
    public ResponseEntity<Ordenes> guardar(@Valid @RequestBody Ordenes orden) {
        return ResponseEntity.ok(ordenService.guardar(orden));
    }

    @GetMapping
    public ResponseEntity<List<Ordenes>> listar() {
        return ResponseEntity.ok(ordenService.listar());
    }
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Ordenes>> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ordenService.buscarPorUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ordenes> buscarPorId(@PathVariable Long id) {
        return ordenService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
