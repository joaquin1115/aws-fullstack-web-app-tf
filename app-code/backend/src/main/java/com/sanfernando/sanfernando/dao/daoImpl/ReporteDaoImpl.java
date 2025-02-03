package com.sanfernando.sanfernando.dao.daoImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Repository;

import com.sanfernando.sanfernando.dao.ReporteDao;
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
import com.sanfernando.sanfernando.utils.Conexion;
import com.sanfernando.sanfernando.utils.TimeUtils;
import com.sanfernando.sanfernando.utils.quartz.ReporteJob;
import com.sanfernando.sanfernando.utils.quartz.ReporteScheduler;

@Repository
public class ReporteDaoImpl implements ReporteDao {

  private final Conexion con = new Conexion();
  private final TimeUtils timeUtils = new TimeUtils();

  @Override
  public List<ReporteOperacionResponse> getReporteOperacion() {
    con.startConexion();
    List<ReporteOperacionResponse> reporteOperacionResponses = new ArrayList<>();
    try {
      String query = 
        "SELECT ot.cod_tipo_operacion, ot.descripcion," +
        "SUM(COALESCE(hora_fin-hora_inicio,'00:00:00')), COUNT(o.id_operacion)::integer, COALESCE(SUM(hora_fin-hora_inicio)/COUNT(o.id_operacion),'00:00:00') as tiempo_medio, " +  
        "STRING_AGG ( " + 
          "o.id_operacion::character varying, " + 
          "'*' " + 
          ") lista_cod_operacion " + 
        "FROM operacion_tipo AS ot " + 
        "LEFT JOIN operacion AS o ON o.cod_tipo_operacion = ot.cod_tipo_operacion " + 
        "GROUP BY " + 
        "ot.cod_tipo_operacion; ";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReporteOperacionResponse reporteOperacionResponse = ReporteOperacionResponse
          .builder()
          .idOperacionTipo(rs.getInt("cod_tipo_operacion"))
          .cantidad(rs.getInt("count"))
          .tiempoTotal(rs.getTime("sum"))
          .tiempoMedio(timeUtils.convertTimeToHours(rs.getTime("tiempo_medio")))
          .nombreOperacion(rs.getString("descripcion"))
          .listaOperacion(rs.getString("lista_cod_operacion"))
          .build();
          reporteOperacionResponses.add(reporteOperacionResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return reporteOperacionResponses;
  }

  @Override
  public ReporteProgramacionResponse newProgramacion(ReporteProgramacionRequest reporteProgramacionRequest) {
    con.startConexion();
    ReporteProgramacionResponse reporteProgramacionResponse = new ReporteProgramacionResponse();
    try {
      String query = 
        "INSERT INTO programacion_reporte " +
        "( cod_representante, cod_reporte_formato, cod_reporte_estado, cod_reporte_tipo,cod_reporte_frecuencia, fecha_inicio, fecha_fin ) VALUES " +
        "(?, ?, ?, ?, ?, ?, ?);";
      PreparedStatement ps = con.getCon().prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
      System.out.println("Preparando batch: \n\n\n");
      ps.setInt(1, reporteProgramacionRequest.getIdRepresentante());
      ps.setInt(2, reporteProgramacionRequest.getIdReporteFormato());

      reporteProgramacionRequest.setIdReporteEstado(1);
      ps.setInt(3, reporteProgramacionRequest.getIdReporteEstado());

      ps.setInt(4, reporteProgramacionRequest.getIdReporteTipo());
      ps.setInt(5, reporteProgramacionRequest.getIdReporteFrecuencia());

      java.sql.Date sqlDateInicio = java.sql.Date.valueOf(reporteProgramacionRequest.getFechaInicio());
      ps.setDate(6, sqlDateInicio);
      java.sql.Date sqlDateFin = java.sql.Date.valueOf(reporteProgramacionRequest.getFechaFin());
      ps.setDate(7, sqlDateFin);

      ps.executeUpdate();
      ResultSet rs = ps.getGeneratedKeys();
      while (rs.next()) {
        reporteProgramacionResponse.setIdProgramacionReporte(rs.getInt(1));
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();

    try {
      this.executeSchedulerReport(reporteProgramacionResponse, reporteProgramacionRequest.getFechaFin());
    } catch (SchedulerException e) {
      e.printStackTrace();
    }

    return reporteProgramacionResponse;
  }

  public void executeSchedulerReport(ReporteProgramacionResponse reporteProgramacionResponse, String fechaFin) throws SchedulerException{
    if (reporteProgramacionResponse.getIdProgramacionReporte() != null) {
      ReporteScheduler scheduler = ReporteScheduler
				.builder()
				.nombreTrigger("ReporteTrigger" + reporteProgramacionResponse.getIdProgramacionReporte())
				.nombreJob("ReporteJob" + reporteProgramacionResponse.getIdProgramacionReporte())
				.nombreGroup("ReporteGroup" + reporteProgramacionResponse.getIdProgramacionReporte())
				.build();
			scheduler.startScheduler(ReporteJob.class, fechaFin,timeUtils.localTimePlus(20), reporteProgramacionResponse.getIdProgramacionReporte());
    }
  }

  @Override
  public void stopProgramacionReporte(int idProgramacionReporte)  {
    con.startConexion();
    try {
      String query = "UPDATE programacion_reporte SET cod_reporte_estado = ? WHERE cod_programacion_reporte = ?";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ps.setInt(1, 2);
      ps.setInt(2, idProgramacionReporte);
      ps.executeUpdate();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
  }

  @Override
  public List<ReporteReclamoUrgenciaResponse> getReporteReclamoUrgencia() {
    con.startConexion();
    List<ReporteReclamoUrgenciaResponse> reporteReclamoUrgenciaResponses = new ArrayList<>();
    try {
      String query =            
        "SELECT " +
        "    rt.cod_tipo_reclamo, " +
        "    rt.descripcion, " +
        "    COUNT(re.cod_reclamo) AS total, " +
        "    SUM(CASE WHEN nu.cod_nivel_urgencia = 'B' THEN 1 ELSE 0 END) AS urgencia_baja, " +
        "    SUM(CASE WHEN nu.cod_nivel_urgencia = 'M' THEN 1 ELSE 0 END) AS urgencia_media, " +
        "    SUM(CASE WHEN nu.cod_nivel_urgencia = 'A' THEN 1 ELSE 0 END) AS urgencia_alta " +
        "FROM reclamo_tipo AS rt " +
        "LEFT JOIN reclamo AS re ON re.cod_tipo_reclamo = rt.cod_tipo_reclamo " +
        "LEFT JOIN nivel_urgencia AS nu ON nu.cod_nivel_urgencia = re.cod_nivel_urgencia " +
        "GROUP BY rt.cod_tipo_reclamo " +
        "ORDER BY rt.cod_tipo_reclamo";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReporteReclamoUrgenciaResponse reporteReclamoUrgenciaResponse = ReporteReclamoUrgenciaResponse
          .builder()
          .idTipoReclamo(rs.getString("cod_tipo_reclamo"))
          .descripcion(rs.getString("descripcion"))
          .totalTipoReclamo(rs.getInt("total"))
          .totalUrgenciaBaja(rs.getInt("urgencia_baja"))
          .totalUrgenciaMedia(rs.getInt("urgencia_media"))
          .totalUrgenciaAlta(rs.getInt("urgencia_alta"))
          .build();
        reporteReclamoUrgenciaResponses.add(reporteReclamoUrgenciaResponse);
      } 
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return reporteReclamoUrgenciaResponses;
  }

  @Override
  public List<ReporteReclamoTiempoResponse> getReporteReclamoTiempo() {
    con.startConexion();
    List<ReporteReclamoTiempoResponse> reporteReclamoTiempoResponses = new ArrayList<>();
    try {
      String query =
        "SELECT " +
        "    nu.cod_nivel_urgencia, " +
        "    nu.descripcion, " +
        "    SUM(se.fecha_resolucion - re.fecha_reclamo) / COUNT(re.cod_reclamo) AS tiempo_medio, " +
        "    COUNT(re.cod_reclamo) AS cantidad " +
        "FROM nivel_urgencia AS nu " +
        "LEFT JOIN reclamo AS re ON re.cod_nivel_urgencia = nu.cod_nivel_urgencia " +
        "LEFT JOIN seguimiento AS se ON se.cod_seguimiento = re.cod_seguimiento " +
        "GROUP BY nu.cod_nivel_urgencia";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();

      double totalCantidadUrgencia = 0;
      double totalTiempoUrgencia = 0;

      while (rs.next()) {
        ReporteReclamoTiempoResponse reporteReclamoTiempoResponse = ReporteReclamoTiempoResponse
          .builder()
          .idNivelUrgencia(rs.getString("cod_nivel_urgencia"))
          .descripcion(rs.getString("descripcion"))
          .tiempoMedio(rs.getDouble("tiempo_medio"))
          .totalNivelUrgencia(rs.getInt("cantidad"))
          .build();
          totalCantidadUrgencia += reporteReclamoTiempoResponse.getTotalNivelUrgencia();
          totalTiempoUrgencia += (reporteReclamoTiempoResponse.getTiempoMedio()*reporteReclamoTiempoResponse.getTotalNivelUrgencia());
        reporteReclamoTiempoResponses.add(reporteReclamoTiempoResponse);
      }
      ReporteReclamoTiempoResponse reporteReclamoTiempoResponse = ReporteReclamoTiempoResponse
        .builder()
        .idNivelUrgencia("TOTAL")
        .descripcion("Tiempo promedio total")
        .tiempoMedio( totalTiempoUrgencia/totalCantidadUrgencia )
        .totalNivelUrgencia((int) totalCantidadUrgencia)
        .build();
      reporteReclamoTiempoResponses.add(reporteReclamoTiempoResponse);
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return reporteReclamoTiempoResponses;
  }

  @Override
  public List<ReporteReclamoMesResponse> getReporteReclamoMes() {
    con.startConexion();
    List<ReporteReclamoMesResponse> reporteReclamoMesResponses = new ArrayList<>();
    try {
      String query =
        "WITH meses AS ( " +
        "    SELECT generate_series(1, 12) AS mes_num " +
        ") " +
        "SELECT " +
        "    TO_CHAR(TO_DATE(mes_num::text, 'MM'), 'TMMonth') AS mes, " +
        "    COUNT(re.fecha_reclamo) AS total_reclamos " +
        "FROM meses " +
        "LEFT JOIN reclamo AS re " +
        "ON EXTRACT(MONTH FROM re.fecha_reclamo) = meses.mes_num " +
        "AND EXTRACT(YEAR FROM re.fecha_reclamo) = 2024 " +
        "GROUP BY meses.mes_num " +
        "ORDER BY meses.mes_num";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReporteReclamoMesResponse reporteReclamoMesResponse = ReporteReclamoMesResponse
          .builder()
          .mes(rs.getString("mes"))
          .totalReclamos(rs.getInt("total_reclamos"))
          .build();
        reporteReclamoMesResponses.add(reporteReclamoMesResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return reporteReclamoMesResponses;
  }

  @Override
  public List<ReportePedidoMesResponse> getReportePedidoMes() {
    con.startConexion();
    List<ReportePedidoMesResponse> reportePedidoMesResponses = new ArrayList<>();
    try {
      String query =
        "WITH meses AS ( " +
        "    SELECT generate_series(1, 12) AS mes_num " +
        ") " +
        "SELECT " +
        "    TO_CHAR(TO_DATE(mes_num::text, 'MM'), 'TMMonth') AS mes, " +
        "    COUNT(pe.fecha_registro) AS total_pedidos " +
        "FROM meses " +
        "LEFT JOIN pedido AS pe " +
        "ON EXTRACT(MONTH FROM pe.fecha_registro) = meses.mes_num " +
        "AND EXTRACT(YEAR FROM pe.fecha_registro) = 2024 " +
        "GROUP BY meses.mes_num " +
        "ORDER BY meses.mes_num";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReportePedidoMesResponse reportePedidoMesResponse = ReportePedidoMesResponse
          .builder()
          .mes(rs.getString("mes"))
          .totalPedidos(rs.getInt("total_pedidos"))
          .build();
        reportePedidoMesResponses.add(reportePedidoMesResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return reportePedidoMesResponses;
  }

  @Override
  public List<ReportePedidoTopResponse> getReportePedidoTop() {
    con.startConexion();
    List<ReportePedidoTopResponse> reportePedidoTopResponses = new ArrayList<>();
    try {
      String query =
        "SELECT ec.id_elemento_catalogo, ec.nombre, ec.descripcion, SUM(dtp.cantidad) AS cantidad " +
        "FROM pedido AS pe " +
        "INNER JOIN ticket AS t ON t.cod_ticket = pe.cod_ticket AND pe.cod_pedido_tipo = 'V' " +
        "INNER JOIN detalle_ticket_producto AS dtp ON dtp.cod_ticket = t.cod_ticket " +
        "INNER JOIN elemento_catalogo AS ec ON ec.id_elemento_catalogo = dtp.id_elemento_catalogo " +
        "GROUP BY ec.id_elemento_catalogo, ec.nombre, ec.descripcion " +
        "ORDER BY cantidad DESC " +
        "LIMIT 5";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReportePedidoTopResponse reportePedidoTopResponse = ReportePedidoTopResponse
          .builder()
          .idElementoCatalogo(rs.getInt("id_elemento_catalogo"))
          .nombre(rs.getString("nombre"))
          .descripcion(rs.getString("descripcion"))
          .cantidad(rs.getInt("cantidad"))
          .build();
        reportePedidoTopResponses.add(reportePedidoTopResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return reportePedidoTopResponses;
  }

  @Override
  public List<ReporteTipoResponse> getReporteTipo() {
    con.startConexion();
    List<ReporteTipoResponse> reporteTipoResponses = new ArrayList<>();
    try {
      String query = "SELECT * FROM reporte_tipo; ";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReporteTipoResponse reporteTipoResponse = ReporteTipoResponse
          .builder()
          .idReporteTipo(rs.getInt("cod_reporte_tipo"))
          .descripcion(rs.getString("descripcion"))
          .build();
        reporteTipoResponses.add(reporteTipoResponse);
      } 
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return reporteTipoResponses;
  }

  @Override
  public List<ReporteFrecuenciaResponse> getReporteFrecuencia() {
    con.startConexion();
    List<ReporteFrecuenciaResponse> reporteFrecuenciaResponses = new ArrayList<>();
    try {
      String query = "SELECT cod_reporte_frecuencia, descripcion FROM reporte_frecuencia;";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReporteFrecuenciaResponse reporteFrecuenciaResponse = ReporteFrecuenciaResponse
          .builder()
          .idReporteFrecuencia(rs.getInt("cod_reporte_frecuencia"))
          .descripcion(rs.getString("descripcion"))
          .build();
        reporteFrecuenciaResponses.add(reporteFrecuenciaResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return reporteFrecuenciaResponses;
  }

  @Override
  public List<ReporteFormatoResponse> getReporteFormato() {
    con.startConexion();
    List<ReporteFormatoResponse> reporteFormatoResponses = new ArrayList<>();
    try {
      String query = "SELECT cod_reporte_formato, descripcion FROM reporte_formato;";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReporteFormatoResponse reporteFormatoResponse = ReporteFormatoResponse
          .builder()
          .idReporteFormato(rs.getInt("cod_reporte_formato"))
          .descripcion(rs.getString("descripcion"))
          .build();
        reporteFormatoResponses.add(reporteFormatoResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return reporteFormatoResponses;
  }

  @Override
  public List<ReporteAlmacenStockResponse> getReporteAlmacenStock() {
    con.startConexion();
    List<ReporteAlmacenStockResponse> reporteAlmacenStockResponses = new ArrayList<>();
    try {
      String query = 
        "SELECT ec.id_elemento_catalogo, " +
        "ec.nombre, " +
        "ec.peso_unitario, " +
        "ecu.descripcion AS unidad, " +
        "ect.descripcion AS tipo, " +
        "ep.descripcion AS produccion, " +
        "SUM(st.cantidad_disponible) AS cantidad " +
        "FROM elemento_catalogo AS ec " +
        "LEFT JOIN stock AS st ON st.id_elemento_catalogo = ec.id_elemento_catalogo " +
        "LEFT JOIN elemento_catalogo_unidad AS ecu ON ecu.cod_unidad = ec.cod_unidad " +
        "LEFT JOIN elemento_catalogo_tipo AS ect ON ect.id_elemento_catalogo_tipo = ec.id_elemento_catalogo_tipo " +
        "LEFT JOIN elemento_produccion AS ep ON ep.id_elemento_produccion = ect.id_elemento_produccion " +
        "GROUP BY ec.id_elemento_catalogo, ecu.cod_unidad, ect.id_elemento_catalogo_tipo, ep.id_elemento_produccion;";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReporteAlmacenStockResponse reporteAlmacenStockResponse = ReporteAlmacenStockResponse
          .builder()
          .idElementoCatalogo(rs.getInt("id_elemento_catalogo"))
          .nombre(rs.getString("nombre"))
          .pesoUnitario(rs.getDouble("peso_unitario"))
          .unidad(rs.getString("unidad"))
          .tipoElemento(rs.getString("tipo"))
          .tipoProduccion(rs.getString("produccion"))
          .cantidad(rs.getDouble("cantidad"))
          .build();
        reporteAlmacenStockResponses.add(reporteAlmacenStockResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return reporteAlmacenStockResponses;
  }

  @Override
  public List<ReporteMostrarProgramacionResponse> getReporteProgramacionAll() {
    con.startConexion();
    List<ReporteMostrarProgramacionResponse> reporteMostrarProgramacionResponses = new ArrayList<>();
    try {
      String query =           
        "SELECT "
        + "pr.cod_programacion_reporte, "
        + "rfo.descripcion AS formato, "
        + "rt.descripcion AS tipo, "
        + "rfe.descripcion AS frecuencia, "
        + "pr.fecha_inicio, "
        + "pr.fecha_fin "
        + "FROM programacion_reporte AS pr "
        + "INNER JOIN reporte_formato AS rfo ON rfo.cod_reporte_formato = pr.cod_reporte_formato "
        + "INNER JOIN reporte_tipo AS rt ON rt.cod_reporte_tipo = pr.cod_reporte_tipo "
        + "INNER JOIN reporte_frecuencia AS rfe ON rfe.cod_reporte_frecuencia = pr.cod_reporte_frecuencia "
        + "WHERE pr.cod_reporte_estado = 1 "
        + "ORDER BY pr.cod_programacion_reporte";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReporteMostrarProgramacionResponse reporteMostrarProgramacionResponse = ReporteMostrarProgramacionResponse
          .builder()
          .idProgramacionReporte(rs.getInt("cod_programacion_reporte"))
          .descripcionFormato(rs.getString("formato"))
          .descripcionTipo(rs.getString("tipo"))
          .descripcionFrecuencia(rs.getString("frecuencia"))
          .fechaInicio(rs.getString("fecha_inicio"))
          .fechaFin(rs.getString("fecha_fin"))
          .build();
        reporteMostrarProgramacionResponses.add(reporteMostrarProgramacionResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return reporteMostrarProgramacionResponses;
  }

  @Override
  public ReporteRequest newReporte(ReporteRequest reporteRequest) {
    con.startConexion();
    ReporteRequest reporteRequestResponse = new ReporteRequest();
    try {
      String query = 
        "INSERT INTO reporte " +
        "(cod_representante, cod_reporte_formato, cod_reporte_tipo, fecha_generacion, hora_generacion) " +
        "VALUES (?, ?, ?, ?, ?); ";
      PreparedStatement ps = con.getCon().prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, reporteRequest.getIdRepresentante());
      ps.setInt(2, reporteRequest.getIdReporteFormato());
      ps.setInt(3, reporteRequest.getIdReporteTipo());

      java.sql.Date sqlFechaGeneracion = java.sql.Date.valueOf(timeUtils.getCurrentDate());
      ps.setDate(4, sqlFechaGeneracion);
      java.sql.Time sqlHoraGeneracion = java.sql.Time.valueOf(timeUtils.getCurrentTime());
      ps.setTime(5, sqlHoraGeneracion);
      
      ps.executeUpdate();
      ResultSet rs = ps.getGeneratedKeys();
      while (rs.next()) {
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return reporteRequest;
  }
}
