import { Routes } from "@angular/router";
import { authGuard } from "../../core/guards/auth.guard";
import { ReclamosComponent } from "./reclamos.component";
import { ListaReclamoComponent } from "./lista-reclamo/lista-reclamo.component";
import { DatosClienteComponent } from "./datos-cliente/datos-cliente.component";
import { ReclamosVisorComponent } from "./reclamos-visor/reclamos-visor.component";

export const RECLAMOS_ROUTES: Routes = [
  {
    path: 'reclamos',
    component: ReclamosComponent,
    children: [
      {
        path: '',
        component: ListaReclamoComponent
      },
      {
        path: 'datos-cliente',
        component: DatosClienteComponent
      },
      {
        path: 'visor/:codReclamo',
        component: ReclamosVisorComponent
      }
    ],
    canActivate: [authGuard]
  },
];