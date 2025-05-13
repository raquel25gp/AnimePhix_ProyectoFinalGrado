import { Component } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FiltroComponent } from "./filtro/filtro.component";
import { TarjetaDirectorio } from '../../../interfaces/anime/tarjeta-directorio';
import { AnimeService } from '../../../services/anime.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-directorio',
  standalone: true,
  imports: [RouterModule, FiltroComponent, CommonModule],
  templateUrl: './directorio.component.html',
  styleUrl: './directorio.component.css'
})
export class DirectorioComponent {
  limite = 8;
  desplazamiento = 0;
  totalAnimes = 0;
  listadoAnimes: TarjetaDirectorio[] = [];
  nombreAnime = '';

  // Utilizo ActivatedRoute para recuperar el parametro nombre cuando se realiza una busqueda
  constructor(private animeService: AnimeService, private route: ActivatedRoute) { } 

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.nombreAnime = params['nombre'] || '';  // Si no hay 'nombre', asigna un valor vacío
      this.cargarTodosLosAnimes();
    });
  }
  
  // Método para gestionar la paginación (sólo se usará cuando no haya búsqueda)
  get animesPaginados(): TarjetaDirectorio[] {
    return this.listadoAnimes.slice(this.desplazamiento, this.desplazamiento + this.limite);
  }
  
  // Método para gestionar el desplazamiento
  cambiarPagina(direccion: number): void {
    const nuevoDesplazamiento = this.desplazamiento + this.limite * direccion;
    if (nuevoDesplazamiento >= 0 && nuevoDesplazamiento < this.totalAnimes) {
      this.desplazamiento = nuevoDesplazamiento;
    }
  }
  
  // Método para cargar los animes, con manejo para búsqueda y paginación
  cargarTodosLosAnimes(): void {
    if (this.nombreAnime) {
      // Cuando hay búsqueda, sólo se busca un anime
      this.animeService.getAnimeBuscado(this.nombreAnime).subscribe(data => {
        this.listadoAnimes = data;
        this.totalAnimes = data.length;
        this.desplazamiento = 0; // Reseteo la paginación
      });
    } else {
      // Cuando no hay búsqueda, se cargan todos los animes y se usa la paginación
      this.animeService.getTodosAnimes().subscribe(data => {
        this.listadoAnimes = data;
        this.totalAnimes = data.length;
        this.desplazamiento = 0; // Reseteo la paginación
      });
    }
  }

  // Recibo los filtros y los aplico
  aplicarFiltros(filtros: any): void {
    this.animeService.getAnimesFiltrados(filtros).subscribe(data => {
      this.listadoAnimes = data;
      this.totalAnimes = data.length;
      this.desplazamiento = 0; // Reseteo la paginación
    });
  }
}
