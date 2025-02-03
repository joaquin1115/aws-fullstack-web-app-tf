import { Component, OnInit } from '@angular/core';
import { ReportesService } from '../../core/services/reportes.service';
import { Router } from '@angular/router';
import { ReporteAlmacenStockResponse, ReporteFormatoResponse, ReporteFrecuenciaResponse, ReporteLookUpTablesResponse, ReporteOperacionResponse, ReporteReclamoTiempoResponse, ReporteTipoResponse } from '../../core/models/response/reporte-responses';
import { chartHorBar, getArrayDataUrgenciaTipo, getDataSetHorBar, getEChartUrgenciaTipo, getEchart } from '../../shared/utils/chartUtil';
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

import * as echarts from 'echarts';
import { formatDate } from '../../shared/utils/dateUtil';
import { ReporteGenerarRequest, ReporteProgramacionRequest } from '../../core/models/request/reporte-requests';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';

interface ReporteGenerarForm {
  idReporteTipo: number;
  fechaInicio: string;
  fechaFin: string;
  fomarto: string;
}

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [
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
    CommonModule
  ],
  templateUrl: './reportes.component.html',
  styleUrl: './reportes.component.scss'
})
export class ReportesComponent implements OnInit {

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
      console.log(reporteGenerarRequest)
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

  dataStockAlmacenView: boolean = false;
  products: ReporteAlmacenStockResponse[] = [];
  product!: ReporteAlmacenStockResponse;
  size = { name: 'Small', class: 'p-datatable-sm' };

  constructor(
    private router: Router,
    private reportesService: ReportesService,
    private formBuilder: FormBuilder,
  ) { }

  dataTiempoReclamoView: boolean = false;
  dataTiempoReclamo: ReporteReclamoTiempoResponse[] = [];
  dataDescripcionTipoReclamo: { idTipoReclamo?: string, descripcion?: string }[] = [];

  ngOnInit(): void {

    this.reportesService.getLookUpTables().subscribe((response) => {
      this.reporteTipo = response.reporteTipoResponse;
      this.reporteTipoView = true;
      this.reporteFrecuencia = response.reporteFrecuenciaResponse;
      this.reporteFrecuenciaView = true;
      this.reporteFormato = response.reporteFormatoResponse;
      this.reporteFormatoView = true;
    })

    this.reportesService.getReporteAlmacenStock().subscribe((response) => {
      this.products = response;
      this.dataStockAlmacenView = true;
    })

    this.reportesService.getReportePedidoMes().subscribe((response) => {
      this.generateChart(
        "pedidoMes",
        getEchart(response, "totalPedidos", "mes", "Pedidos por mes", "Pedidos por mes", "line", "vertical")
      )
    })
    this.reportesService.getReportePedidoTop().subscribe((response) => {
      this.generateChart(
        "pedidoTop",
        getEchart(response, "cantidad", "idElementoCatalogo", "Cantidad total pedida", "Cantidad total pedida", "bar", "vertical")
      )
    })

    this.reportesService.getReporteReclamoUrgencia().subscribe((response) => {
      getArrayDataUrgenciaTipo(response, ["totalUrgenciaBaja", "totalUrgenciaAlta", "totalUrgenciaAlta"], "idTipoReclamo");
      response.forEach((item) => {
        this.dataDescripcionTipoReclamo.push({ idTipoReclamo: item.idTipoReclamo, descripcion: item.descripcion })
      })
      this.generateChart(
        "reclamoUrgenciaTipo",
        getEChartUrgenciaTipo(response, ["totalUrgenciaBaja", "totalUrgenciaMedia", "totalUrgenciaAlta"], "idTipoReclamo", "Reclamos por mes", "Reclamos por mes")
      )
    })
    this.reportesService.getReporteReclamoTiempo().subscribe((response) => {
      response.forEach((item) => {
        this.dataTiempoReclamo.push(item)
      })
      this.dataTiempoReclamoView = true;
    })
    this.reportesService.getReporteReclamoMes().subscribe((response) => {
      // console.log(getArrayData(response, "totalReclamos", "mes"));
      this.generateChart(
        "reclamoMesTipo",
        getEchart(response, "totalReclamos", "mes", "Reclamos por mes", "Reclamos por mes", "line", "vertical")
      )
    })

    this.reportesService.getReporteOperacion().subscribe((response) => {
      this.generateChart(
        "almacenOperacionReporte",
        getEchart(response, "tiempoMedio", "nombreOperacion", "Tiempo medio por tipo de operación", "Tiempo medio por tipo de operación", "bar", "horizontal")
      )
    })
  }

  generateChart(idChart: string, option: any) {
    var chartDom = document.getElementById(idChart);
    var myChart = echarts.init(chartDom);
    option && myChart.setOption(option);
  }
}
