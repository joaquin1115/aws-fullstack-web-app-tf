package com.sanfernando.sanfernando.dao.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.sanfernando.sanfernando.dao.ReclamoDao;
import com.sanfernando.sanfernando.dtos.requests.reclamos.ReclamoFormCreateRequest;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoEmpresaListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoRepresentanteListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketProductoDetalleResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoTicketProductoListaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoVisorClienteResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoVisorDetalleResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoVisorEvidenciaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoVisorNaturalezaResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoVisorResolucionResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoVisorResponse;
import com.sanfernando.sanfernando.dtos.responses.reclamos.ReclamoVisorSeguimientoResponse;
import com.sanfernando.sanfernando.utils.Conexion;

@Repository
public class ReclamoDaoImpl implements ReclamoDao {

  private final Conexion con = new Conexion();

  @Override
  public List<ReclamoListaResponse> obtenerReclamosLista() {
    List<ReclamoListaResponse> listaReclamos = new ArrayList<>();
    String query = 
      "SELECT " +
      "r.cod_reclamo, " +
      "er.descripcion, " +
      "r.fecha_reclamo, " +
      "c.nombre || ' (' || c.ruc || ')' AS \"cliente (ruc)\", " +
      "ct.tipo_cliente, " +
      "rt.cod_tipo_reclamo, " +
      "c1.nombre AS \"área responsable\" " +
      "FROM " +
      "reclamo r " +
      "INNER JOIN " +
      "estado_reclamo er ON r.cod_estado_reclamo = er.cod_estado_reclamo " +
      "INNER JOIN " +
      "representante re ON r.cod_representante = re.cod_representante " +
      "INNER JOIN " +
      "cliente c ON re.cod_cliente = c.cod_cliente " +
      "INNER JOIN " +
      "cliente_tipo ct ON c.cod_cliente_tipo = ct.cod_cliente_tipo " +
      "INNER JOIN " +
      "reclamo_tipo rt ON r.cod_tipo_reclamo = rt.cod_tipo_reclamo " +
      "INNER JOIN " +
      "seguimiento s ON r.cod_seguimiento = s.cod_seguimiento " +
      "INNER JOIN " +
      "cliente c1 ON s.cod_cliente_interno = c1.cod_cliente " +
      "ORDER BY cod_reclamo ";
    try {
      con.startConexion();
      PreparedStatement pstmt = con.getCon().prepareStatement(query);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
          ReclamoListaResponse reclamo = ReclamoListaResponse
            .builder()
            .codReclamo(rs.getString("cod_reclamo"))
            .estadoReclamo(rs.getString("descripcion"))
            .fechaReclamo(rs.getString("fecha_reclamo"))
            .clienteRuc(rs.getString("cliente (ruc)"))
            .tipoCliente(rs.getString("tipo_cliente"))
            .tipoReclamo(rs.getString("cod_tipo_reclamo"))
            .areaRes(rs.getString("área responsable"))
            .build();
          listaReclamos.add(reclamo);
        }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      con.closeConexion();
    }
    return listaReclamos;

  }

  @Override
  public void insertarReclamo(ReclamoFormCreateRequest reclamoDTO) throws Exception {
    // 1. Obtener cod_cliente, cod_representante y datos adicionales
    String query = 
      "SELECT c.cod_cliente, r.cod_representante, p.direccion, r.correo_empresarial " +
      "FROM cliente c " +
      "JOIN representante r ON c.cod_cliente = r.cod_cliente " +
      "JOIN persona p ON r.cod_persona = p.cod_persona " +
      "WHERE c.nombre = ? AND p.prenombre = ? AND r.cargo = ?";
    // 2. Obtener información del producto y ticket
    String sqlProducto = 
      "SELECT ec.id_elemento_catalogo, t.fecha_entrega, dtp.cantidad " +
      "FROM elemento_catalogo ec " +
      "JOIN detalle_ticket_producto dtp ON ec.id_elemento_catalogo = dtp.id_elemento_catalogo " +
      "JOIN ticket t ON dtp.cod_ticket = t.cod_ticket " +
      "WHERE ec.nombre = ? AND t.cod_ticket = ?";
    // 3. Insertar en la tabla seguimiento
    String sqlSeguimiento = 
      "INSERT INTO seguimiento (cod_cliente_interno, cod_tipo_accion, comentario, fecha_resolucion, numero_caso) " +
      "VALUES (?, (SELECT cod_tipo_accion FROM accion_tipo WHERE descripcion = ?), ?, ?, ?) " +
      "RETURNING cod_seguimiento";
    // 4. Insertar en la tabla reclamo
    String sqlReclamo = 
      "INSERT INTO reclamo (cod_representante, cod_pedido, cod_seguimiento, cod_tipo_reclamo, " +
      "cod_nivel_urgencia, cod_estado_reclamo, comentario, fecha_suceso, fecha_reclamo) " +
      "VALUES (?, (SELECT cod_pedido FROM pedido WHERE cod_ticket = ?), ?, " +
      "(SELECT cod_tipo_reclamo FROM reclamo_tipo WHERE descripcion = ?), " +
      "(SELECT cod_nivel_urgencia FROM nivel_urgencia WHERE descripcion = ?), " +
      "(SELECT cod_estado_reclamo FROM estado_reclamo WHERE descripcion = ?), ?, ?, CURRENT_DATE) " +
      "RETURNING cod_reclamo";

    // 5. Insertar en la tabla evidencia
    String sqlEvidencia = 
      "INSERT INTO evidencia (cod_reclamo, cod_tipo_evidencia, cod_tipo_archivo, nombre_evidencia) " +
      "VALUES (?, (SELECT cod_tipo_evidencia FROM evidencia_tipo WHERE descripcion = ?), " +
      "(SELECT cod_tipo_archivo FROM archivo_tipo WHERE descripcion = ?), ?)";

    try {

        con.startConexion();
        Connection conn = con.getCon();
        PreparedStatement pstmt=conn.prepareStatement(query);

        pstmt.setString(1, reclamoDTO.getNombCliente());
        pstmt.setString(2, reclamoDTO.getNomRepresentante());
        pstmt.setString(3, reclamoDTO.getCargoRepresentante());
        ResultSet rsCliente = pstmt.executeQuery();
        int codCliente = 0;
        int codRepresentante = 0;
        if (rsCliente.next()) {
            codCliente = rsCliente.getInt("cod_cliente");
            codRepresentante = rsCliente.getInt("cod_representante");
        } else {
            throw new Exception("No se encontró el cliente o representante especificado");
        }
        pstmt = conn.prepareStatement(sqlProducto);
        pstmt.setString(1, reclamoDTO.getNombProducto());
        pstmt.setInt(2, reclamoDTO.getCodticket());
        ResultSet rsProducto = pstmt.executeQuery();
        int idElementoCatalogo = 0;
        if (rsProducto.next()) {
            idElementoCatalogo = rsProducto.getInt("id_elemento_catalogo");
        } else {
            throw new Exception("No se encontró el producto o ticket especificado");
        }

        pstmt = conn.prepareStatement(sqlSeguimiento);
        pstmt.setInt(1, codCliente);
        pstmt.setString(2, reclamoDTO.getAccionSolicitada());
        pstmt.setString(3, reclamoDTO.getComentario());
        pstmt.setDate(4, java.sql.Date.valueOf(reclamoDTO.getFechaEsperada()));
        pstmt.setInt(5, Integer.parseInt(reclamoDTO.getNroCaso()));
        ResultSet rsSeguimiento = pstmt.executeQuery();
        int codSeguimiento = 0;
        if (rsSeguimiento.next()) {
            codSeguimiento = rsSeguimiento.getInt(1);
        }


        pstmt = conn.prepareStatement(sqlReclamo);
        pstmt.setInt(1, codRepresentante);
        pstmt.setInt(2, reclamoDTO.getCodticket());
        pstmt.setInt(3, codSeguimiento);
        pstmt.setString(4, reclamoDTO.getTipoReclamo());
        pstmt.setString(5, reclamoDTO.getUrgencia());
        pstmt.setString(6, reclamoDTO.getEstadoReclamo());
        pstmt.setString(7, reclamoDTO.getDescripcionProblema());
        pstmt.setDate(8, java.sql.Date.valueOf(reclamoDTO.getFechaIncidencia()));
        ResultSet rsReclamo = pstmt.executeQuery();
        int codReclamo = 0;
        if (rsReclamo.next()) {
            codReclamo = rsReclamo.getInt(1);
        }

        pstmt = conn.prepareStatement(sqlEvidencia);
        pstmt.setInt(1, codReclamo);
        pstmt.setString(2, reclamoDTO.getTipoEvidencia());
        pstmt.setString(3, reclamoDTO.getTipoArchivo());
        pstmt.setString(4, reclamoDTO.getNombreEvidencia());
        pstmt.executeUpdate();

        conn.commit();
    }catch (SQLException e) {
      e.printStackTrace();
    } finally {
      con.closeConexion();
    }
  }

  @Override
  public List<ReclamoRepresentanteListaResponse> obtenerRepresentantesLista(int empresaId) {
    List<ReclamoRepresentanteListaResponse> listaRepresentantes = new ArrayList<>();
    String query = 
      "SELECT " +
      "re.cod_representante, " +
      "CONCAT(pe.prenombre, ' ', pe.primer_apellido, ' ', pe.segundo_apellido) AS representante, " +
      "re.cargo, " +
      "re.correo_empresarial, " +
      "pe.direccion " +
      "FROM representante AS re " +
      "INNER JOIN cliente AS cl ON cl.cod_cliente = re.cod_cliente " +
      "INNER JOIN persona AS pe ON pe.cod_persona = re.cod_persona " +
      "WHERE cl.cod_cliente = ? ";
    try {
      con.startConexion();
      PreparedStatement pstmt = con.getCon().prepareStatement(query);
      pstmt.setInt(1, empresaId);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        ReclamoRepresentanteListaResponse representante = ReclamoRepresentanteListaResponse
          .builder()
          .codRepresentante(rs.getInt("cod_representante"))
          .nombreRepresentante(rs.getString("representante"))
          .cargoRepresentante(rs.getString("cargo"))
          .correoEmpresarial(rs.getString("correo_empresarial"))
          .direccion(rs.getString("direccion"))
          .build();
        listaRepresentantes.add(representante);
      }
    } catch (SQLException | NullPointerException e) {
      e.printStackTrace();
    } finally {
      con.closeConexion();
    }
    return listaRepresentantes;
  }
  
  @Override
  public List<ReclamoEmpresaListaResponse> obtenerEmpresas() {
    con.startConexion();
    List<ReclamoEmpresaListaResponse> listaEmpresas = new ArrayList<>();
    try {
      String query =
        "SELECT cl.cod_cliente, cl.nombre FROM cliente AS cl; ";
      // "WHERE cl.cod_cliente_tipo = ? ";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      // ps.setInt(1, 1);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReclamoEmpresaListaResponse empresa = ReclamoEmpresaListaResponse
          .builder()
          .idCliente(rs.getInt("cod_cliente"))
          .nombre(rs.getString("nombre"))
          .build();
        listaEmpresas.add(empresa);
      }
      rs.close();
      // ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      con.closeConexion();
    }
    return listaEmpresas;
  }

  @Override
  public List<ReclamoTicketListaResponse> obtenerTickets(int idRepresentante) {
    con.startConexion();
    List<ReclamoTicketListaResponse> listaTickets = new ArrayList<>();
    try {
      String query =
        "SELECT ti.cod_ticket FROM pedido AS pe " + 
        "INNER JOIN representante AS re ON re.cod_representante = pe.cod_representante " +
        "INNER JOIN ticket AS ti ON ti.cod_ticket = pe.cod_ticket " +
        "WHERE re.cod_representante = ?; ";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ps.setInt(1, idRepresentante);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReclamoTicketListaResponse ticket = ReclamoTicketListaResponse
          .builder()
          .idTicket(rs.getInt("cod_ticket"))
          .build();
        listaTickets.add(ticket);
      }
      rs.close();
      // ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      con.closeConexion();
    }
    return listaTickets;
  }

  @Override
  public List<ReclamoTicketProductoListaResponse> obtenerProductos(int idTicket) {
    con.startConexion();
    List<ReclamoTicketProductoListaResponse> listaProductos = new ArrayList<>();
    try {
      String query =
        "SELECT ec.id_elemento_catalogo, ec.nombre FROM ticket AS ti " +
        "INNER JOIN detalle_ticket_producto AS dtp ON dtp.cod_ticket = ti.cod_ticket " +
        "INNER JOIN elemento_catalogo AS ec ON ec.id_elemento_catalogo = dtp.id_elemento_catalogo " +
        "WHERE ti.cod_ticket = ?; ";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ps.setInt(1, idTicket);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReclamoTicketProductoListaResponse producto = ReclamoTicketProductoListaResponse
          .builder()
          .idProducto(rs.getInt("id_elemento_catalogo"))
          .nombre(rs.getString("nombre"))
          .build();
        listaProductos.add(producto);
      }
      rs.close();
      // ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      con.closeConexion();
    }
    return listaProductos;
  }

  @Override
  public List<ReclamoTicketProductoDetalleResponse> obtenerProductoDetalle(int idTicket, int idProducto) {
    con.startConexion();
    List<ReclamoTicketProductoDetalleResponse> listaProductoDetalle = new ArrayList<>();
    try {
      String query = 
        "SELECT ti.fecha_entrega, dtp.cantidad, st.nro_lote, ec.nombre " +
        "FROM ticket AS ti " +
        "INNER JOIN detalle_ticket_producto AS dtp ON dtp.cod_ticket = ti.cod_ticket " +
        "INNER JOIN elemento_catalogo AS ec ON ec.id_elemento_catalogo = dtp.id_elemento_catalogo " +
        "LEFT JOIN stock AS st ON st.id_elemento_catalogo = ec.id_elemento_catalogo " +
        "WHERE ti.cod_ticket = ? AND ec.id_elemento_catalogo = ?";
      PreparedStatement ps = con.getCon().prepareStatement(query);
      ps.setInt(1, idTicket);
      ps.setInt(2, idProducto);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ReclamoTicketProductoDetalleResponse productoDetalle = ReclamoTicketProductoDetalleResponse
          .builder()
          .fecha(rs.getString("fecha_entrega"))
          .cantidad(rs.getInt("cantidad"))
          .nroLote(rs.getString("nro_lote"))
          .nombre(rs.getString("nombre"))
          .build();
        listaProductoDetalle.add(productoDetalle);
      }
      rs.close();
      // ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      con.closeConexion();
    }
    return listaProductoDetalle;
  }

  @Override
  public ReclamoVisorResponse obtenerVisor(int idReclamo) {

    con.startConexion();
    ReclamoVisorResponse visor = new ReclamoVisorResponse();
    try {

      ReclamoVisorClienteResponse visorCliente = new ReclamoVisorClienteResponse();
      String query1 = 
        "SELECT " +
        "cl.nombre, " +
        "CONCAT(pe.prenombre,' ',pe.primer_apellido,' ',pe.segundo_apellido) representante, " +
        "cargo, " +
        "correo_empresarial, " +
        "pe.direccion " +
        "FROM reclamo r " +
        "INNER JOIN representante AS re ON re.cod_representante = r.cod_representante " +
        "INNER JOIN cliente AS cl ON cl.cod_cliente = re.cod_cliente " +
        "INNER JOIN persona AS pe ON pe.cod_persona = re.cod_persona " +
        "WHERE r.cod_reclamo = ?; ";
      PreparedStatement pstmt = con.getCon().prepareStatement(query1);
      pstmt.setInt(1, idReclamo);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        visorCliente.setNombre(rs.getString("nombre"));
        visorCliente.setRepresentante(rs.getString("representante"));
        visorCliente.setCargo(rs.getString("cargo"));
        visorCliente.setCorreoEmpresarial(rs.getString("correo_empresarial"));
        visorCliente.setDireccion(rs.getString("direccion"));
        visor.setCliente(visorCliente);
      }
      rs.close();
 
      String query2 = 
        "SELECT " +
        "ti.cod_ticket, " +
        "ec.nombre, " +
        "ti.fecha_entrega, " +
        "st.nro_lote, " +
        "dtip.cantidad " +
        "FROM reclamo r " +
        "INNER JOIN pedido AS pe ON r.cod_pedido = pe.cod_pedido " +
        "INNER JOIN ticket AS ti ON pe.cod_ticket = ti.cod_ticket " +
        "INNER JOIN detalle_ticket_producto AS dtip ON ti.cod_ticket = dtip.cod_ticket " +
        "INNER JOIN elemento_catalogo AS ec ON dtip.id_elemento_catalogo = ec.id_elemento_catalogo " +
        "INNER JOIN stock AS st ON ec.id_elemento_catalogo = st.id_elemento_catalogo " +
        "WHERE r.cod_reclamo = ?;";
      PreparedStatement pstmt2 = con.getCon().prepareStatement(query2);
      pstmt2.setInt(1, idReclamo);
      ResultSet rs2 = pstmt2.executeQuery();
      while (rs2.next()) {
        ReclamoVisorDetalleResponse productoDetalle = ReclamoVisorDetalleResponse
          .builder()
          .idTicket(rs2.getInt("cod_ticket"))
          .fechaEntrega(rs2.getString("fecha_entrega"))
          .cantidad(rs2.getInt("cantidad"))
          .nroLote(rs2.getString("nro_lote"))
          .nombre(rs2.getString("nombre"))
          .build();
        visor.setDetalle(productoDetalle);
      }
      rs2.close();

      String query3 = 
        "SELECT " +
        "rt.descripcion, " +
        "r.comentario, " +
        "r.fecha_suceso, " +
        "r.fecha_reclamo, " +
        "nu.descripcion " +
        "FROM reclamo r " +
        "INNER JOIN reclamo_tipo AS rt ON r.cod_tipo_reclamo = rt.cod_tipo_reclamo " +
        "INNER JOIN nivel_urgencia AS nu ON r.cod_nivel_urgencia = nu.cod_nivel_urgencia " +
        "WHERE r.cod_reclamo = ?; ";
      PreparedStatement pstmt3 = con.getCon().prepareStatement(query3);
      pstmt3.setInt(1, idReclamo);
      ResultSet rs3 = pstmt3.executeQuery();
      while (rs3.next()) {
        ReclamoVisorNaturalezaResponse visorNaturaleza = ReclamoVisorNaturalezaResponse
          .builder()
          .descripcion(rs3.getString("descripcion"))
          .comentario(rs3.getString("comentario"))
          .fechaSuceso(rs3.getString("fecha_suceso"))
          .fechaReclamo(rs3.getString("fecha_reclamo"))
          .descripcionNivelUrgencia(rs3.getString("descripcion"))
          .build();
        visor.setNaturaleza(visorNaturaleza);
      }
      rs3.close();

      String query4 = 
        "SELECT " +
        "CONCAT(ev.nombre_evidencia,'.',at.descripcion) evidencia " +
        "FROM reclamo r " +
        "INNER JOIN evidencia AS ev ON r.cod_reclamo = ev.cod_reclamo " +
        "INNER JOIN archivo_tipo AS at ON ev.cod_tipo_archivo = at.cod_tipo_archivo " +
        "WHERE r.cod_reclamo = ?; ";
      PreparedStatement pstmt4 = con.getCon().prepareStatement(query4);
      pstmt4.setInt(1, idReclamo);
      List<ReclamoVisorEvidenciaResponse> visorEvidencias = new ArrayList<>();
      ResultSet rs4 = pstmt4.executeQuery();
      while (rs4.next()) {
        ReclamoVisorEvidenciaResponse visorEvidencia = ReclamoVisorEvidenciaResponse
          .builder()
          .evidencia(rs4.getString("evidencia"))
          .build();
          visorEvidencias.add(visorEvidencia);
      }
      rs4.close();
      visor.setEvidencias(visorEvidencias);

      String query5 = 
        "SELECT " +
        "cl.nombre, " +
        "at.descripcion, " +
        "se.comentario " +
        "FROM reclamo r " +
        "INNER JOIN seguimiento AS se ON r.cod_seguimiento = se.cod_seguimiento " +
        "INNER JOIN accion_tipo AS at ON se.cod_tipo_accion = at.cod_tipo_accion " +
        "INNER JOIN cliente AS cl ON se.cod_cliente_interno = cl.cod_cliente " +
        "WHERE r.cod_reclamo = ?;";
      PreparedStatement pstmt5 = con.getCon().prepareStatement(query5);
      pstmt5.setInt(1, idReclamo);
      ResultSet rs5 = pstmt5.executeQuery();
      while (rs5.next()) {
        ReclamoVisorResolucionResponse visorResolucion = ReclamoVisorResolucionResponse
          .builder()
          .nombre(rs5.getString("nombre"))
          .descripcion(rs5.getString("descripcion"))
          .comentario(rs5.getString("comentario"))
          .build();
        visor.setResolucion(visorResolucion);
      }
      rs5.close();

      String query6 =
        "SELECT " +
        "se.fecha_resolucion, " +
        "se.numero_caso, " +
        "er.descripcion " +
        "FROM reclamo r " +
        "INNER JOIN seguimiento AS se ON r.cod_seguimiento = se.cod_seguimiento " +
        "INNER JOIN estado_reclamo AS er ON r.cod_estado_reclamo = er.cod_estado_reclamo " +
        "WHERE r.cod_reclamo = ?; ";
      PreparedStatement pstmt6 = con.getCon().prepareStatement(query6);
      pstmt6.setInt(1, idReclamo);
      ResultSet rs6 = pstmt6.executeQuery();
      while (rs6.next()) {
        ReclamoVisorSeguimientoResponse visorSeguimiento = ReclamoVisorSeguimientoResponse
          .builder()
          .fechaResolucion(rs6.getString("fecha_resolucion"))
          .numeroCaso(rs6.getString("numero_caso"))
          .descripcionEstadoReclamo(rs6.getString("descripcion"))
          .build();
        visor.setSeguimiento(visorSeguimiento);
      }
      rs6.close();

    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      con.closeConexion();
    }
    return visor;
  }
}
