import { Time } from "@angular/common";

export interface ReporteOperacionResponse {
  idOperacionTipo?: number;
  nombreOperacion?: string;
  tiempoTotal?: Time;
  cantidad?: number;
  tiempoMedio?: number;
  listaOperacion?: string;
  listaOperacionSplit?: number[];
}
export interface ReporteAlmacenStockResponse {
  idElementoCatalogo?: number;
  nombre?: string;
  pesoUnitario?: number;
  unidad?: string;
  tipoElemento?: string;
  tipoProduccion?: string;
  cantidad?: number;
}

export interface ReportePedidoMesResponse {
  mes?: string;
  totalPedidos?: number;
}
export interface ReportePedidoTopResponse {
  idElementoCatalogo?: number;
  nombre?: string;
  descripcion?: string;
  cantidad?: number;
}

export interface ReporteReclamoUrgenciaResponse {
  idTipoReclamo?: string;
  descripcion?: string;
  totalTipoReclamo?: number;
  totalUrgenciaBaja?: number;
  totalUrgenciaMedia?: number;
  totalUrgenciaAlta?: number;
}
export interface ReporteReclamoTiempoResponse {
  idNivelUrgencia?: string;
  descripcion?: string;
  tiempoMedio?: number;
  totalNivelUrgencia?: number;
}
export interface ReporteReclamoMesResponse {
  mes?: string;
  totalReclamos?: number;
}

export interface ReporteLookUpTablesResponse {
  reporteTipoResponse?: ReporteTipoResponse[];
  reporteFormatoResponse?: ReporteFormatoResponse[];
  reporteFrecuenciaResponse?: ReporteFrecuenciaResponse[];
}
export interface ReporteTipoResponse {
  idReporteTipo?: number;
  descripcion?: string;
}
export interface ReporteFormatoResponse {
  idReporteFormato?: number;
  descripcion?: string;
}
export interface ReporteFrecuenciaResponse {
  idReporteFrecuencia?: number;
  descripcion?: string;
}
export interface ReporteProgramacionResponse {
  idReporteProgramacion?: number;
}
export interface ReporteProgramacionMostrarResponse {
  idProgramacionReporte?: number;
  descripcionFormato?: string;
  descripcionTipo?: string;
  descripcionFrecuencia?: string;
  fechaInicio?: string;
  fechaFin?: string;
}
