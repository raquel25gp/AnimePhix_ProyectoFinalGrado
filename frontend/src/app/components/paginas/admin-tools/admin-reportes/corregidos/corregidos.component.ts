import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { ListarReportes } from '../../../../../interfaces/problema/listar-reportes';
import { ProblemaService } from '../../../../../services/problema.service';

@Component({
  selector: 'app-corregidos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './corregidos.component.html',
  styleUrl: './corregidos.component.css'
})
export class CorregidosComponent {
  @Input() listadoCorregidos: ListarReportes[] = [];

  constructor(private problemaService: ProblemaService) { }

  // Método del botón para marcar como Pendiente un reporte
    marcarPendiente(idReporte: number) {
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
