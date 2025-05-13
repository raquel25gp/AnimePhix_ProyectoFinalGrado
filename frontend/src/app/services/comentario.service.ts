import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Comentario } from '../interfaces/episodio/comentario';
import { Observable } from 'rxjs';
import { ListadoComentarios } from '../interfaces/episodio/listado-comentarios';
import { NombreNumLista } from '../interfaces/episodio/nombre-num-lista';

@Injectable({
  providedIn: 'root'
})
export class ComentarioService {
  private apiURL: string = 'http://localhost:8080/comentarios';

  constructor(private http: HttpClient) { }

  // Método que permite a los usuarios registrados agregar comentarios a un episodio
  agregarComentario(datos: Comentario): Observable<string> {
    return this.http.post<string>(`${this.apiURL}/agregar`, datos, { responseType: 'text' as 'json' });
  }

  // Obtiene la lista de comentarios que tiene el episodio enviado como parámetro
  listarComentarios(datos: NombreNumLista): Observable<ListadoComentarios[]> {
    return this.http.post<ListadoComentarios[]>(`${this.apiURL}/listar`, datos, { responseType: 'text' as 'json' });
  }

  // Método de los administradores para ocultar comentarios
  cambiarVisibilidad(datosComentario: ListadoComentarios, nombreAnime: string, numEpisodio: number): Observable<string> {
    const params = new HttpParams()
      .set('nombreAnime', nombreAnime.toString())
      .set('numEpisodio', numEpisodio.toString());
      
    return this.http.put<string>(`${this.apiURL}/cambiar-visibilidad`, datosComentario, { params, responseType: 'text' as 'json' });
  }

  // Obtiene la lista de todos los comentarios que han sido ocultados
  getAll(): Observable<any> {
    return this.http.get<any>(`${this.apiURL}/listar-ocultos`);
  }
}
