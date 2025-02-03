import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Order } from '../../../core/models/response/pedidos-response';
import { PedidosService } from '../../../core/services/pedidos.service';
import { CommonModule, CurrencyPipe } from "@angular/common";
import { BadgeModule } from "primeng/badge";
import { PanelModule } from "primeng/panel";
import { FormsModule } from "@angular/forms";
import { CalendarModule } from "primeng/calendar";
import { ToolbarModule } from "primeng/toolbar";
import { TableModule } from "primeng/table";
import { DropdownModule } from "primeng/dropdown";
import { InputTextModule } from "primeng/inputtext";

@Component({
  selector: 'app-detalle-pedido',
  standalone: true,
  imports: [
    CurrencyPipe,
    CommonModule,
    BadgeModule,
    PanelModule,
    FormsModule,
    CalendarModule,
    ToolbarModule,
    TableModule,
    DropdownModule,
    InputTextModule
  ],
  templateUrl: './detalle-pedido.component.html',
  styleUrl: './detalle-pedido.component.scss'
})
export class DetallePedidoComponent implements OnInit {

  public idPedido?: Number;
  order?: Order;

  constructor(
    private route: ActivatedRoute,
    private pedidosService: PedidosService
  ) { }
  ngOnInit(): void {
    this.route.params.subscribe(params => this.idPedido = params["idPedido"]);

    this.pedidosService.getDetallePedido(this.idPedido as any).subscribe(order => {
      this.order = order;
      this.loadOrderItems();
    });

    // this.pedidosService.getDetallePedidoItems(this.idPedido as any).subscribe(items =>
    //   // this.order.items = items;
    // );
  }

  loadOrderItems() {
    if (this.idPedido) {
      this.pedidosService.getDetallePedidoItems(this.idPedido as any).subscribe(items => {
        console.log('Items del Pedido:', items);
        if (this.order) {
          this.order.items = items as any;
        }
      });
    }
  }

}
