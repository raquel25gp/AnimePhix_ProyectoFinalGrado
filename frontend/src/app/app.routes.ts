import { Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
    {
        path: 'inicio',
        loadComponent: () =>
            import('./components/paginas/inicio/inicio.component').then(
                m => m.InicioComponent
            )
    },
    {
        path: 'directorio',
        loadComponent: () =>
            import('./components/paginas/directorio/directorio.component').then(
                m => m.DirectorioComponent
            )
    },
    {
        path: 'calendario',
        canActivate: [AuthGuard],
        loadComponent: () =>
            import('./components/paginas/calendario/calendario.component').then(
                m => m.CalendarioComponent
            )
    },
    {
        path: 'registrar',
        loadComponent: () =>
            import('./components/paginas/registrar/registrar.component').then(
                m => m.RegistrarComponent
            )
    },
    {
        path: 'iniciarSesion',
        loadComponent: () =>
            import('./components/paginas/login/login.component').then(
                m => m.LoginComponent
            )
    },
    {
        path: 'recuperar-password',
        loadComponent: () =>
            import('./components/paginas/recuperar-password/recuperar-password.component').then(
                m => m.RecuperarPasswordComponent
            )
    },
    {
        path: 'anime/:nombre',
        loadComponent: () =>
            import('./components/paginas/anime/anime.component').then(
                m => m.AnimeComponent
            )
    },
    {
        path: 'anime/:nombre/episodio/:numero',
        loadComponent: () =>
            import('./components/paginas/episodio/episodio.component').then(
                m => m.EpisodioComponent
            )
    },
    {
        path: 'perfil',
        canActivate: [AuthGuard],
        loadComponent: () =>
            import('./components/paginas/perfil/perfil.component').then(
                m => m.PerfilComponent
            )
    },
    {
        path: 'favoritos',
        canActivate: [AuthGuard],
        loadComponent: () =>
            import('./components/paginas/favoritos/favoritos.component').then(
                m => m.FavoritosComponent
            )
    },
    {
        path: 'admin-tools',
        canActivate: [AuthGuard],
        loadComponent: () =>
            import('./components/paginas/admin-tools/admin-tools.component').then(
                m => m.AdminToolsComponent
            )
    },
    {
        path: 'crear-anime',
        loadComponent: () =>
            import('./components/paginas/admin-tools/admin-animes/crear-anime/crear-anime.component').then(
                m => m.CrearAnimeComponent
            )
    },
    {
        path: 'subir-episodio',
        loadComponent: () =>
            import('./components/paginas/admin-tools/admin-episodios/subir-episodio/subir-episodio.component').then(
                m => m.SubirEpisodioComponent
            )
    },
    {
        path: 'sobre-anime-phix',
        loadComponent: () =>
            import('./components/paginas/footer/sobre-anime-phix/sobre-anime-phix.component').then(
                m => m.SobreAnimePhixComponent
            )
    },
    {
        path: 'terminos',
        loadComponent: () =>
            import('./components/paginas/footer/terminos/terminos.component').then(
                m => m.TerminosComponent
            )
    },
    {
        path: 'politica-privacidad',
        loadComponent: () =>
            import('./components/paginas/footer/politica-privacidad/politica-privacidad.component').then(
                m => m.PoliticaPrivacidadComponent
            )
    },
    {
        path: 'reportar-problema',
        loadComponent: () =>
            import('./components/paginas/footer/reportar-problema/reportar-problema.component').then(
                m => m.ReportarProblemaComponent
            )
    },
    { path: '', redirectTo: 'inicio', pathMatch: 'full' },
    {
        path: '**',
        loadComponent: () =>
            import('./components/page-not-found/page-not-found.component').then(
                m => m.PageNotFoundComponent
            )
    }
];
