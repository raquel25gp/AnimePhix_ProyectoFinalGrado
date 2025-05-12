package com.animephix.backend.controller;

import com.animephix.backend.dto.NuevoReporteDTO;
import com.animephix.backend.dto.ListadoReportesDTO;
import com.animephix.backend.service.ReporteService;
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
@RequestMapping("/reportes")
public class ReporteController {
    private ReporteService reporteService;

    @Autowired
    public ReporteController (ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearReporte(@RequestBody NuevoReporteDTO nuevoReporte) {
        try {
            reporteService.crearReporte(nuevoReporte);
            return ResponseEntity.ok("Muchas gracias por notificar este problema. ¡Nos pondremos lo mas pronto posible con ello!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listar")
    public ResponseEntity<?> listarProblemas () {
        try {
            List<ListadoReportesDTO> listado = reporteService.listarTodos();
            return ResponseEntity.ok(listado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/actualizar-estado")
    public ResponseEntity<?> cambiarEstadoCorregido (@RequestParam Long idReporte) {
        try {
            reporteService.cambiarEstadoCorregido(idReporte);
            return ResponseEntity.ok("Se ha cambiado el estado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
