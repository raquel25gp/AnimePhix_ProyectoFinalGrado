package com.animephix.backend.service;

import com.animephix.backend.dto.AnimeFavoritoDTO;
import com.animephix.backend.dto.ListadoFavoritosDTO;
import com.animephix.backend.model.Anime;
import com.animephix.backend.model.FavoritoAnimeUsuario;
import com.animephix.backend.model.Notificacion;
import com.animephix.backend.model.Usuario;
import com.animephix.backend.model.compositePK.FavoritoAnimeUsuarioId;
import com.animephix.backend.repository.AnimeRepository;
import com.animephix.backend.repository.FavoritoRepository;
import com.animephix.backend.repository.NotificacionRepository;
import com.animephix.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoritoService {
    private FavoritoRepository favoritoRepository;
    private UsuarioRepository usuarioRepository;
    private AnimeRepository animeRepository;
    private NotificacionRepository notificacionRepository;

    @Autowired
    public FavoritoService (FavoritoRepository favoritoRepository,
                            UsuarioRepository usuarioRepository,
                            AnimeRepository animeRepository,
                            NotificacionRepository notificacionRepository) {
        this.favoritoRepository = favoritoRepository;
        this.usuarioRepository = usuarioRepository;
        this.animeRepository = animeRepository;
        this.notificacionRepository = notificacionRepository;
    }

    @Transactional
    public void agregarFavorito(AnimeFavoritoDTO datos) {
        Usuario usuario = usuarioRepository.findByEmail(datos.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        Anime anime = animeRepository.findByNombreAndVisibleTrue(datos.getNombreAnime())
                .orElseThrow(() -> new RuntimeException("Anime no encontrado."));

        FavoritoAnimeUsuarioId favoritoId = new FavoritoAnimeUsuarioId(usuario.getIdUsuario(), anime.getIdAnime());

        // Guardar favorito si no existe ya
        if (!favoritoRepository.existsById(favoritoId)) {
            FavoritoAnimeUsuario favorito = new FavoritoAnimeUsuario();
            favorito.setId(favoritoId);
            favorito.setHabilitado(true);
            favoritoRepository.save(favorito);
        }

        // Crear notificación si aplica y no existe
        if ("En emisión".equals(anime.getEstado().getNombre()) &&
                notificacionRepository.buscarPorUsuarioYAnime(usuario.getIdUsuario(), anime.getIdAnime()).isEmpty()) {

            Notificacion nuevaNotificacion = new Notificacion();
            nuevaNotificacion.setTexto("Nuevo capítulo de " + anime.getNombre());
            nuevaNotificacion.setTipo(Notificacion.TipoNotificacion.Anime);
            nuevaNotificacion.setFechaInicio(anime.getFechaInicio());
            nuevaNotificacion.setFechaFin(anime.getFechaFin());
            nuevaNotificacion.setDiaSemana(anime.getDiaSemana());
            nuevaNotificacion.setUsuario(usuario);
            nuevaNotificacion.setAnime(anime);

            notificacionRepository.save(nuevaNotificacion);
        }
    }

    @Transactional(readOnly = true)
    public List<ListadoFavoritosDTO> listarPorUsuario(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        List<FavoritoAnimeUsuario> favoritos = favoritoRepository.buscarFavoritosPorUsuario(usuario.getIdUsuario());
        List<ListadoFavoritosDTO> listadoFinal = new ArrayList<>();

        if (favoritos.isEmpty()) {
            throw new RuntimeException("¡Todavía no has agregado animes como favoritos!");
        }
        for (FavoritoAnimeUsuario fav : favoritos) {
            String nombreAnime = fav.getAnime().getNombre();
            String urlImagen = fav.getAnime().getUrlImagen();
            listadoFinal.add(favoritosMapToRequestDTO(nombreAnime, urlImagen));
        }
        return listadoFinal;
    }

    @Transactional
    public void eliminarFavorito(AnimeFavoritoDTO datos) {
        Usuario usuario = usuarioRepository.findByEmail(datos.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        Anime anime = animeRepository.findByNombreAndVisibleTrue(datos.getNombreAnime())
                .orElseThrow(() -> new RuntimeException("Anime no encontrado."));

        FavoritoAnimeUsuarioId favoritoId = new FavoritoAnimeUsuarioId(usuario.getIdUsuario(), anime.getIdAnime());

        favoritoRepository.deleteById(favoritoId);
        notificacionRepository.eliminarPorAnimeYUsuario(anime.getIdAnime(), usuario.getIdUsuario());
    }

    //Metodo privado auxiliar
    private ListadoFavoritosDTO favoritosMapToRequestDTO(String nombre, String urlImagen) {
        return ListadoFavoritosDTO.builder()
                .nombre(nombre)
                .urlImagen(urlImagen)
                .build();
    }
}
