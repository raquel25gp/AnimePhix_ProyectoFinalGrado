import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { UsuarioService } from '../../../../services/usuario.service';
import { HabilitadosComponent } from "./habilitados/habilitados.component";
import { DeshabilitadosComponent } from "./deshabilitados/deshabilitados.component";
import { CuentasAdminsComponent } from "./cuentas-admins/cuentas-admins.component";

@Component({
  selector: 'app-admin-usuarios',
  imports: [HabilitadosComponent, DeshabilitadosComponent, CuentasAdminsComponent],
  templateUrl: './admin-usuarios.component.html',
  styleUrl: './admin-usuarios.component.css'
})
export class AdminUsuariosComponent implements OnInit {
  listadoUsuarios: any[] = [];
  listadoDeshabilitados: any[] = [];
  listadoHabilitados: any[] = [];
  listadoAdmins: any[] = [];
  mensaje: string = '';
  mensajeError: boolean = false;

  constructor(private usuarioService: UsuarioService) { }

  ngOnInit(): void {
    // Obtengo un listado de todos los usuarios al crear el componente
    this.usuarioService.getAll().subscribe(
      (data) => {
        this.listadoUsuarios = data;
        this.mensajeError = false;
        this.clasificar();
      }
    );
  }

  // Clasifico los usuarios según si son administradores, usuarios habilitados o usuarios deshabilitados
  clasificar() {
    for (let user of this.listadoUsuarios) {
      if (user.rol.idRol == 2) {
        if (user.habilitado === false) {
          this.listadoDeshabilitados.push(user);
        } else {
          this.listadoHabilitados.push(user);
        }
      } else {
        this.listadoAdmins.push(user);
      }
    }
  }
}
