package com.animephix.backend.service;

import com.animephix.backend.dto.NuevoReporteDTO;
import com.animephix.backend.dto.ListadoReportesDTO;
import com.animephix.backend.model.*;
import com.animephix.backend.repository.ReporteRepository;
import com.animephix.backend.repository.TipoProblemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {
    private ReporteRepository reporteRepository;
    private TipoProblemaRepository tipoProblemaRepository;
    private EmailService emailService;

    @Autowired
    public ReporteService(ReporteRepository reporteRepository,
                          TipoProblemaRepository tipoProblemaRepository,
                          EmailService emailService) {
        this.reporteRepository = reporteRepository;
        this.tipoProblemaRepository = tipoProblemaRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void crearReporte(NuevoReporteDTO nuevoReporte) {
        TipoProblema tipoProblema = tipoProblemaRepository.findByNombre(nuevoReporte.getTipoProblema())
                .orElseThrow(() -> new RuntimeException("El tipo de problema indicado no existe en la base de datos."));

        Reporte reporte = new Reporte();
        reporte.setDescripcion(nuevoReporte.getDescripcion());
        reporte.setCorregido(false);
        reporte.setEmail(nuevoReporte.getEmail());
        reporte.setTipoProblema(tipoProblema);

        reporteRepository.save(reporte);

        //Envio del mensaje con el reporte
        String subject = "Reporte Nº" + reporte.getId();
        String body = """
                Hola,

                Gracias por habernos informado sobre el problema en AnimePhix.com. 😊

                Hemos recibido tu reporte y nuestro equipo está trabajando para solucionarlo lo más rápido posible. Apreciamos mucho tu ayuda para mantener la calidad de nuestra plataforma.

                Si tienes más detalles o información que crees que pueda ser útil, no dudes en enviarnos otro mensaje.

                ¡Muchas gracias por tu aportación!

                El equipo de AnimePhix.
                """;
        emailService.enviarCorreo(reporte.getEmail(), subject, body);
    }

    @Transactional(readOnly = true)
    public List<ListadoReportesDTO> listarTodos() {
        List<Reporte> reportes = reporteRepository.findAllByOrderByIdDesc();
        return reportes.stream()
                .map(this::mapToRequestDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cambiarEstadoCorregido(Long idReporte) {
        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        reporte.setCorregido(!reporte.isCorregido());
        reporteRepository.save(reporte);

        // Cuando el reporte se ha corregido se le notifica al usuario
        String subject = "Reporte Nº" + reporte.getId() + " solucionado";
        String body = """
                Hola,

                ¡Queríamos informarte que el problema que nos reportaste en AnimePhix.com ya ha sido solucionado! ✅

                Gracias por tomarte el tiempo de avisarnos. Tu colaboración nos ayuda a mejorar la plataforma cada día.

                Si detectas cualquier otro error o tienes sugerencias, estaremos encantados de escucharte.

                ¡Gracias por ser parte de AnimePhix! 🧡

                El equipo de AnimePhix.
                """;
        emailService.enviarCorreo(reporte.getEmail(), subject, body);
    }

    // Metodo privado auxiliar
    private ListadoReportesDTO mapToRequestDTO(Reporte reporte) {
        return ListadoReportesDTO.builder()
                .idReporte(reporte.getId())
                .descripcion(reporte.getDescripcion())
                .corregido(reporte.isCorregido())
                .tipoProblema(reporte.getTipoProblema().getNombre())
                .emailUsuario(reporte.getEmail())
                .build();
    }
}
