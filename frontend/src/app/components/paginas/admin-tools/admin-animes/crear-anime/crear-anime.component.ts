import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AnimeService } from '../../../../../services/anime.service';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-crear-anime',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './crear-anime.component.html',
  styleUrl: './crear-anime.component.css'
})
export class CrearAnimeComponent {
  listadoGenero: String[] = [];
  listadoEstado: String[] = [];
  nombre: string = '';
  descripcion: string = '';
  fechaCreacion = new Date;
  fechaFin = new Date;
  diaSemana: number = 0;
  visible: boolean = true;
  urlImagen: string = '';
  genero: string = '';
  estado: string = '';
  mensaje: string = '';
  mensajeError: boolean = false;

  constructor(private animeService: AnimeService, private router: Router) {
    // Obtengo la navegación actual para recuperar los datos enviados anteriormente
    const navegacion = this.router.getCurrentNavigation();
    // Extraigo los datos enviados
    const state = navegacion?.extras.state as { generos: String[]; estados: String[] };
    // Guardo los datos en los arrays
    this.listadoGenero = state?.generos || [];
    this.listadoEstado = state?.estados || [];
  }

  // Método del botón que permite crear un anime en la BBDD
  crear() {
    const datos = {
      nombre: this.nombre,
      descripcion: this.descripcion,
      fechaCreacion: this.fechaCreacion,
      fechaFin: this.fechaFin,
      diaSemana: this.diaSemana,
      visible: this.visible,
      urlImagen: `/Imagenes/Portadas/${this.urlImagen}`,
      genero: this.genero,
      estado: this.estado
    }
    this.animeService.crearAnime(datos).subscribe({
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
