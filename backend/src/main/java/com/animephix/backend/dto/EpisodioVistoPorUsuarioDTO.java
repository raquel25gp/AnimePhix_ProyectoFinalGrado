package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EpisodioVistoPorUsuarioDTO {
    private String email;
    private String nombreAnime;
    private Long numEpisodio;
}
