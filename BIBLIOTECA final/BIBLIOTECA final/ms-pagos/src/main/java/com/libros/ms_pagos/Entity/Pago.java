package com.libros.ms_pagos.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La orden es obligatoria")
    @Column(nullable = false)
    private Long ordenId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a cero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @NotBlank(message = "El método de pago es obligatorio")
    @Column(nullable = false, length = 50)
    private String metodoPago;

    @Column(nullable = false, length = 30)
    private String estado;
    @Column(nullable = false)
    private LocalDateTime fechaPago;

    @Column(length = 100)
    private String referencia;
}
