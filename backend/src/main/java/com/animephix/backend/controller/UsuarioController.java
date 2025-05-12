package com.animephix.backend.controller;

import com.animephix.backend.dto.ActualizarImagenPerfilDTO;
import com.animephix.backend.dto.ActualizarNombreUsuarioDTO;
import com.animephix.backend.dto.ActualizarPasswordDTO;
import com.animephix.backend.dto.RegistroDTO;
import com.animephix.backend.model.Usuario;
import com.animephix.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200", originPatterns = "*")
@EnableWebSecurity
@EnableMethodSecurity
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private UsuarioService usuarioService;

    @Autowired
    public UsuarioController (UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    //Metodo para validar el controlador
    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody RegistroDTO nuevoUsuario, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            usuarioService.registrar(nuevoUsuario);
            return ResponseEntity.ok("Se ha realizado el registro con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/cargar-datos")
    public ResponseEntity<?> obtenerDatosUsuario(@RequestParam String email) {
        try {
            Usuario datosUsuario = usuarioService.devolverDatosUsuario(email);
            return ResponseEntity.ok(datosUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/solicitar-password-olvidada")
    public ResponseEntity<?> solicitarPassword(@RequestBody String email) {
        try {
            usuarioService.solicitarPassword(email);
            return ResponseEntity.ok("Correo enviado con la nueva contraseña. Puede tardar unos minutos.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/actualizar-password")
    public ResponseEntity<?> actualizarPassword(@RequestBody ActualizarPasswordDTO datos) {
        try {
            usuarioService.modificarPassword(datos);
            return ResponseEntity.ok("Contraseña actualizada correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/actualizar-nombre")
    public ResponseEntity<?> actualizarNombre(@RequestBody ActualizarNombreUsuarioDTO datos) {
        try {
            usuarioService.modificarNombre(datos);
            return ResponseEntity.ok("El nombre de usuario se ha modificado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/actualizar-imagen")
    public ResponseEntity<?> actualizarImagen(@RequestBody ActualizarImagenPerfilDTO datos) {
        try {
            usuarioService.modificarImagen(datos);
            return ResponseEntity.ok("Imagen actualizada correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarUsuario(@RequestParam String email) {
        try {
            usuarioService.eliminarPorEmail(email);
            return ResponseEntity.ok(Collections.singletonMap("mensaje", "Su perfil ha sido eliminado de AnimePhix."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listar")
    public ResponseEntity<?> getAll() {
        try {
            List<Usuario> listado = usuarioService.devolverTodosUsuarios();
            return ResponseEntity.ok(listado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/actualizar-habilitado")
    public ResponseEntity<?> cambiarEstadoHabilitado(@RequestParam Long idUsuario) {
        try {
            usuarioService.cambiarEstadoHabilitado(idUsuario);
            return ResponseEntity.ok("Se ha cambiado la habilitación del usuario.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/modificar-rol")
    public ResponseEntity<?> crearAdministrador(@RequestParam Long idUsuario, @RequestParam String emailAdmin) {
        try {
            usuarioService.habilitarAdminitrador(idUsuario, emailAdmin);
            return ResponseEntity.ok("Se ha creado un nuevo Administrador.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
