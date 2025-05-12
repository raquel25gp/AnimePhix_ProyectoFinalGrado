package com.animephix.backend.repository;

import com.animephix.backend.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    @Query("Select a.nombre from Anime a WHERE a.estado.idEstado=1 AND a.visible = true order by a.fechaInicio")
    List<String> buscarNombresAnimesEnEmision();

    // Buscar todos animes visibles
    List<Anime> findByVisibleTrue();

    // Filtros del directorio
    List<Anime> findByGeneroNombreAndVisibleTrue (String genero);

    @Query("Select a FROM Anime a WHERE YEAR(a.fechaInicio) = ?1 AND a.visible = true")
    List<Anime> buscarByAnio(int anio);

    List<Anime> findByEstadoNombreAndVisibleTrue (String estado);

    @Query("Select a FROM Anime a WHERE a.genero.nombre = ?1 AND YEAR(a.fechaInicio) = ?2 AND a.visible = true")
    List<Anime> buscarByGeneroAndAnio(String genero, int anio);

    List<Anime> findByGeneroNombreAndEstadoNombreAndVisibleTrue (String genero, String estado);

    @Query("Select a FROM Anime a WHERE YEAR(a.fechaInicio) = ?1 AND a.estado.nombre = ?2 AND a.visible = true")
    List<Anime> buscarByAnioAndEstado(int anio, String estado);

    @Query("Select a FROM Anime a WHERE a.genero.nombre = ?1 AND YEAR(a.fechaInicio) = ?2 AND a.estado.nombre = ?3 AND a.visible = true")
    List<Anime> buscarByGeneroAndAnioAndEstado(String genero, int anio, String estado);

    // Buscar anime visible por su nombre ignorando mayusculas en la barra de búsqueda
    List<Anime> findByNombreContainingIgnoreCaseAndVisibleTrue (String nombre);

    // Buscar un anime visible por su nombre completo
    Optional<Anime> findByNombreAndVisibleTrue (String nombre);

    // Buscar un anime por su nombre
    Optional<Anime> findByNombre(String nombre);

}
