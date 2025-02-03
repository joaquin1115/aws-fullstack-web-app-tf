export interface ReporteProgramacionRequest {
  idRepresentante?: number;
  idReporteFormato?: number;
  idReporteEstado?: number;
  idReporteFrecuencia?: number;
  idReporteTipo?: number;
  fechaInicio?: string;
  fechaFin?: string;
}

export interface ReporteGenerarRequest {
  idRepresentante?: number;
  idReporteFormato?: number;
  idReporteTipo?: number;
  fechaInicio?: string;
  fechaFin?: string;
}