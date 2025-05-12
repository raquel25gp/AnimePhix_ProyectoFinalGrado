package com.animephix.backend.controller;

import com.animephix.backend.dto.NotificacionCalendarioDTO;
import com.animephix.backend.dto.NotificacionPersonalizadaDTO;
import com.animephix.backend.service.NotificacionService;
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
@RequestMapping("/notificaciones")
public class NotificacionController {
    private NotificacionService notificacionService;

    @Autowired
    public NotificacionController (NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/crear")
    public ResponseEntity<?> crearNotificacionPersonalizada(@RequestBody NotificacionPersonalizadaDTO notificacion) {
        try {
            notificacionService.crearNotificacionPersonalizada(notificacion);
            return ResponseEntity.ok("Notificacion creada con exito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/listar")
    public ResponseEntity<?> mandarNotificaciones(@RequestParam String email) {
        try {
            List<NotificacionCalendarioDTO> notificaciones = notificacionService.enviarNotificaciones(email);
            return ResponseEntity.ok(notificaciones);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarNotificacion(@RequestBody NotificacionPersonalizadaDTO datos) {
        try {
            notificacionService.eliminarNotificacion(datos);
            return ResponseEntity.ok("Notificacion eliminada.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
