import { Component, OnInit } from '@angular/core';
import { AnimeService } from '../../../../services/anime.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FiltroDirectorio } from '../../../../interfaces/anime/filtro-directorio';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-admin-animes',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './admin-animes.component.html',
  styleUrl: './admin-animes.component.css'
})
export class AdminAnimesComponent implements OnInit {
  listadoAnimes: any[] = [];
  nombreAnime: string = '';
  animeBuscado: string = '';
  listadoGenero: String[] = [];
  listadoEstado: String[] = [];
  mensaje: string = '';
  mensajeError: boolean = false;

  //Utilizo ActivatedRoute para recuperar el parametro nombre cuando se realiza una busqueda
  constructor(private animeService: AnimeService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.nombreAnime = params['nombre'] || '';  // Si no hay 'nombre', asigna un valor vacío
      this.cargarTodosLosAnimes();
    });
    this.cargarEstadosGeneros();
  }

  cargarTodosLosAnimes(): void {
    if (this.nombreAnime) {
      //Cuando hay búsqueda, sólo se busca un anime
      this.animeService.getAnimeBuscado(this.nombreAnime).subscribe(data => {
        this.listadoAnimes = Array.isArray(data) ? data : [data];
      });
    } else {
      //Cuando no hay búsqueda, se cargan todos los animes y se usa la paginación
      this.animeService.getTodos().subscribe(data => {
        this.listadoAnimes = data;
      });
    }
  }

  cargarEstadosGeneros(): void {
    this.animeService.getDatosFiltrosDirectorio().subscribe((datos: FiltroDirectorio[]) => {
      // Con new Set elimino duplicados
      this.listadoGenero = [...new Set(datos.map(f => f.genero))];
      this.listadoEstado = [...new Set(datos.map(f => f.estado))];
    });
  }

  // Función para buscar anime
  buscarAnime(): void {
    if (this.animeBuscado) {
      this.router.navigate(['/admin-tools'], {
        relativeTo: this.route,
        queryParams: { nombre: this.animeBuscado }
      });
    }
  }

  // Refrescar pagina
  refrescar(): void {
    this.router.navigate(['/admin-tools'], {
      relativeTo: this.route,
      queryParams: { nombre: '' }
    });
  }

  // Método que dirige a la pestaña de crear anime
  navegarACrearAnime() {
    const generos = this.listadoGenero;
    const estados = this.listadoEstado;

    this.router.navigate(['/crear-anime'], {
      state: { generos, estados }
    });
  }

  // Actualizar los datos del anime
  actualizar(anime: any) {
    const datosAnime = {
      nombre: anime.nombre,
      descripcion: anime.descripcion,
      fechaFin: anime.fechaFin,
      diaSemana: anime.diaSemana,
      genero: anime.genero.nombre,
      estado: anime.estado.nombre
    }

    this.animeService.actualizarAnime(datosAnime, anime.idAnime).subscribe({
      next: (response) => {
        // Se muestra un mensaje al usuario
        this.mensaje = response;
        this.mensajeError = false;
        // Espera 1 segundo y luego recarga la página para visualizar los cambios
        setTimeout(() => {
          window.location.reload();
        }, 1000);
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

  //Ocultar visualización del anime
  cambiarEstadoVisbleAnime(idAnime: number) {
    this.animeService.cambiarEstadoVisual(idAnime).subscribe({
      next: (response) => {
        // Se muestra un mensaje al usuario
        this.mensaje = response;
        this.mensajeError = false;
        // Espera 1 segundo y luego recarga la página para visualizar los cambios
        setTimeout(() => {
          window.location.reload();
        }, 1000);
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

  //Eliminar anime de la base de datos
  eliminarAnime(idAnime: number) {
    Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Desea eliminar el anime seleccionado? Tenga en cuenta que se eliminarán los episodios y todo su contenido relacionado.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.animeService.eliminarAnime(idAnime).subscribe({
          next: (response) => {
            // Se muestra un mensaje al usuario
            this.mensaje = response;
            this.mensajeError = false;
            // Espera 1 segundo y luego recarga la página para visualizar los cambios
            setTimeout(() => {
              window.location.reload();
            }, 1000);
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
    });
  }
}
