import { jwtDecode } from 'jwt-decode';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { JwtPayload } from '../interfaces/usuario/JwtPayload';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiURL: string = 'http://localhost:8080/login';
  private readonly TOKEN_KEY = 'auth-token'; // Nombre de la clave en localStorage
  private loginStatus = new BehaviorSubject<boolean>(this.estaLogueado());

  constructor(private http: HttpClient) { }

  // Método para loguearse en la aplicación guardando el token recibido
  login(email: string, password: string): Observable<any> {
    return this.http.post<{ token: string }>(`${this.apiURL}`, { email, password })
      // Utilizo pipe para trabajar con el token devuelto
      .pipe(
        // tap me permite trabajar con el token sin modificarlo
        tap(response => {
          // Guardo el token para que el usuario quede autenticado en la app
          this.guardarToken(response.token);         
        })
      );
  }

  // Elimina el token para cerrar sesión
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.loginStatus.next(false); // Notifica el nuevo estado
  }

  // Recupera el token guardado, para el interceptor
  obtenerToken(): string | null {
    try {
      return localStorage.getItem(this.TOKEN_KEY);
    } catch (e) {
      console.error('Error al acceder al localStorage:', e);
      return null;
    }
  }

  // Guarda el token en el navegador usando localStorage
  guardarToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  //Devuelve true o false si hay un token o no teniendo en cuenta su expiracion
  estaLogueado(): boolean {
    const token = this.obtenerToken();
    if (!token) return false;

    try {
      // Obtengo la fecha de expiración del token, en segundos, decodificandolo con la libreria jwt-decode
      const { exp } = jwtDecode<JwtPayload>(token);
      // Si el token, por algun motivo, no devuelve expiración, se asume que está logueado
      if (!exp) return true;

      // Obtengo el tiempo actual en segundos de la aplicación
      const ahora = Math.floor(Date.now() / 1000);

      // Si la fecha de expiración es superior a la actual, el token todavía no ha caducado
      return exp > ahora;

    } catch {
      // En caso de error se devuelve un false 
      return false;
    }
  }

  // Método reactivo para obtener el estado del usuario
  get loginStatus$(): Observable<boolean> {
    return this.loginStatus.asObservable();
  }

  // Notifica que el usuario ha iniciado sesión
  notificarLogin() {
    this.loginStatus.next(true);
  }

  // Obtiene los datos del token decodificados
  obtenerDatosToken(): JwtPayload | null {
    const token = localStorage.getItem(this.TOKEN_KEY);
    if (token) {
      try {
        // Si se encuentra un token, se decodifica para obtener sus datos
        return jwtDecode<JwtPayload>(token);
      } catch (e) {
        // En caso de error, se muestra en la consola y no se devuelve nada
        console.error('Error al decodificar el token:', e);
        return null;
      }
    }
    return null;
  }
}
