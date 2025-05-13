import { NombreNumLista } from "../episodio/nombre-num-lista";

export interface AnimePagina {
    nombre: string;
    descripcion: string;
    urlImagen: string;
    genero: string;
    estado: string;
    listadoEpisodios: NombreNumLista[];
}
