import { Component, Input } from '@angular/core';
import { ListarReportes } from '../../../../../interfaces/problema/listar-reportes';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProblemaService } from '../../../../../services/problema.service';

@Component({
  selector: 'app-pendientes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pendientes.component.html',
  styleUrl: './pendientes.component.css'
})
export class PendientesComponent {
  @Input() listadoPendientes: ListarReportes[] = [];

  constructor(private problemaService: ProblemaService) { }

  // Método del botón para marcar como Corregido un reporte
  marcarCorregido(idReporte: number) {
    this.problemaService.cambiarEstadoCorregido(idReporte).subscribe(
      () => {
        // Espera 1 segundo y luego recarga la página para visualizar los cambios
        setTimeout(() => {
          window.location.reload();
        }, 1000);
      }
    );
  }
}
