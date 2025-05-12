package com.animephix.backend.model;

import com.animephix.backend.model.compositePK.VistoEpisodioUsuarioId;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "vistoEpisodioUsuario")
public class VistoEpisodioUsuario {

    @EmbeddedId
    private VistoEpisodioUsuarioId id;

    @Column(nullable = false)
    private boolean habilitado = false;

    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false, insertable=false, updatable=false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "animeId", referencedColumnName = "animeId", insertable=false, updatable=false),
            @JoinColumn(name = "numEpisodio", referencedColumnName = "numEpisodio", insertable=false, updatable=false)
    })
    private Episodio episodio;
}
