import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { VistoService } from '../../../../services/visto.service';
import { AuthService } from '../../../../services/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-video',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './video.component.html',
  styleUrl: './video.component.css'
})
export class VideoComponent implements OnInit {
  @Input() nombre: string = '';
  @Input() numEpisodio: number = 0;
  @Input() urlPoster: string | undefined = '';
  @Input() urlVideo: string | undefined = '';
  @Input() totalEpisodios: number = 0;
  email: string = '';
  texto: string = '';
  visto: boolean = false;

  constructor(private vistoService: VistoService, private authService: AuthService) { }

  ngOnInit(): void {
    // Cuando se genera el componente, se obtiene el token del usuario
    const datos = this.authService.obtenerDatosToken();
    if (datos) {
      // En caso de que exista el token se guarda el email del usuario
      this.email = datos.email;
    }
    this.comprobarEstado();
  }

  // Método para comprobar si el usuario está logueado
  get estaLogueado(): boolean {
    return this.authService.estaLogueado();
  }

  // Muestro un mensaje si no está logueado
  mostrarMensaje() {
    Swal.fire({
      title: 'Acceso denegado',
      text: 'Para poder marcar episodios como vistos, hay que iniciar sesión.',
      icon: 'error',
      confirmButtonText: 'Aceptar'
    });
  }

  // Compruebo el estado del episodio al inicio del componente
  comprobarEstado() {
    const datos = {
      email: this.email,
      nombreAnime: this.nombre,
      numEpisodio: this.numEpisodio
    }
    this.vistoService.comprobarEstado(datos).subscribe(response => this.texto = response);
  }

  // Metodo del botón para cambiar el estado de visualización del episodio
  modificarEstadoEpisodio() {
    const datos = {
      email: this.email,
      nombreAnime: this.nombre,
      numEpisodio: this.numEpisodio
    }
    this.vistoService.marcarDesmarcarEpisodio(datos).subscribe({
      next: () => {
        this.visto = !this.visto;
        // Espera 1 segundo y luego recarga la página para visualizar los cambios
        setTimeout(() => {
          window.location.reload();
        }, 1000);
      },
      error: () => {
        // En caso de error se le muestra una alerta
        Swal.fire({
          title: 'Acción denegada',
          text: 'Se ha producido un error al marcar o desmarcar episodio como visto',
          icon: 'error',
          confirmButtonText: 'Aceptar'
        });
      }
    })
  }
}
