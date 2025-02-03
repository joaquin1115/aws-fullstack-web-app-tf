package com.sanfernando.sanfernando.services.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sanfernando.sanfernando.dao.ReporteDao;
import com.sanfernando.sanfernando.dtos.requests.ReporteProgramacionRequest;
import com.sanfernando.sanfernando.dtos.requests.reportes.ReporteRequest;
import com.sanfernando.sanfernando.dtos.responses.ReporteFrecuenciaResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteAlmacenStockResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteLookUpTablesResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteMostrarProgramacionResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteOperacionResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReportePedidoMesResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReportePedidoTopResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteProgramacionResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteReclamoMesResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteReclamoTiempoResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteReclamoUrgenciaResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.ReporteTipoResponse;
import com.sanfernando.sanfernando.services.ReporteService;

@Service
public class ReporteServiceImpl implements ReporteService{

  @Autowired
  private ReporteDao reporteDao;

  @Override
  public List<ReporteOperacionResponse> getReporteOperacion() {
    return reporteDao.getReporteOperacion();
  }

  @Override
  public ReporteProgramacionResponse newProgramacion(ReporteProgramacionRequest reporteProgramacionRequest) {
    return reporteDao.newProgramacion(reporteProgramacionRequest);
  }

  @Override
  public List<ReporteReclamoUrgenciaResponse> getReporteReclamoUrgencia() {
    return reporteDao.getReporteReclamoUrgencia();
  }

  @Override
  public List<ReporteReclamoTiempoResponse> getReporteReclamoTiempo() {
    return reporteDao.getReporteReclamoTiempo();
  }

  @Override
  public List<ReporteReclamoMesResponse> getReporteReclamoMes() {
    return reporteDao.getReporteReclamoMes();
  }

  @Override
  public List<ReportePedidoMesResponse> getReportePedidoMes() {
    return reporteDao.getReportePedidoMes();
  }

  @Override
  public List<ReportePedidoTopResponse> getReportePedidoTop() {
    return reporteDao.getReportePedidoTop();
  }

  @Override
  public List<ReporteTipoResponse> getReporteTipo() {
    return reporteDao.getReporteTipo();
  }

  @Override
  public List<ReporteFrecuenciaResponse> getReporteFrecuencia() {
    return reporteDao.getReporteFrecuencia();
  }

  @Override
  public ReporteLookUpTablesResponse getReporteLookUpTables() {
    ReporteLookUpTablesResponse reporteLookUpTablesResponse = ReporteLookUpTablesResponse
      .builder()
      .reporteTipoResponse(this.getReporteTipo())
      .reporteFrecuenciaResponse(this.getReporteFrecuencia())
      .reporteFormatoResponse(reporteDao.getReporteFormato())
      .build();
    return reporteLookUpTablesResponse;
  }

  @Override
  public List<ReporteAlmacenStockResponse> getReporteAlmacenStock() {
    return reporteDao.getReporteAlmacenStock();
  }

  @Override
  public void stopProgramacionReporte(int idProgramacionReporte) {
    reporteDao.stopProgramacionReporte(idProgramacionReporte);
  }

  @Override
  public List<ReporteMostrarProgramacionResponse> getReporteProgramacionAll() {
    return reporteDao.getReporteProgramacionAll();
  }

  @Override
  public ReporteRequest newReporte(ReporteRequest reporteRequest) {
    return reporteDao.newReporte(reporteRequest);
  }
}
