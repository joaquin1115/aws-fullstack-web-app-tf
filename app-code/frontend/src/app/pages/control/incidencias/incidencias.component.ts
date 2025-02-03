import { Component, OnInit } from '@angular/core';
import { CommonModule } from "@angular/common";
import { IncidenciaResponse } from "../../../core/models/response/incidencia-response";
import { FormsModule } from "@angular/forms";
import { TableModule } from "primeng/table";
import { ButtonModule } from "primeng/button";
import { DropdownModule } from "primeng/dropdown";
import { Router, RouterLink } from "@angular/router";
import { IncidenciasService } from '../../../core/services/incidencias.service';
import { response } from 'express';
@Component({
  selector: 'app-incidencias',
  standalone: true,
  imports: [CommonModule, FormsModule, TableModule, ButtonModule, DropdownModule, RouterLink],
  templateUrl: './incidencias.component.html',
  styleUrl: './incidencias.component.scss'
})
export class IncidenciasComponent implements OnInit {
  incidencias: IncidenciaResponse[] = [];
  incidenciasFiltradas: IncidenciaResponse[] = [];
  searchMes: string = '';
  searchAnio: string = '';
  meses: any[] = [];
  anios: any[] = [];
  estados: any[] = [];

  selectedEstado: String = 'S';

  constructor(private incidenciasService: IncidenciasService, private router: Router) {
    this.meses = [
      { label: '01', value: '01' }, { label: '02', value: '02' }, { label: '03', value: '03' },
      { label: '04', value: '04' }, { label: '05', value: '05' }, { label: '06', value: '06' },
      { label: '07', value: '07' }, { label: '08', value: '08' }, { label: '09', value: '09' },
      { label: '10', value: '10' }, { label: '11', value: '11' }, { label: '12', value: '12' }
    ];
    this.anios = Array.from({ length: 22 }, (v, k) => ({ label: (2015 + k).toString(), value: (2015 + k).toString() }));
    this.estados = [
      { label: 'Solucionado', value: 'S' },
      { label: 'Pendiente', value: 'P' }
    ];
  }

  ngOnInit(): void {
    this.incidenciasService.getIncidencias().subscribe(
      data => {
        this.incidencias = data;
        this.incidenciasFiltradas = data;
      },
      error => console.error('Error al obtener incidencias:', error)
    );
  }

  filtrarIncidencias(): void {
    this.incidenciasFiltradas = this.incidencias.filter(incidencia =>
      incidencia.fechaDeOcurrencia.includes(`${this.searchAnio}-${this.searchMes.padStart(2, '0')}`)
    );
  }

  nuevaIncidencia(): void {
    this.router.navigate(['pages/control/registro-incidencias']);
  }

  cambiarEstado(codigoIncidencia: string, incidencia: any): void {
    this.incidenciasService.actualizarEstadoIncidencia(codigoIncidencia, this.selectedEstado).subscribe(
      response => {
        if (response === 1) {
          incidencia.estadoDeIncidencia = this.selectedEstado;
        }
      },
    );
  }
}
