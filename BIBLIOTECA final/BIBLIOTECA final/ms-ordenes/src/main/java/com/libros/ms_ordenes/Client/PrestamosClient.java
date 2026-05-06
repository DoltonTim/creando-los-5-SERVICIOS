package com.libros.ms_ordenes.Client;

import com.libros.ms_ordenes.DTO.PrestamoRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PrestamosClient {

    private final RestTemplate restTemplate;

    public PrestamosClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "prestamosService", fallbackMethod = "crearPrestamoFallback")
    @Retry(name = "prestamosService")
    public void crearPrestamo(PrestamoRequest prestamoRequest) {
        restTemplate.postForObject("http://ms-prestamos/prestamos", prestamoRequest, Object.class);
    }

    public void crearPrestamoFallback(PrestamoRequest prestamoRequest, Throwable ex) {
        throw new RuntimeException("No se pudo registrar el préstamo. ms-prestamos no está disponible: " + ex.getMessage());
    }
}
