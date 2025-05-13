import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UltimosEpisodios } from '../interfaces/episodio/ultimos-episodios';
import { EpisodioVideo } from '../interfaces/episodio/episodio-video';
import { CrearEpisodio } from '../interfaces/episodio/crear-episodio';

@Injectable({
  providedIn: 'root'
})
export class EpisodioService {
  private apiURL: string = 'http://localhost:8080/episodios';

  constructor(private http: HttpClient) { }

  // Obtiene una lista con los últimos 9 episodios publicados recientemente
  getUltimosEpisodios(): Observable<UltimosEpisodios[]> {
    return this.http.get<UltimosEpisodios[]>(`${this.apiURL}/recientes`);
  }

  // Obtiene datos de un episodio de un anime en específico
  getEpisodioEspecifico(nombre: string, numero: number): Observable<EpisodioVideo> {
    return this.http.get<EpisodioVideo>(`${this.apiURL}/especifico?nombre=${nombre}&numero=${numero}`);
  }

  // Obtiene el número total de episodios del anime pasado como parámetro
  getTotalEpisodios(nombre: string): Observable<number> {
    return this.http.get<number>(`${this.apiURL}/total-anime?nombre=${nombre}`);
  }
  
  // Obtiene una lista de todos los episodios con todos sus datos
  getAll(): Observable<any> {
    return this.http.get<any>(`${this.apiURL}/listar-todos`);
  }

  // Método que permite a los administradores subir nuevos episodios a la BBDD
  crearEpisodio(episodio: CrearEpisodio): Observable<string> {
    return this.http.post<string>(`${this.apiURL}/crear`, episodio, { responseType: 'text' as 'json' });
  }

  // Método que permite a los administradores actualizar datos de un episodio en específico
  actualizarEpisodio(datosEpisodio: any, idAnime: number, numEpisodio: number): Observable<string> {
    const params = new HttpParams()
      .set('idAnime', idAnime.toString())
      .set('numEpisodio', numEpisodio.toString());

    return this.http.put<string>(`${this.apiURL}/actualizar`, datosEpisodio, { params, responseType: 'text' as 'json' });
  }
}
