package com.libros.ms_prestamos.Service;

import com.libros.ms_prestamos.Entity.Prestamo;
import com.libros.ms_prestamos.Repository.PrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoService {
    private final PrestamoRepository prestamoRepository;
    private final RestTemplate restTemplate;

    public PrestamoService(PrestamoRepository prestamoRepository, RestTemplate restTemplate) {
        this.prestamoRepository = prestamoRepository;
        this.restTemplate = restTemplate;
    }


    public Prestamo guardar(Prestamo prestamo) {
        if (prestamo.getFechaFin().isBefore(prestamo.getFechaInicio())) {
            throw new RuntimeException("La fecha fin no puede ser menor que la fecha de inicio");
        }

        // 🔥 LLAMADA A ms-libros
        String url = "http://ms-libros/libros/reducir-stock/" + prestamo.getLibroId();
        restTemplate.put(url, null);

        prestamo.setEstado("ACTIVO");
        prestamo.setMulta(BigDecimal.ZERO);
        prestamo.setFechaDevolucion(null);

        return prestamoRepository.save(prestamo);
    }
    public List<Prestamo> listar() {
        return prestamoRepository.findAll();
    }

    public Optional<Prestamo> buscarPorId(Long id) {
        return prestamoRepository.findById(id);
    }

    public List<Prestamo> buscarPorUsuario(Long usuarioId) {
        return prestamoRepository.findByUsuarioId(usuarioId);
    }

    public Prestamo devolver(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if ("DEVUELTO".equals(prestamo.getEstado())) {
            return prestamo;
        }

        LocalDate hoy = LocalDate.now();
        prestamo.setFechaDevolucion(hoy);

        if (hoy.isAfter(prestamo.getFechaFin())) {
            long diasRetraso = ChronoUnit.DAYS.between(prestamo.getFechaFin(), hoy);
            BigDecimal multa = BigDecimal.valueOf(diasRetraso).multiply(BigDecimal.valueOf(5.00));

            prestamo.setEstado("VENCIDO");
            prestamo.setMulta(multa);
        } else {
            prestamo.setEstado("DEVUELTO");
            prestamo.setMulta(BigDecimal.ZERO);
        }

        String url = "http://ms-libros/libros/aumentar-stock/" + prestamo.getLibroId();
        restTemplate.put(url, null);
        return prestamoRepository.save(prestamo);
    }
}
