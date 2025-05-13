import { Component } from '@angular/core';
import { ListadoUltimosAnimesComponent } from './listado-ultimos-animes/listado-ultimos-animes.component';
import { ListadoUltimosEpisodiosComponent } from "./listado-ultimos-episodios/listado-ultimos-episodios.component";
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [ListadoUltimosAnimesComponent, ListadoUltimosEpisodiosComponent, RouterModule],
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.css'
})
export class InicioComponent {

}
