package com.animephix.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "reporte")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 3, max = 1500)
    private String descripcion;

    @Column(nullable = false)
    private boolean corregido = false;

    @Email(message = "El email debe de tener un formato válido")
    private String email;

    @ManyToOne
    @JoinColumn(name = "tipoProblemaId", referencedColumnName = "idTipoProblema")
    private TipoProblema tipoProblema;
}
