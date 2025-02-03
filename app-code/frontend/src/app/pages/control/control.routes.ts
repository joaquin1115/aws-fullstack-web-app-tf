import { Routes } from "@angular/router";
import { modulesGuard } from "../../core/guards/modules.guard";
import { authGuard } from "../../core/guards/auth.guard";
import { ControlComponent } from "./control.component";
import { TransportistaComponent } from "./transportista/transportista.component";
import { VehiculoComponent } from "./vehiculo/vehiculo.component";
import { IncidenciasComponent } from "./incidencias/incidencias.component";
import { RegistroIncidenciaComponent } from "./registro-incidencia/registro-incidencia.component";

export const CONTROL_ROUTES: Routes = [
  {
    path: 'control',
    component: ControlComponent,
    children: [
      { path: '', component: TransportistaComponent },
      { path: 'vehiculo', component: VehiculoComponent },
      { path: 'incidencias', component: IncidenciasComponent },
      { path: 'registro-incidencias', component: RegistroIncidenciaComponent }
    ],
    canActivate: [authGuard]
  },
];