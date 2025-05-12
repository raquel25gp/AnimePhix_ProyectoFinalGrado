package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FiltrosDirectorioDTO {
    private String genero;
    private int anio;
    private String estado;
    private LocalDate fechaInicio;
}
