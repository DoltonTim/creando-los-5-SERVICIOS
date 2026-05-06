package com.libros.ms_pagos.Controller;

import com.libros.ms_pagos.Entity.Pago;
import com.libros.ms_pagos.Service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
public class PagoController {
    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping("/saludo")
    public String saludo() {
        return "ms-pagos funcionando";
    }

    @PostMapping
    public ResponseEntity<Pago> registrar(@Valid @RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.registrar(pago));
    }

    @GetMapping
    public ResponseEntity<List<Pago>> listar() {
        return ResponseEntity.ok(pagoService.listar());
    }
    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<List<Pago>> buscarPorOrden(@PathVariable Long ordenId) {
        return ResponseEntity.ok(pagoService.buscarPorOrden(ordenId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable Long id) {
        return pagoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
