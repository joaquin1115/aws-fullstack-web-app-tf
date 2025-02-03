package com.sanfernando.sanfernando.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {
  
  @Autowired
  private ReporteService reporteService;

  @GetMapping(value = "lookup/tipo")
  public ResponseEntity<Object> getReporteTipo() {
    List<ReporteTipoResponse> response = reporteService.getReporteTipo();
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "lookup/frecuencia")
  public ResponseEntity<Object> getReporteFrecuencia() {
    List<ReporteFrecuenciaResponse> response = reporteService.getReporteFrecuencia();
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "lookup/all")
  public ResponseEntity<Object> getReporteLookUpTables() {
    ReporteLookUpTablesResponse response = reporteService.getReporteLookUpTables();
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "programacion")
  public ResponseEntity<Object> newForm(@RequestBody ReporteProgramacionRequest request) {
    ReporteProgramacionResponse response = reporteService.newProgramacion(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "new")
  public ResponseEntity<Object> newReporte(@RequestBody ReporteRequest request) {
    ReporteRequest response = reporteService.newReporte(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "programacion/all")
  public ResponseEntity<Object> getReporteProgramacionAll() {
    List<ReporteMostrarProgramacionResponse> response = reporteService.getReporteProgramacionAll();
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "almacen/stock")
  public ResponseEntity<Object> getReporteAlmacenStock() {
    List<ReporteAlmacenStockResponse> response = reporteService.getReporteAlmacenStock();
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "operacion")
  public ResponseEntity<Object> getReporteOperacion() {
    List<ReporteOperacionResponse> response = reporteService.getReporteOperacion();
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "reclamo/urgencia")
  public ResponseEntity<Object> getReclamoUrgencia() {
    List<ReporteReclamoUrgenciaResponse> response = reporteService.getReporteReclamoUrgencia();
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "reclamo/tiempo")
  public ResponseEntity<Object> getReporteReclamoTiempo() {
    List<ReporteReclamoTiempoResponse> response = reporteService.getReporteReclamoTiempo();
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "reclamo/mes")
  public ResponseEntity<Object> getReporteReclamoMes() {
    List<ReporteReclamoMesResponse> response = reporteService.getReporteReclamoMes();
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "pedido/mes")
  public ResponseEntity<Object> getReportePedidoMes() {
    List<ReportePedidoMesResponse> response = reporteService.getReportePedidoMes();
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "pedido/top")
  public ResponseEntity<Object> getReportePedidoTop() {
    List<ReportePedidoTopResponse> response = reporteService.getReportePedidoTop();
    return ResponseEntity.ok(response);
  }
}
