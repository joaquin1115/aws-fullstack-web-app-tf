import { Routes } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { authGuard } from "../core/guards/auth.guard";
import { ReclamosComponent } from "./reclamos/reclamos.component";

export const PAGES_ROUTES: Routes = [
  {
    path: 'home',
    component: HomeComponent,
    canActivate: []
  },
  {
    path: '',
    loadChildren: () => import('./almacen/almacen.routes').then(m => m.ALMACEN_ROUTES),
  },
  {
    path: '',
    loadChildren: () => import('./control/control.routes').then(m => m.CONTROL_ROUTES),
  },
  {
    path: '',
    loadChildren: () => import('./pedidos/pedidos.routes').then(m => m.PEDIDOS_ROUTES),
  },
  {
    path: '',
    loadChildren: () => import('./reportes/reportes.routes').then(m => m.REPORTES_ROUTES),
  },
  {
    path: '',
    loadChildren: () => import('./reclamos/reclamos.routes').then(m => m.RECLAMOS_ROUTES),
  },
  {
    path: '',
    loadChildren: () => import('./seguimiento/seguimiento.routes').then(m => m.SEGUIMIENTO_ROUTES),
  },
];