import { Component, OnInit } from '@angular/core';
import { ListadoComentarios } from '../../../../interfaces/episodio/listado-comentarios';
import { ComentarioService } from '../../../../services/comentario.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-comentarios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-comentarios.component.html',
  styleUrl: './admin-comentarios.component.css'
})
export class AdminComentariosComponent implements OnInit {
  listadoComentariosOcultos: any[] = [];
  mensaje: string = '';
  mensajeError: boolean = false;

  constructor(private comentarioService: ComentarioService) { }

  ngOnInit(): void {
    this.comentarioService.getAll().subscribe(data => {this.listadoComentariosOcultos = data});
    
  }

  // Cambiar visibilidad comentario
  cambiarVisibilidad(comentario: any) {
    const datosFormateados = {
      nombreUsuario: comentario.usuario.nombre,
      urlImagen: comentario.usuario.urlImagen,
      comentario: comentario.comentario,
      habilitado: comentario.habilitado,
      fechaCreacion: comentario.fechaCreacion
    }
    const nombreAnime = comentario.episodio.anime.nombre;
    const numEpisodio = comentario.episodio.numEpisodio;
    
    this.comentarioService.cambiarVisibilidad(datosFormateados, nombreAnime, numEpisodio).subscribe({
      next: (response) => {
        this.mensaje = response;
        this.mensajeError = false;
        // Espera 1 segundo y luego recarga la página para visualizar los cambios
        setTimeout(() => {
          window.location.reload();
        }, 1000);
      },
      error: err => {
        // En caso de error se muestra un mensaje
        if (typeof err.error === 'string') {
          this.mensaje = err.error; // Mensaje de error del backend
        } else {
          this.mensaje = '¡Oh no! Algo ha salido mal.';
        }
        this.mensajeError = true;
      }
    });
  }
}
