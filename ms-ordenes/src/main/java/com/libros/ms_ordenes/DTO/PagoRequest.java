package com.libros.ms_ordenes.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagoRequest {
    private Long ordenId;
    private BigDecimal monto;
    private String metodoPago;
}
