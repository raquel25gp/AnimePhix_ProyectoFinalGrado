package com.animephix.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comentarEpisodio")
public class ComentarioEpisodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComentario;

    @Column(nullable = false)
    private boolean habilitado = true;

    @NotBlank(message = "El comentario no puede estar vacío")
    @Size(min = 3, max = 500)
    private String comentario;

    @Column(name = "fechaCreacion", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @CreationTimestamp //La fecha se establece automaticamente al crear el comentario
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "animeId", referencedColumnName = "animeId"),
            @JoinColumn(name = "numEpisodio", referencedColumnName = "numEpisodio")
    })
    private Episodio episodio;
}
