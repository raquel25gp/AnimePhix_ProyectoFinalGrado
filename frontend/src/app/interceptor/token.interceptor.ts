import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

// Se utiliza un interceptor para añadir el token de acceso cuando se realiza una petición HTTP
// El interceptor recibe la petición HTTP original y una función para que la petición siga su camino hacia el servidor
export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  // Se utiliza inject para obtener el auth.service
  const authService = inject(AuthService);
  // Se obtiene el token guardado al iniciar sesión
  const token = authService.obtenerToken();

  if (token) {
    // En caso de que haya token, se clona la petición ya que estas son inmutables,
    req = req.clone({
      // Se añade la cabecera tal y coo está establecida en el servidor
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  // Se envia la petición modificada, o no en caso de que no haya token, al servidor
  return next(req);
};