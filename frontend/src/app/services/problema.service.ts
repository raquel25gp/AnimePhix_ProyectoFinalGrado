import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ReporteTipo } from '../interfaces/problema/reporte-tipo';
import { CrearReporte } from '../interfaces/problema/crear-reporte';
import { ListarReportes } from '../interfaces/problema/listar-reportes';

@Injectable({
  providedIn: 'root'
})
export class ProblemaService {
  private apiURL: string = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  // Método para el formulario que devuelve los tipos de problemas guardados en la BBDD
  getNombresTiposProblemas(): Observable<ReporteTipo[]> {
    return this.http.get<ReporteTipo[]>(`${this.apiURL}/tipos-problemas/listado-nombres`);
  }

  // Método para crear un reporte
  crearReporte(reporte: CrearReporte): Observable<string> {
    return this.http.post<string>(`${this.apiURL}/reportes/crear`, reporte, { responseType: 'text' as 'json' });
  }

  // Obtiene una lista de todos los reportes con los datos indicados
  getTodos(): Observable<ListarReportes[]> {
    return this.http.get<ListarReportes[]>(`${this.apiURL}/reportes/listar`);
  }

  // Manejador del evento de cambiar el estado del reporte a corregido. También permite volver a ponerlo como pendiente.
  cambiarEstadoCorregido(idReporte: number): Observable<string> {
    const params = new HttpParams().set('idReporte', idReporte.toString());
    return this.http.put<string>(`${this.apiURL}/reportes/actualizar-estado`, {}, { params, responseType: 'text' as 'json' });
  }
}
