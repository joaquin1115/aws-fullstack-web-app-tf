import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { DropdownModule } from "primeng/dropdown";
import { CalendarModule } from "primeng/calendar";
import { DatePipe } from "@angular/common";
import { PaginatorModule } from "primeng/paginator";
import { IncidenciasFormRequest } from '../../../core/models/request/incidencias-form-request';
import { IncidenciasService } from '../../../core/services/incidencias.service';

@Component({
  selector: 'app-registro-incidencia',
  standalone: true,
  imports: [
    FormsModule,
    DropdownModule,
    CalendarModule,
    PaginatorModule,
  ],
  templateUrl: './registro-incidencia.component.html',
  styleUrl: './registro-incidencia.component.scss',
  providers: [DatePipe]
})
export class RegistroIncidenciaComponent implements OnInit {
  tiposIncidencia = [
    { label: 'Incidencia de tipo A: retrasos en la entrega', value: 'A' },
    { label: 'Incidencia de tipo B: errores en el etiquetado o embalaje ', value: 'B' },
    { label: 'Incidencia de tipo C: fallas en la documentación', value: 'C' },
    { label: 'Incidencia de tipo D: problemas mecánicos con el vehículo', value: 'D' },
    { label: 'Incidencia de tipo E: error en la asignación de ruta', value: 'E' }
  ];

  tiposProcedimiento = [
    { label: 'Procedimiento de tipo A: retrasos en la entrega', value: 'A' },
    { label: 'Procedimiento de tipo B: errores en el etiquetado o embalaje', value: 'B' },
    { label: 'Procedimiento de tipo C: fallas en la documentación', value: 'C' },
    { label: 'Procedimiento de tipo D: problemas mecánicos con el vehículo', value: 'D' },
    { label: 'Procedimiento de tipo E: error en la asignación de ruta', value: 'E' }
  ];

  tiposNorma = [
    { label: 'Norma 1: NTP 209.027', value: 'X' },
    { label: 'Norma 2: ISO 17712', value: 'Y' },
    { label: 'Norma 3: ISO 22000', value: 'Z' },
    { label: 'Norma 4: ISO 9001', value: 'W' }
  ];

  incidencia: IncidenciasFormRequest = new IncidenciasFormRequest('', '', '', '', '', '', 0, 0);

  constructor(private router: Router, private incidenciasService: IncidenciasService, private datePipe: DatePipe) { }

  ngOnInit(): void { }

  registrarIncidencia() {
    // Clona el objeto incidencia para evitar modificar el original
    const incidenciaToSend = { ...this.incidencia };

    // Formatea la fecha y la hora
    incidenciaToSend.fecha = this.formatDate(this.incidencia.fecha);
    incidenciaToSend.hora = this.formatTime(this.incidencia.hora);

    console.log('Incidencia a enviar:', incidenciaToSend);

    this.incidenciasService.crearIncidencia(incidenciaToSend).subscribe(
      (response: any) => {
        console.log('Respuesta del servidor:', response);
        if (typeof response === 'object' && response.message) {
          console.log('Mensaje del servidor:', response.message);
        }
        // Navega a la lista de incidencias
        this.router.navigate(['/pages/control/incidencias']).then(() => {
          console.log('Navegación completada');
        }).catch(err => {
          console.error('Error en la navegación:', err);
        });
      },
      error => {
        console.error('Error al crear la incidencia:', error);
        // Aquí puedes agregar lógica para mostrar un mensaje de error al usuario
      }
    );
  }

  private formatDate(date: Date | string): string {
    return this.datePipe.transform(date, 'yyyy-MM-dd') || '';
  }

  private formatTime(time: Date | string): string {
    if (time instanceof Date) {
      return this.datePipe.transform(time, 'HH:mm:ss') || '';
    } else if (typeof time === 'string') {
      return time;
    }
    return '';
  }


  descartarIncidencia() {
    this.router.navigate(['pages/control/incidencias']);
  }
}
