package com.animephix.backend.service;

import com.animephix.backend.dto.NotificacionCalendarioDTO;
import com.animephix.backend.dto.NotificacionPersonalizadaDTO;
import com.animephix.backend.model.Notificacion;
import com.animephix.backend.model.Usuario;
import com.animephix.backend.repository.NotificacionRepository;
import com.animephix.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificacionService {
    private NotificacionRepository notificacionRepository;
    private UsuarioRepository usuarioRepository;

    @Autowired
    public NotificacionService (NotificacionRepository notificacionRepository,
                                UsuarioRepository usuarioRepository) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // El usuario crea una notificación personalizada
    @Transactional
    public void crearNotificacionPersonalizada(NotificacionPersonalizadaDTO datosNotificacion) {
        // Busca al usuario por su email
        Usuario usuario = usuarioRepository.findByEmail(datosNotificacion.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        // Crea y guarda la nueva notificación
        Notificacion nuevaNotificacion = new Notificacion();
        nuevaNotificacion.setTexto(datosNotificacion.getTexto());
        nuevaNotificacion.setTipo(Notificacion.TipoNotificacion.Personalizada);
        nuevaNotificacion.setFechaInicio(datosNotificacion.getFecha());
        nuevaNotificacion.setFechaFin(datosNotificacion.getFecha());
        nuevaNotificacion.setDiaSemana(datosNotificacion.getFecha().getDayOfWeek().getValue());
        nuevaNotificacion.setUsuario(usuario);

        notificacionRepository.save(nuevaNotificacion);
    }

    // Se envian las notificaciones al Frontend
    @Transactional(readOnly = true)
    public List<NotificacionCalendarioDTO> enviarNotificaciones(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        List<Notificacion> notificaciones = notificacionRepository.buscarTodasPorEmail(usuario.getEmail());
        if (notificaciones.isEmpty()) {
            throw new RuntimeException("No hay notificaciones.");
        }

        return notificaciones.stream()
                .map(this::mapToRequestDTO)
                .collect(Collectors.toList());
    }


    // Se eliminan las notificaciones personalizadas
    @Transactional
    public void eliminarNotificacion(NotificacionPersonalizadaDTO datosNotificacion) {
        Usuario usuario = usuarioRepository.findByEmail(datosNotificacion.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        // Busca la notificación asociada al usuario y sus datos
        Notificacion notificacion = notificacionRepository
                .buscarPorUsuarioYTextoYFecha(usuario.getIdUsuario(), datosNotificacion.getTexto(), datosNotificacion.getFecha())
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada."));

        // Si existe, la elimina
        notificacionRepository.deleteById(notificacion.getIdNotificacion());
    }

    // Metodo privado auxiliar
    private NotificacionCalendarioDTO mapToRequestDTO (Notificacion notificacion) {
        return NotificacionCalendarioDTO.builder()
                .texto(notificacion.getTexto())
                .fechaInicio(notificacion.getFechaInicio())
                .fechaFin(notificacion.getFechaFin())
                .diaSemana(notificacion.getDiaSemana())
                .build();
    }
}
