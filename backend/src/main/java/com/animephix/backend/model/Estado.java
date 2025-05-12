package com.animephix.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "estado")
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstado;

    @Column(nullable = false)
    @NotBlank(message = "El nombre del estado es obligatorio")
    @Size(min = 3, max = 60)
    private String nombre;

    @OneToMany(mappedBy = "estado")
    private Set<Anime> animes;
}
