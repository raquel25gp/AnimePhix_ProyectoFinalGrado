import { Component, OnInit } from '@angular/core';
import { ProblemaService } from '../../../../services/problema.service';
import { ListarReportes } from '../../../../interfaces/problema/listar-reportes';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PendientesComponent } from "./pendientes/pendientes.component";
import { CorregidosComponent } from "./corregidos/corregidos.component";

@Component({
  selector: 'app-admin-reportes',
  standalone: true,
  imports: [CommonModule, FormsModule, PendientesComponent, CorregidosComponent],
  templateUrl: './admin-reportes.component.html',
  styleUrl: './admin-reportes.component.css'
})
export class AdminReportesComponent implements OnInit {
  listado: ListarReportes[] = [];
  listadoPendientes: ListarReportes[] = [];
  listadoCorregidos: ListarReportes[] = [];

  constructor(private problemaService: ProblemaService) { }

  ngOnInit(): void {
    // Obtengo los tipos de problemas al crear el componente
    this.problemaService.getTodos().subscribe((data) => {
      this.listado = data
      this.clasificar();
    });

  }

  // Método que clasifica los reporte en pendientes y corregidos
  clasificar() {
    for (let rep of this.listado) {
      if (rep.corregido === false) {
        this.listadoPendientes.push(rep);
      } else {
        this.listadoCorregidos.push(rep);
      }
    }
  }
}
