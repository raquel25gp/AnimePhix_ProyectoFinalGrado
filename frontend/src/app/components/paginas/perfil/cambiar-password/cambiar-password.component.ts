import { Component, Input } from '@angular/core';
import { UsuarioService } from '../../../../services/usuario.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cambiar-password',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './cambiar-password.component.html',
  styleUrl: './cambiar-password.component.css'
})
export class CambiarPasswordComponent {
  @Input() email: string = '';

  nuevaPassword: string = '';
  confirmarPassword: string = '';
  mensaje: string = '';
  mensajeError: boolean = false;

  constructor(private usuarioService: UsuarioService) { }

  // Método del botón para aplicar la nueva contraseña
  modificarPassword() {
    if (this.nuevaPassword !== this.confirmarPassword) {
      this.mensaje = 'La contraseña debe de ser la misma en ambos campos.';
      this.mensajeError = true;
    } else {
      this.usuarioService.actualizarPassword({ email: this.email, nuevaPassword: this.nuevaPassword }).subscribe({
        next: (response) => {
          // Se muestra un mensaje al usuario
          this.mensaje = response;
          this.mensajeError = false;
          // Espera 1 segundo y luego recarga la página para visualizar los cambios
          setTimeout(() => {
            window.location.reload();
          }, 1000);
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
    }
  }
}
