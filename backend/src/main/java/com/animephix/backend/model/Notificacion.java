package com.animephix.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Data
@Table(name = "notificacion")
public class Notificacion {
    //Clase para el tipo enum
    public enum TipoNotificacion {
        Anime,
        Personalizada
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotificacion;

    @Column(nullable = false)
    @NotBlank(message = "La notificación debe tener un texto")
    @Size(min = 3, max = 100)
    private String texto;

    @Column(columnDefinition = "ENUM('Anime', 'Personalizada')")
    @Enumerated(EnumType.STRING)
    private TipoNotificacion tipo;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Column(nullable = false)
    @Min(0)
    @Max(6)
    private int diaSemana;

    @ManyToOne
    @JoinColumn(name = "usuarioId", referencedColumnName = "idUsuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "animeId", referencedColumnName = "idAnime")
    private Anime anime;
}
