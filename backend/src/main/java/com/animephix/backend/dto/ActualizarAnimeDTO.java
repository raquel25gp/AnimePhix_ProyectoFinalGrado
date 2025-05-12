package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class ActualizarAnimeDTO {
    private String nombre;
    private String descripcion;
    private LocalDate fechaFin;
    private int diaSemana;
    private String genero;
    private String estado;
}
