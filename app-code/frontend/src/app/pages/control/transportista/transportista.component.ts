import { Component, OnInit } from '@angular/core';
import { TransportistaListaResponse } from "../../../core/models/response/transportista-lista";
import { Router } from "@angular/router";
import { ButtonModule } from "primeng/button";
import { TableModule } from "primeng/table";
import { DropdownModule } from "primeng/dropdown";
import { NgClass } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { PaginatorModule } from "primeng/paginator";
import { TransportistaService } from '../../../core/services/transportista.service';


interface Column {
  field: string;
  header: string;
}

@Component({
  selector: 'app-transportista',
  standalone: true,
  imports: [
    ButtonModule,
    TableModule,
    DropdownModule,
    NgClass,
    FormsModule,
    PaginatorModule,
  ],
  templateUrl: './transportista.component.html',
  styleUrl: './transportista.component.scss'
})
export class TransportistaComponent implements OnInit {
  conductores: TransportistaListaResponse[] = [];
  conductoresFiltrados: TransportistaListaResponse[] = [];
  estados: any[] = [];
  searchTerm: string = '';
  first: number = 0;
  rows: number = 10;
  cols!: Column[];
  selectedConductor!: Column[];

  constructor(
    private controlService: TransportistaService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.controlService.getConductores().subscribe(data => {
      console.log('Datos recibidos:', data);
      this.conductores = data;
      this.actualizarConductoresFiltrados();
    });

    this.estados = [
      { label: 'Disponible', value: 'Disponible' },
      { label: 'No disponible', value: 'No disponible' },
      { label: 'Cuarentena', value: 'Cuarentena' }
    ];

    this.cols = [
      { field: 'codigoDelConductor', header: 'Código' },
      { field: 'codigoDelEmpleado', header: 'Empleado' },
      { field: 'tipoDeLicencia', header: 'Tipo de licencia' },
      { field: 'fechaDeVencimientoDeLicencia', header: 'Fecha de vencimiento de licencia' },
      { field: 'fechaUltimoTraslado', header: 'Fecha de último traslado' },
      { field: 'estadoDelConductor', header: 'Estado' }
    ];

    this.selectedConductor = this.cols;
  }

  cambiarEstado(conductor: TransportistaListaResponse): void {
    this.controlService.actualizarEstadoConductor(conductor.codigoDelConductor, conductor.estadoDelConductor).subscribe(
      response => {
        console.log(`Estado del conductor ${conductor.codigoDelConductor} cambiado a ${conductor.estadoDelConductor}`);
      },
      error => {
        console.error(`Error al cambiar el estado del conductor ${conductor.codigoDelConductor}:`, error);
      }
    );
  }

  filtrarConductores(): void {
    this.conductoresFiltrados = this.conductores.filter(conductor =>
      conductor.codigoDelConductor.includes(this.searchTerm) ||
      conductor.codigoDelEmpleado.includes(this.searchTerm) ||
      conductor.tipoDeLicencia.includes(this.searchTerm) ||
      conductor.estadoDelConductor.includes(this.searchTerm)
    );
    this.actualizarConductoresFiltrados();
  }

  onPageChange(event: any): void {
    this.first = event.first;
    this.rows = event.rows;
    this.actualizarConductoresFiltrados();
  }

  actualizarConductoresFiltrados(): void {
    const start = this.first;
    const end = this.first + this.rows;
    this.conductoresFiltrados = this.conductores.slice(start, end);
    console.log('Conductores filtrados actualizados:', this.conductoresFiltrados);
  }

  irAVehiculos(): void {
    this.router.navigate(['pages/control/vehiculo']);
  }

  irAIncidencias(): void {
    this.router.navigate(['pages/control/incidencias']);
  }
}
