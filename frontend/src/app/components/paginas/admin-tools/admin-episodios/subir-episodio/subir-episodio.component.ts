import { Component } from '@angular/core';
import { EpisodioService } from '../../../../../services/episodio.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-subir-episodio',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './subir-episodio.component.html',
  styleUrl: './subir-episodio.component.css'
})
export class SubirEpisodioComponent {
  listadoAnime: String[] = [];
  nombreAnime: string = '';
  fechaLanzamiento = new Date;
  urlVideo = '/Videos/Nombre-Carpeta-Anime/0000_0.mp4';
  urlPoster = '/Imagenes/Poster/Nombre-Anime.jpg';
  mensaje: string = '';
  mensajeError: boolean = false;

  constructor(private episodioService: EpisodioService, private router: Router) {
    // Obtengo la navegación actual para recuperar los datos enviados anteriormente
    const navigation = this.router.getCurrentNavigation();
    // Extraigo los datos enviados
    const state = navigation?.extras.state as { animes: String[] };
    // Guardo los datos en el array
    this.listadoAnime = state?.animes || [];
  }

  // Método del botón que permite crear un episodio en la BBDD
  crear() {
    const datos = {
      nombreAnime: this.nombreAnime,
      fechaLanzamiento: this.fechaLanzamiento,
      urlVideo: this.urlVideo,
      urlPoster: this.urlPoster
    }
    this.episodioService.crearEpisodio(datos).subscribe({
      next: (response) => {
        // Se muestra un mensaje al usuario
        this.mensaje = response;
        this.mensajeError = false;
      },
      error: (err) => {
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
