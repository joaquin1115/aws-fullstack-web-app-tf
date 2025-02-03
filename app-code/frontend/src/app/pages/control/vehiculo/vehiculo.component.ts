import { Component, OnInit } from '@angular/core';
import { RouterModule } from "@angular/router";
import { CommonModule } from "@angular/common";
import { TableModule } from "primeng/table";
import { FormsModule } from "@angular/forms";
import { DropdownModule } from "primeng/dropdown";
import { VehiculoResponse } from '../../../core/models/response/vehiculo-response';
import { VehiculoService } from '../../../core/services/vehiculo.service';


@Component({
  selector: 'app-vehiculo',
  standalone: true,
  imports: [RouterModule, CommonModule, TableModule, FormsModule, DropdownModule

  ],
  templateUrl: './vehiculo.component.html',
  styleUrl: './vehiculo.component.scss'
})
export class VehiculoComponent implements OnInit {
  vehiculos: VehiculoResponse[] = [];
  vehiculosFiltrados: VehiculoResponse[] = [];
  estados = [
    { label: 'Disponible', value: 'disponible' },
    { label: 'No disponible', value: 'no disponible' },
    { label: 'Cuarentena', value: 'cuarentena' }
  ];
  searchTerm = '';

  constructor(private vehiculoService: VehiculoService) { }

  ngOnInit(): void {
    this.cargarVehiculos();
  }

  cargarVehiculos(): void {
    this.vehiculoService.getVehiculos().subscribe(data => {
      this.vehiculos = data;
      this.vehiculosFiltrados = data;
    });
  }

  cambiarEstado(vehiculo: VehiculoResponse): void {
    this.vehiculoService.actualizarEstado(vehiculo.codigoDelVehiculo, vehiculo.estadoDelVehiculo).subscribe(
      () => {
        console.log(`Estado del vehículo ${vehiculo.codigoDelVehiculo} cambiado a ${vehiculo.estadoDelVehiculo}`);
        // Puedes agregar aquí lógica adicional después de actualizar el estado
      },
      error => {
        console.error('Error al actualizar estado del vehículo:', error);
        // Manejo de errores aquí
      }
    );
  }

  filtrarVehiculos(): void {
    this.vehiculosFiltrados = this.vehiculos.filter(vehiculo =>
      vehiculo.codigoDelVehiculo.includes(this.searchTerm) ||
      vehiculo.modelo.includes(this.searchTerm) ||
      vehiculo.placa.includes(this.searchTerm) ||
      vehiculo.estadoDelVehiculo.includes(this.searchTerm)
    );
  }
}
