package com.libros.ms_prestamos.Client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LibrosClient {

    private final RestTemplate restTemplate;

    public LibrosClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "librosService", fallbackMethod = "reducirStockFallback")
    @Retry(name = "librosService")
    public void reducirStock(Long libroId) {
        restTemplate.put("http://ms-libros/libros/reducir-stock/" + libroId, null);
    }

    public void reducirStockFallback(Long libroId, Throwable ex) {
        throw new RuntimeException("No se pudo reducir el stock del libro " + libroId + ". ms-libros no está disponible: " + ex.getMessage());
    }

    @CircuitBreaker(name = "librosService", fallbackMethod = "aumentarStockFallback")
    @Retry(name = "librosService")
    public void aumentarStock(Long libroId) {
        restTemplate.put("http://ms-libros/libros/aumentar-stock/" + libroId, null);
    }

    public void aumentarStockFallback(Long libroId, Throwable ex) {
        throw new RuntimeException("No se pudo aumentar el stock del libro " + libroId + ". ms-libros no está disponible: " + ex.getMessage());
    }
}
