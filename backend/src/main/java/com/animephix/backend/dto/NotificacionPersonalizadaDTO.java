package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class NotificacionPersonalizadaDTO {
    private String email;
    private String texto;
    private LocalDate fecha;
}
