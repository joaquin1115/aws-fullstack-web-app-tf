import { Component, OnInit } from '@angular/core';
import { SeguimientoService } from '../../../../core/services/seguimiento.service';
import { PanelModule } from 'primeng/panel';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import {
  SeguimientoTrasladoDetalleResponse,
  SeguimientoTrasladoListaResponse,
  SeguimientoTrasladoPedidoListaResponse,
} from '../../../../core/models/response/seguimiento-responses';
import { CardModule } from 'primeng/card';
import { InputGroupModule } from 'primeng/inputgroup';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { InputNumberModule } from 'primeng/inputnumber';
import { SkeletonModule } from 'primeng/skeleton';
import { DialogModule } from 'primeng/dialog';
import { TableModule } from 'primeng/table';
import { Table } from 'primeng/table';

@Component({
  selector: 'app-traslados-programado',
  standalone: true,
  imports: [
    PanelModule,
    ScrollPanelModule,
    CardModule,
    InputGroupModule,
    ButtonModule,
    InputTextModule,
    InputNumberModule,
    FormsModule,
    SkeletonModule,
    DialogModule,
    TableModule,
  ],
  templateUrl: './traslados-programado.component.html',
  styleUrl: './traslados-programado.component.scss',
})
export class TrasladosProgramadoComponent implements OnInit {
  guiaDeRemision: String = '';
  trasladoProgramado!: SeguimientoTrasladoDetalleResponse;
  trasladoProgramados!: SeguimientoTrasladoListaResponse[];
  filteredTraslados!: SeguimientoTrasladoListaResponse[];
  trasladoProgramadoPedidos!: SeguimientoTrasladoPedidoListaResponse[];

  verTrasladoPedido: boolean = false;

  constructor(private seguimientoService: SeguimientoService) {}
  ngOnInit(): void {
    this.seguimientoService
      .obtenerTrasladosProceso()
      .subscribe((response: any) => {
        this.trasladoProgramado = response;
        this.filteredTraslados = this.trasladoProgramados;
      });
  }
  obtenerTrasladoDetalle(guia: String = this.guiaDeRemision) {
    console.log(this.guiaDeRemision);
    this.seguimientoService
      .obtenerTrasladoProcesoDetalle(String(guia))
      .subscribe((response: any) => {
        this.trasladoProgramado = response;
        this.guiaDeRemision = guia;
      });
  }
  cargarTrasladoPedido() {
    this.seguimientoService
      .obtenerTrasladosProcesoPedidos(String(this.guiaDeRemision))
      .subscribe((response: any) => {
        this.trasladoProgramadoPedidos = response;
      });
  }
  cambiarVerTrasladoPedido() {
    if (this.trasladoProgramado.placa == null) {
      this.verTrasladoPedido = false;
    } else {
      this.cargarTrasladoPedido();
      this.verTrasladoPedido = true;
    }
  }

}
