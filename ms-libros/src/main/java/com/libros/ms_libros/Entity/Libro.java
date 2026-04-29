package com.libros.ms_libros.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "libros")
@Data
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Column(nullable = false)
    private String autor;

    @NotBlank(message = "La categoría es obligatoria")
    @Column(nullable = false)
    private String categoria;

    @Column(length = 50)
    private String isbn;

    @Column(length = 500)
    private String descripcion;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    @DecimalMin(value = "0.0", inclusive = true, message = "El precio de compra no puede ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @DecimalMin(value = "0.0", inclusive = true, message = "El precio de alquiler no puede ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioAlquiler;

    @Column(nullable = false)
    private Boolean disponible;
}
