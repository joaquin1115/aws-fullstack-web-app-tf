import { Component } from '@angular/core';
import { CommonModule } from "@angular/common";
import { Router, RouterModule } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { ButtonModule } from "primeng/button";
import { InputTextModule } from "primeng/inputtext";
import { MessagesModule } from "primeng/messages";
import { MessageModule } from "primeng/message";
import { TableModule } from "primeng/table";
import { RadioButtonModule } from "primeng/radiobutton";
import { AlmacenService } from "../../../core/services/almacen.service";
import { BusquedaAlmacenResponse } from '../../../core/models/response/almacen-response';

@Component({
  selector: 'app-vista-procesos',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ButtonModule,
    InputTextModule,
    MessagesModule,
    MessageModule,
    TableModule,
    RadioButtonModule
  ],
  templateUrl: './vista-procesos.component.html',
  styleUrl: './vista-procesos.component.scss'
})
export class VistaProcesosComponent {
  nroPrecinto: string = '';
  codGuiaRemision: string = '';
  resultadoBusqueda!: BusquedaAlmacenResponse;
  mensajeError: string = '';
  idPicking!: number;
  searchType: string = 'nroPrecinto';

  constructor(private almacenService: AlmacenService, private router: Router) { }

  onSearchTypeChange() {
    this.nroPrecinto = '';
    this.codGuiaRemision = '';
    this.mensajeError = '';
  }

  buscarProceso() {
    this.mensajeError = ''; // Reset error message
    if (this.searchType === 'nroPrecinto' && this.nroPrecinto) {
      if (this.nroPrecinto.length !== 20) {
        this.mensajeError = 'El número de precinto debe tener 20 dígitos.';
        return;
      }
      this.almacenService.validarNroPrecinto(this.nroPrecinto).subscribe(existe => {
        if (existe) {
          this.almacenService.buscarPorNroPrecinto(this.nroPrecinto).subscribe(resultado => {
            this.resultadoBusqueda = resultado;
            this.idPicking = this.resultadoBusqueda.proceso.operaciones[0]?.idOperacion;
          });
        } else {
          this.mensajeError = 'El número de precinto no existe.';
        }
      });
    } else if (this.searchType === 'codGuiaRemision' && this.codGuiaRemision) {
      if (this.codGuiaRemision.length !== 21) {
        this.mensajeError = 'El código de guía de remisión debe tener 21 dígitos.';
        return;
      }
      this.almacenService.validarCodGuiaRemision(this.codGuiaRemision).subscribe(existe => {
        if (existe) {
          this.almacenService.buscarPorCodGuiaRemision(this.codGuiaRemision).subscribe(resultado => {
            this.resultadoBusqueda = resultado;
            this.idPicking = this.resultadoBusqueda.proceso.operaciones[0]?.idOperacion;
          });
        } else {
          this.mensajeError = 'El código de guía de remisión no existe.';
        }
      });
    } else {
      this.mensajeError = 'Por favor ingrese un número de precinto de 19 dígitos o un código de guía de remisión de 21 dígitos.';
    }
  }

  navigateToNext() {
    const length = this.resultadoBusqueda?.proceso.operaciones.length;
    if (length) {
      if (length >= 1 && length <= 6) {
        this.router.navigate(['pages/almacen/registro-operacion'], { queryParams: { idPicking: this.idPicking, codTipoOperacion: length + 1 } });
      }
    }
  }

  navigateToPicking() {
    this.router.navigate(['pages/almacen/registro-operacion'], { queryParams: { idPicking: 0, codTipoOperacion: 1 } });
  }
}