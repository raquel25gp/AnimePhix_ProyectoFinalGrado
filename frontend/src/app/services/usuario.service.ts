import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Registro } from '../interfaces/usuario/registro';
import { ActualizarPassword } from '../interfaces/usuario/actualizar-password';
import { ActualizarNombre } from '../interfaces/usuario/actualizar-nombre';
import { ActualizarImagen } from '../interfaces/usuario/actualizar-imagen';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private apiURL: string = 'http://localhost:8080/usuarios';

  constructor(private http: HttpClient) { }

  // Método para crear un usuario
  registrar(usuario: Registro): Observable<string> {
    return this.http.post<string>(`${this.apiURL}/registrar`, usuario, { responseType: 'text' as 'json' });
  }

  // Método para enviar al usuario una contraseña aleatoria
  solicitarPassword(email: string): Observable<string> {
    return this.http.post<string>(`${this.apiURL}/solicitar-password-olvidada`, email, { responseType: 'text' as 'json' });
  }

  // Obtiene todos los datos del usuario
  getDatos(email: string): Observable<any> {
    return this.http.get<any>(`${this.apiURL}/cargar-datos?email=${email}`);
  }

  // Método del usuario para actualizar su contraseña
  actualizarPassword(datos: ActualizarPassword): Observable<string> {
    return this.http.put<string>(`${this.apiURL}/actualizar-password`, datos, { responseType: 'text' as 'json' });
  }

  // Método del usuario para actualizar su nombre de usuario
  actualizarNombre(datos: ActualizarNombre): Observable<string> {
    return this.http.put<string>(`${this.apiURL}/actualizar-nombre`, datos, { responseType: 'text' as 'json' });
  }

  // Método del usuario para cambiar su foto de perfil
  actualizarImagen(datos: ActualizarImagen): Observable<string> {
    return this.http.put<string>(`${this.apiURL}/actualizar-imagen`, datos, { responseType: 'text' as 'json' });
  }

  // Método del usuario para darse de baja en la aplicación
  eliminarUsuario(email: string): Observable<string> {
    return this.http.delete<string>(`${this.apiURL}/eliminar?email=${encodeURIComponent(email)}`);
  }

  // Obtiene la lista de todos los usuarios con sus datos
  getAll(): Observable<any> {
    return this.http.get<any>(`${this.apiURL}/listar`);
  }

  // Manejador del evento de cambiar la habilitación o deshabilitación de la cuenta del usuario
  cambiarEstadoHabilitado(idUsuario: number): Observable<string> {
    const params = new HttpParams().set('idUsuario', idUsuario.toString());
    return this.http.put<string>(`${this.apiURL}/actualizar-habilitado`,  {}, { params, responseType: 'text' as 'json' });
  }

  // Método del superusuario Admin para crear cuentas administradoras
  generarAdmin(idUsuario: number, emailAdmin: string): Observable<string> {
    const params = new HttpParams()
    .set('idUsuario', idUsuario.toString())
    .set('emailAdmin', emailAdmin.toString());
    return this.http.put<string>(`${this.apiURL}/modificar-rol`, {}, { params, responseType: 'text' as 'json' });
  }

}
