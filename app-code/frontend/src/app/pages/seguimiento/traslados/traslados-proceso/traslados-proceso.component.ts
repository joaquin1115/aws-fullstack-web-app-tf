import { Component, Inject, OnInit } from '@angular/core';
import { SeguimientoService } from '../../../../core/services/seguimiento.service';
import { PanelModule } from 'primeng/panel';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { SeguimientoTrasladoDetalleResponse, SeguimientoTrasladoListaResponse, SeguimientoTrasladoPedidoListaResponse } from '../../../../core/models/response/seguimiento-responses';
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
  selector: 'app-traslados-proceso',
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
    TableModule
  ],
  templateUrl: './traslados-proceso.component.html',
  styleUrl: './traslados-proceso.component.scss'
})
export class TrasladosProcesoComponent implements OnInit {

  trasladoProcesos!: SeguimientoTrasladoListaResponse[];
  filteredTraslados!: SeguimientoTrasladoListaResponse[];
  trasladoProceso!: SeguimientoTrasladoDetalleResponse;


  trasladoProcesoPedidos!: SeguimientoTrasladoPedidoListaResponse[];

  guiaDeRemision: String = "";
  codigoPedido: String = "";

  verTrasladoPedido: boolean = false;

  constructor(
    private seguimientoService: SeguimientoService
  ) { }

  ngOnInit(): void {
    this.seguimientoService.obtenerTrasladosProceso().subscribe((response: any) => {
      this.trasladoProcesos = response;
      this.filteredTraslados = this.trasladoProcesos;
    });

  }

  obtenerTrasladoDetalle(guia: String = this.guiaDeRemision) {
    console.log(this.guiaDeRemision);
    this.seguimientoService.obtenerTrasladoProcesoDetalle(String(guia)).subscribe((response: any) => {
      this.trasladoProceso = response;
      this.guiaDeRemision = guia;
    });
  }

  cambiarVerTrasladoPedido() {
    if (this.trasladoProceso.placa == null) {
      this.verTrasladoPedido = false;
    } else {
      this.cargarTrasladoPedido();
      this.verTrasladoPedido = true;
    }
  }

  actualizarEstadoPedido(idPedido: number) {
    this.seguimientoService.actualizarTrasladoProcesoPedido(idPedido).subscribe((response: any) => {
      if (response) {
        this.cargarTrasladoPedido();
      }
    });
  }

  cargarTrasladoPedido() {
    this.seguimientoService.obtenerTrasladosProcesoPedidos(String(this.guiaDeRemision)).subscribe((response: any) => {
      this.trasladoProcesoPedidos = response;
    });
  }


  onChangeCodigoPedido() {

  }
  clear(table: Table) {
    table.clear();
    this.codigoPedido = "";
  }

}
