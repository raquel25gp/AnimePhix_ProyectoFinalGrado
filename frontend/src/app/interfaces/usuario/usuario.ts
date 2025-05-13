export interface Usuario {
    idUsuario: number;
    nombre: string;
    email: string;
    password: string;
    urlImagen: string;
    habilitado: boolean;
    rol: {
        idRol: number,
        nombre: string
    }
}
