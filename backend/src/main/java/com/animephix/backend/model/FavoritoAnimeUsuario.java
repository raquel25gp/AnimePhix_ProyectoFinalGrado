package com.animephix.backend.model;

import com.animephix.backend.model.compositePK.FavoritoAnimeUsuarioId;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "favoritoAnimeUsuario")
public class FavoritoAnimeUsuario {

    @EmbeddedId
    private FavoritoAnimeUsuarioId id;

    @Column(nullable = false)
    private boolean habilitado = false;

    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false, insertable=false, updatable=false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "animeId", nullable = false, insertable=false, updatable=false)
    private Anime anime;

}
