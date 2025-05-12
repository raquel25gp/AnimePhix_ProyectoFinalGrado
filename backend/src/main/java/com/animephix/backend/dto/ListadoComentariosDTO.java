package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ListadoComentariosDTO {
    private String nombreUsuario;
    private String urlImagen;
    private String comentario;
    private boolean habilitado;
    private String fechaCreacion;
}
