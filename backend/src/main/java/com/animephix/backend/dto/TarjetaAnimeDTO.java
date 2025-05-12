package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TarjetaAnimeDTO {
    private String nombre;
    private String urlImagen;
}
