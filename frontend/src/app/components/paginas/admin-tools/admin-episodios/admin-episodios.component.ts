import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AnimeService } from '../../../../services/anime.service';
import { EpisodioService } from '../../../../services/episodio.service';
import { ListarEpisodiosComponent } from "./listar-episodios/listar-episodios.component";

@Component({
  selector: 'app-admin-episodios',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule, ListarEpisodiosComponent],
  templateUrl: './admin-episodios.component.html',
  styleUrl: './admin-episodios.component.css'
})
export class AdminEpisodiosComponent implements OnInit {
  listadoAnimes: any[] = [];
  nombreAnime: string = '';
  animeBuscado: string = '';
  listadoEpisodios: any[] = [];
  episodiosFiltrados: any[] = [];
  mensaje: string = '';
  mensajeError: boolean = false;
  listadoNombresAnimes: String[] = [];

  //Utilizo ActivatedRoute para recuperar el parametro nombre cuando se realiza una busqueda
  constructor(private animeService: AnimeService,
    private episodiosService: EpisodioService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.nombreAnime = params['nombre'] || '';  // Si no hay 'nombre', asigna un valor vacío
      this.cargarTodosLosAnimes();
      this.cargarListadoEpisodios();
    });
  }

  // Obtengo todos los animes
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
        this.cargarListadoNombresAnimes();
      });
    }
  }

  // Cargo el listado de todos los episodios
  cargarListadoEpisodios() {
    this.episodiosService.getAll().subscribe(data => {
      this.listadoEpisodios = data;
    });
  }

  // Filtro los episodios por su anime
  cargarEpisodios(idAnime: number): any[] {
    this.episodiosFiltrados = this.listadoEpisodios.filter(
      episodio => episodio.anime.idAnime === idAnime
    );
    return this.episodiosFiltrados;
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

  // Recibir mensaje
  recibirMensaje(mensaje: string) {
    this.mensaje = mensaje;
  }

  // Recibir estado mensaje
  recibirEstadoMensaje(estado: boolean) {
    this.mensajeError = estado;
  }

  // Obtener listado de animes
  cargarListadoNombresAnimes() {
    for (let anime of this.listadoAnimes) {
      this.listadoNombresAnimes.push(anime.nombre);
    }
  }

  // Método para navegar a la pestaña de crear un episodio
  navegarASubirEpisodio() {
    const animes = this.listadoNombresAnimes;
    console.log("Listado enviado:", animes);
    this.router.navigate(["/subir-episodio"], {
      state: { animes }
    });
  }

}
