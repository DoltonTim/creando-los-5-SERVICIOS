package com.libros.ms_ordenes.Client;

import com.libros.ms_ordenes.DTO.PagoRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PagosClient {

    private final RestTemplate restTemplate;

    public PagosClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retry(name = "pagosService", fallbackMethod = "crearPagoFallback")
    @CircuitBreaker(name = "pagosService", fallbackMethod = "crearPagoFallback")
    public boolean crearPago(PagoRequest pagoRequest) {
        restTemplate.postForEntity("http://ms-pagos/pagos", pagoRequest, String.class);
        return true;
    }

    public boolean crearPagoFallback(PagoRequest pagoRequest, Throwable ex) {
        System.out.println("Fallback de pagos activado. No se pudo registrar el pago de la orden "
                + pagoRequest.getOrdenId() + ". Causa: " + ex.getMessage());
        return false;
    }
}
