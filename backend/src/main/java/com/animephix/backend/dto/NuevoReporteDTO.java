package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NuevoReporteDTO {
    private String tipoProblema;
    private String descripcion;
    private String email;
}
