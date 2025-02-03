// import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { API_URL } from "../../shared/constants/urls.constant";
import { ReporteAlmacenStockResponse, ReporteLookUpTablesResponse, ReporteOperacionResponse, ReportePedidoMesResponse, ReportePedidoTopResponse, ReporteProgramacionMostrarResponse, ReporteProgramacionResponse, ReporteReclamoMesResponse, ReporteReclamoTiempoResponse, ReporteReclamoUrgenciaResponse, ReporteTipoResponse } from "../models/response/reporte-responses";
import { ReporteGenerarRequest, ReporteProgramacionRequest } from "../models/request/reporte-requests";

@Injectable({
  providedIn: 'root'
})
export class ReportesService {

  constructor(private http: HttpClient) { }
  private apiurl = API_URL.REPORTES;

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json;charset=utf-8'
    })
  };

  getReporteOperacion() {
    return this.http.get<ReporteOperacionResponse[]>(`${this.apiurl}/operacion`, this.httpOptions);
  }

  getReporteAlmacenStock() {
    return this.http.get<ReporteAlmacenStockResponse[]>(`${this.apiurl}/almacen/stock`, this.httpOptions);
  }

  getReporteReclamoUrgencia() {
    return this.http.get<ReporteReclamoUrgenciaResponse[]>(`${this.apiurl}/reclamo/urgencia`, this.httpOptions);
  }

  getReporteReclamoTiempo() {
    return this.http.get<ReporteReclamoTiempoResponse[]>(`${this.apiurl}/reclamo/tiempo`, this.httpOptions);
  }

  getReporteReclamoMes() {
    return this.http.get<ReporteReclamoMesResponse[]>(`${this.apiurl}/reclamo/mes`, this.httpOptions);
  }

  getReportePedidoMes() {
    return this.http.get<ReportePedidoMesResponse[]>(`${this.apiurl}/pedido/mes`, this.httpOptions);
  }

  getReportePedidoTop() {
    return this.http.get<ReportePedidoTopResponse[]>(`${this.apiurl}/pedido/top`, this.httpOptions);
  }

  getLookUpTables() {
    return this.http.get<ReporteLookUpTablesResponse>(`${this.apiurl}/lookup/all`, this.httpOptions);
  }

  getReporteProgramacion() {
    return this.http.get<ReporteProgramacionMostrarResponse[]>(`${this.apiurl}/programacion/all`, this.httpOptions);
  }

  postDatosReporteProgramacion(reporteProgramacionRequest: ReporteProgramacionRequest) {
    return this.http.post<ReporteProgramacionResponse>(`${this.apiurl}/programacion`, reporteProgramacionRequest, this.httpOptions);
  }

  postDatosGenerarReporte(reporteGenerarRequest: ReporteGenerarRequest) {
    return this.http.post<ReporteGenerarRequest>(`${this.apiurl}/new`, reporteGenerarRequest, this.httpOptions);
  }
}
