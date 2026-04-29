package com.libros.ms_prestamos.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
@Data
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    @Column(nullable = false)
    private Long usuarioId;

    @NotNull(message = "El libro es obligatorio")
    @Column(nullable = false)
    private Long libroId;

    @NotNull(message = "La orden es obligatoria")
    @Column(nullable = false)
    private Long ordenId;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha fin es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaFin;

    private LocalDate fechaDevolucion;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal multa;
}
