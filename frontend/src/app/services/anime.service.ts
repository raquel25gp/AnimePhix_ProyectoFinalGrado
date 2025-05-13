import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AnimeNombre } from '../interfaces/anime/anime-nombre';
import { TarjetaDirectorio } from '../interfaces/anime/tarjeta-directorio';
import { FiltroDirectorio } from '../interfaces/anime/filtro-directorio';
import { AnimePagina } from '../interfaces/anime/anime-pagina';
import { AnimeActualizar } from '../interfaces/anime/anime-actualizar';
import { CrearAnime } from '../interfaces/anime/crear-anime';

@Injectable({
  providedIn: 'root'
})
export class AnimeService {
  private apiURL: string = 'http://localhost:8080/animes';

  constructor(private http: HttpClient) { }

  // Obtiene la lista de los animes que se encuentran en estado de emisión
  getNombreAnimesEnEmision(): Observable<AnimeNombre[]> {
    return this.http.get<AnimeNombre[]>(`${this.apiURL}/en-emision`);
  }

  // Obtiene la lista de todos los animes con los datos para el directorio
  getTodosAnimes(): Observable<TarjetaDirectorio[]> {
    return this.http.get<TarjetaDirectorio[]>(`${this.apiURL}/tarjeta-directorio`);
  }

  // Método para obtener los datos por los que se puede filtrar en el directorio
  getDatosFiltrosDirectorio(): Observable<FiltroDirectorio[]> {
    return this.http.get<FiltroDirectorio[]>(`${this.apiURL}/filtros-directorio`);
  }

  // Obtiene una lista de animes según los filtros pasados como parámetros
  getAnimesFiltrados(filtros: any): Observable<TarjetaDirectorio[]> {
    let params = new HttpParams();
    if (filtros.genero) params = params.set('genero', filtros.genero);
    if (filtros.anio) params = params.set('anio', filtros.anio);
    if (filtros.estado) params = params.set('estado', filtros.estado);
    if (filtros.orden) params = params.set('orden', filtros.orden);

    return this.http.get<TarjetaDirectorio[]>(`${this.apiURL}/establecer-filtros`, { params });
  }

  // Obtiene los animes que coinciden con la palabra introducida en la barra de búsqueda
  getAnimeBuscado(nombre: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiURL}/buscar-anime?nombre=${nombre}`);

  }

  // Obtiene los datos necesarios para la página del anime pasado como parámetro
  getAnimePagina(nombre: string): Observable<AnimePagina> {
    return this.http.get<AnimePagina>(`${this.apiURL}/anime-datos?nombre=${nombre}`);
  }

  // Obtiene una lista con todos los animes y sus datos
  getTodos(): Observable<any> {
    return this.http.get<any>(`${this.apiURL}/listar-todos`);
  }

  // Método de los administradores para poder crear animes en la BBDD
  crearAnime(anime: CrearAnime): Observable<string> {
      return this.http.post<string>(`${this.apiURL}/crear`, anime, { responseType: 'text' as 'json' });
    }

  // Método de los administradores para actualizar datos del anime pasado como parámetro
  actualizarAnime(anime: AnimeActualizar, idAnime: number): Observable<string> {
    const params = new HttpParams().set('idAnime', idAnime.toString());
    return this.http.put<string>(`${this.apiURL}/actualizar`, anime, { params, responseType: 'text' as 'json' });
  }

  // Manejador del evento de cambiar la visibilidad del anime pasado como parámetro
  cambiarEstadoVisual(idAnime: number): Observable<string> {
    const params = new HttpParams().set('idAnime', idAnime.toString());
    return this.http.put<string>(`${this.apiURL}/cambiar-visibilidad`, {}, { params, responseType: 'text' as 'json' });
  }

  // Método que permite a los administradores eliminar un anime completo, con sus episodios y todo lo que tenga relacionado al mismo
  eliminarAnime(idAnime: number): Observable<string> {
    const params = new HttpParams().set('idAnime', idAnime.toString());
    return this.http.delete<string>(`${this.apiURL}/eliminar`, {
      params: new HttpParams().set('idAnime', idAnime.toString()),
      responseType: 'text' as 'json'
    });
  }
}
