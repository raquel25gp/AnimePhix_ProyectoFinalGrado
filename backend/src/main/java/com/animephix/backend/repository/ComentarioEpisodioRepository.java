package com.animephix.backend.repository;

import com.animephix.backend.model.ComentarioEpisodio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComentarioEpisodioRepository extends JpaRepository<ComentarioEpisodio, Long> {
    @Query("Select c FROM ComentarioEpisodio c WHERE c.episodio.anime.idAnime = ?1 AND c.episodio.numEpisodio = ?2 ORDER BY c.fechaCreacion DESC")
    List<ComentarioEpisodio> buscarTodosPorAnimeYEpisodio(Long idAnime, Long numEpisodio);

    @Query("Select c FROM ComentarioEpisodio c WHERE c.comentario = ?1 AND c.usuario.idUsuario = ?2 AND c.episodio.anime.idAnime = ?3 AND c.episodio.numEpisodio = ?4")
    Optional<ComentarioEpisodio> buscarPorTextoYUsuarioYEpisodioYAnime(String textoComentario, Long idUsuario, Long idAnime, Long numEpisodio);

}
