package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ActualizarNombreUsuarioDTO {
    private String email;
    private String nuevoNombre;
}
