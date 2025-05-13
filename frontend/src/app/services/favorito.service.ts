import { Injectable } from '@angular/core';
import { AnimesFavoritos } from '../interfaces/favorito/animes-favoritos';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ListaFavoritos } from '../interfaces/favorito/lista-favoritos';

@Injectable({
  providedIn: 'root'
})
export class FavoritoService {
  private apiURL: string = 'http://localhost:8080/favoritos';

  constructor(private http: HttpClient) { }

  // Método que permite al usuario agregar animes como favoritos.
  agregarFavorito(datos: AnimesFavoritos): Observable<string> {
    return this.http.post<string>(`${this.apiURL}/agregar`, datos, { responseType: 'text' as 'json' });
  }

  // Obtiene todos los animes favoritos del usuario
  getFavoritos(email: string): Observable<ListaFavoritos[]> {
    return this.http.get<ListaFavoritos[]>(`${this.apiURL}/mostrar?email=${email}`);
  }

  // Método que permite al usuario eliminar sus animes favoritos
  eliminarFavorito(datos: AnimesFavoritos): Observable<string> {
    return this.http.delete<string>(`${this.apiURL}/eliminar`, { body: datos, responseType: 'text' as 'json' });
  }
}
