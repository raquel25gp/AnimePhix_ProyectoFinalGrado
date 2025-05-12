package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class NotificacionCalendarioDTO {
    private String texto;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int diaSemana;
}
