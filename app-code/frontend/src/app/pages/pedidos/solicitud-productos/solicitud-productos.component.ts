import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { DataViewModule } from 'primeng/dataview';
import { TagModule } from 'primeng/tag';
import { RatingModule } from 'primeng/rating';
import { SkeletonModule } from 'primeng/skeleton';
import { PedidosService } from '../../../core/services/pedidos.service';
import { ProductoResponse } from '../../../core/models/response/producto';
import { CommonModule } from '@angular/common';
import { CartComponent } from '../../../shared/components/cart/cart.component';
import { CartService } from '../../../core/services/cart.service';
import { TicketProductoRequest } from '../../../core/models/request/ticket-producto-request';
import { PedidoRequest } from '../../../core/models/request/pedido-request';
import { PedidoFormResponse } from '../../../core/models/response/pedido-form-response';
import { formatDate } from '../../../shared/utils/dateUtil';

@Component({
  selector: 'app-solicitud-productos',
  standalone: true,
  imports: [
    CartComponent,
    CommonModule,
    ButtonModule,
    RatingModule,
    TagModule,
    DataViewModule,
    SkeletonModule
  ],
  templateUrl: './solicitud-productos.component.html',
  styleUrl: './solicitud-productos.component.scss'
})
export class SolicitudProductosComponent implements OnInit {

  layout: string = 'list';
  products: ProductoResponse[] = [];
  pedidoFormResponse?: PedidoFormResponse;
  fechaEntrega?: string;
  fechaSolicitud?: string;

  constructor(
    private pedidoService: PedidosService,
    private router: Router,
    private cartService: CartService
  ) { }

  ngOnInit(): void {
    this.pedidoFormResponse = history.state.formResponse;
    this.fechaEntrega = formatDate(history.state.fechaEntrega);
    this.fechaSolicitud = formatDate(history.state.fechaSolicitud);
    console.log(this.fechaEntrega)
    console.log(this.fechaSolicitud)
    console.log(sessionStorage?.getItem("dni"))
    this.pedidoService.getProductos()
      .subscribe((response) => {
        this.products = response
      });
  }

  complete(): void {
    const ticketProducto = this.getTicketProducto();
    const pedidoRequest: PedidoRequest = {
      idEmpleadoRegistro: Number(sessionStorage?.getItem("idEmpleado")),
      idRepresentante: this.pedidoFormResponse?.idRepresentante,
      idEstadoPedido: "A",
      idTipoPedido: "V",
      fechaEntrega: this.fechaEntrega,
      fechaRegistro: this.fechaSolicitud,
      pedidoTicketProductoRequest: ticketProducto
    }
    console.log(pedidoRequest);
    this.pedidoService.postNewPedido(pedidoRequest).subscribe(response => {
      console.log(response);
      this.router.navigate([`pages/pedidos/detalle/${response.idPedido}`]);
    });
  }
  prevPage() {
    this.router.navigate(['pages/pedidos/proceso/datos-envio']);
  }

  onClickAdd(product: ProductoResponse) {
    this.cartService.addNewProduct(product);
  }

  getSeverity(product: ProductoResponse) {
    return null
  };

  getTicketProducto(): TicketProductoRequest[] {
    return this.cartService.getTicketProducto();
  }
}
