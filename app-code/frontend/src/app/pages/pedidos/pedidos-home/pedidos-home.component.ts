import { Component, OnInit } from '@angular/core';
import { TableModule } from 'primeng/table';
import { HttpClientModule } from '@angular/common/http';
import { InputTextModule } from 'primeng/inputtext';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { TagModule } from 'primeng/tag';
import { PedidosService } from '../../../core/services/pedidos.service';
import { PedidoListaResponse } from '../../../core/models/response/pedido-lista';
import { ButtonModule } from 'primeng/button';
import { MultiSelectModule } from 'primeng/multiselect';
import { TabMenuModule } from 'primeng/tabmenu';
import { Router } from '@angular/router';

interface Column {
  field: string;
  header: string;
}
interface Filter {
  label: string;
  command: any;
}

@Component({
  selector: 'app-pedidos-home',
  standalone: true,
  imports: [
    TableModule,
    TabMenuModule,
    MultiSelectModule,
    HttpClientModule,
    ButtonModule,
    InputTextModule,
    TagModule,
    IconFieldModule,
    InputIconModule
  ],
  templateUrl: './pedidos-home.component.html',
  styleUrl: './pedidos-home.component.scss'
})
export class PedidosHomeComponent implements OnInit {
  pedidos: PedidoListaResponse[] = [];
  pedidosFilter: PedidoListaResponse[] = [];
  cols!: Column[];
  filters!: Filter[];
  filter!: Filter;
  // selectedPedido!: PedidoListaResponse[];
  selectedPedido!: Column[];

  constructor(
    private pedidoService: PedidosService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.pedidoService.getPedidos()
      .subscribe((response) => {
        this.pedidos = response
        this.pedidosFilter = response
        console.log(this.pedidos)
      });

    this.cols = [
      { field: 'idPedido', header: 'Número de pedido' },
      { field: 'nombre', header: 'Departamento' },
      { field: 'empleado', header: 'Encargado' },
      { field: 'fecha', header: 'Fecha' },
      { field: 'estado', header: 'Estado' }
    ];

    this.filters = [
      { label: 'Todos', command: () => { this.getPedidosFilter('todos') } },
      { label: 'Aceptado', command: () => { this.getPedidosFilter('aceptado') } },
      { label: 'Rechazado', command: () => { this.getPedidosFilter('rechazado') } },
      { label: 'Otros', command: () => { this.getPedidosFilter('otros') } }
    ];

    this.filter = this.filters[0];
    this.selectedPedido = this.cols;
  }

  nextPage() {
    this.router.navigate(['pages/pedidos/proceso/datos-envio']);
  }

  getPedidosFilter(event: string) {
    console.log(event);
    console.log(this.pedidos);
    if (event.toLowerCase() === 'todos') {
      return this.pedidosFilter = this.pedidos;
    } else if (event.toLowerCase() === 'otros') {
      const filterPedidos: PedidoListaResponse[] = this.pedidos.filter((pedido) =>
        pedido.estado.toLowerCase() === 'en proceso' ||
        pedido.estado.toLowerCase() === 'finalizado'
      );
      return this.pedidosFilter = filterPedidos;
    } else {
      const filterPedidos: PedidoListaResponse[] = this.pedidos.filter((pedido) =>
        pedido.estado.toLowerCase() === event.toLowerCase()
      );
      return this.pedidosFilter = filterPedidos;
    }
  }

  getDetalles(idPedido: number): void {
    this.router.navigate([`pages/pedidos/detalle/${idPedido}`]);
  }

  getSeverity(status: string) {
    switch (status.toLowerCase()) {
      case 'rechazado':
        return "danger";

      case 'aceptado':
        return 'success';

      case 'en proceso':
        return 'info';

      case 'finalizado':
        return 'warning';

      default:
        return 'secondary';
    }
  }
}
