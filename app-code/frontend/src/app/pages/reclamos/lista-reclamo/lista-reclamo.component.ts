import { Component, OnInit } from '@angular/core';
import { DropdownModule } from "primeng/dropdown";
import { FormsModule } from "@angular/forms";
import { Router } from "@angular/router";
import { TableModule } from "primeng/table";
import { ButtonModule } from "primeng/button";
import { ReclamoListaResponse } from '../../../core/models/response/reclamos-responses';
import { ReclamosService } from '../../../core/services/reclamos.service';

@Component({
  selector: 'app-lista-reclamo',
  standalone: true,
  imports: [
    DropdownModule,
    FormsModule,
    TableModule,
    ButtonModule
  ],
  templateUrl: './lista-reclamo.component.html',
  styleUrl: './lista-reclamo.component.scss'
})
export class ListaReclamoComponent implements OnInit {
  reclamos: ReclamoListaResponse[] = [];
  tipoClienteFiltro: string = 'Todos';
  tipoClienteOptions = [
    { label: 'Todos', value: 'Todos' },
    { label: 'Interno', value: 'Interno' },
    { label: 'Externo', value: 'Externo' }
  ];
  estadoOptions = [
    { label: 'Pendiente', value: 'Pendiente' },
    { label: 'En Proceso', value: 'En Proceso' },
    { label: 'Resuelto', value: 'Resuelto' },
    { label: 'Cerrado', value: 'Cerrado' }
  ];

  constructor(private reclamoService: ReclamosService, private router: Router) { }

  ngOnInit(): void {
    this.reclamoService.getReclamos().subscribe(data => {
      this.reclamos = data;
    });
  }

  get filteredReclamos() {
    if (this.tipoClienteFiltro === 'Todos') {
      return this.reclamos;
    }
    return this.reclamos.filter(r => r.tipoCliente === this.tipoClienteFiltro);
  }

  cambiarEstado(reclamo: ReclamoListaResponse): void {
    console.log('Nuevo estado del reclamo:', reclamo);
  }
  crearNuevoReclamo(): void {
    this.router.navigate(['pages/reclamos/datos-cliente']);
  }

  verVistoReclamo(codReclamo: string): void {
    this.router.navigate([`pages/reclamos/visor/${codReclamo}`]);
  }

}
