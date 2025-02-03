import { Component, OnInit } from '@angular/core';
import { MenuItem, MessageService } from 'primeng/api';
import { Subscription } from 'rxjs';
import { StepsModule } from 'primeng/steps';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-proceso-pedido',
  standalone: true,
  imports: [StepsModule, ToastModule],
  providers: [MessageService],
  templateUrl: './proceso-pedido.component.html',
  styleUrl: './proceso-pedido.component.scss'
})
export class ProcesoPedidoComponent implements OnInit {
  items?: MenuItem[];
  subscription?: Subscription;

  ngOnInit() {
    this.items = [
      {
        label: 'Datos de envío',
        routerLink: 'datos-envio'
      },
      {
        label: 'Solicitud de productos',
        routerLink: 'solicitud-productos'
      }
    ];
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
