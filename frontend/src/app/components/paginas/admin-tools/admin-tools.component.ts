import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AdminAnimesComponent } from "./admin-animes/admin-animes.component";
import { AdminEpisodiosComponent } from "./admin-episodios/admin-episodios.component";
import { AdminReportesComponent } from "./admin-reportes/admin-reportes.component";
import { AdminUsuariosComponent } from "./admin-usuarios/admin-usuarios.component";
import { AdminComentariosComponent } from "./admin-comentarios/admin-comentarios.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-tools',
  standalone: true,
  imports: [RouterModule, AdminAnimesComponent, AdminEpisodiosComponent, AdminReportesComponent, AdminUsuariosComponent, AdminComentariosComponent],
  templateUrl: './admin-tools.component.html',
  styleUrl: './admin-tools.component.css'
})
export class AdminToolsComponent {

}
