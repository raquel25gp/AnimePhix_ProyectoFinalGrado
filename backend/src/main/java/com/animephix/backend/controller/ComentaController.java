package com.animephix.backend.controller;

import com.animephix.backend.dto.ComentarioUsuarioDTO;
import com.animephix.backend.dto.ListadoEpisodioResumenDTO;
import com.animephix.backend.dto.ListadoComentariosDTO;
import com.animephix.backend.model.ComentarioEpisodio;
import com.animephix.backend.service.ComentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", originPatterns = "*")
@EnableWebSecurity
@EnableMethodSecurity
@RestController
@RequestMapping("/comentarios")
public class ComentaController {
    private ComentaService comentaService;

    @Autowired
    public ComentaController (ComentaService comentaService) {
        this.comentaService = comentaService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/agregar")
    public ResponseEntity<?> agregarComentario(@RequestBody ComentarioUsuarioDTO nuevoComentario) {
        try {
            comentaService.agregarComentario(nuevoComentario);
            return ResponseEntity.ok("Comentario agregado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/listar")
    public ResponseEntity<?> listarComentarios(@RequestBody ListadoEpisodioResumenDTO episodio) {
        try {
            List<ListadoComentariosDTO> comentarios = comentaService.mostrarComentariosPorEpisodio(episodio);
            return ResponseEntity.ok(comentarios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/cambiar-visibilidad")
    public ResponseEntity<?> cambiarVisibilidadComentario(@RequestBody ListadoComentariosDTO comentario,
                                               @RequestParam String nombreAnime,
                                               @RequestParam Long numEpisodio) {
        try {
            comentaService.cambiarVisibilidadComentario(comentario, nombreAnime, numEpisodio);
            return ResponseEntity.ok("Visibilidad del comentario cambiada correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listar-ocultos")
    public ResponseEntity<?> listarComentariosOcultos() {
        try {
            List<ComentarioEpisodio> ocultos = comentaService.listarComentariosOcultos();
            return ResponseEntity.ok(ocultos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
