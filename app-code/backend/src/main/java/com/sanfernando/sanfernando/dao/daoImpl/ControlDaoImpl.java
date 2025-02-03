package com.sanfernando.sanfernando.dao.daoImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sanfernando.sanfernando.dao.ControlDao;
import com.sanfernando.sanfernando.dtos.requests.control.ControlIncidenciaFormRequest;
import com.sanfernando.sanfernando.dtos.responses.control.ConductoresListaResponse;
import com.sanfernando.sanfernando.dtos.responses.control.IncidenciaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.control.VehiculoListaResponse;
import com.sanfernando.sanfernando.utils.Conexion;

@Repository
public class ControlDaoImpl implements ControlDao {

  @Autowired
  private Conexion conexion;

  @Override
  public List<ConductoresListaResponse> obtenerConductoresLista() {
    List<ConductoresListaResponse> conductores = new ArrayList<>();
    String query = "SELECT " +
      "t.cod_transportista AS codigoDelConductor, " +
      "t.cod_empleado AS codigoDelEmpleado, " +
      "lt.descripcion AS tipoDeLicencia, " +
      "t.fecha_vencimiento_licencia AS fechaDeVencimientoDeLicencia, " +
      "t.fecha_ultimo_traslado AS fechaUltimoTraslado, " +
      "te.descripcion AS estadoDelConductor " +
      "FROM transportista t " +
      "JOIN licencia_tipo lt ON t.cod_tipo_licencia = lt.cod_tipo_licencia " +
      "JOIN transportista_estado te ON t.cod_estado_transportista = te.cod_estado_transportista "+
      "ORDER BY t.cod_transportista ASC";

    try {
      conexion.startConexion();
      PreparedStatement ps = conexion.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ConductoresListaResponse conductor = ConductoresListaResponse.builder()
          .codigoDelConductor(rs.getString("codigoDelConductor"))
          .codigoDelEmpleado(rs.getString("codigoDelEmpleado"))
          .tipoDeLicencia(rs.getString("tipoDeLicencia"))
          .fechaDeVencimientoDeLicencia(rs.getString("fechaDeVencimientoDeLicencia"))
          .fechaUltimoTraslado(rs.getString("fechaUltimoTraslado"))
          .estadoDelConductor(rs.getString("estadoDelConductor"))
          .build();
        conductores.add(conductor);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      conexion.closeConexion();
    }
    return conductores;
  }

  @Override
  public boolean actualizarEstadoConductor(String codigoConductor, String nuevoEstado) {
    String query = "UPDATE transportista " +
      "SET cod_estado_transportista = ? " +
      "(SELECT cod_estado_transportista FROM transportista_estado " +
      "WHERE LOWER(TRIM(descripcion)) = LOWER(TRIM(?))) " +
      "WHERE cod_transportista = ?; ";
    try {
      conexion.startConexion();
      PreparedStatement ps = conexion.getCon().prepareStatement(query);
      ps.setString(1, nuevoEstado);
      ps.setInt(2, Integer.parseInt(codigoConductor));

      System.out.println("Ejecutando query: " + ps.toString());
      System.out.println("Nuevo estado: " + nuevoEstado);

      // Verificar si existe el estado en la tabla transportista_estado
      String checkQuery = "SELECT cod_estado_transportista FROM transportista_estado WHERE LOWER(TRIM(descripcion)) = LOWER(TRIM(?))";
      PreparedStatement checkPs = conexion.getCon().prepareStatement(checkQuery);
      checkPs.setString(1, nuevoEstado);
      ResultSet rs = checkPs.executeQuery();
      if (!rs.next()) {
        System.out.println("No se encontró el estado '" + nuevoEstado + "' en la tabla transportista_estado");
        return false;
      }

      int filasAfectadas = ps.executeUpdate();
      System.out.println("Filas afectadas: " + filasAfectadas);

      if (filasAfectadas == 0) {
        System.out.println("No se actualizó ningún registro. Verificar si existe el conductor con código: " + codigoConductor);
      }

      return filasAfectadas > 0;
    } catch (SQLException | NumberFormatException e) {
      System.out.println("Error al actualizar el estado del conductor: " + e.getMessage());
      e.printStackTrace();
      return false;
    } finally {
      conexion.closeConexion();
    }
  }

  @Override
  public List<IncidenciaListaResponse> obtenerIncidenciasLista() {
    List<IncidenciaListaResponse> incidencias = new ArrayList<>();
    String query = "SELECT " +
      "i.cod_incidencia AS codigoDeIncidencia, " +
      "i.id_traslado AS codigoDeTraslado, " +
      "it.descripcion AS descripcionTipoDeIncidencia, " +
      "i.fecha_ocurrencia AS fechaDeOcurrencia, " +
      "i.hora_ocurrencia AS horaDeOcurrencia, " +
      "isa.descripcion AS estadoDeIncidencia " +
      "FROM incidencia i " +
      "JOIN incidencia_tipo it ON i.cod_tipo_incidencia = it.cod_tipo_incidencia "+
      "JOIN incidencia_estado isa ON i.cod_estado_incidencia = isa.cod_estado_incidencia";
    try {
      conexion.startConexion();
      PreparedStatement ps = conexion.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        IncidenciaListaResponse incidencia = IncidenciaListaResponse.builder()
          .codigoDeIncidencia(rs.getString("codigoDeIncidencia"))
          .codigoDeTraslado(rs.getString("codigoDeTraslado"))
          .descripcionTipoDeIncidencia(rs.getString("descripcionTipoDeIncidencia"))
          .fechaDeOcurrencia(rs.getString("fechaDeOcurrencia"))
          .horaDeOcurrencia(rs.getString("horaDeOcurrencia"))
          .estadoDeIncidencia(rs.getString("estadoDeIncidencia"))
          .build();
        incidencias.add(incidencia);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      conexion.closeConexion();
    }
    return incidencias;
  }

  @Override
  public void crearIncidencia(ControlIncidenciaFormRequest incidenciaForm) {
    String queryIncidencia = "INSERT INTO incidencia (id_traslado, cod_tipo_incidencia, descripcion, fecha_ocurrencia, hora_ocurrencia, cod_estado_incidencia) " +
      "VALUES (?, ?, ?, ?, ?, 'P') RETURNING cod_incidencia";

    try {
      conexion.startConexion();
      conexion.getCon().setAutoCommit(false);

      // Crear incidencia
      PreparedStatement psIncidencia = conexion.getCon().prepareStatement(queryIncidencia);
      psIncidencia.setInt(1, incidenciaForm.getCodTraslado());
      psIncidencia.setString(2, incidenciaForm.getTipoIncidencia());
      psIncidencia.setString(3, incidenciaForm.getDescripcion());
      psIncidencia.setDate(4, java.sql.Date.valueOf(incidenciaForm.getFecha()));
      psIncidencia.setTime(5, java.sql.Time.valueOf(incidenciaForm.getHora()));

      ResultSet rs = psIncidencia.executeQuery();

      if (rs.next()) {
        int codIncidencia = rs.getInt(1);
        crearProcedimiento(codIncidencia, incidenciaForm.getTipoProcedimiento(), incidenciaForm.getTiempoEstimadoProcedimiento());
        crearNorma(codIncidencia, incidenciaForm.getTipoNorma());
      } else {
        throw new SQLException("La creación de la incidencia falló, no se obtuvo el ID.");
      }

      conexion.getCon().commit();
    } catch (SQLException e) {
      try {
        conexion.getCon().rollback();
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
      e.printStackTrace();
      throw new RuntimeException("Error al crear la incidencia: " + e.getMessage());
    } finally {
      try {
        conexion.getCon().setAutoCommit(true);
      } catch (SQLException e) {
        e.printStackTrace();
      }
      conexion.closeConexion();
    }
  }

  @Override
  public void crearProcedimiento(int codIncidencia, String codTipoProcedimiento, int tiempoEstimado) {
    String query = "INSERT INTO procedimiento (cod_incidencia, cod_tipo_procedimiento, nombre, tiempo_estimado) " +
      "VALUES (?, ?, (SELECT descripcion FROM procedimiento_tipo WHERE cod_tipo_procedimiento = ?), ?)";

    try {
      PreparedStatement ps = conexion.getCon().prepareStatement(query);
      ps.setInt(1, codIncidencia);
      ps.setString(2, codTipoProcedimiento);
      ps.setString(3, codTipoProcedimiento);
      ps.setInt(4, tiempoEstimado);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Error al crear el procedimiento: " + e.getMessage());
    }
  }

  @Override
  public void crearNorma(int codIncidencia, String codNormaTipo) {
    String query = "INSERT INTO norma (cod_incidencia, cod_norma_tipo, fecha_emision, fecha_vigencia) " +
      "VALUES (?, ?, CURRENT_DATE, CURRENT_DATE + INTERVAL '1 year')";
    try {
      PreparedStatement ps = conexion.getCon().prepareStatement(query);
      ps.setInt(1, codIncidencia);
      ps.setString(2, codNormaTipo);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Error al crear la norma: " + e.getMessage());
    }
  }

  @Override
  public List<VehiculoListaResponse> obtenerVehiculosLista() {
    List<VehiculoListaResponse> vehiculos = new ArrayList<>();
    String query = "SELECT " +
      "v.cod_vehiculo AS codigoDelVehiculo, " +
      "v.anio_fabricacion AS anioDeFabricacion, " +
      "v.fecha_ultimo_mantenimiento AS fechaDeUltimoMantenimiento, " +
      "v.capacidad_carga AS capacidadDeCarga, " +
      "vm.descripcion AS modelo, " +
      "v.placa AS placa, " +
      "v.fecha_ultimo_viaje AS fechaUltimoViaje, " +
      "ve.descripcion AS estadoDelVehiculo " +
      "FROM vehiculo v " +
      "JOIN vehiculo_modelo vm ON v.cod_vehiculo_modelo = vm.cod_vehiculo_modelo " +
      "JOIN vehiculo_estado ve ON v.cod_vehiculo_estado = ve.cod_vehiculo_estado " +
      "ORDER BY v.cod_vehiculo ASC";
    try {
      conexion.startConexion();
      PreparedStatement ps = conexion.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        VehiculoListaResponse vehiculo = VehiculoListaResponse.builder()
          .codigoDelVehiculo(rs.getString("codigoDelVehiculo"))
          .anioDeFabricacion(rs.getString("anioDeFabricacion"))
          .fechaDeUltimoMantenimiento(rs.getString("fechaDeUltimoMantenimiento"))
          .capacidadDeCarga(rs.getString("capacidadDeCarga"))
          .modelo(rs.getString("modelo"))
          .placa(rs.getString("placa"))
          .fechaUltimoViaje(rs.getString("fechaUltimoViaje"))
          .estadoDelVehiculo(rs.getString("estadoDelVehiculo"))
          .build();
        vehiculos.add(vehiculo);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      conexion.closeConexion();
    }
    return vehiculos;
  }

  @Override
  public boolean actualizarEstado(String codigoVehiculo, String nuevoEstado) {
    String query = "UPDATE vehiculo " +
      "SET cod_vehiculo_estado = " +
      "(SELECT cod_vehiculo_estado FROM vehiculo_estado " +
      "WHERE LOWER(TRIM(descripcion)) = LOWER(TRIM(?))) " +
      "WHERE cod_vehiculo = ?; ";
    try {
      conexion.startConexion();
      PreparedStatement ps = conexion.getCon().prepareStatement(query);
        
      ps.setString(1, nuevoEstado);
      ps.setInt(2, Integer.parseInt(codigoVehiculo));

      System.out.println("Ejecutando query: " + ps.toString());
      System.out.println("Nuevo estado: " + nuevoEstado);

      // Verificar si existe el estado en la tabla transportista_estado
      String checkQuery = "SELECT cod_vehiculo_estado FROM vehiculo_estado WHERE LOWER(TRIM(descripcion)) = LOWER(TRIM(?))";
      PreparedStatement checkPs = conexion.getCon().prepareStatement(checkQuery);
      checkPs.setString(1, nuevoEstado);

      ResultSet rs = checkPs.executeQuery();
      if (!rs.next()) {
        System.out.println("No se encontró el estado '" + nuevoEstado + "' en la tabla transportista_estado");
        return false;
      }

      int filasAfectadas = ps.executeUpdate();
      System.out.println("Filas afectadas: " + filasAfectadas);

      if (filasAfectadas == 0) {
        System.out.println("No se actualizó ningún registro. Verificar si existe el conductor con código: " + codigoVehiculo);
      }

      return filasAfectadas > 0;
    } catch (SQLException | NumberFormatException e) {
      System.out.println("Error al actualizar el estado del conductor: " + e.getMessage());
      e.printStackTrace();
      return false;
    } finally {
      conexion.closeConexion();
    }
  }

  @Override
  public int actualizarEstadoIncidencia(int idIncidencia, String idEstadoIncidencia) {
    String query = "UPDATE incidencia SET cod_estado_incidencia = ? WHERE cod_incidencia = ?; ";
    int response = 0;
    try {
      conexion.startConexion();
      PreparedStatement ps = conexion.getCon().prepareStatement(query);
      ps.setString(1, idEstadoIncidencia);
      ps.setInt(2, idIncidencia);

      System.out.println("Ejecutando query: " + ps.toString());
      System.out.println("Nuevo estado: " + idEstadoIncidencia);

      int filasAfectadas = ps.executeUpdate();
      System.out.println("Filas afectadas: " + filasAfectadas);

      if (filasAfectadas == 0) {
        System.out.println("No se actualizó ningún registro. Verificar si existe el conductor con código: " + idIncidencia);
      }

      response = filasAfectadas;
    } catch (SQLException | NumberFormatException e) {
      System.out.println("Error al actualizar el estado del conductor: " + e.getMessage());
      e.printStackTrace();
      return -1;
    } 
    conexion.closeConexion();
    return response;
  }
}
