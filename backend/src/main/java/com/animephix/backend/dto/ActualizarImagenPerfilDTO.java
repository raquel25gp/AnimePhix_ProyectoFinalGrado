package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ActualizarImagenPerfilDTO {
    private String email;
    private String urlImagen;
}
