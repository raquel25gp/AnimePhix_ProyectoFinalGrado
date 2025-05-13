import { Component, OnInit } from '@angular/core';
import { AnimeNombre } from '../../../../interfaces/anime/anime-nombre';
import { AnimeService } from '../../../../services/anime.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-listado-ultimos-animes',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './listado-ultimos-animes.component.html',
  styleUrl: './listado-ultimos-animes.component.css'
})
export class ListadoUltimosAnimesComponent implements OnInit {
  listaAnimesEnEmision: AnimeNombre[] = [];
  
    constructor(private animeService: AnimeService) {}
  
    ngOnInit(): void {
      // Cuando se genera el componente, obtengo los animes que están en emisión
        this.animeService.getNombreAnimesEnEmision().subscribe(data => this.listaAnimesEnEmision = data);
    }
}
