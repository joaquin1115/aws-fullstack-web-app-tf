import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { authGuard } from './core/guards/auth.guard';
import { PagesComponent } from './pages/pages.component';
import { loginGuard } from './core/guards/login.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: '',
    loadChildren: () => import('./auth/auth.routes').then(m => m.AUTH_ROUTES),
    canActivate: [loginGuard]
  },
  {
    component: PagesComponent,
    path: 'pages',
    loadChildren: () => import('./pages/pages.routes').then(m => m.PAGES_ROUTES),
    canActivate: [authGuard],
  }
];
