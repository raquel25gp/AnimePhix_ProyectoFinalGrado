package com.animephix.backend.repository;

import com.animephix.backend.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    // Buscar todos reportes ordenanos por su id de forma descendente, es decir, los mas recientes
    List<Reporte> findAllByOrderByIdDesc();
}
