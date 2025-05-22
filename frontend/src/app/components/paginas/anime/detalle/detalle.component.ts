import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-detalle',
  standalone: true,
  imports: [],
  templateUrl: './detalle.component.html',
  styleUrl: './detalle.component.css'
})
export class DetalleComponent {
  @Input() nombre: string = '';
  @Input() descripcion: string |undefined = '';
  @Input() genero: string |undefined = '';
}
