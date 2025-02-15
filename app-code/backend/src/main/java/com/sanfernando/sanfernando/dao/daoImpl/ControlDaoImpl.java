package com.sanfernando.sanfernando.dao.daoImpl;

import com.sanfernando.sanfernando.dao.ControlDao;
import com.sanfernando.sanfernando.dtos.requests.control.ControlIncidenciaFormRequest;
import com.sanfernando.sanfernando.dtos.responses.control.ConductoresListaResponse;
import com.sanfernando.sanfernando.dtos.responses.control.IncidenciaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.control.VehiculoListaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.List;

@Repository
public class ControlDaoImpl implements ControlDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<ConductoresListaResponse> obtenerConductoresLista() {
    String sql = """
            SELECT 
                t.cod_transportista AS codigoDelConductor,
                t.cod_empleado AS codigoDelEmpleado,
                lt.descripcion AS tipoDeLicencia,
                t.fecha_vencimiento_licencia AS fechaDeVencimientoDeLicencia,
                t.fecha_ultimo_traslado AS fechaUltimoTraslado,
                te.descripcion AS estadoDelConductor
            FROM transportista t
            JOIN licencia_tipo lt ON t.cod_tipo_licencia = lt.cod_tipo_licencia
            JOIN transportista_estado te ON t.cod_estado_transportista = te.cod_estado_transportista
            ORDER BY t.cod_transportista ASC
        """;

    return jdbcTemplate.query(sql, (rs, rowNum) ->
            ConductoresListaResponse.builder()
                    .codigoDelConductor(rs.getString("codigoDelConductor"))
                    .codigoDelEmpleado(rs.getString("codigoDelEmpleado"))
                    .tipoDeLicencia(rs.getString("tipoDeLicencia"))
                    .fechaDeVencimientoDeLicencia(rs.getString("fechaDeVencimientoDeLicencia"))
                    .fechaUltimoTraslado(rs.getString("fechaUltimoTraslado"))
                    .estadoDelConductor(rs.getString("estadoDelConductor"))
                    .build()
    );
  }

  @Override
  public boolean actualizarEstadoConductor(String codigoConductor, String nuevoEstado) {
    String checkSql = """
            SELECT cod_estado_transportista 
            FROM transportista_estado 
            WHERE LOWER(TRIM(descripcion)) = LOWER(TRIM(?))
        """;

    // Check if state exists
    List<Integer> estados = jdbcTemplate.query(
            checkSql,
            new Object[]{nuevoEstado},
            new int[]{Types.VARCHAR},
            (rs, rowNum) -> rs.getInt(1)
    );

    if (estados.isEmpty()) {
      return false;
    }

    String updateSql = """
            UPDATE transportista 
            SET cod_estado_transportista = 
                (SELECT cod_estado_transportista 
                 FROM transportista_estado 
                 WHERE LOWER(TRIM(descripcion)) = LOWER(TRIM(?)))
            WHERE cod_transportista = ?
        """;

    int rowsAffected = jdbcTemplate.update(
            updateSql,
            nuevoEstado,
            Integer.parseInt(codigoConductor)
    );

    return rowsAffected > 0;
  }

  @Override
  public List<IncidenciaListaResponse> obtenerIncidenciasLista() {
    String sql = """
            SELECT 
                i.cod_incidencia AS codigoDeIncidencia,
                i.id_traslado AS codigoDeTraslado,
                it.descripcion AS descripcionTipoDeIncidencia,
                i.fecha_ocurrencia AS fechaDeOcurrencia,
                i.hora_ocurrencia AS horaDeOcurrencia,
                isa.descripcion AS estadoDeIncidencia
            FROM incidencia i
            JOIN incidencia_tipo it ON i.cod_tipo_incidencia = it.cod_tipo_incidencia
            JOIN incidencia_estado isa ON i.cod_estado_incidencia = isa.cod_estado_incidencia
        """;

    return jdbcTemplate.query(sql, (rs, rowNum) ->
            IncidenciaListaResponse.builder()
                    .codigoDeIncidencia(rs.getString("codigoDeIncidencia"))
                    .codigoDeTraslado(rs.getString("codigoDeTraslado"))
                    .descripcionTipoDeIncidencia(rs.getString("descripcionTipoDeIncidencia"))
                    .fechaDeOcurrencia(rs.getString("fechaDeOcurrencia"))
                    .horaDeOcurrencia(rs.getString("horaDeOcurrencia"))
                    .estadoDeIncidencia(rs.getString("estadoDeIncidencia"))
                    .build()
    );
  }

  @Override
  @Transactional
  public void crearIncidencia(ControlIncidenciaFormRequest incidenciaForm) {
    String insertSql = """
            INSERT INTO incidencia 
            (id_traslado, cod_tipo_incidencia, descripcion, fecha_ocurrencia, 
             hora_ocurrencia, cod_estado_incidencia)
            VALUES (?, ?, ?, ?, ?, 'P')
            RETURNING cod_incidencia
        """;

    Integer codIncidencia = jdbcTemplate.queryForObject(
            insertSql,
            Integer.class,
            incidenciaForm.getCodTraslado(),
            incidenciaForm.getTipoIncidencia(),
            incidenciaForm.getDescripcion(),
            java.sql.Date.valueOf(incidenciaForm.getFecha()),
            java.sql.Time.valueOf(incidenciaForm.getHora())
    );

    if (codIncidencia != null) {
      crearProcedimiento(
              codIncidencia,
              incidenciaForm.getTipoProcedimiento(),
              incidenciaForm.getTiempoEstimadoProcedimiento()
      );
      crearNorma(codIncidencia, incidenciaForm.getTipoNorma());
    } else {
      throw new RuntimeException("La creación de la incidencia falló, no se obtuvo el ID.");
    }
  }

  @Override
  public void crearProcedimiento(int codIncidencia, String codTipoProcedimiento, int tiempoEstimado) {
    String sql = """
            INSERT INTO procedimiento 
            (cod_incidencia, cod_tipo_procedimiento, nombre, tiempo_estimado)
            VALUES (?, ?, 
                (SELECT descripcion FROM procedimiento_tipo WHERE cod_tipo_procedimiento = ?), 
                ?)
        """;

    jdbcTemplate.update(
            sql,
            codIncidencia,
            codTipoProcedimiento,
            codTipoProcedimiento,
            tiempoEstimado
    );
  }

  @Override
  public void crearNorma(int codIncidencia, String codNormaTipo) {
    String sql = """
            INSERT INTO norma 
            (cod_incidencia, cod_norma_tipo, fecha_emision, fecha_vigencia)
            VALUES (?, ?, CURRENT_DATE, CURRENT_DATE + INTERVAL '1 year')
        """;

    jdbcTemplate.update(sql, codIncidencia, codNormaTipo);
  }

  @Override
  public List<VehiculoListaResponse> obtenerVehiculosLista() {
    String sql = """
            SELECT 
                v.cod_vehiculo AS codigoDelVehiculo,
                v.anio_fabricacion AS anioDeFabricacion,
                v.fecha_ultimo_mantenimiento AS fechaDeUltimoMantenimiento,
                v.capacidad_carga AS capacidadDeCarga,
                vm.descripcion AS modelo,
                v.placa AS placa,
                v.fecha_ultimo_viaje AS fechaUltimoViaje,
                ve.descripcion AS estadoDelVehiculo
            FROM vehiculo v
            JOIN vehiculo_modelo vm ON v.cod_vehiculo_modelo = vm.cod_vehiculo_modelo
            JOIN vehiculo_estado ve ON v.cod_vehiculo_estado = ve.cod_vehiculo_estado
            ORDER BY v.cod_vehiculo ASC
        """;

    return jdbcTemplate.query(sql, (rs, rowNum) ->
            VehiculoListaResponse.builder()
                    .codigoDelVehiculo(rs.getString("codigoDelVehiculo"))
                    .anioDeFabricacion(rs.getString("anioDeFabricacion"))
                    .fechaDeUltimoMantenimiento(rs.getString("fechaDeUltimoMantenimiento"))
                    .capacidadDeCarga(rs.getString("capacidadDeCarga"))
                    .modelo(rs.getString("modelo"))
                    .placa(rs.getString("placa"))
                    .fechaUltimoViaje(rs.getString("fechaUltimoViaje"))
                    .estadoDelVehiculo(rs.getString("estadoDelVehiculo"))
                    .build()
    );
  }

  @Override
  public boolean actualizarEstado(String codigoVehiculo, String nuevoEstado) {
    String checkSql = """
            SELECT cod_vehiculo_estado 
            FROM vehiculo_estado 
            WHERE LOWER(TRIM(descripcion)) = LOWER(TRIM(?))
        """;

    List<Integer> estados = jdbcTemplate.query(
            checkSql,
            new Object[]{nuevoEstado},
            new int[]{Types.VARCHAR},
            (rs, rowNum) -> rs.getInt(1)
    );

    if (estados.isEmpty()) {
      return false;
    }

    String updateSql = """
            UPDATE vehiculo
            SET cod_vehiculo_estado = 
                (SELECT cod_vehiculo_estado 
                 FROM vehiculo_estado 
                 WHERE LOWER(TRIM(descripcion)) = LOWER(TRIM(?)))
            WHERE cod_vehiculo = ?
        """;

    int rowsAffected = jdbcTemplate.update(
            updateSql,
            nuevoEstado,
            Integer.parseInt(codigoVehiculo)
    );

    return rowsAffected > 0;
  }

  @Override
  public int actualizarEstadoIncidencia(int idIncidencia, String idEstadoIncidencia) {
    String sql = "UPDATE incidencia SET cod_estado_incidencia = ? WHERE cod_incidencia = ?";

    return jdbcTemplate.update(sql, idEstadoIncidencia, idIncidencia);
  }
}