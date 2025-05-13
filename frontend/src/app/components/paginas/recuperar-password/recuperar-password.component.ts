import { Component } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-recuperar-password',
  standalone: true,
  imports: [RouterModule, FormsModule, CommonModule],
  templateUrl: './recuperar-password.component.html',
  styleUrl: './recuperar-password.component.css'
})
export class RecuperarPasswordComponent {
  email: string = '';
  mensaje: string = '';
  mensajeError: boolean = false;

  constructor(private usuarioService: UsuarioService, private router: Router) { }

  // Método del botón para solicitar la contraseña
  enviar() {
    this.usuarioService.solicitarPassword(this.email).subscribe({
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
  }

  // Método del botón para navegar a la página de login
  iniciarSesion() {
    this.router.navigate(['/iniciarSesion']);
  }
}
