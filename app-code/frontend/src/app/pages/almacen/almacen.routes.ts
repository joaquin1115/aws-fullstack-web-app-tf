import { Routes } from "@angular/router";
import { modulesGuard } from "../../core/guards/modules.guard";
import { authGuard } from "../../core/guards/auth.guard";
import { AlmacenComponent } from "./almacen.component";
import { VistaProcesosComponent } from "./vista-procesos/vista-procesos.component";
import { RegistroOperacionComponent } from "./registro-operacion/registro-operacion.component";

export const ALMACEN_ROUTES: Routes = [
  {
    path: 'almacen',
    component: AlmacenComponent,
    canActivate: [authGuard],
    children: [
      { path: '', component: VistaProcesosComponent },
      { path: 'vista-procesos', component: VistaProcesosComponent },
      { path: 'registro-operacion', component: RegistroOperacionComponent }
    ]
  },
];