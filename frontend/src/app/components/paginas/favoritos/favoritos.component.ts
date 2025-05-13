import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ListaFavoritos } from '../../../interfaces/favorito/lista-favoritos';
import { AuthService } from '../../../services/auth.service';
import { FavoritoService } from '../../../services/favorito.service';

@Component({
  selector: 'app-favoritos',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './favoritos.component.html',
  styleUrl: './favoritos.component.css'
})
export class FavoritosComponent implements OnInit {
  favoritos: ListaFavoritos[] = [];
  email: string = '';
  mensaje: string = '';
  mensajeError: boolean = false;

  constructor(private favoritosService: FavoritoService, private authService: AuthService) { }

  ngOnInit(): void {
    // Cuando se genera el componente, se obtiene el token del usuario
    const datos = this.authService.obtenerDatosToken();
    if (datos) {
      //En caso de que exista el token se guarda el email del usuario
      this.email = datos.email;
      //Se cargan sus animes favoritos
      this.cargarFavoritos();
    }
  }

  // Método para obtener los animes favoritos
  cargarFavoritos() {
    // Llamada al servicio para obtener los animes favoritos del usuario
    this.favoritosService.getFavoritos(this.email).subscribe({
      next: (data) => {
        // Se guardan los favoritos en el array
        this.favoritos = data;
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

  // Método del botón para eliminar un anime como favorito
  eliminarFavorito(nombre: string) {
    // Llamada al servicio para eliminar el anime de los favoritos del usuario
    this.favoritosService.eliminarFavorito({ email: this.email, nombreAnime: nombre }).subscribe({
      next: (response) => {
        // Elimina el anime del array local
        this.favoritos = this.favoritos.filter(f => f.nombre !== nombre);
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
