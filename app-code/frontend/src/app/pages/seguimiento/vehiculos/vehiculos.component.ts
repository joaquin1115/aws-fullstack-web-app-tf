import { Component, OnInit } from '@angular/core';
import { TableModule } from 'primeng/table';
import { InputSwitchModule } from 'primeng/inputswitch';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SeguimientoService } from '../../../core/services/seguimiento.service';
import { ButtonModule } from 'primeng/button';
import {
  SeguimientoVehiculoDetallesResponse,
  SeguimientoVehiculoListaResponse } from '../../../core/models/response/seguimiento-responses';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';



@Component({
  selector: 'app-vehiculos',
  standalone: true,
  imports: [
    TableModule,
    InputSwitchModule,
    FormsModule,
    CommonModule,
    ButtonModule,
    DialogModule,
    InputTextModule,
    CalendarModule,
  ],
  templateUrl: './vehiculos.component.html',
  styleUrl: './vehiculos.component.scss',
})
export class VehiculosComponent implements OnInit {
  constructor(private seguimientoService: SeguimientoService) {}

  vehiculos!: SeguimientoVehiculoListaResponse[];
  vehiculoSeleccionado!: SeguimientoVehiculoListaResponse;
  vehiculoDetalle!: SeguimientoVehiculoDetallesResponse;

  vehiculo = {
    marca: '',
    modelo: '',
    estado: '',
    anioFabricacion: '',
    placa: '',
    tipo: '',
    capacidadCarga: '',
    fechaMantenimiento: '',
  };
  nuevoVehiculo: boolean = false;
  editarVehiculo: boolean = false;
  vehiculoMarca!: string;
  vehiculoEstado!: string;
  vehiculoFabricacion!: number;
  vehiculoPlaca!: string;
  vehiculoTipo!: string;
  vehiculoCarga!: number;
  vehiculoMantenimiento!: Date;

  ngOnInit(): void {
    this.seguimientoService
      .obtenerVehiculos()
      .subscribe((data: SeguimientoVehiculoListaResponse[]) => {
        this.vehiculos = data;
      });
  }
  detalleVehiculo() {
    this.seguimientoService
      .obtenerVehiculoDetalle(this.vehiculoSeleccionado.idVehiculo)
      .subscribe((data: SeguimientoVehiculoDetallesResponse) => {
        this.vehiculoDetalle = data;
      });
  }
  cambiarEditarVehiculo() {
    if (this.vehiculoSeleccionado == null) {
      this.editarVehiculo = false;
    } else {
      this.editarVehiculo = true;
    }
  }
  cambiarNuevoVehiculo() {
    if (this.vehiculoSeleccionado != null) {
      this.nuevoVehiculo = false;
    } else {
      this.nuevoVehiculo = true;
    }
  }
  onSubmit() {
    console.log(this.vehiculo);
    ;
  }
}
