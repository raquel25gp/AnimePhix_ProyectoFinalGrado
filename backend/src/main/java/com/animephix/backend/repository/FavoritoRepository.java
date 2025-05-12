package com.animephix.backend.repository;

import com.animephix.backend.model.FavoritoAnimeUsuario;
import com.animephix.backend.model.compositePK.FavoritoAnimeUsuarioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritoRepository extends JpaRepository<FavoritoAnimeUsuario, FavoritoAnimeUsuarioId> {
    @Query("Select f FROM FavoritoAnimeUsuario f WHERE f.usuario.idUsuario = ?1")
    List<FavoritoAnimeUsuario> buscarFavoritosPorUsuario(Long idUsuario);
}
