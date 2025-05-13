import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { EpisodioService } from '../../../../../services/episodio.service';

@Component({
  selector: 'app-listar-episodios',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './listar-episodios.component.html',
  styleUrl: './listar-episodios.component.css'
})
export class ListarEpisodiosComponent {
  @Input() listado: any[] = [];
  @Input() padreIndex: number = 0;
  @Output() notificarMensaje = new EventEmitter<string>();
  @Output() notificarEstadoMensaje = new EventEmitter<boolean>();
  mensaje: string = '';
  mensajeError: boolean = false;

  constructor(private episodioService: EpisodioService) { }

  // Actualizar los datos del anime
  actualizar(episodio: any) {
    const datosEpisodio = {
      urlVideo: episodio.urlVideo,
      fechaLanzamiento: episodio.fechaLanzamiento,
      urlPoster: episodio.urlPoster
    }

    this.episodioService.actualizarEpisodio(datosEpisodio, episodio.anime.idAnime, episodio.numEpisodio).subscribe({
      next: (response) => {
        this.notificarEstadoMensaje.emit(false);
        this.notificarMensaje.emit(response);
        // Espera 1 segundo y luego recarga la página para visualizar los cambios
        setTimeout(() => {
          window.location.reload();
        }, 1000);
      },
      error: (err) => {
        // En caso de error se muestra un mensaje
        if (typeof err.error === 'string') {
          this.notificarMensaje.emit(err.error); // Mensaje de error simple
        } else {
          this.notificarMensaje.emit("¡Oh no! Algo ha salido mal.");
        }
        this.notificarEstadoMensaje.emit(true);
      }
    });
  }
}