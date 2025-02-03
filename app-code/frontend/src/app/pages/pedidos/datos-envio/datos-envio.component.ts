import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { Genero, PERSONA_GENERO } from '../../../shared/constants/persona.constants';
import { CommonModule } from '@angular/common';
import { PedidoFormRequest } from '../../../core/models/request/pedido-form-request';
import { PedidosService } from '../../../core/services/pedidos.service';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { CalendarModule } from 'primeng/calendar';
import { PedidoFormResponse } from '../../../core/models/response/pedido-form-response';

interface PedidoForm {
  nombres: string;
  primerApellido: string;
  segundoApellido: string;
  dni: string;
  telefono: string;
  correo: string;
  empresa: string;
  fechaSolicitud: Date;
  fechaEntrega: Date;
}

@Component({
  selector: 'app-datos-envio',
  standalone: true,
  imports: [
    ButtonModule,
    FormsModule,
    DropdownModule,
    CommonModule,
    ReactiveFormsModule,
    InputTextModule,
    FloatLabelModule,
    CalendarModule
  ],
  templateUrl: './datos-envio.component.html',
  styleUrl: './datos-envio.component.scss'
})
export class DatosEnvioComponent implements OnInit {

  pedidoFormRequest?: PedidoFormRequest;
  pedidoFormResponse?: PedidoFormResponse;

  pedidoForm = this.formBuilder.group({
    nombres: this.formBuilder.control('', Validators.required),
    primerApellido: this.formBuilder.control('', Validators.required),
    segundoApellido: this.formBuilder.control('', Validators.required),
    dni: this.formBuilder.control('', Validators.required),
    telefono: this.formBuilder.control('', Validators.required),
    correo: this.formBuilder.control('', Validators.required),
    empresa: this.formBuilder.control('', Validators.required),
    fechaSolicitud: this.formBuilder.control<Date | null>(null, Validators.required),
    fechaEntrega: this.formBuilder.control<Date | null>(null, Validators.required),
  })

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private pedidoService: PedidosService,
  ) { }

  ngOnInit(): void {

  }

  sendDatosEnvio(): void {
    if (this.pedidoForm.valid) {
      const formValue: PedidoForm = this.pedidoForm.value as PedidoForm;
      this.pedidoFormRequest = {
        personaRequest: {
          prenombre: formValue.nombres,
          primerApellido: formValue.primerApellido as string,
          segundoApellido: formValue.segundoApellido,
          dni: formValue.dni,
        },
        clienteRequest: {
          nombre: formValue.empresa,
        },
        representanteRequest: {
          telefono: formValue.telefono,
          correoEmpresarial: formValue.correo,
        }
      };
      this.pedidoService.postDatosEnvio(this.pedidoFormRequest as PedidoFormRequest).subscribe((response) => {
        this.pedidoFormResponse = response;
        this.nextPage();
      });
    }
  }

  nextPage() {
    this.router.navigate(
      ['pages/pedidos/proceso/solicitud-productos'],
      {
        state:
        {
          formResponse: this.pedidoFormResponse,
          fechaEntrega: this.pedidoForm.value.fechaEntrega,
          fechaSolicitud: this.pedidoForm.value.fechaSolicitud
        }
      }
    );
  }
}
