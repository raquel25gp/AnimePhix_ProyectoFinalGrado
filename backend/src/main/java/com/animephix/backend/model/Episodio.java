package com.animephix.backend.model;

import com.animephix.backend.model.compositePK.EpisodioId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@IdClass(EpisodioId.class)
@Table(name = "episodio")
public class Episodio {
    @Id
    @ManyToOne
    @JoinColumn(name = "animeId", nullable = false)
    private Anime anime;

    @Id
    private Long numEpisodio;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 3, max = 200)
    private String urlVideo;

    private LocalDate fechaLanzamiento;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 3, max = 200)
    private String urlPoster;
}
