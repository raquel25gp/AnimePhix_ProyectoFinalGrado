import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { UltimosEpisodios } from '../../../../interfaces/episodio/ultimos-episodios';
import { EpisodioService } from '../../../../services/episodio.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-listado-ultimos-episodios',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './listado-ultimos-episodios.component.html',
  styleUrl: './listado-ultimos-episodios.component.css'
})
export class ListadoUltimosEpisodiosComponent implements OnInit {
  listadoEpisodios: UltimosEpisodios[] = [];

  constructor(private episodioService: EpisodioService) { };

  ngOnInit(): void {
    // Cuando se genera el componente, obtengo los episodios más recientes
    this.episodioService.getUltimosEpisodios().subscribe(data => this.listadoEpisodios = data);
  }
}
