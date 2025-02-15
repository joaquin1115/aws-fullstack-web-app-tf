package com.sanfernando.sanfernando.dao.daoImpl;

import com.sanfernando.sanfernando.dao.ReporteDao;
import com.sanfernando.sanfernando.dtos.requests.ReporteProgramacionRequest;
import com.sanfernando.sanfernando.dtos.requests.reportes.ReporteRequest;
import com.sanfernando.sanfernando.dtos.responses.ReporteFrecuenciaResponse;
import com.sanfernando.sanfernando.dtos.responses.reporte.*;
import com.sanfernando.sanfernando.utils.TimeUtils;
import com.sanfernando.sanfernando.utils.quartz.ReporteJob;
import com.sanfernando.sanfernando.utils.quartz.ReporteScheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReporteDaoImpl implements ReporteDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final TimeUtils timeUtils = new TimeUtils();

  @Override
  public List<ReporteOperacionResponse> getReporteOperacion() {
    String query =
            "SELECT ot.cod_tipo_operacion, ot.descripcion, " +
                    "SUM(COALESCE(hora_fin-hora_inicio,'00:00:00')), COUNT(o.id_operacion)::integer, " +
                    "COALESCE(SUM(hora_fin-hora_inicio)/COUNT(o.id_operacion),'00:00:00') as tiempo_medio, " +
                    "STRING_AGG(o.id_operacion::character varying, '*') lista_cod_operacion " +
                    "FROM operacion_tipo AS ot " +
                    "LEFT JOIN operacion AS o ON o.cod_tipo_operacion = ot.cod_tipo_operacion " +
                    "GROUP BY ot.cod_tipo_operacion";

    return jdbcTemplate.query(query, (rs, rowNum) ->
            ReporteOperacionResponse.builder()
                    .idOperacionTipo(rs.getInt("cod_tipo_operacion"))
                    .cantidad(rs.getInt("count"))
                    .tiempoTotal(rs.getTime("sum"))
                    .tiempoMedio(timeUtils.convertTimeToHours(rs.getTime("tiempo_medio")))
                    .nombreOperacion(rs.getString("descripcion"))
                    .listaOperacion(rs.getString("lista_cod_operacion"))
                    .build()
    );
  }

  @Override
  public ReporteProgramacionResponse newProgramacion(ReporteProgramacionRequest request) {
    String query =
            "INSERT INTO programacion_reporte " +
                    "(cod_representante, cod_reporte_formato, cod_reporte_estado, cod_reporte_tipo, " +
                    "cod_reporte_frecuencia, fecha_inicio, fecha_fin) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      var ps = connection.prepareStatement(query, new String[]{"cod_programacion_reporte"});
      ps.setInt(1, request.getIdRepresentante());
      ps.setInt(2, request.getIdReporteFormato());
      ps.setInt(3, 1); // default estado
      ps.setInt(4, request.getIdReporteTipo());
      ps.setInt(5, request.getIdReporteFrecuencia());
      ps.setDate(6, java.sql.Date.valueOf(request.getFechaInicio()));
      ps.setDate(7, java.sql.Date.valueOf(request.getFechaFin()));
      return ps;
    }, keyHolder);

    ReporteProgramacionResponse response = new ReporteProgramacionResponse();
    response.setIdProgramacionReporte(keyHolder.getKey().intValue());

    try {
      executeSchedulerReport(response, request.getFechaFin());
    } catch (SchedulerException e) {
      e.printStackTrace();
    }

    return response;
  }

  @Override
  public void stopProgramacionReporte(int idProgramacionReporte) {
    String query = "UPDATE programacion_reporte SET cod_reporte_estado = ? WHERE cod_programacion_reporte = ?";
    jdbcTemplate.update(query, 2, idProgramacionReporte);
  }

  @Override
  public List<ReporteReclamoUrgenciaResponse> getReporteReclamoUrgencia() {
    String query =
            "SELECT rt.cod_tipo_reclamo, rt.descripcion, COUNT(re.cod_reclamo) AS total, " +
                    "SUM(CASE WHEN nu.cod_nivel_urgencia = 'B' THEN 1 ELSE 0 END) AS urgencia_baja, " +
                    "SUM(CASE WHEN nu.cod_nivel_urgencia = 'M' THEN 1 ELSE 0 END) AS urgencia_media, " +
                    "SUM(CASE WHEN nu.cod_nivel_urgencia = 'A' THEN 1 ELSE 0 END) AS urgencia_alta " +
                    "FROM reclamo_tipo AS rt " +
                    "LEFT JOIN reclamo AS re ON re.cod_tipo_reclamo = rt.cod_tipo_reclamo " +
                    "LEFT JOIN nivel_urgencia AS nu ON nu.cod_nivel_urgencia = re.cod_nivel_urgencia " +
                    "GROUP BY rt.cod_tipo_reclamo " +
                    "ORDER BY rt.cod_tipo_reclamo";

    return jdbcTemplate.query(query, (rs, rowNum) ->
            ReporteReclamoUrgenciaResponse.builder()
                    .idTipoReclamo(rs.getString("cod_tipo_reclamo"))
                    .descripcion(rs.getString("descripcion"))
                    .totalTipoReclamo(rs.getInt("total"))
                    .totalUrgenciaBaja(rs.getInt("urgencia_baja"))
                    .totalUrgenciaMedia(rs.getInt("urgencia_media"))
                    .totalUrgenciaAlta(rs.getInt("urgencia_alta"))
                    .build()
    );
  }

  @Override
  public List<ReporteReclamoTiempoResponse> getReporteReclamoTiempo() {
    String query =
            "SELECT nu.cod_nivel_urgencia, nu.descripcion, " +
                    "SUM(se.fecha_resolucion - re.fecha_reclamo) / COUNT(re.cod_reclamo) AS tiempo_medio, " +
                    "COUNT(re.cod_reclamo) AS cantidad " +
                    "FROM nivel_urgencia AS nu " +
                    "LEFT JOIN reclamo AS re ON re.cod_nivel_urgencia = nu.cod_nivel_urgencia " +
                    "LEFT JOIN seguimiento AS se ON se.cod_seguimiento = re.cod_seguimiento " +
                    "GROUP BY nu.cod_nivel_urgencia";

    List<ReporteReclamoTiempoResponse> responses = jdbcTemplate.query(query, (rs, rowNum) ->
            ReporteReclamoTiempoResponse.builder()
                    .idNivelUrgencia(rs.getString("cod_nivel_urgencia"))
                    .descripcion(rs.getString("descripcion"))
                    .tiempoMedio(rs.getDouble("tiempo_medio"))
                    .totalNivelUrgencia(rs.getInt("cantidad"))
                    .build()
    );

    // Calculate totals
    double totalCantidad = responses.stream()
            .mapToDouble(ReporteReclamoTiempoResponse::getTotalNivelUrgencia)
            .sum();
    double totalTiempo = responses.stream()
            .mapToDouble(r -> r.getTiempoMedio() * r.getTotalNivelUrgencia())
            .sum();

    responses.add(ReporteReclamoTiempoResponse.builder()
            .idNivelUrgencia("TOTAL")
            .descripcion("Tiempo promedio total")
            .tiempoMedio(totalTiempo / totalCantidad)
            .totalNivelUrgencia((int) totalCantidad)
            .build());

    return responses;
  }

  @Override
  public List<ReporteReclamoMesResponse> getReporteReclamoMes() {
    String query =
            "WITH meses AS (SELECT generate_series(1, 12) AS mes_num) " +
                    "SELECT TO_CHAR(TO_DATE(mes_num::text, 'MM'), 'TMMonth') AS mes, " +
                    "COUNT(re.fecha_reclamo) AS total_reclamos " +
                    "FROM meses " +
                    "LEFT JOIN reclamo AS re ON EXTRACT(MONTH FROM re.fecha_reclamo) = meses.mes_num " +
                    "AND EXTRACT(YEAR FROM re.fecha_reclamo) = 2024 " +
                    "GROUP BY meses.mes_num ORDER BY meses.mes_num";

    return jdbcTemplate.query(query, (rs, rowNum) ->
            ReporteReclamoMesResponse.builder()
                    .mes(rs.getString("mes"))
                    .totalReclamos(rs.getInt("total_reclamos"))
                    .build()
    );
  }

  @Override
  public List<ReportePedidoMesResponse> getReportePedidoMes() {
    String query =
            "WITH meses AS (SELECT generate_series(1, 12) AS mes_num) " +
                    "SELECT TO_CHAR(TO_DATE(mes_num::text, 'MM'), 'TMMonth') AS mes, " +
                    "COUNT(pe.fecha_registro) AS total_pedidos " +
                    "FROM meses " +
                    "LEFT JOIN pedido AS pe ON EXTRACT(MONTH FROM pe.fecha_registro) = meses.mes_num " +
                    "AND EXTRACT(YEAR FROM pe.fecha_registro) = 2024 " +
                    "GROUP BY meses.mes_num ORDER BY meses.mes_num";

    return jdbcTemplate.query(query, (rs, rowNum) ->
            ReportePedidoMesResponse.builder()
                    .mes(rs.getString("mes"))
                    .totalPedidos(rs.getInt("total_pedidos"))
                    .build()
    );
  }

  @Override
  public List<ReportePedidoTopResponse> getReportePedidoTop() {
    String query =
            "SELECT ec.id_elemento_catalogo, ec.nombre, ec.descripcion, SUM(dtp.cantidad) AS cantidad " +
                    "FROM pedido AS pe " +
                    "INNER JOIN ticket AS t ON t.cod_ticket = pe.cod_ticket AND pe.cod_pedido_tipo = 'V' " +
                    "INNER JOIN detalle_ticket_producto AS dtp ON dtp.cod_ticket = t.cod_ticket " +
                    "INNER JOIN elemento_catalogo AS ec ON ec.id_elemento_catalogo = dtp.id_elemento_catalogo " +
                    "GROUP BY ec.id_elemento_catalogo, ec.nombre, ec.descripcion " +
                    "ORDER BY cantidad DESC LIMIT 5";

    return jdbcTemplate.query(query, (rs, rowNum) ->
            ReportePedidoTopResponse.builder()
                    .idElementoCatalogo(rs.getInt("id_elemento_catalogo"))
                    .nombre(rs.getString("nombre"))
                    .descripcion(rs.getString("descripcion"))
                    .cantidad(rs.getInt("cantidad"))
                    .build()
    );
  }

  @Override
  public List<ReporteTipoResponse> getReporteTipo() {
    return jdbcTemplate.query(
            "SELECT * FROM reporte_tipo",
            (rs, rowNum) -> ReporteTipoResponse.builder()
                    .idReporteTipo(rs.getInt("cod_reporte_tipo"))
                    .descripcion(rs.getString("descripcion"))
                    .build()
    );
  }

  @Override
  public List<ReporteFrecuenciaResponse> getReporteFrecuencia() {
    return jdbcTemplate.query(
            "SELECT cod_reporte_frecuencia, descripcion FROM reporte_frecuencia",
            (rs, rowNum) -> ReporteFrecuenciaResponse.builder()
                    .idReporteFrecuencia(rs.getInt("cod_reporte_frecuencia"))
                    .descripcion(rs.getString("descripcion"))
                    .build()
    );
  }

  @Override
  public List<ReporteFormatoResponse> getReporteFormato() {
    return jdbcTemplate.query(
            "SELECT cod_reporte_formato, descripcion FROM reporte_formato",
            (rs, rowNum) -> ReporteFormatoResponse.builder()
                    .idReporteFormato(rs.getInt("cod_reporte_formato"))
                    .descripcion(rs.getString("descripcion"))
                    .build()
    );
  }

  @Override
  public List<ReporteAlmacenStockResponse> getReporteAlmacenStock() {
    String query =
            "SELECT ec.id_elemento_catalogo, ec.nombre, ec.peso_unitario, " +
                    "ecu.descripcion AS unidad, ect.descripcion AS tipo, " +
                    "ep.descripcion AS produccion, SUM(st.cantidad_disponible) AS cantidad " +
                    "FROM elemento_catalogo AS ec " +
                    "LEFT JOIN stock AS st ON st.id_elemento_catalogo = ec.id_elemento_catalogo " +
                    "LEFT JOIN elemento_catalogo_unidad AS ecu ON ecu.cod_unidad = ec.cod_unidad " +
                    "LEFT JOIN elemento_catalogo_tipo AS ect ON ect.id_elemento_catalogo_tipo = ec.id_elemento_catalogo_tipo " +
                    "LEFT JOIN elemento_produccion AS ep ON ep.id_elemento_produccion = ect.id_elemento_produccion " +
                    "GROUP BY ec.id_elemento_catalogo, ecu.cod_unidad, ect.id_elemento_catalogo_tipo, ep.id_elemento_produccion";

    return jdbcTemplate.query(query, (rs, rowNum) ->
            ReporteAlmacenStockResponse.builder()
                    .idElementoCatalogo(rs.getInt("id_elemento_catalogo"))
                    .nombre(rs.getString("nombre"))
                    .pesoUnitario(rs.getDouble("peso_unitario"))
                    .unidad(rs.getString("unidad"))
                    .tipoElemento(rs.getString("tipo"))
                    .tipoProduccion(rs.getString("produccion"))
                    .cantidad(rs.getDouble("cantidad"))
                    .build()
    );
  }

  @Override
  public List<ReporteMostrarProgramacionResponse> getReporteProgramacionAll() {
    String query =
            "SELECT pr.cod_programacion_reporte, rfo.descripcion AS formato, " +
                    "rt.descripcion AS tipo, rfe.descripcion AS frecuencia, " +
                    "pr.fecha_inicio, pr.fecha_fin " +
                    "FROM programacion_reporte AS pr " +
                    "INNER JOIN reporte_formato AS rfo ON rfo.cod_reporte_formato = pr.cod_reporte_formato " +
                    "INNER JOIN reporte_tipo AS rt ON rt.cod_reporte_tipo = pr.cod_reporte_tipo " +
                    "INNER JOIN reporte_frecuencia AS rfe ON rfe.cod_reporte_frecuencia = pr.cod_reporte_frecuencia " +
                    "WHERE pr.cod_reporte_estado = 1 " +
                    "ORDER BY pr.cod_programacion_reporte";

    return jdbcTemplate.query(query, (rs, rowNum) ->
            ReporteMostrarProgramacionResponse.builder()
                    .idProgramacionReporte(rs.getInt("cod_programacion_reporte"))
                    .descripcionFormato(rs.getString("formato"))
                    .descripcionTipo(rs.getString("tipo"))
                    .descripcionFrecuencia(rs.getString("frecuencia"))
                    .fechaInicio(rs.getString("fecha_inicio"))
                    .fechaFin(rs.getString("fecha_fin"))
                    .build()
    );
  }

  @Override
  public ReporteRequest newReporte(ReporteRequest reporteRequest) {
    String query =
            "INSERT INTO reporte " +
                    "(cod_representante, cod_reporte_formato, cod_reporte_tipo, fecha_generacion, hora_generacion) " +
                    "VALUES (?, ?, ?, ?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      var ps = connection.prepareStatement(query, new String[]{"cod_reporte"});
      ps.setInt(1, reporteRequest.getIdRepresentante());
      ps.setInt(2, reporteRequest.getIdReporteFormato());
      ps.setInt(3, reporteRequest.getIdReporteTipo());
      ps.setDate(4, java.sql.Date.valueOf(timeUtils.getCurrentDate()));
      ps.setTime(5, java.sql.Time.valueOf(timeUtils.getCurrentTime()));
      return ps;
    }, keyHolder);

    return reporteRequest;
  }

  private void executeSchedulerReport(ReporteProgramacionResponse reporteProgramacionResponse, String fechaFin)
          throws SchedulerException {
    if (reporteProgramacionResponse.getIdProgramacionReporte() != null) {
      ReporteScheduler scheduler = ReporteScheduler.builder()
              .nombreTrigger("ReporteTrigger" + reporteProgramacionResponse.getIdProgramacionReporte())
              .nombreJob("ReporteJob" + reporteProgramacionResponse.getIdProgramacionReporte())
              .nombreGroup("ReporteGroup" + reporteProgramacionResponse.getIdProgramacionReporte())
              .build();
      scheduler.startScheduler(
              ReporteJob.class,
              fechaFin,
              timeUtils.localTimePlus(20),
              reporteProgramacionResponse.getIdProgramacionReporte()
      );
    }
  }
}