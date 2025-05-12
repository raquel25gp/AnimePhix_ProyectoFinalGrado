package com.animephix.backend.service;

import com.animephix.backend.dto.ActualizarEpisodioDTO;
import com.animephix.backend.dto.NuevoEpisodioDTO;
import com.animephix.backend.dto.VideoEpisodioDTO;
import com.animephix.backend.dto.UltimosEpisodiosDTO;
import com.animephix.backend.model.Anime;
import com.animephix.backend.model.Episodio;
import com.animephix.backend.model.compositePK.EpisodioId;
import com.animephix.backend.repository.AnimeRepository;
import com.animephix.backend.repository.EpisodioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EpisodioService {
    private EpisodioRepository episodioRepository;
    private AnimeRepository animeRepository;

    @Autowired
    public EpisodioService (EpisodioRepository episodioRepository, AnimeRepository animeRepository) {
        this.episodioRepository = episodioRepository;
        this.animeRepository = animeRepository;
    }

    @Transactional(readOnly = true)
    public List<UltimosEpisodiosDTO> buscarUltimosEpisodiosPublicados() {
        return episodioRepository.obtenerUltimosEpisodios();
    }

    @Transactional(readOnly = true)
    public VideoEpisodioDTO devolverDatosEpisodio(String nombreAnime, Long numEpisodio) {
        Anime anime = animeRepository.findByNombre(nombreAnime)
                .orElseThrow(() -> new RuntimeException("Anime no encontrado,"));

        Episodio episodio = episodioRepository.buscarEpisodioByIdAnimeAndNumEpisodio(anime.getIdAnime(), numEpisodio)
                .orElseThrow(() -> new RuntimeException("Episodio no encontrado."));

        return VideoEpisodioDTO.builder()
                .urlPoster(episodio.getUrlPoster())
                .urlVideo(episodio.getUrlVideo())
                .build();
    }

    @Transactional(readOnly = true)
    public Long devolverTotalEpisodiosPorAnime(String nombreAnime) {
        Anime anime = animeRepository.findByNombre(nombreAnime)
                .orElseThrow(() -> new RuntimeException("Anime no encontrado."));

        return episodioRepository.calcularTotalEpisodios(anime.getIdAnime());
    }

    @Transactional
    public void crear(NuevoEpisodioDTO dto) {
        Anime anime = animeRepository.findByNombre(dto.getNombreAnime())
                .orElseThrow(() -> new RuntimeException("Anime no encontrado."));

        Long totalEpisodios = episodioRepository.calcularTotalEpisodios(anime.getIdAnime());

        Episodio nuevoEpisodio = new Episodio();
        nuevoEpisodio.setAnime(anime);
        nuevoEpisodio.setNumEpisodio(totalEpisodios + 1);
        nuevoEpisodio.setFechaLanzamiento(dto.getFechaLanzamiento());
        nuevoEpisodio.setUrlVideo(dto.getUrlVideo());
        nuevoEpisodio.setUrlPoster(dto.getUrlPoster());

        episodioRepository.save(nuevoEpisodio);
    }

    @Transactional(readOnly = true)
    public List<Episodio> listarTodos() {
        return episodioRepository.findAll();
    }

    @Transactional
    public void actualizar(ActualizarEpisodioDTO dto, Long idAnime, Long numEpisodio) {
        Anime anime = animeRepository.findById(idAnime)
                .orElseThrow(() -> new RuntimeException("Anime no encontrado."));

        Episodio episodio = episodioRepository.findById(new EpisodioId(idAnime, numEpisodio))
                .orElseThrow(() -> new RuntimeException("Episodio no encontrado."));

        if (dto.getUrlVideo() != null && !dto.getUrlVideo().isBlank()) {
            episodio.setUrlVideo(dto.getUrlVideo());
        }

        if (dto.getFechaLanzamiento() != null) {
            episodio.setFechaLanzamiento(dto.getFechaLanzamiento());
        }

        if (dto.getUrlPoster() != null && !dto.getUrlPoster().isBlank()) {
            episodio.setUrlPoster(dto.getUrlPoster());
        }

        episodioRepository.save(episodio);
    }
}
