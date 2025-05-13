import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReporteTipo } from '../../../../interfaces/problema/reporte-tipo';
import { ProblemaService } from '../../../../services/problema.service';
import { AuthService } from '../../../../services/auth.service';

@Component({
  selector: 'app-reportar-problema',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './reportar-problema.component.html',
  styleUrl: './reportar-problema.component.css'
})
export class ReportarProblemaComponent implements OnInit {
  listadoNombreTipos: ReporteTipo[] = [];
  categoria: string = '';
  descripcion: string = '';
  email: string = '';
  mensaje: string = '';
  mensajeError: boolean = false;
  emailContacto: string = 'animephix.contacto@gmail.com';

  constructor(private problemaService: ProblemaService) { }

  ngOnInit(): void {
    // Al crear el componente, obtengo los tipos de problemas del servidor
    this.problemaService.getNombresTiposProblemas().subscribe(data => this.listadoNombreTipos = data);
  }

  // Método de envío del formulario
  onSubmit() {
    if (this.categoria && this.descripcion) {
      const nuevoReporte = {
        tipoProblema: this.categoria,
        descripcion: this.descripcion,
        email: this.email
      }
      this.problemaService.crearReporte(nuevoReporte).subscribe({
        next: (response) => {
          // Se muestra un mensaje al usuario
          this.mensaje = response;
          this.mensajeError = false;
        },
        error: err => {
          // En caso de error se muestra un mensaje
          if (typeof err.error === 'string') {
            this.mensaje = err.error; // Mensaje de error del backend
          } else {
            this.mensaje = '¡Oh no! Algo ha salido mal.';
          }
          this.mensajeError = true;
        }
      });
    } else {
      this.mensaje = "Por favor, completa todos los campos para enviar el reporte.";
      this.mensajeError = true;
    }
  }
}
