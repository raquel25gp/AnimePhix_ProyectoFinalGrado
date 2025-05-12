package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class ActualizarEpisodioDTO {
    private String urlVideo;
    private LocalDate fechaLanzamiento;
    private String urlPoster;
}
