import { Component, OnInit } from '@angular/core';
import { TableModule } from 'primeng/table';
import { InputSwitchModule } from 'primeng/inputswitch';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SeguimientoService } from '../../../core/services/seguimiento.service';
import { ButtonModule } from 'primeng/button';
import {
  SeguimientoTransportistaDetalleResponse,
  SeguimientoTransportistaListaResponse
} from '../../../core/models/response/seguimiento-responses';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';

@Component({
  selector: 'app-transportistas',
  standalone: true,
  imports: [
    TableModule,
    InputSwitchModule,
    FormsModule,
    CommonModule,
    ButtonModule,
    DialogModule,
    InputTextModule,
    CalendarModule
  ],
  templateUrl: './transportistas.component.html',
  styleUrl: './transportistas.component.scss',
})
export class TransportistasComponent implements OnInit {
  constructor(private seguimientoService: SeguimientoService) {}

  transportistas!: SeguimientoTransportistaListaResponse[];
  transportistaSeleccionado!: SeguimientoTransportistaListaResponse;
  transportistaDetalle!: SeguimientoTransportistaDetalleResponse;

  transportista = {
    dni: '',
    numeroLicencia: '',
    estado: '',
    vencimientoLicencia: '',
    tipoLicencia: '',
  };

  nuevoTransportista: boolean = false;
  editarTransportista: boolean = false;
  idTransportista!: number;
  numLicencia!: string;
  tipoLicencia!: string;
  vencimientoLicencia!: Date;
  estadoTransportista!: string;

  ngOnInit(): void {
    this.seguimientoService
      .obtenerTransportistas()
      .subscribe((data: SeguimientoTransportistaListaResponse[]) => {
        this.transportistas = data;
      });
  }
  detalleTransportista() {
    this.seguimientoService
      .obtenerTransportistaDetalle(
        this.transportistaSeleccionado.idTransportista
      )
      .subscribe((data: SeguimientoTransportistaDetalleResponse) => {
        this.transportistaDetalle = data;
      });
  }

  cambiarEditarTransportista() {
    if (this.transportistaSeleccionado == null) {
      this.editarTransportista = false;
    } else {
      this.editarTransportista = true;
    }
  }
  cambiarNuevoTransportista() {
    if (this.transportistaSeleccionado != null) {
      this.editarTransportista = false;
    } else {
      this.editarTransportista = true;
    }
  }
  onSubmit() {
    console.log(this.transportista);
  }
}
