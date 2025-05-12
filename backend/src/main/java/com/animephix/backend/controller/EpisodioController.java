package com.animephix.backend.controller;

import com.animephix.backend.dto.ActualizarEpisodioDTO;
import com.animephix.backend.dto.NuevoEpisodioDTO;
import com.animephix.backend.dto.VideoEpisodioDTO;
import com.animephix.backend.dto.UltimosEpisodiosDTO;
import com.animephix.backend.model.Episodio;
import com.animephix.backend.service.EpisodioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableWebSecurity
@EnableMethodSecurity
@RequestMapping("/episodios")
@CrossOrigin("http://localhost:4200/")
public class EpisodioController {
    private EpisodioService episodioService;

    @Autowired
    public EpisodioController (EpisodioService episodioService) {
        this.episodioService = episodioService;
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<UltimosEpisodiosDTO>> mostrarEpisodiosRecientes() {
        List<UltimosEpisodiosDTO> episodios = episodioService.buscarUltimosEpisodiosPublicados();
        return ResponseEntity.ok(episodios);
    }

    @GetMapping("/especifico")
    public ResponseEntity<VideoEpisodioDTO> devolverEpisodioEspecifico(@RequestParam String nombre,
                                                                       @RequestParam Long numero) {
        VideoEpisodioDTO episodio = episodioService.devolverDatosEpisodio(nombre, numero);
        return ResponseEntity.ok(episodio);
    }

    @GetMapping("/total-anime")
    public ResponseEntity<Long> devolverTotalEpisodiosPorAnime(@RequestParam String nombre) {
        Long total = episodioService.devolverTotalEpisodiosPorAnime(nombre);
        return ResponseEntity.ok(total);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listar-todos")
    public ResponseEntity<?> devolverTodos() {
        try {
            List<Episodio> listado = episodioService.listarTodos();
            return ResponseEntity.ok(listado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<?> actualizarEpisodio(@RequestBody NuevoEpisodioDTO nuevoEpisodio) {
        try {
            episodioService.crear(nuevoEpisodio);
            return ResponseEntity.ok("Episodio creado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarEpisodio(@RequestBody ActualizarEpisodioDTO datosEpisodio,
                                                @RequestParam Long idAnime,
                                                @RequestParam Long numEpisodio) {
        try {
            episodioService.actualizar(datosEpisodio, idAnime, numEpisodio);
            return ResponseEntity.ok("Episodio actualizado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
