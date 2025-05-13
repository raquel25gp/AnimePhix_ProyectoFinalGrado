import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../../../../services/usuario.service';
import { CambiarPasswordComponent } from "../cambiar-password/cambiar-password.component";

@Component({
  selector: 'app-datos-usuario',
  standalone: true,
  imports: [FormsModule, CommonModule, CambiarPasswordComponent],
  templateUrl: './datos-usuario.component.html',
  styleUrl: './datos-usuario.component.css'
})
export class DatosUsuarioComponent {
  @Input() nombre: string | undefined = '';
  @Input() email: string = '';
  @Input() habilitado: boolean = false;
  @Input() estadoModificar: boolean = true;

  nuevoNombre = this.nombre;
  mensajeNombre: string = '';
  mensajeNombreError: boolean = false;

  constructor(private usuarioService: UsuarioService) { }

  // Método del botón para aplicar los cambios del nombre de usuario
  modificarNombre() {
    let auxNombre = this.nombre?.toLocaleLowerCase;
    let auxNuevoNombre = this.nuevoNombre?.toLowerCase;
    if (auxNombre === auxNuevoNombre) {
      this.mensajeNombre = '¡No se ha realizado ningún cambio!';
      this.mensajeNombreError = true;
    } else if (this.nuevoNombre !== undefined) {
      this.usuarioService.actualizarNombre({ email: this.email, nuevoNombre: this.nuevoNombre }).subscribe({
        next: (response) => {
          // Se muestra un mensaje al usuario
          this.mensajeNombre = response;
          this.mensajeNombreError = false;
          // Espera 1 segundo y luego recarga la página para visualizar los cambios
          setTimeout(() => {
            window.location.reload();
          }, 1000);
        },
        error: err => {
          // En caso de error se muestra un mensaje
          if (typeof err.error === 'string') {
            this.mensajeNombre = err.error; // Mensaje de error del backend
          } else {
            this.mensajeNombre = '¡Oh no! Algo ha salido mal.';
          }
          this.mensajeNombreError = true;
        }
      });
    }
  }
}
