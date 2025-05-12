package com.animephix.backend.repository;

import com.animephix.backend.model.VistoEpisodioUsuario;
import com.animephix.backend.model.compositePK.VistoEpisodioUsuarioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VistoRepository extends JpaRepository<VistoEpisodioUsuario, VistoEpisodioUsuarioId> {
}
