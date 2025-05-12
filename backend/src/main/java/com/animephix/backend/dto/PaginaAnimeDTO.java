package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginaAnimeDTO {
    private String nombre;
    private String descripcion;
    private String urlImagen;
    private String genero;
    private String estado;
    private List<ListadoEpisodioResumenDTO> listadoEpisodios;
}
