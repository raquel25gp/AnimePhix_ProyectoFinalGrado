import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AnimeService } from '../../../services/anime.service';
import { CommonModule } from '@angular/common';
import { LadoIzquierdoComponent } from "./lado-izquierdo/lado-izquierdo.component";
import { DetalleComponent } from "./detalle/detalle.component";
import { EpisodiosComponent } from "./episodios/episodios.component";
import { AnimePagina } from '../../../interfaces/anime/anime-pagina';
import { PublicidadComponent } from "./publicidad/publicidad.component";

@Component({
  selector: 'app-anime',
  standalone: true,
  imports: [RouterModule, CommonModule, LadoIzquierdoComponent, DetalleComponent, EpisodiosComponent, PublicidadComponent],
  templateUrl: './anime.component.html',
  styleUrl: './anime.component.css'
})
export class AnimeComponent implements OnInit {
  nombreAnime: string = '';
  datosAnime: AnimePagina | null = null;

  constructor( private route: ActivatedRoute, private animeService: AnimeService, private router: Router) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.nombreAnime = params.get('nombre') || ''; // Si no hay 'nombre', asigna un valor vacío
      this.cargarAnime();
    });
  }

  // Método para cargar los datos del anime
  cargarAnime(): void {
    this.animeService.getAnimePagina(this.nombreAnime).subscribe({
      next: (data) => {
        // Guardo los datos del anime
        this.datosAnime = data;
      },
      error: () => {
        // En caso de error, dirijo al usuario a la página not found
        this.router.navigate(['/page-not-found']);
      }
    });
  }
}
