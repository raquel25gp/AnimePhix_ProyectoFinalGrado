package com.animephix.backend.controller;

import com.animephix.backend.dto.AnimeFavoritoDTO;
import com.animephix.backend.dto.ListadoFavoritosDTO;
import com.animephix.backend.service.FavoritoService;
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
@RequestMapping("/favoritos")
public class FavoritoController {
    private FavoritoService favoritoService;

    @Autowired
    public FavoritoController (FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/agregar")
    public ResponseEntity<?> agregarFavorito(@RequestBody AnimeFavoritoDTO datos) {
        try {
            favoritoService.agregarFavorito(datos);
            return ResponseEntity.ok("Anime agregado a tu lista.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/mostrar")
    public ResponseEntity<?> mostrarTodos(@RequestParam String email) {
        try {
            List<ListadoFavoritosDTO> listado = favoritoService.listarPorUsuario(email);
            return ResponseEntity.ok(listado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarFavorito(@RequestBody AnimeFavoritoDTO datos) {
        try {
            favoritoService.eliminarFavorito(datos);
            return ResponseEntity.ok("Anime eliminado de la lista.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
