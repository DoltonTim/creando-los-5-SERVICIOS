package com.libros.ms_ordenes.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PrestamoRequest {
    private Long usuarioId;
    private Long libroId;
    private Long ordenId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
