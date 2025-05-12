package com.animephix.backend.service;

import com.animephix.backend.dto.*;
import com.animephix.backend.model.Anime;
import com.animephix.backend.model.Episodio;
import com.animephix.backend.model.Estado;
import com.animephix.backend.model.Genero;
import com.animephix.backend.repository.AnimeRepository;
import com.animephix.backend.repository.EpisodioRepository;
import com.animephix.backend.repository.EstadoRepository;
import com.animephix.backend.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnimeService {
    private AnimeRepository animeRepository;
    private EpisodioRepository episodioRepository;
    private GeneroRepository generoRepository;
    private EstadoRepository estadoRepository;

    @Autowired
    public AnimeService (AnimeRepository animeRepository,
                         EpisodioRepository episodioRepository,
                         GeneroRepository generoRepository,
                         EstadoRepository estadoRepository) {
        this.animeRepository = animeRepository;
        this.episodioRepository = episodioRepository;
        this.generoRepository = generoRepository;
        this.estadoRepository = estadoRepository;
    }

    @Transactional(readOnly = true)
    public List<NombreAnimeDTO> listarAnimesRecientes() {
        return animeRepository.buscarNombresAnimesEnEmision().stream()
                .map(this::mapToNombreAnimeDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TarjetaAnimeDTO> enviarDatosAnimesAlDirectorio() {
        return mapToTarjetaAnimeDTOList(animeRepository.findByVisibleTrue());
    }

    @Transactional(readOnly = true)
    public List<FiltrosDirectorioDTO> enviarDatosFiltros() {
        return animeRepository.findAll().stream()
                .map(this::mapToFiltroDirectorioDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TarjetaAnimeDTO> gestionarFiltros(String genero, String anio, String estado, String orden) {
        //Paso el año a número solo si tiene valor
        Integer anioNum = (anio != null && !anio.isBlank()) ? parseAnio(anio) : null;

        List<Anime> listadoFiltrado = obtenerListadoFiltrado(genero, anioNum, estado);

        //Devuelvo el resultado en función del orden pasado como parámetro
        return (orden != null && !orden.equals("Por defecto")) ? ordenarAnimes(listadoFiltrado, orden)
                : mapToTarjetaAnimeDTOList(listadoFiltrado);
    }

    @Transactional(readOnly = true)
    public List<Anime> buscarEnBarraBusqueda(String nombre) {
        return animeRepository.findByNombreContainingIgnoreCaseAndVisibleTrue(nombre);
    }

    @Transactional(readOnly = true)
    public PaginaAnimeDTO devolverDatosAnime(String nombre) {
        Anime anime = animeRepository.findByNombreAndVisibleTrue(nombre)
                .orElseThrow(() -> new RuntimeException("Anime no encontrado"));
        List<Episodio> episodios = episodioRepository.buscarEpisodiosPorAnime(anime.getIdAnime());

        //Ordeno los episodios por los más recientes
        episodios.sort(Comparator.comparing(Episodio::getFechaLanzamiento).reversed());

        //Generación del listado con el nombre y el episodio
        List<ListadoEpisodioResumenDTO> listadoEpisodios = new ArrayList<>();
        for (Episodio episodio: episodios) {
            listadoEpisodios.add(
                    ListadoEpisodioResumenDTO.builder()
                            .anime(anime.getNombre())
                            .numEpisodio(episodio.getNumEpisodio())
                            .build()
            );
        }

        return mapToPaginaAnimeDTO(anime, listadoEpisodios);
    }

    @Transactional(readOnly = true)
    public List<Anime> listarTodos() {
        return animeRepository.findAll();
    }

    @Transactional
    public void crear(NuevoAnimeDTO nuevoAnime) {
        Anime anime = new Anime();
        anime.setNombre(nuevoAnime.getNombre());
        anime.setDescripcion(nuevoAnime.getDescripcion());
        anime.setFechaInicio(nuevoAnime.getFechaCreacion());

        // Como la fecha fin es opcional, compruebo si está presente y si es posterior a la fecha de inicio
        if (Objects.nonNull(nuevoAnime.getFechaFin()) && (nuevoAnime.getFechaFin().isAfter(anime.getFechaInicio()))) {
            anime.setFechaFin(nuevoAnime.getFechaFin());
        }
        // En caso de que la fecha de finalización sea posterior a la fecha de inicio, muestro un error
        if (Objects.nonNull(nuevoAnime.getFechaFin()) && nuevoAnime.getFechaFin().isBefore(anime.getFechaInicio())) {
            throw new RuntimeException("La fecha de finalización tiene que ser posterior a la de inicio.");
        }

        // Compruebo que el dia de la semana tiene un valor correcto y sino muestro un error
        if (nuevoAnime.getDiaSemana() >= 0 && nuevoAnime.getDiaSemana() <= 6) {
            anime.setDiaSemana(nuevoAnime.getDiaSemana());
        } else {
            throw new RuntimeException("El valor tiene que estar en 0 - Lunes y 6 - Domingo.");
        }

        anime.setVisible(nuevoAnime.isVisible());
        anime.setUrlImagen(nuevoAnime.getUrlImagen());

        Genero genero = generoRepository.findByNombre(nuevoAnime.getGenero())
                .orElseThrow(() -> new RuntimeException("Género no encontrado."));

        Estado estado = estadoRepository.findByNombre(nuevoAnime.getEstado())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado."));

        anime.setGenero(genero);
        anime.setEstado(estado);

        animeRepository.save(anime);
    }

    @Transactional
    public void actualizar(ActualizarAnimeDTO anime, Long id_anime) {
        Anime animeDB = animeRepository.findById(id_anime)
                .orElseThrow(() -> new RuntimeException("Anime no encontrado."));

        if (Objects.nonNull(anime.getNombre()) && !"".equalsIgnoreCase(anime.getNombre())) {
            animeDB.setNombre(anime.getNombre());
        }

        if (Objects.nonNull(anime.getDescripcion()) && !"".equalsIgnoreCase(anime.getDescripcion())) {
            animeDB.setDescripcion(anime.getDescripcion());
        }

        // Compruebo si está presente y si es posterior a la fecha de inicio
        if (Objects.nonNull(anime.getFechaFin()) && (anime.getFechaFin().isAfter(animeDB.getFechaInicio()))) {
            animeDB.setFechaFin(anime.getFechaFin());
        }
        // En caso de que la fecha de finalización sea posterior a la fecha de inicio, muestro un error
        if (Objects.nonNull(anime.getFechaFin()) && anime.getFechaFin().isBefore(animeDB.getFechaInicio())) {
            throw new RuntimeException("La fecha de finalización tiene que ser posterior a la de inicio.");
        }

        // Compruebo que el dia de la semana tiene un valor correcto y sino muestro un error
        if (anime.getDiaSemana() >= 0 && anime.getDiaSemana() <= 6) {
            animeDB.setDiaSemana(anime.getDiaSemana());
        } else {
            throw new RuntimeException("El valor tiene que estar en 0 - Lunes y 6 - Domingo.");
        }

        Genero genero = generoRepository.findByNombre(anime.getGenero())
                .orElseThrow(() -> new RuntimeException("Genero no encontrado."));

        Estado estado = estadoRepository.findByNombre(anime.getEstado())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado."));

        animeDB.setGenero(genero);
        animeDB.setEstado(estado);

        animeRepository.save(animeDB);
    }

    @Transactional
    public void cambiarVisibilidadAnime (Long idAnime) {
        Anime anime = animeRepository.findById(idAnime)
                .orElseThrow(() -> new RuntimeException("Anime no encontrado."));
        anime.setVisible(!anime.isVisible());
        animeRepository.save(anime);
    }

    @Transactional
    public void eliminarPorId(Long idAnime) {
        if (!animeRepository.existsById(idAnime)) {
            throw new RuntimeException("Anime no encontrado.");
        }
        animeRepository.deleteById(idAnime);
    }

    //Métodos privados auxiliares
    private NombreAnimeDTO mapToNombreAnimeDTO(String nombre) {
        return NombreAnimeDTO.builder()
                .nombre(nombre)
                .build();
    }

    private TarjetaAnimeDTO mapToTarjetaAnimeDTO(Anime anime) {
        return TarjetaAnimeDTO.builder()
                .nombre(anime.getNombre())
                .urlImagen(anime.getUrlImagen())
                .build();
    }

    private FiltrosDirectorioDTO mapToFiltroDirectorioDTO(Anime anime) {
        return FiltrosDirectorioDTO.builder()
                .genero(anime.getGenero().getNombre())
                .anio(anime.getFechaInicio().getYear())
                .estado(anime.getEstado().getNombre())
                .fechaInicio(anime.getFechaInicio())
                .build();
    }

    private List<TarjetaAnimeDTO> mapToTarjetaAnimeDTOList(List<Anime> listado) {
        return listado.stream()
                .map(this::mapToTarjetaAnimeDTO)
                .collect(Collectors.toList());
    }

    private PaginaAnimeDTO mapToPaginaAnimeDTO(Anime anime, List<ListadoEpisodioResumenDTO> listado) {
        return PaginaAnimeDTO.builder()
                .nombre(anime.getNombre())
                .descripcion(anime.getDescripcion())
                .urlImagen(anime.getUrlImagen())
                .genero(anime.getGenero().getNombre())
                .estado(anime.getEstado().getNombre())
                .listadoEpisodios(listado)
                .build();
    }

    //Metodo privado para implementar el orden pasado como filtro
    private List<TarjetaAnimeDTO> ordenarAnimes(List<Anime> animes, String tipoOrden) {
        switch (tipoOrden) {
            case "Ascendente":
                animes.sort(Comparator.comparing(Anime::getNombre));
                break;
            case "Descendente":
                animes.sort(Comparator.comparing(Anime::getNombre).reversed());
                break;
            case "Añadidos recientemente":
                animes.sort(Comparator.comparing(Anime::getFechaInicio).reversed());
                break;
            default:
                break; // Orden predeterminado (sin ordenar)
        }
        return mapToTarjetaAnimeDTOList(animes);
    }

    private Integer parseAnio(String anio) {
        try {
            return Integer.parseInt(anio);
        } catch (NumberFormatException e) {
            return null; // Ignora si no es válido
        }
    }
    private List<Anime> obtenerListadoFiltrado(String genero, Integer anioNum, String estado) {
        if (genero != null && !genero.isBlank()) {
            if (anioNum != null && estado != null && !estado.isBlank()) {
                return animeRepository.buscarByGeneroAndAnioAndEstado(genero, anioNum, estado);
            } else if (anioNum != null) {
                return animeRepository.buscarByGeneroAndAnio(genero, anioNum);
            } else if (estado != null && !estado.isBlank()) {
                return animeRepository.findByGeneroNombreAndEstadoNombreAndVisibleTrue(genero, estado);
            } else {
                return animeRepository.findByGeneroNombreAndVisibleTrue(genero);
            }
        } else if (anioNum != null && estado != null && !estado.isBlank()) {
            return animeRepository.buscarByAnioAndEstado(anioNum, estado);
        } else if (anioNum != null) {
            return animeRepository.buscarByAnio(anioNum);
        } else if (estado != null && !estado.isBlank()) {
            return animeRepository.findByEstadoNombreAndVisibleTrue(estado);
        } else {
            return animeRepository.findAll(); // Sin filtros
        }
    }
}
