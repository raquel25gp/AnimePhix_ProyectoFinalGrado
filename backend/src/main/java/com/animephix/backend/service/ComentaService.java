package com.animephix.backend.service;

import com.animephix.backend.dto.ComentarioUsuarioDTO;
import com.animephix.backend.dto.ListadoEpisodioResumenDTO;
import com.animephix.backend.dto.ListadoComentariosDTO;
import com.animephix.backend.model.Anime;
import com.animephix.backend.model.ComentarioEpisodio;
import com.animephix.backend.model.Episodio;
import com.animephix.backend.model.Usuario;
import com.animephix.backend.model.compositePK.EpisodioId;
import com.animephix.backend.repository.AnimeRepository;
import com.animephix.backend.repository.ComentarioEpisodioRepository;
import com.animephix.backend.repository.EpisodioRepository;
import com.animephix.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ComentaService {
    private ComentarioEpisodioRepository comentaRepository;
    private UsuarioRepository usuarioRepository;
    private AnimeRepository animeRepository;
    private EpisodioRepository episodioRepository;

    @Autowired
    public ComentaService(ComentarioEpisodioRepository comentaRepository,
                          UsuarioRepository usuarioRepository,
                          AnimeRepository animeRepository,
                          EpisodioRepository episodioRepository) {
        this.comentaRepository = comentaRepository;
        this.usuarioRepository = usuarioRepository;
        this.animeRepository = animeRepository;
        this.episodioRepository = episodioRepository;
    }

    @Transactional
    public void agregarComentario(ComentarioUsuarioDTO comentario) {
        Usuario usuario = usuarioRepository.findByEmail(comentario.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        Anime anime = animeRepository.findByNombreAndVisibleTrue(comentario.getNombreAnime())
                .orElseThrow(() -> new RuntimeException("Anime no encontrado."));

        EpisodioId episodioId = new EpisodioId(anime.getIdAnime(), comentario.getNumEpisodio());
        Episodio episodio = episodioRepository.findById(episodioId)
                .orElseThrow(() -> new RuntimeException("Episodio no encontrado."));

        ComentarioEpisodio nuevoComentario = new ComentarioEpisodio();
        nuevoComentario.setComentario(comentario.getComentario());
        nuevoComentario.setHabilitado(true);
        nuevoComentario.setUsuario(usuario);
        nuevoComentario.setEpisodio(episodio);

        comentaRepository.save(nuevoComentario);
    }

    @Transactional(readOnly = true)
    public List<ListadoComentariosDTO> mostrarComentariosPorEpisodio(ListadoEpisodioResumenDTO episodioDTO) {
        Anime anime = animeRepository.findByNombreAndVisibleTrue(episodioDTO.getAnime())
                .orElseThrow(() -> new RuntimeException("Anime no encontrado."));

        EpisodioId episodioId = new EpisodioId(anime.getIdAnime(), episodioDTO.getNumEpisodio());
        Episodio episodio = episodioRepository.findById(episodioId)
                .orElseThrow(() -> new RuntimeException("Episodio no encontrado."));

        List<ComentarioEpisodio> comentarios = comentaRepository.buscarTodosPorAnimeYEpisodio(anime.getIdAnime(), episodio.getNumEpisodio());

        // Ordeno por fecha de creación descendente (más reciente primero)
        comentarios.sort(Comparator.comparing(ComentarioEpisodio::getFechaCreacion).reversed());

        List<ListadoComentariosDTO> resultado = new ArrayList<>();
        for (ComentarioEpisodio comentario : comentarios) {
            if (comentario.isHabilitado()) {
                // Formateo la fecha de creación del comentario a un estilo UserFrinedly "01-01-2025 23:59"
                String fechaFormateada = String.format("%02d-%02d-%04d %02d:%02d",
                        comentario.getFechaCreacion().getDayOfMonth(),
                        comentario.getFechaCreacion().getMonthValue(),
                        comentario.getFechaCreacion().getYear(),
                        comentario.getFechaCreacion().getHour(),
                        comentario.getFechaCreacion().getMinute());

                resultado.add(listadoMapToRequestDTO(comentario, fechaFormateada));
            }
        }


        return resultado;
    }

    @Transactional
    public void cambiarVisibilidadComentario(ListadoComentariosDTO comentarioDTO, String nombreAnime, Long numEpisodio) {
        Usuario usuario = usuarioRepository.findByNombre(comentarioDTO.getNombreUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        Anime anime = animeRepository.findByNombre(nombreAnime)
                .orElseThrow(() -> new RuntimeException("Anime no encontrado."));

        EpisodioId episodioId = new EpisodioId(anime.getIdAnime(), numEpisodio);
        episodioRepository.findById(episodioId)
                .orElseThrow(() -> new RuntimeException("Episodio no encontrado."));

        ComentarioEpisodio comentario = comentaRepository
                .buscarPorTextoYUsuarioYEpisodioYAnime(
                        comentarioDTO.getComentario(),
                        usuario.getIdUsuario(),
                        anime.getIdAnime(),
                        numEpisodio
                ).orElseThrow(() -> new RuntimeException("Comentario no encontrado."));

        comentario.setHabilitado(!comentarioDTO.isHabilitado());
        comentaRepository.save(comentario);
    }

    @Transactional(readOnly = true)
    public List<ComentarioEpisodio> listarComentariosOcultos() {
        List<ComentarioEpisodio> todos = comentaRepository.findAll();

        List<ComentarioEpisodio> ocultos = new ArrayList<>();

        for(ComentarioEpisodio comentario: todos) {
            if (!comentario.isHabilitado()) {
                ocultos.add(comentario);
            }
        }

        ocultos.sort(Comparator.comparing(ComentarioEpisodio::getFechaCreacion).reversed());
        return ocultos;
    }

    //Metodo privado auxiliar
    private ListadoComentariosDTO listadoMapToRequestDTO(ComentarioEpisodio comentario, String fecha) {
        return ListadoComentariosDTO.builder()
                .nombreUsuario(comentario.getUsuario().getNombre())
                .urlImagen(comentario.getUsuario().getUrlImagen())
                .comentario(comentario.getComentario())
                .habilitado(comentario.isHabilitado())
                .fechaCreacion(fecha)
                .build();
    }
}
