import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { NombreNumLista } from '../../../../interfaces/episodio/nombre-num-lista';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-episodios',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './episodios.component.html',
  styleUrl: './episodios.component.css'
})
export class EpisodiosComponent {
  @Input() listado: NombreNumLista[] | undefined = [];
}
