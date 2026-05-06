package com.libros.ms_ordenes.Service;

import com.libros.ms_ordenes.Client.LibrosClient;
import com.libros.ms_ordenes.Client.PagosClient;
import com.libros.ms_ordenes.Client.PrestamosClient;
import com.libros.ms_ordenes.DTO.PagoRequest;
import com.libros.ms_ordenes.DTO.PrestamoRequest;
import com.libros.ms_ordenes.Entity.DetalleOrden;
import com.libros.ms_ordenes.Entity.Ordenes;
import com.libros.ms_ordenes.Repository.OrdenesRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenesService {

    private final OrdenesRepository ordenRepository;
    private final PagosClient pagosClient;
    private final LibrosClient librosClient;
    private final PrestamosClient prestamosClient;

    public OrdenesService(OrdenesRepository ordenRepository,
                          PagosClient pagosClient,
                          LibrosClient librosClient,
                          PrestamosClient prestamosClient) {
        this.ordenRepository = ordenRepository;
        this.pagosClient = pagosClient;
        this.librosClient = librosClient;
        this.prestamosClient = prestamosClient;
    }

    public Ordenes guardar(Ordenes orden) {

        if (orden.getDetalles() == null || orden.getDetalles().isEmpty()) {
            throw new RuntimeException("La orden debe tener al menos un detalle");
        }

        if (orden.getTipoOrden() == null || orden.getTipoOrden().isBlank()) {
            throw new RuntimeException("El tipo de orden es obligatorio: COMPRA o ALQUILER");
        }

        String tipoOrden = orden.getTipoOrden().trim().toUpperCase();

        if (!"COMPRA".equals(tipoOrden) && !"ALQUILER".equals(tipoOrden)) {
            throw new RuntimeException("Tipo de orden inválido. Use COMPRA o ALQUILER");
        }

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleOrden detalle : orden.getDetalles()) {

            if (detalle.getLibroId() == null) {
                throw new RuntimeException("El ID del libro es obligatorio");
            }

            if (detalle.getPrecioUnitario() == null) {
                throw new RuntimeException("El precio unitario es obligatorio");
            }

            if (detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("El precio unitario debe ser mayor a cero");
            }

            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor a cero");
            }

            detalle.setOrden(orden);

            BigDecimal subtotal = detalle.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()));

            detalle.setSubtotal(subtotal);

            total = total.add(subtotal);
        }

        orden.setTipoOrden(tipoOrden);
        orden.setTotal(total);
        orden.setEstado("PENDIENTE");
        orden.setFechaCreacion(LocalDateTime.now());

        Ordenes ordenGuardada = ordenRepository.save(orden);

        try {
            boolean pagoRegistrado = registrarPago(ordenGuardada);

            if (!pagoRegistrado) {
                ordenGuardada.setEstado("PAGO_PENDIENTE");
                return ordenRepository.save(ordenGuardada);
            }

            for (DetalleOrden detalle : ordenGuardada.getDetalles()) {

                if ("COMPRA".equals(tipoOrden)) {
                    reducirStockPorCantidad(detalle);
                }

                if ("ALQUILER".equals(tipoOrden)) {
                    crearPrestamosPorCantidad(ordenGuardada, detalle);
                }
            }

            ordenGuardada.setEstado("COMPLETADA");
            return ordenRepository.save(ordenGuardada);

        } catch (RuntimeException ex) {
            ordenGuardada.setEstado("FALLIDA");
            ordenRepository.save(ordenGuardada);

            throw new RuntimeException("La orden quedó FALLIDA: " + ex.getMessage());
        }
    }

    private boolean registrarPago(Ordenes ordenGuardada) {
        PagoRequest pagoRequest = new PagoRequest();
        pagoRequest.setOrdenId(ordenGuardada.getId());
        pagoRequest.setMonto(ordenGuardada.getTotal());
        pagoRequest.setMetodoPago("YAPE");

        return pagosClient.crearPago(pagoRequest);
    }

    private void reducirStockPorCantidad(DetalleOrden detalle) {
        for (int i = 0; i < detalle.getCantidad(); i++) {
            librosClient.reducirStock(detalle.getLibroId());
        }
    }

    private void crearPrestamosPorCantidad(Ordenes ordenGuardada, DetalleOrden detalle) {
        for (int i = 0; i < detalle.getCantidad(); i++) {
            PrestamoRequest prestamoRequest = new PrestamoRequest();
            prestamoRequest.setUsuarioId(ordenGuardada.getUsuarioId());
            prestamoRequest.setLibroId(detalle.getLibroId());
            prestamoRequest.setOrdenId(ordenGuardada.getId());
            prestamoRequest.setFechaInicio(LocalDate.now());
            prestamoRequest.setFechaFin(LocalDate.now().plusDays(7));

            prestamosClient.crearPrestamo(prestamoRequest);
        }
    }

    public List<Ordenes> listar() {
        return ordenRepository.findAll();
    }

    public Optional<Ordenes> buscarPorId(Long id) {
        return ordenRepository.findById(id);
    }

    public List<Ordenes> buscarPorUsuario(Long usuarioId) {
        return ordenRepository.findByUsuarioId(usuarioId);
    }
}