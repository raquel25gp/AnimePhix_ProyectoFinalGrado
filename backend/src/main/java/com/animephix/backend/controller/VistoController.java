package com.animephix.backend.controller;

import com.animephix.backend.dto.EpisodioVistoPorUsuarioDTO;
import com.animephix.backend.service.VistoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", originPatterns = "*")
@EnableWebSecurity
@EnableMethodSecurity
@RestController
@RequestMapping("/episodios-vistos")
public class VistoController {
    private VistoService vistoService;

    @Autowired
    public VistoController (VistoService vistoService) {
        this.vistoService = vistoService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/marcar-desmarcar")
    public ResponseEntity<?> marcarDesmarcarEpisodio(@RequestBody EpisodioVistoPorUsuarioDTO datosInput) {
        try {
            vistoService.cambiarEstadoEpisodioVisto(datosInput);
            return ResponseEntity.ok("Método ejecutado con exito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/comprobar-estado")
    public ResponseEntity<?> devolverEstado(@RequestBody EpisodioVistoPorUsuarioDTO datosInput) {
        try {
            String estado = vistoService.devolverVistoNoVisto(datosInput);
            return ResponseEntity.ok(estado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
