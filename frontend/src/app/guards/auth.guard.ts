import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import Swal from 'sweetalert2';


@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) { }

  // Metodo para las rutas protegidas
  canActivate(): boolean {
    if (this.authService.estaLogueado()) {
      return true; // Permite el acceso
    } else {
      this.showError('Debes iniciar sesión para acceder a esta página.');
      this.router.navigate(['/login']); // Redirige al login si no está autenticado
      return false;
    }
  }

  //Utilizo sweetalert para hacer una alerta mas bonita para el usuario
  showError(message: string) {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: message,
      confirmButtonColor: '#d33'
    });
  }
}
