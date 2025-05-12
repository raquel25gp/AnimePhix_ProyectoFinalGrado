package com.animephix.backend.service;

import com.animephix.backend.dto.EpisodioVistoPorUsuarioDTO;
import com.animephix.backend.model.*;
import com.animephix.backend.model.compositePK.EpisodioId;
import com.animephix.backend.model.compositePK.VistoEpisodioUsuarioId;
import com.animephix.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VistoService {
    private VistoRepository vistoRepository;
    private UsuarioRepository usuarioRepository;
    private AnimeRepository animeRepository;
    private EpisodioRepository episodioRepository;

    @Autowired
    public VistoService (VistoRepository vistoRepository,
                         UsuarioRepository usuarioRepository,
                         AnimeRepository animeRepository,
                         EpisodioRepository episodioRepository) {
        this.vistoRepository = vistoRepository;
        this.usuarioRepository = usuarioRepository;
        this.animeRepository = animeRepository;
        this.episodioRepository = episodioRepository;
    }

    @Transactional
    public void cambiarEstadoEpisodioVisto(EpisodioVistoPorUsuarioDTO datosInput) {
        Usuario usuario = usuarioRepository.findByEmail(datosInput.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        Anime anime = animeRepository.findByNombreAndVisibleTrue(datosInput.getNombreAnime())
                .orElseThrow(() -> new RuntimeException("Anime no encontrado."));
        EpisodioId episodioId = new EpisodioId(anime.getIdAnime(), datosInput.getNumEpisodio());
        Episodio episodio = episodioRepository.findById(episodioId)
                .orElseThrow(() -> new RuntimeException("Episodio no encontrado."));

        VistoEpisodioUsuarioId id = new VistoEpisodioUsuarioId(usuario.getIdUsuario(), anime.getIdAnime(), episodio.getNumEpisodio());
        Optional<VistoEpisodioUsuario> episodioUsuarioVistoOptional = vistoRepository.findById(id);

        // si el episodio está o no visto, se cambia su estado
        if (episodioUsuarioVistoOptional.isPresent()) {
            VistoEpisodioUsuario resultado = episodioUsuarioVistoOptional.get();
            resultado.setHabilitado(!resultado.isHabilitado());
            vistoRepository.save(resultado);
        } else { // En caso de que sea la primera vez que lo marca como visto, se guarda en la BBDD
            VistoEpisodioUsuario nuevo = crear(usuario, episodio);
            vistoRepository.save(nuevo);
        }
    }

    // Manejador del texto que se muestra en el frontend
    @Transactional(readOnly = true)
    public String devolverVistoNoVisto(EpisodioVistoPorUsuarioDTO datosInput) {
        Usuario usuario = usuarioRepository.findByEmail(datosInput.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        Anime anime = animeRepository.findByNombreAndVisibleTrue(datosInput.getNombreAnime())
                .orElseThrow(() -> new RuntimeException("Anime no encontrado."));
        EpisodioId episodioId = new EpisodioId(anime.getIdAnime(), datosInput.getNumEpisodio());
        Episodio episodio = episodioRepository.findById(episodioId)
                .orElseThrow(() -> new RuntimeException("Episodio no encontrado."));

        VistoEpisodioUsuarioId id = new VistoEpisodioUsuarioId(usuario.getIdUsuario(), anime.getIdAnime(), episodio.getNumEpisodio());
        Optional<VistoEpisodioUsuario> episodioUsuarioVistoOptional = vistoRepository.findById(id);

        if (episodioUsuarioVistoOptional.isPresent() && episodioUsuarioVistoOptional.get().isHabilitado()) {
            return "Episodio visto";
        } else {
            return "Marcar como visto";
        }
    }

    //Metodo privado auxiliar
    private VistoEpisodioUsuario crear (Usuario usuario, Episodio episodio) {
        VistoEpisodioUsuario episodioVistoDB = new VistoEpisodioUsuario();
        VistoEpisodioUsuarioId vistoId = new VistoEpisodioUsuarioId(usuario.getIdUsuario(), episodio.getAnime().getIdAnime(), episodio.getNumEpisodio());

        episodioVistoDB.setId(vistoId);
        episodioVistoDB.setUsuario(usuario);
        episodioVistoDB.setEpisodio(episodio);
        episodioVistoDB.setHabilitado(true);

        vistoRepository.save(episodioVistoDB);
        return episodioVistoDB;
    }
}
