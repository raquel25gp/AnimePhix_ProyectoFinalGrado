import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../../../../../services/usuario.service';
import { AuthService } from '../../../../../services/auth.service';

@Component({
  selector: 'app-deshabilitados',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './deshabilitados.component.html',
  styleUrl: './deshabilitados.component.css'
})
export class DeshabilitadosComponent {
  @Input() listadoDeshabilitados: any[] = [];
  mensaje: string = '';
  mensajeError: boolean = false;
  email: string = '';

  constructor(private usuarioService: UsuarioService, private authService: AuthService) { }

  ngOnInit(): void {
    // Si el administrador está loggueado
    this.authService.loginStatus$.subscribe((logueado) => {
      if (logueado) {
        // Obtengo su token
        const datos = this.authService.obtenerDatosToken();
        if (datos) {
          // Y guardo su email
          this.email = datos.email;
        }
      }
    });
  }

  // Solamente el superadministrador Admin puede crear o eliminar mas administradores cambiando su rol
  cambiarRol(usuario: any) {
    const idUsuario = usuario.idUsuario;
    const email = this.email;
    this.usuarioService.generarAdmin(idUsuario, email).subscribe({
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

  // Método del botón que habilita o deshabilita un usuario
  cambiarEstadoHabilitado(usuario: any) {
    const idUsuario = usuario.idUsuario;
    this.usuarioService.cambiarEstadoHabilitado(idUsuario).subscribe({
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
