package com.sanfernando.sanfernando.dao.daoImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.sanfernando.sanfernando.dao.SeguimientoDao;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoRutaCrearRequest;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoRutaParaderoRequest;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoTransportistaCrearRequest;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoTransportistaDetalleActualizarRequest;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoVehiculoCrearRequest;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.SeguimientoVehiculoDetalleActualizarRequest;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoRutaDetalleResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoRutaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTransporstistaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTransportistaDetalleResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTrasladoDetalleResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTrasladoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoTrasladoPedidoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoVehiculoDetallesResponse;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.SeguimientoVehiculoListaResponse;
import com.sanfernando.sanfernando.utils.Conexion;

@Repository
public class SeguimientoDaoImpl implements SeguimientoDao{

  private final Conexion con = new Conexion();

  @Override
  public List<SeguimientoTrasladoListaResponse> getTrasladosProceso() {
    con.startConexion();
    List<SeguimientoTrasladoListaResponse> seguimientoTrasladoListaResponses = new ArrayList<>();
    try {
      String query = 
      "SELECT t.cod_guia_remision,  lo.denominacion AS origen, ld.denominacion AS destino " + 
      "FROM traslado t " + 
      "JOIN operacion o ON t.id_operacion_inicia = o.id_operacion " + 
      "JOIN paradero po ON po.cod_ruta = t.cod_ruta AND po.cod_paradero_tipo = 1 " + 
      "JOIN local lo ON po.cod_local = lo.cod_local " + 
      "JOIN paradero pd ON pd.cod_ruta = t.cod_ruta AND pd.cod_paradero_tipo = 3 " + 
      "JOIN local ld ON pd.cod_local = ld.cod_local;";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery(); 
      while (rs.next()) {
        SeguimientoTrasladoListaResponse seguimientoTrasladoListaResponse = SeguimientoTrasladoListaResponse
          .builder()
          .codGuiaRemision(rs.getString("cod_guia_remision"))
          .origen(rs.getString("origen"))
          .destino(rs.getString("destino"))
          .build();
        seguimientoTrasladoListaResponses.add(seguimientoTrasladoListaResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return seguimientoTrasladoListaResponses;
  }

  @Override
  public SeguimientoTrasladoDetalleResponse getTrasladoProcesoDetalle(String codGuiaRemision) {
    con.startConexion();
    SeguimientoTrasladoDetalleResponse seguimientoTrasladoDetalleResponse = new SeguimientoTrasladoDetalleResponse();
    try {
      String query =
        "SELECT " +
        "p.prenombre || ' ' || p.primer_apellido || ' ' || p.segundo_apellido AS conductor, " +
        "v.placa AS placa_vehiculo, " +
        "lo.denominacion AS origen, " +
        "os.hora_fin AS hora_salida, " +
        "ld.denominacion AS destino " +
        "FROM " +
        "traslado t " +
        "JOIN operacion os ON t.id_operacion_inicia = os.id_operacion AND os.cod_tipo_operacion = (SELECT cod_tipo_operacion FROM operacion_tipo WHERE descripcion = 'Salida') " +
        "JOIN transportista tr ON t.cod_transportista = tr.cod_transportista " +
        "JOIN empleado e ON tr.cod_empleado = e.cod_empleado " +
        "JOIN persona p ON e.cod_persona = p.cod_persona " +
        "JOIN vehiculo v ON t.cod_vehiculo = v.cod_vehiculo " +
        "JOIN ruta r ON t.cod_ruta = r.cod_ruta " +
        "JOIN paradero po ON r.cod_ruta = po.cod_ruta AND po.cod_paradero_tipo = 1 " +
        "JOIN \"local\" lo ON po.cod_local = lo.cod_local " +
        "JOIN paradero pd ON r.cod_ruta = pd.cod_ruta AND pd.cod_paradero_tipo = 3 " +
        "JOIN \"local\" ld ON pd.cod_local = ld.cod_local " +
        "WHERE t.cod_guia_remision = ? ";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ps.setString(1, codGuiaRemision);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        seguimientoTrasladoDetalleResponse.setNombreCompletoTransportista(rs.getString("conductor"));
        seguimientoTrasladoDetalleResponse.setPlaca(rs.getString("placa_vehiculo"));
        seguimientoTrasladoDetalleResponse.setOrigen(rs.getString("origen"));
        seguimientoTrasladoDetalleResponse.setHoraSalida(rs.getString("hora_salida"));
        seguimientoTrasladoDetalleResponse.setDestino(rs.getString("destino"));
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return seguimientoTrasladoDetalleResponse;
  }

  @Override
  public List<SeguimientoTrasladoPedidoListaResponse> getTrasladoProcesoPedidos(String codGuiaRemision) {
    con.startConexion();
    List<SeguimientoTrasladoPedidoListaResponse> seguimientoTrasladoPedidoListaResponses = new ArrayList<>();
    try {
      String query =
        "SELECT p.cod_pedido, " +
        "pt.tipo_pedido, " +
        "osa.fecha AS fecha_salida, " +
        "ore.fecha AS fecha_llegada, " +
        "ld.denominacion AS destino, " +
        "p.cod_pedido, " +
        "p.cod_pedido_estado " +
        "FROM " +
        "traslado t " +
        "JOIN detalle_ticket_traslado dtt ON t.id_traslado = dtt.id_traslado " +
        "JOIN ticket tk ON dtt.cod_ticket = tk.cod_ticket " +
        "JOIN pedido p ON tk.cod_ticket = p.cod_ticket " +
        "JOIN pedido_tipo pt ON p.cod_pedido_tipo = pt.cod_pedido_tipo " +
        "JOIN operacion osa ON t.id_operacion_inicia = osa.id_operacion AND osa.cod_tipo_operacion = (SELECT cod_tipo_operacion FROM operacion_tipo WHERE descripcion = 'Salida') " +
        "JOIN operacion ore ON t.id_operacion_termina = ore.id_operacion AND ore.cod_tipo_operacion = (SELECT cod_tipo_operacion FROM operacion_tipo WHERE descripcion = 'Recepción') " +
        "JOIN ruta r ON t.cod_ruta = r.cod_ruta " +
        "JOIN paradero pd ON r.cod_ruta = pd.cod_ruta AND pd.orden = (SELECT MAX(orden) FROM paradero WHERE cod_ruta = r.cod_ruta) " +
        "JOIN \"local\" ld ON pd.cod_local = ld.cod_local " +
        "WHERE t.cod_guia_remision = ? ";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ps.setString(1, codGuiaRemision);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        SeguimientoTrasladoPedidoListaResponse seguimientoTrasladoPedidoListaResponse = SeguimientoTrasladoPedidoListaResponse
          .builder()
          .idPedido(rs.getString("cod_pedido"))
          .tipoPedido(rs.getString("tipo_pedido"))
          .fechaSalida(rs.getString("fecha_salida"))
          .fechaLLegada(rs.getString("fecha_llegada"))
          .destino(rs.getString("destino"))
          .idEstadoPedido(rs.getString("cod_pedido_estado"))
          .build();
        seguimientoTrasladoPedidoListaResponses.add(seguimientoTrasladoPedidoListaResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return seguimientoTrasladoPedidoListaResponses;
  }

  @Override
  public int actualizarTrasladoProcesoPedido(int idPedido) {
    con.startConexion();
    int response = 0;
    try {
      String query = "UPDATE pedido SET cod_pedido_estado  = ? WHERE cod_pedido = ?";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ps.setString(1, "E");
      ps.setInt(2, idPedido);
      ps.executeUpdate();
      ps.close();
      response = 1;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return response;
  }

  
  @Override
  public List<SeguimientoVehiculoListaResponse> obtenerVehiculos() {
    con.startConexion();
    List<SeguimientoVehiculoListaResponse> seguimientoVehiculoListaResponses = new ArrayList<>();
    try {
      String query = 
        "SELECT v.cod_vehiculo, " +
        "v.placa, " +
        "vm.descripcion AS modelo, " +
        "v.anio_fabricacion, " +
        "v.capacidad_carga, " +
        "v.fecha_ultimo_viaje, " +
        "v.fecha_ultimo_mantenimiento, " +
        "ve.descripcion AS estado " +
        "FROM vehiculo v " +
        "JOIN vehiculo_marca vm ON v.cod_vehiculo_marca = vm.cod_vehiculo_marca " +
        "JOIN vehiculo_estado ve ON v.cod_vehiculo_estado = ve.cod_vehiculo_estado";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery(); 
      while (rs.next()) {
        SeguimientoVehiculoListaResponse seguimientoVehiculoListaResponse = SeguimientoVehiculoListaResponse
          .builder()
          .idVehiculo(rs.getInt("cod_vehiculo"))
          .placa(rs.getString("placa"))
          .modelo(rs.getString("modelo"))
          .anioFabricacion(rs.getInt("anio_fabricacion"))
          .capacidadCarga(rs.getInt("capacidad_carga"))
          .fechaUltimoViaje(rs.getString("fecha_ultimo_viaje"))
          .fechaUltimoMantenimiento(rs.getString("fecha_ultimo_mantenimiento"))
          .estado(rs.getString("estado"))
          .build();
        seguimientoVehiculoListaResponses.add(seguimientoVehiculoListaResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return seguimientoVehiculoListaResponses;
  }

  @Override
  public SeguimientoVehiculoDetallesResponse obtenerVehiculoDetalle(int idVehiculo) {
    con.startConexion();
    SeguimientoVehiculoDetallesResponse seguimientoVehiculoDetallesResponse = new SeguimientoVehiculoDetallesResponse();  
    try {
      String query = 
        "SELECT vma.descripcion marca, " +
        "vmo.descripcion modelo, " +
        "ves.descripcion estado, " +
        "v.anio_fabricacion, " +
        "v.placa, " +
        "vti.descripcion tipo, " +
        "v.capacidad_carga, " +
        "v.fecha_ultimo_mantenimiento " +
        "FROM vehiculo v " +
        "JOIN vehiculo_modelo vmo ON v.cod_vehiculo_modelo = vmo.cod_vehiculo_modelo " +
        "JOIN vehiculo_estado ves ON v.cod_vehiculo_estado = ves.cod_vehiculo_estado " +
        "JOIN vehiculo_marca vma ON v.cod_vehiculo_marca = vma.cod_vehiculo_marca " +
        "JOIN vehiculo_tipo vti ON vti.cod_vehiculo_tipo = v.cod_vehiculo_tipo " +
        "WHERE v.cod_vehiculo = ?";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ps.setInt(1, idVehiculo);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        seguimientoVehiculoDetallesResponse.setMarca(rs.getString("marca"));
        seguimientoVehiculoDetallesResponse.setModelo(rs.getString("modelo"));
        seguimientoVehiculoDetallesResponse.setEstado(rs.getString("estado"));
        seguimientoVehiculoDetallesResponse.setAnioFabricacion(rs.getInt("anio_fabricacion"));
        seguimientoVehiculoDetallesResponse.setPlaca(rs.getString("placa"));
        seguimientoVehiculoDetallesResponse.setTipo(rs.getString("tipo"));
        seguimientoVehiculoDetallesResponse.setCapacidadCarga(rs.getDouble("capacidad_carga"));
        seguimientoVehiculoDetallesResponse.setFechaUltimoMantenimiento(rs.getDate("fecha_ultimo_mantenimiento"));
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return seguimientoVehiculoDetallesResponse;
  }

  @Override
  public int actualizarVehiculo(SeguimientoVehiculoDetalleActualizarRequest request) {
    con.startConexion();
    int response = 0;
    try {
      String query = 
        "UPDATE vehiculo SET " +
        "cod_vehiculo_marca = ?, " +
        "cod_vehiculo_modelo = ?, " +
        "cod_vehiculo_estado = ?, " +
        "anio_fabricacion = ?, " +
        "placa = ?, " +
        "cod_vehiculo_tipo = ?, " +
        "capacidad_carga = ?, " +
        "fecha_ultimo_mantenimiento = ? " +
        "WHERE cod_vehiculo = ?";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ps.setInt(1, request.getIdVehiculoMarca());
      ps.setString(2, request.getIdVehiculoModelo());
      ps.setString(3, request.getIdVehiculoEstado());
      ps.setInt(4, request.getAnioFabricacion());
      ps.setString(5, request.getPlaca());
      ps.setString(6, request.getCodVehiculoTipo());
      ps.setDouble(7, request.getCapacidadCarga());
      ps.setDate(8, new java.sql.Date(request.getFechaUltimoMantenimiento().getTime()));
      ps.setInt(9, request.getIdVehiculo());
      ps.executeUpdate();
      ps.close();
      response = 1;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return response;
  }

  @Override
  public int crearVehiculo(SeguimientoVehiculoCrearRequest request) {
    con.startConexion();
    int response = 0;
    try {
      String query =
        "INSERT INTO vehiculo " + 
        "(cod_vehiculo_marca, cod_vehiculo_modelo, cod_vehiculo_estado, anio_fabricacion, placa, cod_vehiculo_tipo, capacidad_carga, fecha_ultimo_mantenimiento) " + 
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?); ";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ps.setInt(1, request.getIdVehiculoMarca());
      ps.setString(2, request.getIdVehiculoModelo());
      ps.setString(3, request.getIdVehiculoEstado());
      ps.setInt(4, request.getAnioFabricacion());
      ps.setString(5, request.getPlaca());
      ps.setString(6, request.getCodVehiculoTipo());
      ps.setDouble(7, request.getCapacidadCarga());
      ps.setDate(8, new java.sql.Date(request.getFechaUltimoMantenimiento().getTime()));
      ps.executeUpdate();
      ps.close();
      response = 1;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return response;
  }

  @Override
  public List<SeguimientoTransporstistaListaResponse> obtenerTransportistas() {
    con.startConexion();
    List<SeguimientoTransporstistaListaResponse> seguimientoTransportistasResponses = new ArrayList<>();
    try {
      String query = 
        "SELECT CONCAT(p.prenombre, ' ', p.primer_apellido, ' ', p.segundo_apellido) AS nombre, " +
        "t.cod_transportista, " +
        "t.num_licencia AS licencia, " +
        "lt.descripcion AS tipo_licencia, " +
        "t.fecha_vencimiento_licencia AS vencimiento_licencia, " +
        "te.descripcion AS estado " +
        "FROM transportista t " +
        "JOIN empleado e ON t.cod_empleado = e.cod_empleado " +
        "JOIN persona p ON e.cod_persona = p.cod_persona " +
        "JOIN licencia_tipo lt ON t.cod_tipo_licencia = lt.cod_tipo_licencia " +
        "JOIN transportista_estado te ON t.cod_estado_transportista = te.cod_estado_transportista; ";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery(); 
      while (rs.next()) {
        SeguimientoTransporstistaListaResponse seguimientoTransportistasResponse = SeguimientoTransporstistaListaResponse
          .builder()
          .idTransportista(rs.getInt("cod_transportista"))
          .nombreCompleto(rs.getString("nombre"))
          .licencia(rs.getString("licencia"))
          .tipoLicencia(rs.getString("tipo_licencia"))
          .fechaVencimientoLicencia(rs.getDate("vencimiento_licencia"))
          .estado(rs.getString("estado"))
          .build();
        seguimientoTransportistasResponses.add(seguimientoTransportistasResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return seguimientoTransportistasResponses;
  }

  @Override
  public SeguimientoTransportistaDetalleResponse obtenerTransportistaDetalle(int idTransportista) {
    con.startConexion();
    SeguimientoTransportistaDetalleResponse seguimientoTransportistaDetalleResponse = new SeguimientoTransportistaDetalleResponse();
    try {
      String sql =  
        "SELECT CONCAT(p.prenombre, ' ', p.primer_apellido, ' ', p.segundo_apellido) AS nombre, " +
        "p.dni AS dni, " +
        "t.num_licencia AS licencia, " +
        "lt.descripcion AS tipo_licencia, " +
        "t.fecha_vencimiento_licencia AS vencimiento_licencia, " +
        "te.descripcion AS estado " +
        "FROM transportista t " +
        "JOIN empleado e ON t.cod_empleado = e.cod_empleado " +
        "JOIN persona p ON e.cod_persona = p.cod_persona " +
        "JOIN licencia_tipo lt ON t.cod_tipo_licencia = lt.cod_tipo_licencia " +
        "JOIN transportista_estado te ON t.cod_estado_transportista = te.cod_estado_transportista " +
        "WHERE t.cod_transportista = ?; ";
      PreparedStatement ps = con.getCon().prepareStatement(sql);
      ps.setInt(1, idTransportista);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        seguimientoTransportistaDetalleResponse.setNombreCompleto(rs.getString("nombre"));
        seguimientoTransportistaDetalleResponse.setDni(rs.getString("dni"));
        seguimientoTransportistaDetalleResponse.setLicencia(rs.getString("licencia"));
        seguimientoTransportistaDetalleResponse.setTipoLicencia(rs.getString("tipo_licencia"));
        seguimientoTransportistaDetalleResponse.setFechaVencimientoLicencia(rs.getDate("vencimiento_licencia"));
        seguimientoTransportistaDetalleResponse.setEstado(rs.getString("estado"));
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return seguimientoTransportistaDetalleResponse;
  } 

  @Override
  public int actualizarTransportista(SeguimientoTransportistaDetalleActualizarRequest request) {
    con.startConexion();
    int response = 0;
    try {
      String query =
        "UPDATE transportista " +
        "SET " +
        "num_licencia = ?, " + 
        "cod_tipo_licencia = ?, " + 
        "fecha_vencimiento_licencia = ?, " + 
        "cod_estado_transportista = ? " + 
        "WHERE cod_transportista = ?; ";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ps.setString(1, request.getNumLicencia());
      ps.setString(2, request.getCodTipoLicencia());
      ps.setDate(3, new java.sql.Date(request.getFechaVencimientoLicencia().getTime()));
      ps.setString(4, request.getCodEstadoTransportista());
      ps.setInt(5, request.getIdTransportista());
      ps.executeUpdate();
      ps.close();
      response = 1;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return response;
  }

  @Override
  public int crearTransportista(SeguimientoTransportistaCrearRequest request) {
    con.startConexion();
    int response = 0;
    try {
      String query =
        "INSERT INTO transportista " + 
        "(cod_empleado, cod_estado_transportista, cod_tipo_licencia, num_licencia, fecha_vencimiento_licencia) " +
        "VALUES (?, ?, ?, ?, ?); ";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ps.setInt(1, request.getIdEmpleado());
      ps.setString(2, request.getIdEstadoTransportista());
      ps.setString(3, request.getIdTipoLicencia());
      ps.setString(4, request.getNumLicencia());
      ps.setDate(5, new java.sql.Date(request.getFechaVencimientoLicencia().getTime()));
      ps.executeUpdate();
      ps.close();
      response = 1;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return response;
  }

  @Override
  public List<SeguimientoRutaListaResponse> obtenerRutas() {
    con.startConexion();
    List<SeguimientoRutaListaResponse> seguimientoRutaListaResponses = new ArrayList<>();
    try {
      String query =
        "SELECT r.cod_ruta, " +
        "rt.descripcion AS tipo_ruta, " +
        "r.distancia_total, " +
        "lo.denominacion AS origen, " +
        "ld.denominacion AS destino " +
        "FROM " +
        "ruta r " +
        "JOIN ruta_tipo rt ON r.cod_ruta_tipo = rt.cod_ruta_tipo " +
        "JOIN paradero po ON r.cod_ruta = po.cod_ruta AND po.orden = 1 " +
        "JOIN \"local\" lo ON po.cod_local = lo.cod_local " +
        "JOIN paradero pd ON r.cod_ruta = pd.cod_ruta AND pd.orden = (SELECT MAX(orden) FROM paradero WHERE cod_ruta = r.cod_ruta) " +
        "JOIN \"local\" ld ON pd.cod_local = ld.cod_local;";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        SeguimientoRutaListaResponse seguimientoRutaListaResponse = SeguimientoRutaListaResponse
          .builder()
          .idRuta(rs.getInt("cod_ruta"))
          .tipoRuta(rs.getString("tipo_ruta"))
          .distanciaTotal(rs.getDouble("distancia_total"))
          .origen(rs.getString("origen"))
          .destino(rs.getString("destino"))
          .build();
        seguimientoRutaListaResponses.add(seguimientoRutaListaResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return seguimientoRutaListaResponses;
  }

  @Override
  public List<SeguimientoRutaDetalleResponse> obtenerRutaDetalle(int idRuta) {
    con.startConexion();
    List<SeguimientoRutaDetalleResponse> seguimientoRutaDetalleResponses = new ArrayList<>();
    try {
      String query =
        "SELECT p.orden, " +
        "l.denominacion AS local, " +
        "pt.descripcion AS tipo_paradero " +
        "FROM paradero p " +
        "JOIN \"local\" l ON p.cod_local = l.cod_local " +
        "JOIN paradero_tipo pt ON p.cod_paradero_tipo = pt.cod_paradero_tipo " +
        "WHERE p.cod_ruta = ? " +
        "ORDER BY p.orden; ";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ps.setInt(1, idRuta);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        SeguimientoRutaDetalleResponse seguimientoRutaDetalleResponse = SeguimientoRutaDetalleResponse
          .builder()
          .orden(rs.getInt("orden"))
          .local(rs.getString("local"))
          .tipoParadero(rs.getString("tipo_paradero"))
          .build();
        seguimientoRutaDetalleResponses.add(seguimientoRutaDetalleResponse);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return seguimientoRutaDetalleResponses;
  }

  @Override
  public int borrarRuta(int idRuta) {
    con.startConexion();
    int response = 0;
    try {

      String query1 = "DELETE FROM paradero WHERE cod_ruta = ?;";
      PreparedStatement ps1 = con.getCon().prepareStatement(query1);
      ps1.setInt(1, idRuta);
      ps1.executeUpdate();
      ps1.close();

      String query2 = "DELETE FROM ruta WHERE cod_ruta = ?; ";
      PreparedStatement ps2 = con.getCon().prepareStatement(query2);
      ps2.setInt(1, idRuta);
      ps2.executeUpdate();
      ps2.close();
      response = 1;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return response;
  }

  public int crearRutaParadero(SeguimientoRutaParaderoRequest[] seguimientoRutaParaderoRequests, int idRuta) {
    con.startConexion();
    int response = 0;
    try {
      String query = 
        "INSERT INTO paradero " +
        "( cod_ruta, cod_local, cod_paradero_tipo, orden ) VALUES " +
        "(?, ?, ?, ?);";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      for (SeguimientoRutaParaderoRequest seguimientoRutaParaderoRequest : seguimientoRutaParaderoRequests) {
        ps.setInt(1, idRuta);
        ps.setInt(2, seguimientoRutaParaderoRequest.getIdLocal());
        ps.setInt(3, seguimientoRutaParaderoRequest.getIdParaderoTipo());
        ps.setInt(4, seguimientoRutaParaderoRequest.getOrden());
        ps.addBatch();
      }
      ps.clearParameters();
      ps.executeBatch();
      ps.close();
      response = 1;
    } catch (SQLException e) {      
      e.printStackTrace();
    }
    con.closeConexion();
    return response;
  }

  @Override
  public int crearRuta(SeguimientoRutaCrearRequest request) {
    con.startConexion();
    int response = 0;
    int idRuta = 0;
    try {
      String query =  
        "INSERT INTO ruta " +
        "(distancia_total, cod_ruta_tipo, duracion ) VALUES " +
        "(?, ?, ?);";
      PreparedStatement ps = con.getCon().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setDouble(1, request.getDistanciaTotal());
      ps.setInt(2, request.getIdRutaTipo());
      ps.setDouble(3, request.getDuracion());
      
      ps.executeUpdate();
      ResultSet rs = ps.getGeneratedKeys();
      while (rs.next()) {
        idRuta = rs.getInt(1);
      }
      rs.close();
      ps.close();
      response = 1;

      this.crearRutaParadero(request.getParaderos(), idRuta);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    con.closeConexion();
    return response;
  }
}
