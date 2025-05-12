package com.animephix.backend.repository;

import com.animephix.backend.dto.UltimosEpisodiosDTO;
import com.animephix.backend.model.Episodio;
import com.animephix.backend.model.compositePK.EpisodioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodioRepository extends JpaRepository<Episodio, EpisodioId> {
    @Query("Select e FROM Episodio e WHERE e.anime.idAnime = ?1")
    List<Episodio> buscarEpisodiosPorAnime(Long idAnime);

    // Obtener los ultimos 9 episodios agregados
    @Query("Select new com.animephix.backend.dto.UltimosEpisodiosDTO(a.nombre, e.numEpisodio, e.urlPoster) FROM Episodio e JOIN Anime a ON a.idAnime = e.anime.idAnime WHERE a.estado.idEstado = 1 AND e.fechaLanzamiento = (SELECT MAX(e2.fechaLanzamiento) FROM Episodio e2 WHERE e2.anime.idAnime = a.idAnime) ORDER BY e.fechaLanzamiento DESC LIMIT 9")
    List<UltimosEpisodiosDTO> obtenerUltimosEpisodios();

    @Query("Select e FROM Episodio e WHERE e.anime.idAnime = ?1 AND e.numEpisodio = ?2")
    Optional<Episodio> buscarEpisodioByIdAnimeAndNumEpisodio(Long idAnime, Long numEpisodio);

    // Calcular total de episodios por anime
    @Query("Select COUNT(e) FROM Episodio e WHERE e.anime.idAnime = ?1")
    Long calcularTotalEpisodios(Long idAnime);
}
