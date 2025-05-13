import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app/app.routes';
import { LOCALE_ID } from '@angular/core';
import { appConfig } from './app/app.config';
import { tokenInterceptor } from './app/interceptor/token.interceptor';
import { registerLocaleData } from '@angular/common';
import localeEs from '@angular/common/locales/es';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes), 
    provideHttpClient(withInterceptors([tokenInterceptor])), 
    { provide: LOCALE_ID, useValue: 'es' },
     ...appConfig.providers
    ],
})
.catch(err => console.error(err));

registerLocaleData(localeEs);