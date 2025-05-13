import { CommonModule } from '@angular/common';
import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../../services/auth.service';
import Swal from 'sweetalert2';
import { UsuarioService } from '../../../../services/usuario.service';
import { ComentarioService } from '../../../../services/comentario.service';
import { ListadoComentarios } from '../../../../interfaces/episodio/listado-comentarios';

@Component({
  selector: 'app-comentario',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './comentario.component.html',
  styleUrl: './comentario.component.css'
})
export class ComentarioComponent implements OnInit, OnChanges {
  @Input() nombreAnime: string = '';
  @Input() numEpisodio: number = 0;
  texto: string = '';
  comentarios: ListadoComentarios[] = [];
  email: string = '';
  datosUsuario: any | null = null;
  mensaje: string = '';
  mensajeError: boolean = false;
  estado: boolean = false;

  constructor(private authService: AuthService, private usuarioService: UsuarioService, private comentarioService: ComentarioService) { }

  // Cuando se cambia de episodio recarga los comentarios
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['numEpisodio'] || changes['nombreAnime']) {
      this.cargarComentarios();
    }
  }

  ngOnInit(): void {
    // Cuando se genera el componente, se obtiene el token del usuario
    const datos = this.authService.obtenerDatosToken();
    if (datos) {
      // En caso de que exista el token se guarda el email del usuario
      this.email = datos.email;
      this.cargarUsuario();
    }
    this.cargarComentarios();
  }

  // Cargo los comentarios en caso de que haya
  cargarComentarios() {
    const datosEpisodio = {
      anime: this.nombreAnime,
      numEpisodio: this.numEpisodio
    }
    this.comentarioService.listarComentarios(datosEpisodio).subscribe(
      (data) => {
        this.comentarios = data;
        // Parseo los datos cuando se reciben como string
        if (typeof data === 'string') {
          this.comentarios = JSON.parse(data);
        }
      }
    );
  }

  // Compruebo si el usuario está logueado
  get estaLogueado(): boolean {
    return this.authService.estaLogueado();
  }

  // Cargo los datos del usuario logueado
  cargarUsuario(): void {
    this.usuarioService.getDatos(this.email).subscribe(data => this.datosUsuario = data);
  }

  // Muestro un mensaje si no está logueado
  mostrarMensaje() {
    Swal.fire({
      title: 'Acceso denegado',
      text: 'Para poder comentar, hay que iniciar sesión.',
      icon: 'error',
      confirmButtonText: 'Aceptar'
    });
  }

  // Crear comentario
  crearComentario() {
    const nuevoComentario = {
      email: this.email,
      nombreAnime: this.nombreAnime,
      numEpisodio: this.numEpisodio,
      comentario: this.texto,
      habilitado: true,
      fechaCreacion: new Date().toISOString
    }
    this.comentarioService.agregarComentario(nuevoComentario).subscribe({
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

  //El usuario cambia si quiere mostrar o no los comentarios
  mostrarComentarios() {
    this.estado = !this.estado;
  }

  //Cambiar visibilidad comentario
  cambiarVisibilidad(comentario: ListadoComentarios) {
    this.comentarioService.cambiarVisibilidad(comentario, this.nombreAnime, this.numEpisodio).subscribe({
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
