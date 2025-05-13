import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UsuarioService } from '../../../../services/usuario.service';
import Swal from 'sweetalert2';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../../services/auth.service';

@Component({
  selector: 'app-botones',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './botones.component.html',
  styleUrl: './botones.component.css'
})
export class BotonesComponent {
  @Output() habilitarEdicion = new EventEmitter<boolean>();
  @Output() mostrarCuadro = new EventEmitter<boolean>();
  @Output() abrirSelect = new EventEmitter<boolean>();
  @Input() email: string | undefined = '';
  flag: boolean = false;

  constructor(private usuarioService: UsuarioService, private authService: AuthService, private router: Router) { }

  // Método para enviar el estado del cuadro
  modificarNombre() {
    this.flag = false;
    this.habilitarEdicion.emit(this.flag);
  }

  // Método para enviar el estado del cuadro
  actualizarPassword() {
    this.flag = true;
    this.mostrarCuadro.emit(this.flag);
  }

  // Método para enviar el estado del cuadro
  cambiarImagen() {
    this.flag = true;
    this.abrirSelect.emit(this.flag);
  }

  // Método del boton de darse de baja
  desabilitarUsuario() {
    // Se pide confirmación al usuario
    Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Deseas eliminar al usuario con email ${this.email}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed && this.email !== undefined) {
        this.usuarioService.eliminarUsuario(this.email).subscribe({
          next: () => {
            // Se muestra un mensaje al usuario
            Swal.fire('Eliminado', 'El usuario ha sido eliminado.', 'success');
            // Se vuelve al inicio
            this.router.navigate(["/inicio"]);
            // Se cierra la sesión para eliminar el token
            this.authService.logout();
          },
          error: err => {
            // En caso de error se muestra un mensaje
            if (typeof err.error === 'string') {
              Swal.fire('Error', err.error, 'error'); // Mensaje de error del backend
            } else {
              Swal.fire('Error', 'Hubo un problema al eliminar el usuario.', 'error');
            }
            // Se vuelve al inicio
            this.router.navigate(["/inicio"]);
            // Se cierra la sesión para eliminar el token
            this.authService.logout();
          }
        });
      }
    });
  }
}
