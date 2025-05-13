import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Notificacion } from '../interfaces/notificacion/notificacion';
import { Observable } from 'rxjs';
import { NotificacionCalendario } from '../interfaces/notificacion/notificacion-calendario';

@Injectable({
  providedIn: 'root'
})
export class NotificacionService {
  private apiURL: string = 'http://localhost:8080/notificaciones';

  constructor(private http: HttpClient) { }

  // Método para crear una notificación en el calendario ya sea por animes favoritos o personalizada
  crearNotificacion(datos: Notificacion): Observable<string> {
    return this.http.post<string>(`${this.apiURL}/crear`, datos, { responseType: 'text' as 'json' });
  }

  // Obtiene todas las notificaciones del usuario
  getNotificaciones(email: string): Observable<NotificacionCalendario[]> {
    return this.http.get<NotificacionCalendario[]>(`${this.apiURL}/listar?email=${email}`);
  }

  // Método que permite al usuario eliminar sus notificaciones personalizadas
  eliminarNotificacion(datos: Notificacion): Observable<string> {
    return this.http.delete<string>(`${this.apiURL}/eliminar`, { body: datos, responseType: 'text' as 'json' });
  }
}
