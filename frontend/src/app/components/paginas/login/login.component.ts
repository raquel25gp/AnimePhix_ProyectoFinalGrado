import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterModule, FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  error: string = '';
  flag: boolean = false;

  constructor(private authService: AuthService, private router: Router) { }

  // Método del botón para iniciar sesión
  iniciarSesion(): void {
    this.authService.login(this.email, this.password).subscribe({
      next: (data) => {
        // Guardo el token recibido del servidor
        this.authService.guardarToken(data.token);
        // Notifico el login
        this.authService.notificarLogin();
        // Llevo al usuario a la página de inicio
        this.router.navigate(['/inicio']);
      },
      error: err => {
        // En caso de error se muestra un mensaje
        if (typeof err.error === 'string') {
          this.error = err.error; // Mensaje de error del backend
        } else {
          this.error = '¡Oh no! Algo ha salido mal.';
        }
      }
    });
  }

  // Método del botón para mostrar u ocultar el contenido del campo contraseña
  mostrarPassword(): void {
    this.flag = !this.flag;
  }
}
