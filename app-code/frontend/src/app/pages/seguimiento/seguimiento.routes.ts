import { Routes } from "@angular/router";
import { authGuard } from "../../core/guards/auth.guard";
import { SeguimientoHomeComponent } from "./seguimiento-home/seguimiento-home.component";
import { TrasladosProcesoComponent } from "./traslados/traslados-proceso/traslados-proceso.component";
import { TrasladosProgramadoComponent } from "./traslados/traslados-programado/traslados-programado.component";
import { RutasComponent } from "./rutas/rutas.component";
import { VehiculoComponent } from "../control/vehiculo/vehiculo.component";
import { TransportistasComponent } from "./transportistas/transportistas.component";
import { VehiculosComponent } from "./vehiculos/vehiculos.component";

export const SEGUIMIENTO_ROUTES: Routes = [
  {
    path: 'seguimiento',
    children: [
      {
        path: 'traslados',
        children: [
          {
            path: 'proceso',
            component: TrasladosProcesoComponent,
          },
          {
            path: 'programado',
            component: TrasladosProgramadoComponent,
          },
        ],
      },
      {
        path: 'rutas',
        component: RutasComponent,
      },
      {
        path: 'vehiculos',
        component: VehiculosComponent,
      },
      {
        path: 'transportistas',
        component: TransportistasComponent,
      },
      {
        path: '',
        component: SeguimientoHomeComponent,
      },
    ],
    canActivate: [authGuard],
  },
];