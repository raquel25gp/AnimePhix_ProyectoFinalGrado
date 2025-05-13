import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { DatosUsuarioComponent } from "./datos-usuario/datos-usuario.component";
import { BotonesComponent } from './botones/botones.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [DatosUsuarioComponent, BotonesComponent, CommonModule, FormsModule],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent implements OnInit {
  datosUsuario: any | null = null;
  email: string = '';
  cuadroPassword: boolean = false;
  cuadroEdicion: boolean = true;
  cuadroImagen: boolean = false;
  imagenSeleccionada: string = '/Imagenes/FotosPerfil/default.jpg'; //Siempre marcada la imagen por defecto
  // Array con los avatares disponibles
  avatares = [
    { nombre: 'Predeterminado', ruta: '/Imagenes/FotosPerfil/default.jpg' },
    { nombre: 'Naruto', ruta: '/Imagenes/FotosPerfil/avatar1.jpg' },
    { nombre: 'Sailor Moon', ruta: '/Imagenes/FotosPerfil/avatar2.jpg' },
    { nombre: 'Goku', ruta: '/Imagenes/FotosPerfil/avatar3.jpg' },
    { nombre: 'ShinChan', ruta: '/Imagenes/FotosPerfil/avatar4.jpg' },
    { nombre: 'Luffy', ruta: '/Imagenes/FotosPerfil/avatar5.jpg' },
    { nombre: 'Porum', ruta: '/Imagenes/FotosPerfil/avatar6.jpg' },
    { nombre: 'Kirito', ruta: '/Imagenes/FotosPerfil/avatar7.jpg' },
    { nombre: 'Asuna', ruta: '/Imagenes/FotosPerfil/avatar8.jpg' }
  ];
  mensajeImagen: string = '';
  mensajeImagenError: boolean = false;

  constructor(private authService: AuthService, private usuarioService: UsuarioService, private router: Router) { }

  ngOnInit(): void {
    // Cuando se genera el componente, se obtiene el token del usuario
    const datos = this.authService.obtenerDatosToken();
    if (datos) {
      // En caso de que exista el token se guarda el email del usuario
      this.email = datos.email;
      this.cargarUsuario();
    }
  }

  // Cargo los datos del usuario logueado
  cargarUsuario(): void {
    this.usuarioService.getDatos(this.email).subscribe({
      next: (data) => {
        this.datosUsuario = data;
      },
      error: () => {
        // En caso de error, dirijo al usuario a la pagina not found
        this.router.navigate(['/page-not-found']);
      }
    });
  }

  // Método para mostrar el cuadro de cambiar la contraseña
  habilitarCambioPassword(estado: boolean) {
    this.cuadroPassword = estado;
  }

  // Método para habilitar el cambio de los datos
  habilitarEdicionDatos(estado: boolean) {
    this.cuadroEdicion = estado;
  }

  // Método para mostrar el cambio de la imagen
  habilitarCambioImagen(estado: boolean) {
    this.cuadroImagen = estado;
  }

  // Método del botón para cambiar la imagen de perfil
  guardarImagen(): void {
    this.usuarioService.actualizarImagen({ email: this.email, urlImagen: this.imagenSeleccionada }).subscribe({
      next: (response) => {
        // Se muestra un mensaje al usuario
        this.mensajeImagen = response;
        this.mensajeImagenError = false;
        // Espera 1 segundo y luego recarga la página para visualizar los cambios
        setTimeout(() => {
          window.location.reload();
        }, 1000);
      },
      error: err => {
        // En caso de error se muestra un mensaje
        if (typeof err.error === 'string') {
          this.mensajeImagen = err.error; // Mensaje de error del backend
        } else {
          this.mensajeImagen = '¡Oh no! Algo ha salido mal.';
        }
        this.mensajeImagenError = true;
      }
    });
  }
}
