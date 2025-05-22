import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { NotificacionService } from '../../../services/notificacion.service';
import { AuthService } from '../../../services/auth.service';
import { Notificacion } from '../../../interfaces/notificacion/notificacion';

interface DiaCalendario {
  fecha: Date;
  esHoy: boolean;
  esMesActual: boolean;
  eventos: { title: string }[];
}

@Component({
  selector: 'app-calendario',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './calendario.component.html',
  styleUrl: './calendario.component.css'
})
export class CalendarioComponent implements OnInit {
  email: string = '';
  fechaVisualizacion: Date = new Date();
  diasSemana = ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'];
  diasCalendario: DiaCalendario[][] = [];
  indiceSemanaExpandida: number | null = null;
  diaSeleccionado: DiaCalendario | null = null;
  fechaSeleccionada: NgbDate | null = null;
  mostrarFormulario = false;
  nuevoEvento = '';

  constructor(private authService: AuthService, private notificacionService: NotificacionService) { }

  ngOnInit(): void {
    // Cuando se genera el componente, se obtiene el token del usuario
    const datos = this.authService.obtenerDatosToken();
    if (datos) {
      // En caso de que exista el token se guarda el email del usuario
      this.email = datos.email;
    }
    this.generarCalendario();
    this.cargarNotificacionesBackend();
  }

  // Método para crear los días del calendario mensual organizado por semanas
  generarCalendario(): void {
    const inicio = new Date(this.fechaVisualizacion.getFullYear(), this.fechaVisualizacion.getMonth(), 1);
    const fin = new Date(this.fechaVisualizacion.getFullYear(), this.fechaVisualizacion.getMonth() + 1, 0);
    const diaSemanaInicio = inicio.getDay() || 7;
    const listadoDias: DiaCalendario[] = [];
    const hoy = new Date();

    // Genero cada día del calendario. Si el mes no empieza en lunes, llena los huecos anteriores
    for (let i = 1 - (diaSemanaInicio - 1); i <= fin.getDate(); i++) {
      const fecha = new Date(this.fechaVisualizacion.getFullYear(), this.fechaVisualizacion.getMonth(), i);
      listadoDias.push({
        fecha,
        esHoy: this.mismoDia(fecha, hoy), // Si es el día de hoy se aplica la función
        esMesActual: fecha.getMonth() === this.fechaVisualizacion.getMonth(), // Compruebo si es el mes actual
        eventos: []
      });
    }

    // Agrupo los días de 7 en 7 para representar semanas del calendario.
    this.diasCalendario = [];
    for (let i = 0; i < listadoDias.length; i += 7) {
      this.diasCalendario.push(listadoDias.slice(i, i + 7));
    }
  }

  // Método del botón para ir al mes anterior y cargar sus datos
  anteriorMes(): void {
    this.fechaVisualizacion = new Date(this.fechaVisualizacion.getFullYear(), this.fechaVisualizacion.getMonth() - 1, 1);
    this.resetearDiaSeleccionado();
    this.generarCalendario();
    this.cargarNotificacionesBackend();
  }

  // Método del botón para ir al mes siguiente y cargar sus datos
  siguienteMes(): void {
    this.fechaVisualizacion = new Date(this.fechaVisualizacion.getFullYear(), this.fechaVisualizacion.getMonth() + 1, 1);
    this.resetearDiaSeleccionado();
    this.generarCalendario();
    this.cargarNotificacionesBackend();
  }

  // Reinicio los valores cuando se selecciona un día diferente
  resetearDiaSeleccionado(): void {
    this.indiceSemanaExpandida = null;
    this.diaSeleccionado = null;
    this.fechaSeleccionada = null;
    this.mostrarFormulario = false;
    this.nuevoEvento = '';
  }

  // Método para comprobar si el día pasado como parámetro es el actual
  mismoDia(fecha: Date, hoy: Date): boolean {
    return fecha.getDate() === hoy.getDate()
      && fecha.getMonth() === hoy.getMonth()
      && fecha.getFullYear() === hoy.getFullYear();
  }

  // Método para cargar las notificaciones del usuario
  cargarNotificacionesBackend(): void {
    this.notificacionService.getNotificaciones(this.email).subscribe(
      (notificaciones) => {
        // Recorro el calendario por semanas y dias
        for (const semana of this.diasCalendario) {
          for (const dia of semana) {
            // Obtengo la fecha y la guardo
            const fechaDia = new Date(dia.fecha);
            fechaDia.setHours(12, 0, 0, 0); // Establezo de hora las 12:00 para evitar errores por defases

            // Formateo el dia de la semana para que 0 sea lunes y asi sucesivamente
            const diaSemana = (fechaDia.getDay() + 6) % 7;

            // Obtengo las notificaciones que pertenecen a este día según el tipo que es
            const eventosDelDia = notificaciones.filter(notif => {
              const fechaCreacion = new Date(notif.fechaInicio);
              fechaCreacion.setHours(12, 0, 0, 0);

              // Compruebo si hay fecha fin y le establezco la hora en caso de que exista
              let fechaFin: Date | null = null;
              if (notif.fechaFin) {
                fechaFin = new Date(notif.fechaFin);
                fechaFin.setHours(12, 0, 0, 0);
              }

              // Notificación tipo 1: El evento es de un solo día
              if (fechaCreacion && fechaFin && fechaCreacion.toDateString() === fechaFin.toDateString()) {
                return fechaDia.toDateString() === fechaCreacion.toDateString(); // Devuelve la/s notificación/es para ese dia
              }

              // Si no coincide el dia de la semana con el de la notificación,
              // se sale del filtro ya que no pertenece a ese día la notificación
              // Se comprueba despues de los eventos personalizados
              const coincideDiaSemana = notif.diaSemana === diaSemana;
              if (!coincideDiaSemana) return false;


              // Notificación tipo 2: El evento no tiene fecha fin, es decir, es indefinido
              if (fechaCreacion && !fechaFin) {
                return fechaDia >= fechaCreacion; // Si la fecha del dia es mayor que cuando se creo la notificación, se devuelve de forma indefinida
              }

              // Notificación tipo 3: El evento esta limitado entre dos fechas
              if (fechaCreacion && fechaFin) {
                return fechaDia >= fechaCreacion && fechaDia <= fechaFin; // Devuelve la/s notificacioón/es entre esas fechas
              }

              return false; // Si no se contempla, no se muestra el evento
            });

            // Guardo los eventos en el dia del calendario
            dia.eventos = eventosDelDia.map(n => ({
              title: n.texto
            }));
          }
        }
      }
    );
  }

  diaClicado(dia: DiaCalendario, indiceSemana: number) {
    // Si ya está expandido ese día, se cierra
    if (
      this.indiceSemanaExpandida === indiceSemana &&
      this.diaSeleccionado &&
      this.mismoDia(this.diaSeleccionado.fecha, dia.fecha)
    ) {
      this.indiceSemanaExpandida = null;
      this.diaSeleccionado = null;
      this.fechaSeleccionada = null;
      this.mostrarFormulario = false;
      this.nuevoEvento = '';
      return;
    }

    // Si es otro día o semana, se expande el apartado normalmente
    this.diaSeleccionado = dia;
    // Convierto esa fecha a un tipo especial (NgbDate) que usa el calendario de Bootstrap
    this.fechaSeleccionada = new NgbDate(dia.fecha.getFullYear(), dia.fecha.getMonth() + 1, dia.fecha.getDate());
    this.mostrarFormulario = true;
    this.nuevoEvento = '';
    this.indiceSemanaExpandida = indiceSemana;
  }

  // Método para agregar eventos personalizados del usuario
  agregarEvento(): void {
    if (this.fechaSeleccionada && this.nuevoEvento.trim()) {
      const fecha = new Date(this.fechaSeleccionada.year, this.fechaSeleccionada.month - 1, this.fechaSeleccionada.day, 12, 0, 0);

      // Datos para el backend
      const notificacion: Notificacion = {
        email: this.email,
        texto: this.nuevoEvento.trim(),
        fecha: fecha
      };

      this.notificacionService.crearNotificacion(notificacion).subscribe(
        () => {
          // Muestro la notificación en el calendario si se guarda correctamente
          const evento = { title: notificacion.texto };
          for (const semana of this.diasCalendario) {
            for (const dia of semana) {
              if (this.mismoDia(dia.fecha, fecha)) {
                dia.eventos.push(evento);
                break;
              }
            }
          }

          // Limpio formulario
          this.nuevoEvento = '';
          this.mostrarFormulario = false;
        }
      );
    }
  }

  // Método para eliminar la notificación del calendario y de la BBDD
  eliminarNotificacion(dia: DiaCalendario, evento: { title: string }) {
    // Elimino la notificación del array
    dia.eventos = dia.eventos.filter(e => e !== evento);

    // Sumo un día a la fecha por el UTC
    const fechaModificada = new Date(dia.fecha);
    fechaModificada.setDate(fechaModificada.getDate() + 1);

    // Datos para el backend
    const notificacion: Notificacion = {
      email: this.email,
      texto: evento.title,
      fecha: fechaModificada
    };

    // Llamo el servicio del backend
    this.notificacionService.eliminarNotificacion(notificacion).subscribe(
      () => {
        // Espera 1 segundo y luego recarga la página para visualizar los cambios
        setTimeout(() => {
          window.location.reload();
        }, 1000);
      }
    );
  }
}
