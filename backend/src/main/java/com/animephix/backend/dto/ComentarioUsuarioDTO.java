package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ComentarioUsuarioDTO {
    private String email;
    private String nombreAnime;
    private Long numEpisodio;
    private String comentario;
    private boolean habilitado;
}
