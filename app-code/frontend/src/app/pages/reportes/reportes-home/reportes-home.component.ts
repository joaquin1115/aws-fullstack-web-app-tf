import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ChartModule } from 'primeng/chart';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { DropdownModule } from 'primeng/dropdown';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { FloatLabelModule } from 'primeng/floatlabel';
import { TableModule } from 'primeng/table';
import { RouterModule, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { ReportesService } from '../../../core/services/reportes.service';
import { formatDate } from '../../../shared/utils/dateUtil';
import { ReporteGenerarRequest, ReporteProgramacionRequest } from '../../../core/models/request/reporte-requests';
import { ReporteFormatoResponse, ReporteFrecuenciaResponse, ReporteProgramacionMostrarResponse, ReporteTipoResponse } from '../../../core/models/response/reporte-responses';
import { AlmacenInventarioComponent } from '../almacen-inventario/almacen-inventario.component';
import { GraficosReportesComponent } from '../graficos-reportes/graficos-reportes.component';
import { ScrollPanelModule } from 'primeng/scrollpanel';

@Component({
  selector: 'app-reportes-home',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterModule,
    ChartModule,
    CardModule,
    DividerModule,
    DialogModule,
    ButtonModule,
    PanelModule,
    DropdownModule,
    ReactiveFormsModule,
    FormsModule,
    FloatLabelModule,
    CalendarModule,
    TableModule,
    InputTextModule,
    CommonModule,
    ScrollPanelModule,
    AlmacenInventarioComponent,
    GraficosReportesComponent
  ],
  templateUrl: './reportes-home.component.html',
  styleUrl: './reportes-home.component.scss'
})
export class ReportesHomeComponent implements OnInit {

  visibleReporteGenerar: boolean = false;
  visibleReporteProgramacion: boolean = false;
  showDialogReporteGenerar(visible: boolean = true) {
    this.visibleReporteGenerar = visible;
  }
  showDialogReporteProgramacion(visible: boolean = true) {
    this.visibleReporteProgramacion = visible;
  }

  reporteTipo?: ReporteTipoResponse[] = [];
  reporteFrecuencia?: ReporteFrecuenciaResponse[] = [];
  reporteFormato?: ReporteFormatoResponse[] = [];

  reporteTipoView: boolean = false;
  reporteFrecuenciaView: boolean = false;
  reporteFormatoView: boolean = false;

  reporteGenerarForm = this.formBuilder.group({
    reporteFormatoModel: this.formBuilder.control<ReporteFormatoResponse | null>(null, Validators.required),
    reporteTipoModel: this.formBuilder.control<ReporteTipoResponse | null>(null, Validators.required),
    fechaInicio: this.formBuilder.control<Date | null>(null, Validators.required),
    fechaFin: this.formBuilder.control<Date | null>(null, Validators.required),
  })
  sendDatosReporteGenerar() {
    if (this.reporteGenerarForm.valid) {
      const formValue = this.reporteGenerarForm.value;
      const fechaInicio = formatDate(formValue.fechaInicio as Date);
      const fechaFin = formatDate(formValue.fechaFin as Date);
      const reporteGenerarRequest: ReporteGenerarRequest = {
        idRepresentante: Number(sessionStorage?.getItem("idEmpleado")),
        idReporteFormato: formValue.reporteFormatoModel?.idReporteFormato,
        idReporteTipo: formValue.reporteTipoModel?.idReporteTipo,
        fechaInicio: fechaInicio,
        fechaFin: fechaFin,
      }
      this.reportesService.postDatosGenerarReporte(reporteGenerarRequest).subscribe((response) => {
        console.log(response);
      })
    } else {
      console.log("Llene todos los campos")
    }
  }
  reporteProgramacionForm = this.formBuilder.group({
    reporteFrecuenciaModel: this.formBuilder.control<ReporteFrecuenciaResponse | null>(null, Validators.required),
    reporteFormatoModel: this.formBuilder.control<ReporteFormatoResponse | null>(null, Validators.required),
    reporteTipoModel: this.formBuilder.control<ReporteTipoResponse | null>(null, Validators.required),
    fechaInicio: this.formBuilder.control<Date | null>(null, Validators.required),
    fechaFin: this.formBuilder.control<Date | null>(null, Validators.required),
  })
  sendDatosReporteProgramacion() {
    if (this.reporteProgramacionForm.valid) {
      const formValue = this.reporteProgramacionForm.value;
      const fechaInicio = formatDate(formValue.fechaInicio as Date);
      const fechaFin = formatDate(formValue.fechaFin as Date);
      const reporteProgramacionRequest: ReporteProgramacionRequest = {
        idRepresentante: Number(sessionStorage?.getItem("idEmpleado")),
        idReporteFormato: formValue.reporteFormatoModel?.idReporteFormato,
        idReporteFrecuencia: formValue.reporteFrecuenciaModel?.idReporteFrecuencia,
        idReporteTipo: formValue.reporteTipoModel?.idReporteTipo,
        fechaInicio: fechaInicio,
        fechaFin: fechaFin,
      }
      this.reportesService.postDatosReporteProgramacion(reporteProgramacionRequest).subscribe((response) => {
        console.log(response);
      })
      console.log(reporteProgramacionRequest)
    }
  }

  constructor(
    private router: Router,
    private reportesService: ReportesService,
    private formBuilder: FormBuilder,
  ) { }

  reporteProgramacionMostrar?: ReporteProgramacionMostrarResponse[] = [];

  ngOnInit(): void {

    this.reportesService.getLookUpTables().subscribe((response) => {
      this.reporteTipo = response.reporteTipoResponse;
      this.reporteTipoView = true;
      this.reporteFrecuencia = response.reporteFrecuenciaResponse;
      this.reporteFrecuenciaView = true;
      this.reporteFormato = response.reporteFormatoResponse;
      this.reporteFormatoView = true;
    })

    this.reportesService.getReporteProgramacion().subscribe((response) => {
      this.reporteProgramacionMostrar = response;
    })
  }

  reporteInventarioView: boolean = true;

  goReportesInventario() {
    this.reporteInventarioView = !this.reporteInventarioView;
  }

  getReportesGraficos() {
    this.reporteInventarioView = false;
  }
}
