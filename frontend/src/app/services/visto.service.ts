import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Visto } from '../interfaces/episodio/visto';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VistoService {

  private apiURL: string = 'http://localhost:8080/episodios-vistos';

  constructor(private http: HttpClient) { }

  // Obtiene si el video está visto o no
  comprobarEstado(datos: Visto): Observable<any> {
    return this.http.post<any>(`${this.apiURL}/comprobar-estado`, datos, { responseType: 'text' as 'json' });
  }

  // Manejador del evento de mar o desmarcar como visto un episodio
  marcarDesmarcarEpisodio(datos: Visto): Observable<string> {
    return this.http.post<string>(`${this.apiURL}/marcar-desmarcar`, datos, { responseType: 'text' as 'json' });
  }
}
