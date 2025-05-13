import { CommonModule } from '@angular/common';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FiltroDirectorio } from '../../../../interfaces/anime/filtro-directorio';
import { AnimeService } from '../../../../services/anime.service';


@Component({
  selector: 'app-filtro',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './filtro.component.html',
  styleUrl: './filtro.component.css'
})
export class FiltroComponent implements OnInit {
  listaDatosFiltros: FiltroDirectorio[] = [];
  generos: String[] = [];
  anios: String[] = [];
  estados: String[] = [];
  fechaInicio: Date[] = [];
  ordenes = ['Por defecto', 'Ascendente', 'Descendente', 'Añadidos recientemente'].map(o => `Órden: ${o}`);
  @Output() aplicarFiltros = new EventEmitter<any>();
  //Filtros seleccionados por el usuario
  generoSeleccionado: String = '';
  anioSeleccionado: String = '';
  estadoSeleccionado: String = '';
  ordenSeleccionado: String = '';

  constructor(private animeService: AnimeService) { }

  ngOnInit(): void {
    this.animeService.getDatosFiltrosDirectorio().subscribe(data => {
      this.listaDatosFiltros = data;
      this.generos = ['Género: Todos', ...this.listaDatosFiltros // Establezo el predeterminado
        .sort((gen1, gen2) => gen1.genero.toLowerCase().localeCompare(gen2.genero.toLowerCase()))
        .map(g => `Género: ${g.genero}`) // Los mapeo añadiendo su etiqueta delante
        .filter((genero, index, listado) => genero && listado.indexOf(genero) === index)]; // Elimino duplicados
      this.anios = ['Año: Todos', ...this.listaDatosFiltros
        .sort((anio1, anio2) => anio1.anio - anio2.anio)
        .map(a => `Año: ${a.anio}`)
        .filter((anio, index, listado) => anio && listado.indexOf(anio) === index)];
      this.estados = ['Estado: Todos', ...this.listaDatosFiltros
        .sort((est1, est2) => est1.estado.toLowerCase().localeCompare(est2.estado.toLowerCase()))
        .map(e => `Estado: ${e.estado}`)
        .filter((estado, index, listado) => estado && listado.indexOf(estado) === index)];
      this.fechaInicio = this.listaDatosFiltros
        .map(g => g.fechaInicio)
        .filter(f => !!f); // Elimino valores vacios, nulos o con formato incorrecto

      // Establezco el valor por defecto después de cargar los filtros
      this.generoSeleccionado = this.generos[0];
      this.anioSeleccionado = this.anios[0];
      this.estadoSeleccionado = this.estados[0];
      this.ordenSeleccionado = this.ordenes[0];
    });
  }

  // Le paso los valores limpios al directorio
  filtrar() {
    const filtrosLimpios: any = {};

    const genero = this.generoSeleccionado.split(': ')[1];
    const anio = this.anioSeleccionado.split(': ')[1];
    const estado = this.estadoSeleccionado.split(': ')[1];
    const orden = this.ordenSeleccionado.split(': ')[1];

    if (genero && genero !== 'Todos') filtrosLimpios.genero = genero;
    if (anio && anio !== 'Todos') filtrosLimpios.anio = anio;
    if (estado && estado !== 'Todos') filtrosLimpios.estado = estado;
    if (orden && orden !== 'Por defecto') filtrosLimpios.orden = orden;

    this.aplicarFiltros.emit(filtrosLimpios);
  }
}
