package com.libros.ms_ordenes.Service;

import com.libros.ms_ordenes.DTO.PrestamoRequest;
import com.libros.ms_ordenes.Entity.DetalleOrden;
import com.libros.ms_ordenes.Entity.Ordenes;
import com.libros.ms_ordenes.Repository.OrdenesRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.libros.ms_ordenes.DTO.PagoRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenesService {

    private final OrdenesRepository ordenRepository;
    private final RestTemplate restTemplate;

    public OrdenesService(OrdenesRepository ordenRepository, RestTemplate restTemplate) {
        this.ordenRepository = ordenRepository;
        this.restTemplate = restTemplate;
    }

    public Ordenes guardar(Ordenes orden) {

        if (orden.getDetalles() == null || orden.getDetalles().isEmpty()) {
            throw new RuntimeException("La orden debe tener al menos un detalle");
        }

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleOrden detalle : orden.getDetalles()) {
            detalle.setOrden(orden);
            detalle.setSubtotal(
                    detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()))
            );
            total = total.add(detalle.getSubtotal());
        }

        orden.setTotal(total);
        orden.setEstado("PENDIENTE");
        orden.setFechaCreacion(LocalDateTime.now());

        Ordenes ordenGuardada = ordenRepository.save(orden);
        PagoRequest pagoRequest = new PagoRequest();
        pagoRequest.setOrdenId(ordenGuardada.getId());
        pagoRequest.setMonto(ordenGuardada.getTotal());
        pagoRequest.setMetodoPago("YAPE");

        String urlPagos = "http://ms-pagos/pagos";
        restTemplate.postForObject(urlPagos, pagoRequest, Object.class);

        for (DetalleOrden detalle : ordenGuardada.getDetalles()) {

            String urlLibros = "http://ms-libros/libros/reducir-stock//" + detalle.getLibroId();
            restTemplate.put(urlLibros, null);

            if ("ALQUILER".equalsIgnoreCase(ordenGuardada.getTipoOrden())) {

                PrestamoRequest prestamoRequest = new PrestamoRequest();
                prestamoRequest.setUsuarioId(ordenGuardada.getUsuarioId());
                prestamoRequest.setLibroId(detalle.getLibroId());
                prestamoRequest.setOrdenId(ordenGuardada.getId());
                prestamoRequest.setFechaInicio(LocalDateTime.now());
                prestamoRequest.setFechaFin(LocalDateTime.now().plusDays(7));

                String urlPrestamos = "http://ms-prestamos/prestamos";
                restTemplate.postForObject(urlPrestamos, prestamoRequest, Object.class);
            }
        }

        ordenGuardada.setEstado("COMPLETADA");

        return ordenRepository.save(ordenGuardada);
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