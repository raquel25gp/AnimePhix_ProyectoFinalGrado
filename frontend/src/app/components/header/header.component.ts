import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, FormsModule, CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  animeBuscado: string = '';
  datosUsuario: any | null = null;
  email: string = '';

  constructor(private router: Router, 
              private route: ActivatedRoute, 
              private authService: AuthService, 
              private usuarioService: UsuarioService
  ) { }

  ngOnInit(): void {
    // Si el usuario está loggueado
    this.authService.loginStatus$.subscribe((logueado) => {
      if (logueado) {
        // Obtengo su token
        const datos = this.authService.obtenerDatosToken();
        if (datos) {
          // Y guardo su email
          this.email = datos.email;
          this.obtenerDatosUsuario();
        }
      }
    });
  }

  // Compruebo si el usuario está logueado
  get estaLogueado(): boolean {
    return this.authService.estaLogueado();
  }

  // Funcion para coger los datos del usuario
  obtenerDatosUsuario(): void {
    this.usuarioService.getDatos(this.email).subscribe(
      (data) => {
        // Se guardan los datos obtenigos
        this.datosUsuario = data;
      }
    );
  }

  // Función para buscar anime que redirige al directorio
  buscarAnime(): void {
    if (this.animeBuscado) {
      this.router.navigate(['/directorio'], {
        relativeTo: this.route,
        queryParams: { nombre: this.animeBuscado }
      });
    }
  }

  // Función para mostrar un mensaje de acceso denegado
  mostrarMensaje() {
    Swal.fire({
      title: 'Acceso denegado',
      text: 'Si quieres disfrutar de un calendario personalizado según tus animes favoritos o agregar tus propios eventos, inicia sesión en nuestra página web o crea una cuenta.',
      icon: 'info',
      confirmButtonText: 'Aceptar'
    });
  }

  // Función para cerrar sesión
  cerrarSesion() {
    this.authService.logout();
    this.router.navigate(['/inicio']);
  }
}
