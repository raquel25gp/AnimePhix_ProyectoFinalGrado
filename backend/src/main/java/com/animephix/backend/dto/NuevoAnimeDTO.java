package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class NuevoAnimeDTO {
    private String nombre;
    private String descripcion;
    private LocalDate fechaCreacion;
    private LocalDate fechaFin;
    private int diaSemana;
    private boolean visible;
    private String urlImagen;
    private String genero;
    private String estado;
}
