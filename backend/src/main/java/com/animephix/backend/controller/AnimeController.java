package com.animephix.backend.controller;

import com.animephix.backend.dto.*;
import com.animephix.backend.model.Anime;
import com.animephix.backend.service.AnimeService;
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
@RequestMapping("/animes")
@CrossOrigin("http://localhost:4200/")
public class AnimeController {
    private AnimeService animeService;

    @Autowired
    public AnimeController (AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping("/en-emision")
    public ResponseEntity<List<NombreAnimeDTO>> getAnimesEnEmision() {
        List<NombreAnimeDTO> animesRecientes = animeService.listarAnimesRecientes();
        return ResponseEntity.ok(animesRecientes);
    }

    @GetMapping("/tarjeta-directorio")
    public ResponseEntity<List<TarjetaAnimeDTO>> getTarjetaAnimes() {
        List<TarjetaAnimeDTO> datosAnimes = animeService.enviarDatosAnimesAlDirectorio();
        return ResponseEntity.ok(datosAnimes);
    }

    @GetMapping("/filtros-directorio")
    public ResponseEntity<List<FiltrosDirectorioDTO>> getFiltrosAnimes() {
        List<FiltrosDirectorioDTO> filtros = animeService.enviarDatosFiltros();
        return ResponseEntity.ok(filtros);
    }

    @GetMapping("/establecer-filtros")
    public ResponseEntity<List<TarjetaAnimeDTO>> gestionarFiltros (@RequestParam (required = false) String genero,
                                                                   @RequestParam (required = false) String anio,
                                                                   @RequestParam (required = false) String estado,
                                                                   @RequestParam (required = false) String orden) {
        List<TarjetaAnimeDTO> animes = animeService.gestionarFiltros(genero, anio, estado, orden);
        return ResponseEntity.ok(animes);
    }

    @GetMapping("/buscar-anime")
    public ResponseEntity<List<Anime>> buscarAnimePorNombre(@RequestParam String nombre) {
        List<Anime> animes = animeService.buscarEnBarraBusqueda(nombre);
        return ResponseEntity.ok(animes);
    }

    @GetMapping("/anime-datos")
    public ResponseEntity<?> devolverDatosAnime(@RequestParam String nombre) {
        try {
            PaginaAnimeDTO anime = animeService.devolverDatosAnime(nombre);
            return ResponseEntity.ok(anime);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listar-todos")
    public ResponseEntity<?> getAll() {
        try {
            List<Anime> animes = animeService.listarTodos();
            return ResponseEntity.ok(animes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody NuevoAnimeDTO anime) {
        try {
            animeService.crear(anime);
            return ResponseEntity.ok("Anime creado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizar(@RequestBody ActualizarAnimeDTO anime, @RequestParam Long idAnime) {
        try {
            animeService.actualizar(anime, idAnime);
            return ResponseEntity.ok("Anime actualizado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/cambiar-visibilidad")
    public ResponseEntity<?> cambiarVisibilidad(@RequestParam Long idAnime) {
        try {
            animeService.cambiarVisibilidadAnime(idAnime);
            return ResponseEntity.ok("Se ha cambiado la visibilidad del anime.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarAnime(@RequestParam Long idAnime) {
        try {
            animeService.eliminarPorId(idAnime);
            return ResponseEntity.ok("Se ha eliminado el anime.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
