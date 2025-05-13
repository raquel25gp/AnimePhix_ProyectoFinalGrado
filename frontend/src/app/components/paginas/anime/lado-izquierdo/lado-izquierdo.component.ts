import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { AuthService } from '../../../../services/auth.service';
import Swal from 'sweetalert2';
import { FavoritoService } from '../../../../services/favorito.service';
import { ListaFavoritos } from '../../../../interfaces/favorito/lista-favoritos';

@Component({
  selector: 'app-lado-izquierdo',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lado-izquierdo.component.html',
  styleUrl: './lado-izquierdo.component.css'
})
export class LadoIzquierdoComponent {
  @Input() nombre: string = '';
  @Input() urlImagen: string | undefined = '';
  @Input() estado: string | undefined = '';
  email: string = '';
  mensaje: string = '';
  mensajeError: boolean = false;
  listadoFavoritos: ListaFavoritos[] = [];
  flag: boolean = false;

  constructor(private authService: AuthService, private favoritoService: FavoritoService) { }

  ngOnInit(): void {
    // Cuando se genera el componente, se obtiene el token del usuario
    const datos = this.authService.obtenerDatosToken();
    if (datos) {
      // En caso de que exista el token se guarda el email del usuario
      this.email = datos.email;
      this.getFavoritos();
    }

  }

  // Compruebo si el usuario está logueado
  get estaLogueado(): boolean {
    return this.authService.estaLogueado();
  }

  // Si no estña logueado se muestra un mensaje
  mostrarMensaje() {
    Swal.fire({
      title: 'Acceso denegado',
      text: 'Por favor, inicia sesión para poder agregar el anime como favorito.',
      icon: 'error',
      confirmButtonText: 'Aceptar'
    });
  }

  // Método del botón para agregar el anime como favorito
  agregarFavorito() {
    this.favoritoService.agregarFavorito({ email: this.email, nombreAnime: this.nombre }).subscribe({
      next: (response) => {
        // Se actualizan las variables
        this.mensajeError = false;
        this.flag = true;
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

  // Método para controlar si el anime está guardado como favorito
  getFavoritos() {
    this.favoritoService.getFavoritos(this.email).subscribe((data) => {
      this.listadoFavoritos = data;
      // Si encuentra el anime se actualiza la variable para el icono
      if (this.listadoFavoritos.find((fav) => this.nombre === fav.nombre)) {
        this.flag = true;
      }
    });
  }
}
