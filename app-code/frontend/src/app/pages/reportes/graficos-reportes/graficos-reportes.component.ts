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

import * as echarts from 'echarts';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { ReportesService } from '../../../core/services/reportes.service';
import { ReporteReclamoTiempoResponse } from '../../../core/models/response/reporte-responses';
import { getArrayDataUrgenciaTipo, getEChartUrgenciaTipo, getEchart } from '../../../shared/utils/chartUtil';
@Component({
  selector: 'app-graficos-reportes',
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
  templateUrl: './graficos-reportes.component.html',
  styleUrl: './graficos-reportes.component.scss'
})
export class GraficosReportesComponent implements OnInit {

  constructor(
    private router: Router,
    private reportesService: ReportesService,
    private formBuilder: FormBuilder,
  ) { }

  dataTiempoReclamoView: boolean = false;
  dataTiempoReclamo: ReporteReclamoTiempoResponse[] = [];
  dataDescripcionTipoReclamo: { idTipoReclamo?: string, descripcion?: string }[] = [];

  ngOnInit(): void {

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
