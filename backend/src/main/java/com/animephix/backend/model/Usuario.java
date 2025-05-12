package com.animephix.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 20)
    private String nombre;

    @Email(message = "El email debe de tener un formato válido")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Se debe introducir una contraseña válida")
    private String password;

    private String urlImagen;

    private boolean habilitado = true;

    @ManyToOne
    @JoinColumn(name = "rolId", referencedColumnName = "idRol")
    private Rol rol;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(idUsuario, usuario.idUsuario) && Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, email);
    }
}
