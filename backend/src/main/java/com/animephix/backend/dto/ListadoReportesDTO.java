package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ListadoReportesDTO {
    private Long idReporte;
    private String descripcion;
    private boolean corregido;
    private String tipoProblema;
    private String emailUsuario;
}
