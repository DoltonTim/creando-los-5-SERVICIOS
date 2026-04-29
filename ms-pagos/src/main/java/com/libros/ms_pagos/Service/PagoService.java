package com.libros.ms_pagos.Service;

import com.libros.ms_pagos.Entity.Pago;
import com.libros.ms_pagos.Repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PagoService {
    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public Pago registrar(Pago pago) {
        pago.setEstado("PAGADO");
        pago.setFechaPago(LocalDateTime.now());

        if (pago.getReferencia() == null || pago.getReferencia().isBlank()) {
            pago.setReferencia("REF-" + UUID.randomUUID());
        }

        return pagoRepository.save(pago);
    }

    public List<Pago> listar() {
        return pagoRepository.findAll();
    }
    public Optional<Pago> buscarPorId(Long id) {
        return pagoRepository.findById(id);
    }

    public List<Pago> buscarPorOrden(Long ordenId) {
        return pagoRepository.findByOrdenId(ordenId);
    }
}