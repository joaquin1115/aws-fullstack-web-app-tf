import { Routes } from "@angular/router";
import { DatosEnvioComponent } from "./datos-envio/datos-envio.component";
import { ProcesoPedidoComponent } from "./proceso-pedido/proceso-pedido.component";
import { SolicitudProductosComponent } from "./solicitud-productos/solicitud-productos.component";
import { DetallePedidoComponent } from "./detalle-pedido/detalle-pedido.component";
import { PedidosHomeComponent } from "./pedidos-home/pedidos-home.component";
import { modulesGuard } from "../../core/guards/modules.guard";
import { authGuard } from "../../core/guards/auth.guard";

export const PEDIDOS_ROUTES: Routes = [
  {
    path: 'pedidos',
    children: [
      {
        path: 'proceso',
        component: ProcesoPedidoComponent,
        children: [
          {
            path: 'datos-envio',
            component: DatosEnvioComponent
          },
          {
            path: 'solicitud-productos',
            component: SolicitudProductosComponent
          },
        ]
      },
      {
        path: 'detalle/:idPedido',
        component: DetallePedidoComponent,
      },
      {
        path: '',
        component: PedidosHomeComponent
      }
    ],
    canActivate: [authGuard]
  },
];