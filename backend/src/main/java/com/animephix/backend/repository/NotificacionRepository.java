package com.animephix.backend.repository;

import com.animephix.backend.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    @Query("Select n FROM Notificacion n WHERE n.usuario.email = ?1")
    List<Notificacion> buscarTodasPorEmail(String email);

    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = ?1 AND n.texto = ?2 AND n.fechaInicio = ?3")
    Optional<Notificacion> buscarPorUsuarioYTextoYFecha(Long idUsuario, String texto, LocalDate fecha);

    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = ?1 AND n.anime.idAnime = ?2")
    Optional<Notificacion> buscarPorUsuarioYAnime(Long idUsuario, Long idAnime);

    @Modifying // Añado el modifiying para que me permita hacer el DELETE
    @Transactional
    @Query("DELETE FROM Notificacion n WHERE n.anime.idAnime = ?1 AND n.usuario.idUsuario = ?2")
    void eliminarPorAnimeYUsuario(Long idAnime, Long idUsuario);
}
