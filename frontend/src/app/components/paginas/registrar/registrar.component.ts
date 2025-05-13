import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { UsuarioService } from '../../../services/usuario.service';

@Component({
  selector: 'app-registrar',
  standalone: true,
  imports: [RouterModule, FormsModule, CommonModule],
  templateUrl: './registrar.component.html',
  styleUrl: './registrar.component.css'
})
export class RegistrarComponent {
  nombre: string = '';
  email: string = '';
  password: string = '';
  confirmarPassword: string = '';
  mensaje: string = '';
  mensajeError: boolean = false;

  constructor(private usuarioService: UsuarioService, private router: Router) { }

  // Método del botón para crear un usuario
  registrar() {
    // Validación de que se ha introducido la misma contraseña en ambos campos
    if (this.password !== this.confirmarPassword) {
      this.mensaje = 'La contraseña debe de ser la misma en ambos campos.';
      this.mensajeError = true;
    } else {
      const nuevoUsuario = {
        nombre: this.nombre,
        email: this.email,
        password: this.password
      }
      this.usuarioService.registrar(nuevoUsuario).subscribe({
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
  }

  // Método del botón para navegar a la página de login
  iniciarSesion() {
    this.router.navigate(['/iniciarSesion']);
  }
}
