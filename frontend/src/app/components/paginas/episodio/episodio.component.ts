import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { VideoComponent } from "./video/video.component";
import { ComentarioComponent } from "./comentario/comentario.component";
import { EpisodioVideo } from '../../../interfaces/episodio/episodio-video';
import { EpisodioService } from '../../../services/episodio.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-episodio',
  standalone: true,
  imports: [RouterModule, VideoComponent, ComentarioComponent, CommonModule],
  templateUrl: './episodio.component.html',
  styleUrl: './episodio.component.css'
})
export class EpisodioComponent {
  nombreAnime: string = '';
  numEpisodio: number = 0;
  datosEpisodio: EpisodioVideo | null = null;
  totalEpisodios: number = 0;

  constructor(private route: ActivatedRoute, private episodioService: EpisodioService, private router: Router) { }

  ngOnInit(): void {
    // Suscribo para escuchar cambios en los parámetros de la URL
    this.route.paramMap.subscribe(params => {
      const nuevoNombre = params.get('nombre') || '';
      const nuevoNumEpisodio = Number(params.get('numero')) || 0;

      // Solo actualizo si hay cambios
      if (nuevoNombre !== this.nombreAnime || nuevoNumEpisodio !== this.numEpisodio) {
        this.nombreAnime = nuevoNombre;
        this.numEpisodio = nuevoNumEpisodio;
        this.cargarEpisodio();
      }
    });
  }

  cargarEpisodio(): void {
    // Reinicia los datos para forzar el *ngIf
    this.datosEpisodio = null;
    // Llamada al servicio para obtener los datos del episodio indicado
    this.episodioService.getEpisodioEspecifico(this.nombreAnime, this.numEpisodio).subscribe({
      next: (data) => {
        // Se guardan los datos obtenidos
        this.datosEpisodio = data;
        this.obtenerTotalEpisodios();
      },
      error: (error) => {
        // En caso de error, llevo al usuario a la página de not found
        this.router.navigate(['/page-not-found']);
      }
    });
  }

  // Método para obtener el número total de episodios según el anime pasado como parámetro
  obtenerTotalEpisodios(): void {
    this.episodioService.getTotalEpisodios(this.nombreAnime).subscribe(total => this.totalEpisodios = total);
  }

  // Lógica para cambiar al episodio anterior
  episodioAnterior(): void {
    if (this.numEpisodio > 1) {
      this.router.navigate(['/anime', this.nombreAnime, 'episodio', this.numEpisodio - 1]);
    }
  }

  // Lógica para cambiar al episodio siguiente
  episodioSiguiente(): void {
    if (this.numEpisodio < this.totalEpisodios) {
      this.router.navigate(['/anime', this.nombreAnime, 'episodio', this.numEpisodio + 1]);
    }
  }
}
