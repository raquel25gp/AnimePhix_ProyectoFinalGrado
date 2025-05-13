export interface JwtPayload {
    authority: string;
    email: string;
    exp: number;
    iat: number;
    sub: string;
    [key: string]: any; // por si el token tiene más campos
}
