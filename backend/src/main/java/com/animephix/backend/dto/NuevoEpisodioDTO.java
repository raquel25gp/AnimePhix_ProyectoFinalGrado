package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class NuevoEpisodioDTO {
    private String nombreAnime;
    private LocalDate fechaLanzamiento;
    private String urlVideo;
    private String urlPoster;
}
