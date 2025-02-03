import { Component, OnInit } from '@angular/core';
import { TableModule } from 'primeng/table';
import { InputSwitchModule } from 'primeng/inputswitch';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import {
  SeguimientoRutaDetalleResponse,
  SeguimientoRutaListaResponse,
} from '../../../core/models/response/seguimiento-responses';
import { SeguimientoService } from '../../../core/services/seguimiento.service';
import { ButtonModule } from 'primeng/button';
import { SelectItem } from 'primeng/api';

@Component({
  selector: 'app-rutas',
  standalone: true,
  imports: [
    TableModule,
    InputSwitchModule,
    FormsModule,
    CommonModule,
    ButtonModule,
  ],
  templateUrl: './rutas.component.html',
  styleUrls: ['./rutas.component.scss'],
})
export class RutasComponent implements OnInit {
  rutas!: SeguimientoRutaListaResponse[];
  rutaSeleccionada!: SeguimientoRutaListaResponse;
  paraderos!: SeguimientoRutaDetalleResponse[];

  constructor(private seguimientoService: SeguimientoService) { }

  ngOnInit() {
    this.seguimientoService
      .obtenerRutas()
      .subscribe((data: SeguimientoRutaListaResponse[]) => {
        this.rutas = data;
      });
  }
  paraderoRuta() {
    this.seguimientoService
      .obtenerRutaDetalle(this.rutaSeleccionada.idRuta)
      .subscribe((data: SeguimientoRutaDetalleResponse[]) => {
        this.paraderos = data;
      });
  }

}
