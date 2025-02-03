package com.sanfernando.sanfernando.dao;

import java.util.List;

import com.sanfernando.sanfernando.dtos.requests.ReporteProgramacionRequest;
import com.sanfernando.sanfernando.dtos.requests.reportes.ReporteRequest;
import com.sanfernando.sanfernando.dtos.responses.ReporteFrecuenciaResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteAlmacenStockResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteFormatoResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteMostrarProgramacionResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteOperacionResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReportePedidoMesResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReportePedidoTopResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteProgramacionResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteReclamoMesResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteReclamoTiempoResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteReclamoUrgenciaResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteTipoResponse;

public interface ReporteDao {
  public List<ReporteOperacionResponse> getReporteOperacion();
  public ReporteProgramacionResponse newProgramacion(ReporteProgramacionRequest reporteProgramacionRequest);
  public List<ReporteReclamoUrgenciaResponse> getReporteReclamoUrgencia();
  public List<ReporteReclamoTiempoResponse> getReporteReclamoTiempo();
  public List<ReporteReclamoMesResponse> getReporteReclamoMes();
  public List<ReportePedidoMesResponse> getReportePedidoMes();
  public List<ReportePedidoTopResponse> getReportePedidoTop();
  public List<ReporteTipoResponse> getReporteTipo();
  public List<ReporteFrecuenciaResponse> getReporteFrecuencia();
  public List<ReporteFormatoResponse> getReporteFormato();
  public List<ReporteAlmacenStockResponse> getReporteAlmacenStock();
  public void stopProgramacionReporte(int idProgramacionReporte);
  public List<ReporteMostrarProgramacionResponse> getReporteProgramacionAll();
  public ReporteRequest newReporte(ReporteRequest reporteRequest);
} 
