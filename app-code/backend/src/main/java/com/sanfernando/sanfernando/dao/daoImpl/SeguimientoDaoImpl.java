package com.sanfernando.sanfernando.dao.daoImpl;

import com.sanfernando.sanfernando.dao.SeguimientoDao;
import com.sanfernando.sanfernando.dtos.requests.seguimiento.*;
import com.sanfernando.sanfernando.dtos.responses.seguimiento.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SeguimientoDaoImpl implements SeguimientoDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private PlatformTransactionManager transactionManager;

  @Override
  public List<SeguimientoTrasladoListaResponse> getTrasladosProceso() {
    String query =
            "SELECT t.cod_guia_remision, lo.denominacion AS origen, ld.denominacion AS destino " +
                    "FROM traslado t " +
                    "JOIN operacion o ON t.id_operacion_inicia = o.id_operacion " +
                    "JOIN paradero po ON po.cod_ruta = t.cod_ruta AND po.cod_paradero_tipo = 1 " +
                    "JOIN local lo ON po.cod_local = lo.cod_local " +
                    "JOIN paradero pd ON pd.cod_ruta = t.cod_ruta AND pd.cod_paradero_tipo = 3 " +
                    "JOIN local ld ON pd.cod_local = ld.cod_local";

    return jdbcTemplate.query(query, (rs, rowNum) ->
            SeguimientoTrasladoListaResponse.builder()
                    .codGuiaRemision(rs.getString("cod_guia_remision"))
                    .origen(rs.getString("origen"))
                    .destino(rs.getString("destino"))
                    .build()
    );
  }

  @Override
  public SeguimientoTrasladoDetalleResponse getTrasladoProcesoDetalle(String codGuiaRemision) {
    String query =
            "SELECT " +
                    "p.prenombre || ' ' || p.primer_apellido || ' ' || p.segundo_apellido AS conductor, " +
                    "v.placa AS placa_vehiculo, " +
                    "lo.denominacion AS origen, " +
                    "os.hora_fin AS hora_salida, " +
                    "ld.denominacion AS destino " +
                    "FROM traslado t " +
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
                    "WHERE t.cod_guia_remision = ?";

    return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
      SeguimientoTrasladoDetalleResponse response = new SeguimientoTrasladoDetalleResponse();
      response.setNombreCompletoTransportista(rs.getString("conductor"));
      response.setPlaca(rs.getString("placa_vehiculo"));
      response.setOrigen(rs.getString("origen"));
      response.setHoraSalida(rs.getString("hora_salida"));
      response.setDestino(rs.getString("destino"));
      return response;
    }, codGuiaRemision);
  }

  @Override
  public List<SeguimientoTrasladoPedidoListaResponse> getTrasladoProcesoPedidos(String codGuiaRemision) {
    String query =
            "SELECT p.cod_pedido, " +
                    "pt.tipo_pedido, " +
                    "osa.fecha AS fecha_salida, " +
                    "ore.fecha AS fecha_llegada, " +
                    "ld.denominacion AS destino, " +
                    "p.cod_pedido_estado " +
                    "FROM traslado t " +
                    "JOIN detalle_ticket_traslado dtt ON t.id_traslado = dtt.id_traslado " +
                    "JOIN ticket tk ON dtt.cod_ticket = tk.cod_ticket " +
                    "JOIN pedido p ON tk.cod_ticket = p.cod_ticket " +
                    "JOIN pedido_tipo pt ON p.cod_pedido_tipo = pt.cod_pedido_tipo " +
                    "JOIN operacion osa ON t.id_operacion_inicia = osa.id_operacion AND osa.cod_tipo_operacion = (SELECT cod_tipo_operacion FROM operacion_tipo WHERE descripcion = 'Salida') " +
                    "JOIN operacion ore ON t.id_operacion_termina = ore.id_operacion AND ore.cod_tipo_operacion = (SELECT cod_tipo_operacion FROM operacion_tipo WHERE descripcion = 'Recepción') " +
                    "JOIN ruta r ON t.cod_ruta = r.cod_ruta " +
                    "JOIN paradero pd ON r.cod_ruta = pd.cod_ruta AND pd.orden = (SELECT MAX(orden) FROM paradero WHERE cod_ruta = r.cod_ruta) " +
                    "JOIN \"local\" ld ON pd.cod_local = ld.cod_local " +
                    "WHERE t.cod_guia_remision = ?";

    return jdbcTemplate.query(query, (rs, rowNum) ->
                    SeguimientoTrasladoPedidoListaResponse.builder()
                            .idPedido(rs.getString("cod_pedido"))
                            .tipoPedido(rs.getString("tipo_pedido"))
                            .fechaSalida(rs.getString("fecha_salida"))
                            .fechaLLegada(rs.getString("fecha_llegada"))
                            .destino(rs.getString("destino"))
                            .idEstadoPedido(rs.getString("cod_pedido_estado"))
                            .build()
            , codGuiaRemision);
  }

  @Override
  public int actualizarTrasladoProcesoPedido(int idPedido) {
    String query = "UPDATE pedido SET cod_pedido_estado = ? WHERE cod_pedido = ?";
    return jdbcTemplate.update(query, "E", idPedido);
  }

  @Override
  public List<SeguimientoVehiculoListaResponse> obtenerVehiculos() {
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

    return jdbcTemplate.query(query, (rs, rowNum) ->
            SeguimientoVehiculoListaResponse.builder()
                    .idVehiculo(rs.getInt("cod_vehiculo"))
                    .placa(rs.getString("placa"))
                    .modelo(rs.getString("modelo"))
                    .anioFabricacion(rs.getInt("anio_fabricacion"))
                    .capacidadCarga(rs.getInt("capacidad_carga"))
                    .fechaUltimoViaje(rs.getString("fecha_ultimo_viaje"))
                    .fechaUltimoMantenimiento(rs.getString("fecha_ultimo_mantenimiento"))
                    .estado(rs.getString("estado"))
                    .build()
    );
  }

  @Override
  public SeguimientoVehiculoDetallesResponse obtenerVehiculoDetalle(int idVehiculo) {
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

    return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
      SeguimientoVehiculoDetallesResponse response = new SeguimientoVehiculoDetallesResponse();
      response.setMarca(rs.getString("marca"));
      response.setModelo(rs.getString("modelo"));
      response.setEstado(rs.getString("estado"));
      response.setAnioFabricacion(rs.getInt("anio_fabricacion"));
      response.setPlaca(rs.getString("placa"));
      response.setTipo(rs.getString("tipo"));
      response.setCapacidadCarga(rs.getDouble("capacidad_carga"));
      response.setFechaUltimoMantenimiento(rs.getDate("fecha_ultimo_mantenimiento"));
      return response;
    }, idVehiculo);
  }

  @Override
  public int actualizarVehiculo(SeguimientoVehiculoDetalleActualizarRequest request) {
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

    return jdbcTemplate.update(query,
            request.getIdVehiculoMarca(),
            request.getIdVehiculoModelo(),
            request.getIdVehiculoEstado(),
            request.getAnioFabricacion(),
            request.getPlaca(),
            request.getCodVehiculoTipo(),
            request.getCapacidadCarga(),
            new Date(request.getFechaUltimoMantenimiento().getTime()),
            request.getIdVehiculo()
    );
  }

  @Override
  public int crearVehiculo(SeguimientoVehiculoCrearRequest request) {
    String query =
            "INSERT INTO vehiculo " +
                    "(cod_vehiculo_marca, cod_vehiculo_modelo, cod_vehiculo_estado, anio_fabricacion, placa, cod_vehiculo_tipo, capacidad_carga, fecha_ultimo_mantenimiento) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    return jdbcTemplate.update(query,
            request.getIdVehiculoMarca(),
            request.getIdVehiculoModelo(),
            request.getIdVehiculoEstado(),
            request.getAnioFabricacion(),
            request.getPlaca(),
            request.getCodVehiculoTipo(),
            request.getCapacidadCarga(),
            new Date(request.getFechaUltimoMantenimiento().getTime())
    );
  }

  @Override
  public List<SeguimientoTransporstistaListaResponse> obtenerTransportistas() {
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
                    "JOIN transportista_estado te ON t.cod_estado_transportista = te.cod_estado_transportista";

    return jdbcTemplate.query(query, (rs, rowNum) ->
            SeguimientoTransporstistaListaResponse.builder()
                    .idTransportista(rs.getInt("cod_transportista"))
                    .nombreCompleto(rs.getString("nombre"))
                    .licencia(rs.getString("licencia"))
                    .tipoLicencia(rs.getString("tipo_licencia"))
                    .fechaVencimientoLicencia(rs.getDate("vencimiento_licencia"))
                    .estado(rs.getString("estado"))
                    .build()
    );
  }

  @Override
  public SeguimientoTransportistaDetalleResponse obtenerTransportistaDetalle(int idTransportista) {
    String sql = """
        SELECT CONCAT(p.prenombre, ' ', p.primer_apellido, ' ', p.segundo_apellido) AS nombre,
        p.dni AS dni,
        t.num_licencia AS licencia,
        lt.descripcion AS tipo_licencia,
        t.fecha_vencimiento_licencia AS vencimiento_licencia,
        te.descripcion AS estado
        FROM transportista t
        JOIN empleado e ON t.cod_empleado = e.cod_empleado
        JOIN persona p ON e.cod_persona = p.cod_persona
        JOIN licencia_tipo lt ON t.cod_tipo_licencia = lt.cod_tipo_licencia
        JOIN transportista_estado te ON t.cod_estado_transportista = te.cod_estado_transportista
        WHERE t.cod_transportista = ?
    """;

    return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
      SeguimientoTransportistaDetalleResponse response = new SeguimientoTransportistaDetalleResponse();
      response.setNombreCompleto(rs.getString("nombre"));
      response.setDni(rs.getString("dni"));
      response.setLicencia(rs.getString("licencia"));
      response.setTipoLicencia(rs.getString("tipo_licencia"));
      response.setFechaVencimientoLicencia(rs.getDate("vencimiento_licencia"));
      response.setEstado(rs.getString("estado"));
      return response;
    }, idTransportista);
  }

  @Override
  public int actualizarTransportista(SeguimientoTransportistaDetalleActualizarRequest request) {
    String sql = """
        UPDATE transportista
        SET num_licencia = ?,
            cod_tipo_licencia = ?,
            fecha_vencimiento_licencia = ?,
            cod_estado_transportista = ?
        WHERE cod_transportista = ?
    """;

    return jdbcTemplate.update(sql,
            request.getNumLicencia(),
            request.getCodTipoLicencia(),
            new java.sql.Date(request.getFechaVencimientoLicencia().getTime()),
            request.getCodEstadoTransportista(),
            request.getIdTransportista()
    );
  }

  @Override
  public int crearTransportista(SeguimientoTransportistaCrearRequest request) {
    String sql = """
        INSERT INTO transportista
        (cod_empleado, cod_estado_transportista, cod_tipo_licencia, num_licencia, fecha_vencimiento_licencia)
        VALUES (?, ?, ?, ?, ?)
    """;

    return jdbcTemplate.update(sql,
            request.getIdEmpleado(),
            request.getIdEstadoTransportista(),
            request.getIdTipoLicencia(),
            request.getNumLicencia(),
            new java.sql.Date(request.getFechaVencimientoLicencia().getTime())
    );
  }

  @Override
  public List<SeguimientoRutaListaResponse> obtenerRutas() {
    String sql = """
        SELECT r.cod_ruta,
        rt.descripcion AS tipo_ruta,
        r.distancia_total,
        lo.denominacion AS origen,
        ld.denominacion AS destino
        FROM ruta r
        JOIN ruta_tipo rt ON r.cod_ruta_tipo = rt.cod_ruta_tipo
        JOIN paradero po ON r.cod_ruta = po.cod_ruta AND po.orden = 1
        JOIN "local" lo ON po.cod_local = lo.cod_local
        JOIN paradero pd ON r.cod_ruta = pd.cod_ruta 
            AND pd.orden = (SELECT MAX(orden) FROM paradero WHERE cod_ruta = r.cod_ruta)
        JOIN "local" ld ON pd.cod_local = ld.cod_local
    """;

    return jdbcTemplate.query(sql, (rs, rowNum) ->
            SeguimientoRutaListaResponse.builder()
                    .idRuta(rs.getInt("cod_ruta"))
                    .tipoRuta(rs.getString("tipo_ruta"))
                    .distanciaTotal(rs.getDouble("distancia_total"))
                    .origen(rs.getString("origen"))
                    .destino(rs.getString("destino"))
                    .build()
    );
  }

  @Override
  public List<SeguimientoRutaDetalleResponse> obtenerRutaDetalle(int idRuta) {
    String sql = """
        SELECT p.orden,
        l.denominacion AS local,
        pt.descripcion AS tipo_paradero
        FROM paradero p
        JOIN "local" l ON p.cod_local = l.cod_local
        JOIN paradero_tipo pt ON p.cod_paradero_tipo = pt.cod_paradero_tipo
        WHERE p.cod_ruta = ?
        ORDER BY p.orden
    """;

    return jdbcTemplate.query(sql, (rs, rowNum) ->
                    SeguimientoRutaDetalleResponse.builder()
                            .orden(rs.getInt("orden"))
                            .local(rs.getString("local"))
                            .tipoParadero(rs.getString("tipo_paradero"))
                            .build(),
            idRuta
    );
  }

  @Override
  public int borrarRuta(int idRuta) {
    // Using TransactionTemplate to ensure atomic operation
    return new TransactionTemplate(transactionManager).execute(status -> {
      jdbcTemplate.update("DELETE FROM paradero WHERE cod_ruta = ?", idRuta);
      return jdbcTemplate.update("DELETE FROM ruta WHERE cod_ruta = ?", idRuta);
    });
  }

  private int crearRutaParadero(SeguimientoRutaParaderoRequest[] paraderos, int idRuta) {
    String sql = """
        INSERT INTO paradero (cod_ruta, cod_local, cod_paradero_tipo, orden)
        VALUES (?, ?, ?, ?)
    """;

    List<Object[]> batchArgs = Arrays.stream(paraderos)
            .map(p -> new Object[]{
                    idRuta,
                    p.getIdLocal(),
                    p.getIdParaderoTipo(),
                    p.getOrden()
            })
            .collect(Collectors.toList());

    jdbcTemplate.batchUpdate(sql, batchArgs);
    return 1;
  }

  @Override
  public int crearRuta(SeguimientoRutaCrearRequest request) {
    // Using TransactionTemplate to ensure atomic operation
    return new TransactionTemplate(transactionManager).execute(status -> {
      String sql = """
            INSERT INTO ruta (distancia_total, cod_ruta_tipo, duracion)
            VALUES (?, ?, ?)
        """;

      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setDouble(1, request.getDistanciaTotal());
        ps.setInt(2, request.getIdRutaTipo());
        ps.setDouble(3, request.getDuracion());
        return ps;
      }, keyHolder);

      int idRuta = keyHolder.getKey().intValue();
      crearRutaParadero(request.getParaderos(), idRuta);
      return 1;
    });
  }
}