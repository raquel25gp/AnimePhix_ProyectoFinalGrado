package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UltimosEpisodiosDTO {
    private String anime;
    private Long numEpisodio;
    private String urlPoster;
}
