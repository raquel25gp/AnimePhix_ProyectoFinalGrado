package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ListadoFavoritosDTO {
    private String nombre;
    private String urlImagen;
}
